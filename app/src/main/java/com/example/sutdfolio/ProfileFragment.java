package com.example.sutdfolio;


import android.annotation.SuppressLint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.sutdfolio.databinding.FragmentOTPverificationBinding;
import com.example.sutdfolio.databinding.FragmentProfileBinding;
import com.example.sutdfolio.utils.APIRequest;
import com.google.gson.Gson;
import com.example.sutdfolio.utils.Listener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
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
    TextView classOf;
    TextView aboutMe;
    ImageView avatar;



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

        super.onCreate(savedInstanceState);
        SharedPreferences pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = pref.getString("token", "");
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
                    postsObj = gson.fromJson(posts, Posts[].class);
                        //object.getString("Profile");
                    }
            }, token);




//        userObj.

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
        classOf = view.findViewById(R.id.ClassOf);
        aboutMe = view.findViewById(R.id.AboutMe);

        id.setText(userObj.getStudentId());
        classOf.setText((userObj.getClass_of()));
        name.setText(userObj.getName());
        aboutMe.setText(userObj.getAboutMe());
        if (!userObj.getAvatar().isEmpty()){
            Glide
                    .with(getActivity())
                    .load(userObj.getAvatar())
                    .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                    .into(avatar);
        }
        getUserPostData();

        return view;


    }

    private void getUserPostData (){
        APIRequest request = APIRequest.getInstance();
        request.getPosts(new Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void getResult(String object) {
                final Gson gson = new Gson();
                Log.d("get",postsObj[0].toString());
//                Toast.makeText(getActivity(),object,Toast.LENGTH_LONG).show();

                adapter = new RecyclerViewAdapter(getActivity(),postsObj);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }
}