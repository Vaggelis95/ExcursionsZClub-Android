package com.zeustech.excursions.tools;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.List;

public class ColorManager {

    public static void setTint(@NonNull ImageView view, @ColorRes int colorRes) {
        Drawable drawable = view.getDrawable();
        int color = ContextCompat.getColor(view.getContext(), colorRes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    public static void setTint(@NonNull List<ImageView> view, @ColorRes int colorRes) {
        for (ImageView imageView : view) {
            setTint(imageView, colorRes);
        }
    }

}
