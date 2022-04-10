package com.example.sutdfolio.data.model;

import androidx.annotation.NonNull;

public class Tag {
    private final String _id;
    private final String name;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public Tag(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
