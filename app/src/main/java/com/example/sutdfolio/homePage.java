package com.example.sutdfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.example.sutdfolio.utils.NetworkManager;
import com.google.gson.Gson;

import java.util.List;

public class homePage extends Fragment{

    private HomePageViewModel mViewModel;
    Posts[] posts;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static homePage newInstance() {
        return new homePage();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPostData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPostData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_page_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }

//    @Override
//    public void onViewCreated(@Nullable Bundle savedInstanceState) {
//        super.onViewCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
//        // TODO: Use the ViewModel
//
//
//    }
    private void getPostData (){

        Toast.makeText(getActivity(),"HELLO",Toast.LENGTH_LONG).show();
        APIRequest request = APIRequest.getInstance();
        request.getPosts(new Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void getResult(String object) {
                final Gson gson = new Gson();
                posts = gson.fromJson(object, ReadPost[].class);
                Log.d("get",posts[0].toString());
//                Toast.makeText(getActivity(),object,Toast.LENGTH_LONG).show();

                adapter = new RecyclerViewAdapter(getActivity(),posts);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

}