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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sutdfolio.data.model.Course;
import com.example.sutdfolio.data.model.CreatePost;
import com.example.sutdfolio.data.model.Image;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.data.model.Tag;
import com.example.sutdfolio.data.model.User;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.example.sutdfolio.utils.Util;
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

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Upload extends Fragment {

    private UploadViewModel mViewModel;
    private List<Course> courses;
    private List<Tag> tags;
    private String selectedCourse;
    Course editCourse;
    private ArrayList<String> selectedTag = new ArrayList<>();
    private int term;
    private ArrayList<Integer> people;
    private ArrayList<Image> image = new ArrayList<>();
    APIRequest request = APIRequest.getInstance();
    SharedPreferences pref;
    String token;
    private StorageReference mStorageRef;
    private ReadPost postInfo;
    String postID;
    NavController navController;



    public static Upload newInstance() {
        return new Upload();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (getArguments() != null) {
            postInfo = Util.GsonParser().fromJson(getArguments().getString("postInfo"),ReadPost.class);
            postID = getArguments().getString("postID");
            Log.d("Edit", postID);
            Log.d("post.id", postInfo.get_id());
        }
        super.onCreate(savedInstanceState);

    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //TODO handle multiple image
                    //TODO Camera input
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri localUri = result.getData().getData();

                        String name = UUID.randomUUID().toString();
                        StorageReference ref
                                = mStorageRef
                                .child("images/" + name);
                        CircularProgressButton btn = (CircularProgressButton) getActivity().findViewById(R.id.uploadPage_addImage_Button);
                        btn.startAnimation();
                        ref.putFile(localUri).addOnSuccessListener(
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
                                                Log.d("image list", image.toString());
                                                btn.revertAnimation();
                                                LinearLayout ll;
                                                ll = getActivity().findViewById(R.id.uploadPage_images_LinearLayout);
                                                ImageButton imageButton = new ImageButton(getContext());
                                                imageButton.setPadding(0,0,20,0);
                                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
                                                imageButton.setLayoutParams(layoutParams);
                                                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                imageButton.setImageURI(localUri);
                                                imageButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Log.d("image pressed", imageButton.toString());
                                                        Log.d("localuri", localUri.toString());
                                                        Log.d("tempimage", tempImage.toString());
                                                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(tempImage.getUrl());
                                                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                // File deleted successfully
                                                                Log.d("DELETE FROM FIREBASE", "onSuccess: deleted file");
                                                                image.remove(tempImage);
                                                                Log.d("image list", image.toString());
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                // Uh-oh, an error occurred!
                                                                Log.d("DELETE FROM FIREBASE", "onFailure: did not delete file");
                                                            }
                                                        });
                                                        // TODO: delete image from list / storage before upload?
                                                        ll.removeView(view);

                                                    }
                                                });

                                                ll.addView(imageButton);
                                                Log.d("Upload", "onActivityResult: "+ uri.toString());
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


                    }
                }
            });


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.upload_fragment, container, false);

        Spinner termSpinner = (Spinner) v.findViewById(R.id.uploadPage_term_Spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.term_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(adapter);
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                if (editCourse!= null){
                Log.d("editCourse", editCourse.getCourseName());
//                Log.d("editcourse index", String.valueOf(courses.indexOf(editCourse)));
                for(Course c: courses){
                    if (c.get_id().equals(editCourse.get_id())){
                        courseSpinner.setSelection(courses.indexOf(c));
                    }
                }}


            }
        }, this.getActivity());


        request.getTags(new Listener<String>() {
            @Override
            public void getResult(String object) {
                Gson gson = new Gson();
                tags = gson.fromJson(object,new TypeToken<List<Tag>>(){}.getType());
                LinearLayout tagListView = getActivity().findViewById(R.id.tagList);
                for (Tag tag:tags){
                    Button tagButton = new Button(getContext());
                    String tagId = tag.get_id();
                    tagButton.setText(tag.getName());
                    Drawable buttonDrawable = tagButton.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    if (!selectedTag.contains(tagId)){DrawableCompat.setTint(buttonDrawable, Color.GRAY);}
                    else{DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.sutd_red_1));}



                    tagButton.setBackground(buttonDrawable);
                    tagButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!selectedTag.contains(tagId)){
                                selectedTag.add(tagId);
                                Drawable buttonDrawable = tagButton.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                //the color is a direct color int and not a color resource
                                DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.sutd_red_1));
                                tagButton.setBackground(buttonDrawable);
                                tagButton.setTextColor(getResources().getColor(R.color.white));
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
        }, this.getActivity());

        final Button addPersonButton = v.findViewById(R.id.uploadPage_addPerson_Button);
        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPerson(view,"");
            }
        });
        EditText titleEdit = (EditText)v.findViewById(R.id.uploadPage_title_EditText);
        EditText descEdit = (EditText)v.findViewById(R.id.uploadPage_projectDescription_EditText);
        EditText youtubeEdit = v.findViewById((R.id.uploadPage_YouTube_EditText));
        EditText linkedInEdit = v.findViewById(R.id.uploadPage_LinkedIn_EditText);
        EditText telegramEdit = v.findViewById(R.id.uploadPage_telegram_EditText);
        LinearLayout peopleLL = v.findViewById(R.id.peopleInvolve);
        CircularProgressButton submitButton = (CircularProgressButton) v.findViewById(R.id.uploadPage_submit_Button);
//        final Button submitButton = v.findViewById(R.id.uploadPage_submit_Button);


        if (token.isEmpty()){
            submitButton.setEnabled(false);
            Toast.makeText(getActivity(),"Not logged in. Please log in before uploading a post.",Toast.LENGTH_LONG).show();
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                submitButton.startAnimation();
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
                Gson gson = Util.GsonParser();
                String postString =  gson.toJson(postToSend);

                if(title.length()<6){
                    Log.d("title char", "title not min 6 chars");
                    Toast.makeText(getActivity(),"Title has to be minimum 6 characters",Toast.LENGTH_LONG).show();
                }else if(!studentIDvalidchecker){
                    Log.d("studentID valid", "invalid");
                    Toast.makeText(getActivity(),"Please ensure the stated student ID of each person involved is valid.",Toast.LENGTH_LONG).show();}
                else{
                    try {
                        JSONObject object = new JSONObject(postString);
                        submitButton.startAnimation();
                        request.uploadPost(new Listener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject object) {
                                try{
                                    Log.d("uploadpost getresult", "getResult: "+object.getString("_id"));
                                    String id = object.getString("_id");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("_id",id);
                                    navController = Navigation.findNavController(v);
                                    navController.navigate(R.id.individualPost,bundle);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },object,token, v);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}

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


        if (postInfo != null){
            titleEdit.setText(postInfo.getTitle());
            descEdit.setText(postInfo.getDesc());
            youtubeEdit.setText(postInfo.getYoutube());
            telegramEdit.setText(postInfo.getTelegram());
            linkedInEdit.setText(postInfo.getLinkIn());
            List<Tag> tags = postInfo.getTag();
            for (Tag tag: tags){
                selectedTag.add(tag.get_id());
            }
            ArrayList<User> peopleInvolved = (ArrayList<User>) postInfo.getPeopleInvolved();
            if (!peopleInvolved.isEmpty()){
                TextView peopleText = (EditText) peopleLL.getChildAt(0);
                peopleText.setText(String.valueOf(peopleInvolved.get(0).getStudentId()));
                for (int i=1;i<peopleInvolved.size();i++){
                    addPerson(v,String.valueOf(peopleInvolved.get(i).getStudentId()));
                }
            }
            Log.d("selected tags", tags.toString());
            editCourse = postInfo.getCourseNo();
            term = postInfo.getTerm();
            Log.d("term", String.valueOf(term));
            Log.d("selected course name", postInfo.getCourseNo().getCourseName());

            ArrayList<Image> postImages = (ArrayList<Image>) postInfo.getImage();
            image.addAll(postImages);
            if (!postImages.isEmpty()){
                for (Image image :postImages){
                    LinearLayout ll;
                    ll = v.findViewById(R.id.uploadPage_images_LinearLayout);
                    ImageButton imageButton = new ImageButton(getContext());
                    imageButton.setPadding(0,0,20,0);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
                    imageButton.setLayoutParams(layoutParams);
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(image.getUrl());
                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                    request.delImage(image.getFilename(),token, postInfo.get_id(), view);
                                    postImages.remove(image);
                                    Log.d("DELETE FROM FIREBASE", "onSuccess: deleted file from project");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                    Log.d("DELETE FROM FIREBASE", "onFailure: did not delete file");
                                }
                            });
                            // TODO: delete image from list / storage before upload?
                            ll.removeView(view);

                        }
                    });
                    ll.addView(imageButton);
                    Glide
                            .with(getContext())
                            .load(image.getUrl())
                            .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                            .into(imageButton);
                }
            }

            termSpinner.setSelection(term-1);

            //TODO SHOULD DELETE THE IMAGE ONLY ON SAVE CHANGES
            submitButton.setText("Save Changes");
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

                    Log.d("Data", "onClick: "+ title.toString()+ selectedTag.toString()+desc.toString()+image.toString()+people.toString());
                    CreatePost postToSend = new CreatePost(title,selectedTag,desc,image,people,selectedCourse,term,telegram,linkedIn,youtube,true);
                    Boolean studentIDvalidchecker = true;
                    for(Integer k : people){
                        if (!(1000000<k && k<1100000)){
                            studentIDvalidchecker=false;
                        }
                    }


                    Gson gson = Util.GsonParser();
                    String postString =  gson.toJson(postToSend);
                    Log.d("post update", "onClick: "+postString);
                    if(title.length()<6){
                        Log.d("title char", "title not min 6 chars");
                        Toast.makeText(getActivity(),"Title has to be minimum 6 characters",Toast.LENGTH_LONG).show();
                    }else if(!studentIDvalidchecker){
                        Log.d("studentID valid", "invalid");
                        Toast.makeText(getActivity(),"Please ensure the stated student ID of each person involved is valid.",Toast.LENGTH_LONG).show();}
                    else {
                        try {
                            JSONObject object = null;
                            try {
                                object = new JSONObject(postString);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            submitButton.startAnimation();
                            request.updatePost(new Listener<JSONObject>() {
                                @Override
                                public void getResult(JSONObject object) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("_id",postID);

                                    navController = Navigation.findNavController(v);
                                    navController.navigate(R.id.individualPost,bundle);
                                }
                            },postID, token, object, view);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return v;
    }
private void addPerson(View v,String text){
    LinearLayout peopleLL = v.findViewById(R.id.peopleInvolve);
    LinearLayout itemSet = new LinearLayout(getContext());
    itemSet.setOrientation(LinearLayout.VERTICAL);
    itemSet.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT ));
    EditText editText = new EditText(getContext());
    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    editText.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

    editText.setText(text);

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


}