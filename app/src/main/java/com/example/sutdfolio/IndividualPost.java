package com.example.sutdfolio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndividualPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualPost extends Fragment {

    Posts post;


    public static IndividualPost newInstance() {
        return new IndividualPost();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            String id = getArguments().getString("_id");
            getData(id);
        }else{
            Toast.makeText(getActivity(),"NO ID GIVEN",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual_post, container, false);
    }
    public void getData(String id){
        APIRequest request = APIRequest.getInstance();
        request.getPost(new Listener<String>() {
            @Override
            public void getResult(String object) {
                final Gson gson = new Gson();
                post = gson.fromJson(object, ReadPost.class);
                Toast.makeText(getActivity(),object,Toast.LENGTH_LONG).show();
            }
        },id);
    }
}