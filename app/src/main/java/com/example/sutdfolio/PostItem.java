package com.example.sutdfolio;

import com.example.sutdfolio.data.model.Image;

public class PostItem {
    private String imageUrl;
    PostItem (String image)
    {

        this.imageUrl = image;

    }

    public String getImage() {
        return this.imageUrl;
    }
}
