package com.example.sutdfolio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sutdfolio.utils.Listener;
import com.example.sutdfolio.utils.NetworkManager;

import org.json.JSONObject;

class homePage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        NetworkManager.getInstance().getPosts(new Listener<String>() {
            @Override
            public void getResult(String object) {
                System.out.println(object);
            }
        });
        return view;
    }

}