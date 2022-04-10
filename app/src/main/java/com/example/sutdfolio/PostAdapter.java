package com.example.sutdfolio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sutdfolio.data.model.Posts;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostAdapterHolder>{
    private List<PostItem> postItems;
    private ViewPager2 viewPager2;
    @NonNull
    @Override
    public PostAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostAdapterHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_item_container,parent,false));
    }

    PostAdapter(List<PostItem> postItems, ViewPager2 viewPager2) {
        this.postItems = postItems;
        this.viewPager2 = viewPager2;
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapterHolder holder, int position) {
        holder.setImage(postItems.get(position));
    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class PostAdapterHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        public PostAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }
        void setImage(PostItem post)
        {
            imageView.setImageResource(post.getImage());
        }
    }

}
