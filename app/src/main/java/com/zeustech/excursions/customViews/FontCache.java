package com.zeustech.excursions.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

import androidx.core.content.res.ResourcesCompat;

public class FontCache {

    private FontCache() { }

    private static SparseArray<Typeface> fontCache = new SparseArray<>();

    public static Typeface getTypeface(Context context, int fontName) {

        Typeface typeface = fontCache.get(fontName);

        if(typeface == null) {

            typeface = ResourcesCompat.getFont(context, fontName);

            fontCache.put(fontName, typeface);
        }

        return  typeface;

    }

}
