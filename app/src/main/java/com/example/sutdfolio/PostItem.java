package com.example.sutdfolio;

import com.example.sutdfolio.data.model.Image;

public class PostItem {
    private String imageUrl;
    private String id;

    public PostItem(String imageUrl, String id) {
        this.imageUrl = imageUrl;
        this.id = id;
    }

    PostItem (String image)
    {

        this.imageUrl = image;


    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return this.imageUrl;
    }
}
