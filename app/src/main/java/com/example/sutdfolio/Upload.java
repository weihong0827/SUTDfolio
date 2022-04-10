package com.example.sutdfolio;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import android.app.Activity;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Spinner;
import android.widget.TextView;


import com.example.sutdfolio.data.model.Course;
import com.example.sutdfolio.data.model.Tag;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Upload extends Fragment {

    private UploadViewModel mViewModel;
    private List<Course> courses;
    private List<Tag> tags;
    private String selectedCourse;
    private ArrayList<String> selectedTag = new ArrayList<>();
    private int term;
    public static Upload newInstance() {
        return new Upload();
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    LinearLayout ll;
                    //TODO handle multiple image
                    //TODO Camera input
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = result.getData().getData();
                        String[] projection = {MediaStore.MediaColumns.DATA};
//                        Cursor cursor = managedQuery(uri, projection, null, null, null);
//                        int column_index = cursor
//                                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                        cursor.moveToFirst();
//                        String imagePath = cursor.getString(column_index);
//                        Bitmap bm = BitmapFactory.decodeFile(imagePath);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

//                        bm.compress(Bitmap.CompressFormat.JPEG,40,baos);


                        // bitmap object

                        byte[] byteImage_photo = baos.toByteArray();

                        //generate base64 string of image

                        String encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);

                        ll = getActivity().findViewById(R.id.uploadPage_images_LinearLayout);
                        ImageView imageView = new ImageView(getContext());
                        imageView.setPadding(0,0,20,0);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageURI(uri);
                        ll.addView(imageView);
                        Log.d("Upload", "onActivityResult: "+ uri.toString());
                    }

                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upload_fragment, container, false);
        Spinner spinner = (Spinner) v.findViewById(R.id.uploadPage_term_Spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.term_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                term = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        APIRequest request = APIRequest.getInstance();

        request.getCourse(new Listener<String>() {
            @Override
            public void getResult(String object) {
                Gson gson = new Gson();
                courses = gson.fromJson(object,new TypeToken<List<Course>>(){}.getType());
                Spinner courseSpinner = (Spinner) v.findViewById(R.id.uploadPage_course_Spinner);
                ArrayAdapter<Course> courseArrayAdapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_spinner_item,courses);
                courseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedCourse = courses.get(i).get_id();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                courseSpinner.setAdapter(courseArrayAdapter);
            }
        });
        request.getTags(new Listener<String>() {
            @Override
            public void getResult(String object) {
                Gson gson = new Gson();
                tags = gson.fromJson(object,new TypeToken<List<Tag>>(){}.getType());
                LinearLayout tagListView = getActivity().findViewById(R.id.tagList);
                for (Tag tag:tags){
                    Button tagButton = new Button(getContext());
                    tagButton.setText(tag.getName());
                    Drawable buttonDrawable = tagButton.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                    tagButton.setBackground(buttonDrawable);
                    tagButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String tagId = tag.get_id();
                            if (!selectedTag.contains(tagId)){
                                selectedTag.add(tagId);
                                Drawable buttonDrawable = tagButton.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                //the color is a direct color int and not a color resource
                                DrawableCompat.setTint(buttonDrawable, Color.BLUE);
                                tagButton.setBackground(buttonDrawable);
                            }else{
                                selectedTag.remove(tagId);
                                Drawable buttonDrawable = tagButton.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                //the color is a direct color int and not a color resource
                                DrawableCompat.setTint(buttonDrawable, Color.GRAY);
                                tagButton.setBackground(buttonDrawable);
                            }
                        }
                    });
                    tagListView.addView(tagButton);
                }

            }
        });
        final Button addPersonButton = v.findViewById(R.id.uploadPage_addPerson_Button);
        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout peopleLL = v.findViewById(R.id.peopleInvolve);
                LinearLayout itemSet = new LinearLayout(getContext());
                itemSet.setOrientation(LinearLayout.VERTICAL);
                itemSet.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT ));
                EditText editText = new EditText(getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                itemSet.addView(editText);
                Button removeButton = new Button(getContext());
                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);

                removeButton.setLayoutParams(param);
                removeButton.setText("remove");
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        peopleLL.removeView(itemSet);
                    }
                });
                itemSet.addView(removeButton);
                peopleLL.addView(itemSet);
            }
        });

        final Button submitButton = v.findViewById(R.id.uploadPage_submit_Button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText titleEdit = v.findViewById(R.id.uploadPage_title_EditText);
                EditText descEdit = v.findViewById(R.id.uploadPage_projectDescription_EditText);
                String title = titleEdit.getText().toString();
                String desc = descEdit.getText().toString();
                LinearLayout imageLinear = v.findViewById(R.id.uploadPage_images_LinearLayout);

            }
        });

        final Button addImageButton = v.findViewById(R.id.uploadPage_addImage_Button);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mGetContent.launch(intent);

            }
        });
        return v;
    }



}