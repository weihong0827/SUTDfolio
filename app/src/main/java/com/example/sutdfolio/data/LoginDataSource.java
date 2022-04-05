package com.example.sutdfolio.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.User;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that retrieves user information.
 */
public class LoginDataSource extends Fragment {

    public LoginDataSource() {
    }

    public LoginDataSource(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = pref.getString("token", "");
        APIRequest api = APIRequest.getInstance();
        api.getUser(new Listener<JSONObject>() {
            @Override
            public void getResult(JSONObject object) {
            }
            
            public User getUser(JSONObject object) {
                try {
                    String user = object.getString("user");
                    final Gson gson = new Gson();
                    User userObj = gson.fromJson(user, User.class);
                    return userObj;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Posts[] getPosts(JSONObject object) {
                try {
                    String posts = object.getString("projects");
                    final Gson gson = new Gson();
                    Posts[] postsObj = gson.fromJson(posts, Posts[].class);
                    return postsObj;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, token);


        //object.getString("Profile");

    }

    public Result<User> login(String username, String password) {
        try {
            APIRequest request = APIRequest.getInstance();

            User fakeUser =
                    new User(
                            "61e455a5ba6ec9dc13677d9b",
                            "Jame","weihong_qiu@mymail.sutd.edu.sg",
                            1005610,"EPD","http://localhost:8000/api/file/image/4a299a6cbfb0f567d6592a407c94edaa.jpg",
                            "SHORT INTRO ABT ME",2024);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}