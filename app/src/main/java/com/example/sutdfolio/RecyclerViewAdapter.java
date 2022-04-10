package com.example.sutdfolio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sutdfolio.data.model.Image;
import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "Recycler adapter";
    private List<ReadPost> posts;
    private final Context context;
    private static String id;
    private ArrayList<ReadPost> tempList;

    public RecyclerViewAdapter(Context context,ReadPost[] posts) {
        this.context = context;
        this.posts = Arrays.asList(posts);
        tempList = new ArrayList<>();
        tempList.addAll(Arrays.asList(posts));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textTitle;
        private final TextView textDesc;
        private final ImageView itemImage;


        public ViewHolder(View v,Context context) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_id",id);
                    Log.d(TAG, "id " + id + " clicked.");
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_homePage_to_individualPost,bundle);
                    final Activity activity = (Activity) context;


                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textTitle = (TextView) v.findViewById(R.id.item_title);
            textDesc = (TextView) v.findViewById(R.id.item_detail);
            itemImage = v.findViewById(R.id.item_image);

        }

        public ImageView getItemImage() {
            return itemImage;
        }

        public TextView getTextTitle() {
            return textTitle;
        }

        public TextView getTextDesc() {
            return textDesc;
        }

        @Override
        public void onClick(View view) {

        }
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);

        return new ViewHolder(v,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.getTextTitle().setText(tempList.get(position).getTitle());
//        holder.getTextDesc().setText(Html.fromHtml(posts[position].getDesc()));
        holder.getTextDesc().setText(tempList.get(position).getDesc());
        List<Image> imageList = tempList.get(position).getImage();
        if (imageList.size()>0){
            Glide
                    .with(context)
                    .load(imageList.get(0).getUrl())
                .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                    .into(holder.getItemImage());
        }
        id = tempList.get(position).get_id();


    }
    public void filter(String s) {
        Log.d(TAG, "filter: input string"+s);
        String lowerS = s.toLowerCase(Locale.ROOT);
        Log.d(TAG, "filter: posts data"+posts.toString());
        tempList.clear();
        if (s.length()==0){
            tempList.addAll(posts);
        }else{
            for (ReadPost item: posts){
                if (item.getTitle().toLowerCase(Locale.ROOT).contains(lowerS)){
                    tempList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tempList.size();
    }
}
