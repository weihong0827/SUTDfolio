package com.example.sutdfolio.data;

import com.example.sutdfolio.data.model.User;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<User> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
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