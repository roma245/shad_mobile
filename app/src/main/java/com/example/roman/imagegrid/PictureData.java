package com.example.roman.imagegrid;

/**
 * Created by Roman on 13.03.2016.
 */

import android.graphics.Bitmap;

public class PictureData {
    int resourceId;
    String description;
    Bitmap thumbnail;

    public PictureData(int resourceId, String description, Bitmap thumbnail) {
        this.resourceId = resourceId;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
