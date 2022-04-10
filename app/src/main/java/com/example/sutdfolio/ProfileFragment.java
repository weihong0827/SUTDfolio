package com.example.sutdfolio;


import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sutdfolio.data.model.Posts;

import com.example.sutdfolio.data.model.User;
import com.example.sutdfolio.utils.APIRequest;
import com.google.gson.Gson;
import com.example.sutdfolio.utils.Listener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    String user = "";
    String posts = "";
    String token = "";

    private User userObj;
    Posts[] postsObj;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView name;
    TextView id;
    TextView year;
    TextView aboutMe;
    ImageView avatar;
    TextView pillar;
    Button logout;
    SharedPreferences pref;
    NavController navController;



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = pref.getString("token", "");
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerViewProfile);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        id = view.findViewById(R.id.StudentID);
        name = view.findViewById(R.id.Name);
        avatar = view.findViewById(R.id.Avatar);
        year = view.findViewById(R.id.year);
        aboutMe = view.findViewById(R.id.AboutMe);
        pillar = view.findViewById(R.id.Pillar);
        logout = view.findViewById(R.id.Logout);


//        if (!checkToken()){
//            navController = Navigation.findNavController(view);
//            navController.navigate(R.id.action_profileFragment_to_loginFragment);
//        }
        Toast.makeText(getActivity(),token,Toast.LENGTH_SHORT).show();
        Log.d("TOKEN", token);

        APIRequest api = APIRequest.getInstance();
        api.getUser(new Listener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) {
                try {
                    user = object.getString("user");
                    posts = object.getString("projects");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final Gson gson = new Gson();
                userObj = gson.fromJson(user, User.class);
                postsObj = gson.fromJson(posts, ReadPost[].class);
                Log.d("profile user", userObj.toString());
                Log.d("profile posts", Arrays.toString(postsObj));
                //object.getString("Profile");

                id.setText(String.valueOf(userObj.getStudentId()));
                year.setText(String.valueOf(userObj.getClass_of()));
                pillar.setText(String.valueOf(userObj.getPillar()));
                name.setText(userObj.getName());
                aboutMe.setText(userObj.getAboutMe());
                if (userObj.getAvatar()!=null){
                    Glide
                            .with(getActivity())
                            .load(userObj.getAvatar())
                            .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                            .into(avatar);
                }
                Log.d("TAG", Arrays.toString(postsObj));
                if (postsObj!=null){
                    if (postsObj.length>0){
                        Log.d("posts not empty", "not empty");
                        getUserPostData();
                    }

                }
            }
        }, token);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    pref.edit().remove("token").apply();
                    getActivity().finish();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
                catch (Error error){
                    Log.d("logout", "no token");
                }

            }
        });


        return view;
    }

    private void getUserPostData (){
        adapter = new RecyclerViewAdapter(getActivity(),postsObj);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

//    private boolean checkToken(){
//        pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        token = pref.getString("token", "");
//        if (token.equals("")){
//            return false;
//        }else{return true;}
//    }
}