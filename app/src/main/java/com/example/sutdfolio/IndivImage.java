package com.example.sutdfolio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sutdfolio.data.model.Image;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndivImage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndivImage extends Fragment {

    private static final String ARG_PARAM1 = "imageUrl";


    // TODO: Rename and change types of parameters
    private String imageUrl;


    public IndivImage() {
        // Required empty public constructor
    }

    public static IndivImage newInstance() {
        IndivImage fragment = new IndivImage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            imageUrl = getArguments().getString(ARG_PARAM1);

        }
        Log.d(" 123", "onCreate: "+imageUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_indv_image, container, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.backgroundImage);
        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);
        return v;
    }
}