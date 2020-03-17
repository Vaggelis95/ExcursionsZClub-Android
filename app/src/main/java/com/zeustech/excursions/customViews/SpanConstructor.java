package com.zeustech.excursions.customViews;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class SpanConstructor {

    public static SpannableString apply(String prefix, String rest, int color) {
        SpannableString ss = new SpannableString(prefix + rest);
        ss.setSpan(new ForegroundColorSpan(color), prefix.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

}
