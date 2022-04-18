package com.example.sutdfolio;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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


        }
        void setImage(PostItem post)
        {
            imageView = (ImageView) itemView.findViewById(R.id.imageSlide);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("imageUrl",post.getImage());
//                    bundle.putString("post",post.getId());
//                    NavController navController = Navigation.findNavController(itemView);
//                    navController.navigate(R.id.indvImage,bundle);
//                }
//            });
            Glide.with(itemView.getContext())
                    .load(post.getImage())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(imageView);

        }
    }

}
