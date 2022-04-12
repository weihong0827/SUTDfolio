package com.example.sutdfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.example.sutdfolio.utils.NetworkManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class homePage extends Fragment{

    private HomePageViewModel mViewModel;
    ReadPost[] posts;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SearchView searchView;

    private SharedPreferences pref;
    private String token;

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
        searchView = rootView.findViewById(R.id.simpleSearchView);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter.filter(s);
                        return true;
                    }
                }

        );

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

        pref = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = pref.getString("token", "");
        APIRequest request = APIRequest.getInstance();
        request.getPosts(new Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void getResult(String object) {
                final Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
                Log.d("TAG", "getResult: "+object);
                posts = gson.fromJson(object, ReadPost[].class);


                adapter = new RecyclerViewAdapter(getActivity(), posts);

                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        },token);
    }

}