package com.zeustech.excursions.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.NonNull;

public class BitmapDrawableResizer {

    private Context context;

    public BitmapDrawableResizer(Context context) {
        this.context = context;
    }

    public BitmapDrawable resize(@NonNull BitmapDrawable drawable, int gravity, int width, int height) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(),
                Bitmap.createScaledBitmap(drawable.getBitmap(), width, height, false));
        bitmapDrawable.setGravity(gravity);
        return  bitmapDrawable;
    }
}
