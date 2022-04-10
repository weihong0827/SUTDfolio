package com.example.sutdfolio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndividualPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualPost extends Fragment {

    Posts post;
    Bundle bundle;
    private ViewPager2 viewPager2;


    public static IndividualPost newInstance() {

        return new IndividualPost();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String id = getArguments().getString("_id");
            getData(id);
        } else {
            Toast.makeText(getActivity(), "NO ID GIVEN", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_individual_post, container, false);

        return view;
    }

    public void getData(String id) {
        APIRequest request = APIRequest.getInstance();
        request.getPost(new Listener<String>() {
            @Override
            public void getResult(String object) {
                final Gson gson = new Gson();
                post = gson.fromJson(object, ReadPost.class);
                Toast.makeText(getActivity(), object, Toast.LENGTH_LONG).show();
            }
        }, id);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("test", String.valueOf(R.id.viewPageImageSlider));
        viewPager2 = (ViewPager2) getView().findViewById(R.id.viewPageImageSlider);
        List<PostItem> postItems = new ArrayList<>();
        postItems.add(new PostItem(R.drawable.ic_baseline_cloud_upload_24));
        postItems.add(new PostItem(R.drawable.ic_baseline_home_24));
        viewPager2.setAdapter(new PostAdapter(postItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);
        CompositePageTransformer compositepagetransformer = new CompositePageTransformer();
        compositepagetransformer.addTransformer(new MarginPageTransformer(40));
        compositepagetransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        TextView textView = getView().findViewById(R.id.indivposttext);
        TextView descview = getView().findViewById(R.id.desc);
        TextView youtube = getView().findViewById(R.id.youtube);
        TextView telegram = getView().findViewById(R.id.telegram);

        descview.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.mollit anim id est laborum. magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        viewPager2.setPageTransformer(compositepagetransformer);
        telegram.setText("hungchiayu1");
        youtube.setText("youtube.com");
        textView.setText("THIS IS FOR TESTING");
    }
}
