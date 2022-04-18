package com.example.sutdfolio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sutdfolio.data.model.Image;
import com.example.sutdfolio.data.model.Posts;
import com.example.sutdfolio.data.model.ReadPost;
import com.example.sutdfolio.data.model.Tag;
import com.example.sutdfolio.utils.APIRequest;
import com.example.sutdfolio.utils.Listener;
import com.example.sutdfolio.utils.Util;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "Recycler adapter";
    private List<ReadPost> posts;
    private final Context context;
    private ArrayList<ReadPost> tempList;
    private String token;
    private APIRequest request = APIRequest.getInstance();
    public RecyclerViewAdapter(Context context,ReadPost[] posts, String token) {
        this.context = context;
        this.posts = Arrays.asList(posts);
        tempList = new ArrayList<>();
        tempList.addAll(Arrays.asList(posts));
        this.token = token;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String id;
        private final TextView textTitle;
        private final TextView textDesc;
        private final ImageView itemImage;
        private final ImageButton heartImageButton;
        private final TextView textHeartCount;
        private final Button updateButton;
        private final Button deleteButton;

        private final LinearLayout tagLinearLayout;
        private APIRequest request = APIRequest.getInstance();
        private SharedPreferences prefs;
        private  String token;
        private RelativeLayout parentLayout;
        private boolean liked = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ViewHolder(View v, Context context) {
            super(v);
            // Define click listener for the ViewHolder's View.
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("_id",id);
//                    Log.d(TAG, "id " + id + " clicked.");
//                    NavController navController = Navigation.findNavController(v);
//                    navController.navigate(R.id.individualPost,bundle);
//                    final Activity activity = (Activity) context;
//
//
//                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
//                }
//            });
            parentLayout = v.findViewById(R.id.parent_layout);
            prefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            token = prefs.getString("token", "");

            textTitle = (TextView) v.findViewById(R.id.item_title);
            textDesc = (TextView) v.findViewById(R.id.item_detail);
            itemImage = v.findViewById(R.id.item_image);
            heartImageButton = (ImageButton) v.findViewById(R.id.item_heart_button);
            textHeartCount = (TextView) v.findViewById(R.id.item_heart_counter);
//            textTags = (TextView) v.findViewById(R.id.item_tags);
            tagLinearLayout = (LinearLayout) v.findViewById(R.id.item_tag_linear_layout);
            updateButton = v.findViewById(R.id.postUpdate);
            deleteButton = v.findViewById(R.id.postDelete);
            heartImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Clicked");
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
                        },id,token, v);
                    }else{
                        Toast.makeText(context,"Please login to like the post!",Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        public Button getUpdateButton() {
            return updateButton;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

        public RelativeLayout getParentLayout() {
            return parentLayout;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
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

        public ImageButton getHeartImageButton() { return heartImageButton; }

        public TextView getTextHeartCount() { return textHeartCount; }

//        public TextView getTextTags() { return textTags; }

        public LinearLayout getTagLinearLayout() {
            return tagLinearLayout;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.getTextTitle().setText(tempList.get(position).getTitle());
//        holder.getTextDesc().setText(Html.fromHtml(posts[position].getDesc()));
        holder.getTextDesc().setText(tempList.get(position).getDesc());
        List<Image> imageList = tempList.get(position).getImage();
        if (imageList.size()>0){
            Glide
                    .with(context)
                    .load(imageList.get(0).getUrl())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                    .into(holder.getItemImage());
        }
        if (tempList.get(position).isEditable()){
            Button deleteButton =holder.getDeleteButton();
            deleteButton.setVisibility(Button.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    request.delPost(new Listener<String>() {
                                        @Override
                                        public void getResult(String object) {
                                            tempList.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
                                        }
                                    },tempList.get(position).get_id(),token, view);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });
            Button updateButton =holder.getUpdateButton();
            updateButton.setVisibility(Button.VISIBLE);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    Gson gson = Util.GsonParser();
                    bundle.putString("postInfo",gson.toJson(tempList.get(position)));
                    Log.d(TAG, "id " + holder.getId() + " update clicked.");
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.upload,bundle);
                }
            });
        }

        holder.setId(tempList.get(position).get_id());

        holder.getParentLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_id",holder.getId());
                    Log.d(TAG, "id " + holder.getId() + " clicked.");
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.individualPost,bundle);


                    }
                }
        );
        // get and update heart count
        holder.setLiked(tempList.get(position).isLiked());

        if (tempList.get(position).isLiked()){
            holder.getHeartImageButton().setImageResource(R.drawable.ic_baseline_favorite_24);
        }else{
            holder.getHeartImageButton().setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
//        holder.getHeartImageButton().setImageResource(R.drawable.ic_baseline_favorite_24);
        holder.getTextHeartCount().setText(String.valueOf(tempList.get(position).getUpvoteCount()));

        // get and update tags
        List<String> tagStrings = new ArrayList<>();
        List<Tag> tagList = tempList.get(position).getTags();
        Log.d("Special tag", "onBindViewHolder: "+ tagList.toString());
        holder.getTagLinearLayout().removeAllViews();

        for (Tag tag : tagList) {
            tagStrings.add("#"+tag.getName());
            if (String.join(" ", tagStrings).length() <= 20) { // check that length of tags do not exceed a certain threshold/wrap

                TextView tagTextView = new TextView(context.getApplicationContext());
                tagTextView.setText("#"+tag.getName());
                tagTextView.setBackgroundResource(R.drawable.tag_border);
                int horizontalPads = (int) Util.pxFromDp(context.getApplicationContext(), 5);
                int verticalPads = (int) Util.pxFromDp(context.getApplicationContext(), 1);
                tagTextView.setPadding(horizontalPads,verticalPads,horizontalPads,verticalPads);
                tagTextView.layout(horizontalPads, verticalPads, horizontalPads, verticalPads);
                tagTextView.setTextSize(18);
                tagTextView.setGravity(Gravity.CENTER);
                holder.getTagLinearLayout().addView(tagTextView);
            }
        }
//        holder.getTextTags().setText(String.join(" ", tagStrings));
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

                // check for matches in tags
                Boolean tagCheck = false;
                for (Tag tag : item.getTags()) {
                    if (tag.getName().toLowerCase(Locale.ROOT).contains(lowerS)) {
                        tagCheck = true;
                    }
                }
                if (tagCheck) {
                    tempList.add(item);
                }

                // check for matches in title
                if (item.getTitle().toLowerCase(Locale.ROOT).contains(lowerS)){
                    tempList.add(0, item); // insertion at index 0 places priority on title matches
                }

                // check for matches in description
                else if (item.getDesc().toLowerCase(Locale.ROOT).contains(lowerS)) {
                    tempList.add(item);
                }

                // check for matches in course
                else if (item.getCourseNo().getCourseName().toLowerCase(Locale.ROOT).contains(lowerS)){
                    tempList.add(item);
                }

                // check for matches in term
                else if (("term "+String.valueOf(item.getTerm()).toLowerCase(Locale.ROOT)).contains(lowerS)) {
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