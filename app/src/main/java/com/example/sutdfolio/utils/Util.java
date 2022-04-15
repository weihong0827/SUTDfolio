package com.example.sutdfolio.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {
    private static Gson gson;
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public static Gson GsonParser(){
        if (gson ==null){
            GsonBuilder builder = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss");
            gson = builder.create();
        }
        return gson;
    }
}
