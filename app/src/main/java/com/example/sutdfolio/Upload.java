package com.example.sutdfolio;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.InputType;
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
import android.widget.Toast;

import com.example.sutdfolio.data.model.Course;
import com.example.sutdfolio.data.model.CreatePost;
import com.example.sutdfolio.data.model.Image;
import com.example.sutdfolio.data.model.Tag;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Upload extends Fragment {

    private UploadViewModel mViewModel;
    private List<Course> courses;
    private List<Tag> tags;
    private String selectedCourse;
    private ArrayList<String> selectedTag = new ArrayList<>();
    private int term;
    private ArrayList<Integer> people;
    private ArrayList<Image> image = new ArrayList<>();
    APIRequest request = APIRequest.getInstance();
    SharedPreferences pref;
    String token;
    private StorageReference mStorageRef;
    NavController navController;

    public static Upload newInstance() {
        return new Upload();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = pref.getString("token", "");
        if (token.isEmpty()){
            Toast.makeText(getActivity(),"Not Logged in. Please Log in on the Profile page before uploading a post.",Toast.LENGTH_LONG).show();
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();
        super.onCreate(savedInstanceState);

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
                        String name = UUID.randomUUID().toString();
                        StorageReference ref
                                = mStorageRef
                                .child("images/" + name);
                        ref.putFile(uri).addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Date today = Date.from(LocalDate.now().atStartOfDay()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toInstant());
                                                Image tempImage = new Image(name,name,name,name,today ,"image/jpeg",uri.toString(),uri.toString());
                                                image.add(tempImage);
                                                Log.d("HELL YEH", "onSuccess: "+uri.toString());
                                            }
                                        });
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("LOSER", "onFailure: ");
                                    }
                                }
                        );
//                        Bitmap bm = BitmapFactory.decodeFile(picturePath);

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
                        selectedCourse = courses.get(i).get_id();}
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
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
        EditText titleEdit = (EditText)v.findViewById(R.id.uploadPage_title_EditText);
        EditText descEdit = (EditText)v.findViewById(R.id.uploadPage_projectDescription_EditText);
        EditText youtubeEdit = v.findViewById((R.id.uploadPage_YouTube_EditText));
        EditText linkedInEdit = v.findViewById(R.id.uploadPage_peopleInvolved_EditText);
        EditText telegramEdit = v.findViewById(R.id.uploadPage_telegram_EditText);
        LinearLayout peopleLL = v.findViewById(R.id.peopleInvolve);
        final Button submitButton = v.findViewById(R.id.uploadPage_submit_Button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (token.isEmpty()){
                    navController = Navigation.findNavController(v);
                    navController.navigate(R.id.loginFragment);
                    Toast.makeText(getActivity(),"Not Logged in. Please Log in before uploading a post.",Toast.LENGTH_LONG).show();
                }
                else {
                    String title = titleEdit.getText().toString();
                    String desc = descEdit.getText().toString();
                    String youtube = youtubeEdit.getText().toString();
                    String linkedIn = linkedInEdit.getText().toString();
                    String telegram = telegramEdit.getText().toString();
                    people = new ArrayList<>();


                    for (int i=0; i<peopleLL.getChildCount();i++){
                        EditText peopleText;
                        if (i==0){
                            peopleText = (EditText) peopleLL.getChildAt(0);
                        }else{
                            LinearLayout subLayout = (LinearLayout) peopleLL.getChildAt(i);
                            peopleText = (EditText) subLayout.getChildAt(0);
                        }
                        String input = peopleText.getText().toString();
                        if (!input.equals("") ){
                            people.add(Integer.parseInt(input));}
                    }
                    CreatePost postToSend = new CreatePost(title,selectedTag,desc,image,people,selectedCourse,term,telegram,linkedIn,youtube,true);
                    Boolean studentIDvalidchecker = true;
                    for(Integer k : people){
                        if (!(1000000<k && k<1100000)){
                            studentIDvalidchecker=false;
                        }
                    }
                    Log.d("upload", "onClick: "+postToSend.toString());
                    Gson gson = new Gson();
                    String postString =  gson.toJson(postToSend);

                    if(title.length()<6){
                        Toast.makeText(getActivity(),"Title has to be minimum 6 characters",Toast.LENGTH_LONG).show();
                    }else if(!studentIDvalidchecker){Toast.makeText(getActivity(),"Please ensure the stated student ID of each person involved is valid.",Toast.LENGTH_LONG).show();}
                    else{
                        try {
                            JSONObject object = new JSONObject(postString);
                            request.uploadPost(new Listener<JSONObject>() {
                                @Override
                                public void getResult(JSONObject object) {
                                    try{
                                        Log.d("HI", "getResult: "+object.getString("_id"));
                                        String id = object.getString("_id");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("_id",id);
                                        navController = Navigation.findNavController(v);
                                        navController.navigate(R.id.individualPost,bundle);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },object,token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }
            }
        });

        final Button addImageButton = v.findViewById(R.id.uploadPage_addImage_Button);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mGetContent.launch(intent);

            }
        });
        return v;
    }



}