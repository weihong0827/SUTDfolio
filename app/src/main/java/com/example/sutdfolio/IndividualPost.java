package com.example.sutdfolio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sutdfolio.data.model.Image;
import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.data.model.Tag;
import com.example.sutdfolio.data.model.User;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.example.sutdfolio.utils.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndividualPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualPost extends Fragment {

    ReadPost post;
    Bundle bundle;
    private ViewPager2 viewPager2;
    private String ID;
    private SharedPreferences prefs;
    private  String token;
    private boolean liked = false;

    private Context context;

    public static IndividualPost newInstance() {

        return new IndividualPost();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        prefs =context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        if (getArguments() != null) {
            ID = getArguments().getString("_id");



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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = getView().findViewById(R.id.indivPostTitle);
        TextView descView = getView().findViewById(R.id.desc);
        TextView youtube = getView().findViewById(R.id.youtube);
        TextView telegram = getView().findViewById(R.id.telegram);
        TextView linkedIn = getView().findViewById(R.id.linkedIn);
        TextView term = getView().findViewById(R.id.term);
        TextView team = getView().findViewById(R.id.teamInvolved);
        TextView textHeartCount = getView().findViewById(R.id.item_heart_counter);
        ImageView heartImageButton = getView().findViewById(R.id.item_heart_button);
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);

        viewPager2 = (ViewPager2) getView().findViewById(R.id.viewPageImageSlider);
        List<PostItem> postItems = new ArrayList<>();

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
        PostAdapter adapter = new PostAdapter(postItems,viewPager2);
        viewPager2.setAdapter(adapter);

        viewPager2.setPageTransformer(compositepagetransformer);
        APIRequest request = APIRequest.getInstance();
        request.getPost(new Listener<String>() {
            @Override
            public void getResult(String object) {
                final Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
                post = gson.fromJson(object, ReadPost.class);
                Toast.makeText(getActivity(), object, Toast.LENGTH_LONG).show();
                Log.d("test",post.getTags().toString());
                List<Image> images = post.getImage();
                postItems.add(new PostItem("https://static.wikia.nocookie.net/pokemon/images/4/49/Ash_Pikachu.png/revision/latest?cb=20200405125039"));
                postItems.add(new PostItem("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/International_Pok%C3%A9mon_logo.svg/500px-International_Pok%C3%A9mon_logo.svg.png"));
                for(Image i:images)
                {
                    postItems.add(new PostItem(i.getUrl()));
                }

                //postItems.add(new PostItem(R.drawable.ic_baseline_home_24));

                telegram.setText("@hungchiayu1");
                youtube.setText("youtube.com");
                title.setText(post.getTitle());
                descView.setText(post.getDesc());

                List<Tag> tags = post.getTags();
                TextView[] tagTextView = new TextView[tags.size()];
                Log.d("test",tags.toString());
                for (int i =0;i<tags.size();i++)
                {
                    Tag tag = tags.get(i);

                    tagTextView[i] = new TextView(linearLayout.getContext());
                    tagTextView[i].setBackgroundResource(R.drawable.tag_border);
                    tagTextView[i].setText("#"+tag.getName());
                    tagTextView[i].setPadding(5,1,5,1);
                    tagTextView[i].layout(5,1,5,1);
                    tagTextView[i].setTextSize(18);

                    linearLayout.addView(tagTextView[i]);
                }
                List<User> users = post.getPeopleInvolved();
                String temp = "";
                for(User i :users)
                {
                   temp+=i.getName().toString()+" ("+i.getPillar()+")"+",";
                }
               int count = post.getUpvoteCount();
                liked = post.isLiked();
                if (liked)
                {
                    heartImageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                else
                {heartImageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);

                }

                heartImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!token.equals("")){
                            request.upVote(new Listener<String>() {
                                @Override
                                public void getResult(String object) {
                                    if (liked){
                                        heartImageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                        liked = !liked;
                                        textHeartCount.setText(String.valueOf(Integer.parseInt(textHeartCount.getText().toString())-1));
                                    }else{
                                        heartImageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                                        liked = !liked;
                                        textHeartCount.setText(String.valueOf(Integer.parseInt(textHeartCount.getText().toString())+1));
                                    }
                                }
                            },ID,token);
                        }else{
                            Toast.makeText(context,"Please login to like the post!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                textHeartCount.setText(String.valueOf(post.getUpvoteCount()));
                team.setText(temp);


                adapter.notifyDataSetChanged();

            }
        }, ID,token);
       ;
    }
}
