package com.example.sutdfolio;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.example.sutdfolio.utils.NetworkManager;
import com.google.gson.Gson;

import java.util.List;

public class homePage extends Fragment {

    private HomePageViewModel mViewModel;

    public static homePage newInstance() {
        return new homePage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        // TODO: Use the ViewModel
        NetworkManager instance = NetworkManager.getInstance();
        Toast.makeText(getActivity(),"HELLO",Toast.LENGTH_LONG).show();
        APIRequest requestInstance = APIRequest.getInstance();
        requestInstance.getPosts(new Listener<String>() {
            @Override
            public void getResult(String object) {
                final Gson gson = new Gson();
                Posts[] posts = gson.fromJson(object, Posts[].class);
                Log.d("get",posts[0].toString());
                Toast.makeText(getActivity(),object,Toast.LENGTH_LONG).show();
            }
        });
    }

}