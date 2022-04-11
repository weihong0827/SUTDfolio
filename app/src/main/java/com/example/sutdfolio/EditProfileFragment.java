package com.example.sutdfolio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sutdfolio.data.model.Image;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    EditText aboutme;
    EditText classof;
    EditText pillar;
    ImageView avatar;
    Button saveChanges;

    String oriAboutMe;
    String oriClassOf;
    String oriPillar;
    String oriAvatar;
    String jwt;

    String setAboutMe;
    String setClassOf;
    String setPillar;
    String setAvatar;
    String newUri;
    NavController navController;
    SharedPreferences prefs;
    private StorageReference mStorageRef;
    Uri uri;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    LinearLayout ll;
                    //TODO handle multiple image
                    //TODO Camera input
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        uri = result.getData().getData();

                        String name = UUID.randomUUID().toString();
                        StorageReference ref
                                = mStorageRef
                                .child("images/" + name);
                        CircularProgressButton btn = (CircularProgressButton) getActivity().findViewById(R.id.SaveChanges);
                        btn.startAnimation();
                        ref.putFile(uri).addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        Bitmap bm = getBitmapFromVectorDrawable(getContext(),
//                                                R.drawable.ic_baseline_check_circle_outline_24);
//                                        Log.d("TAG", "onSuccess: "+bm.toString());
//                                        btn.doneLoadingAnimation(R.color.sutd_red_1,bm);
                                        btn.revertAnimation();
                                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.d("HELL YEH", "onSuccess: "+uri.toString());
                                                newUri = uri.toString();
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
                        avatar.setImageURI(uri);

                        Log.d("Upload", "onActivityResult: "+ uri.toString());
                    }

                }
            });
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oriAboutMe = getArguments().getString("aboutme");
            oriClassOf = getArguments().getString("classof");
            oriPillar = getArguments().getString("pillar");
            oriAvatar = getArguments().getString("avatar");
        }
        prefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        jwt = prefs.getString("token", "");
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_profile, container, false);


        aboutme = view.findViewById(R.id.Edit_About_Me);
        classof = view.findViewById(R.id.Edit_Class_Of);
        pillar = view.findViewById(R.id.Edit_Pillar);
        avatar = view.findViewById(R.id.AvatarEdit);
        saveChanges = view.findViewById(R.id.SaveChanges);

        aboutme.setText(oriAboutMe);
        classof.setText(oriClassOf);
        pillar.setText(oriPillar);
        if (oriAvatar!=null){
            Glide
                    .with(getActivity())
                    .load(oriAvatar)
                    .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                    .into(avatar);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mGetContent.launch(intent);
            }
        });


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo set avatar overriding
                setAboutMe = aboutme.getText().toString();
                setPillar = pillar.getText().toString();
                setClassOf = classof.getText().toString();
                if (newUri != null){
                    setAvatar = newUri;
                }else{setAvatar = oriAvatar;}

                APIRequest api = APIRequest.getInstance();
                api.editUser(new Listener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject object) {
                        Log.d("save changes", "pass");
                        
                        Toast.makeText(getActivity(),"Changes saved.",Toast.LENGTH_LONG).show();
                    }
                }, setAboutMe, setPillar, setClassOf, setAvatar, jwt);

                navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_editProfileFragment_to_profileFragment);
            }
        });



}}