package com.example.sutdfolio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    private FragmentProfileBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String user = "";
    String posts = "";
    String token = "";
    User userObj;
    Posts[] postsObj;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}