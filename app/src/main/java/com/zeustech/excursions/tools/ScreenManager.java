package com.zeustech.excursions.tools;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.zeustech.excursions.R;

import java.util.Objects;

public class ScreenManager {

    private final static String PHONE = "phone";

    public static int getWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static boolean isPhone(@NonNull Context context) {
        String type = context.getString(R.string.screen_type);
        return Objects.equals(type, PHONE);
    }

    /*public static boolean isTablet(@NonNull Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }*/

}
