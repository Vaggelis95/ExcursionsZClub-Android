package com.zeustech.excursions.customViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.zeustech.excursions.R;

import androidx.core.content.ContextCompat;

public class EventButton extends androidx.appcompat.widget.AppCompatButton {

    private GradientDrawable drawable;
    private Style currentStyle;

    public EventButton(Context context) {
        super(context);
        initView(context);
    }

    public EventButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EventButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setTextSize(18);
        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        setBackground(drawable);
        setTypeface(FontCache.getTypeface(context, R.font.brandon_reg));
    }

    public void applyStyle(Context context, Style style) {
        if (currentStyle == style) { return; }
        currentStyle = style;
        switch (style) {
            case TRANSPARENT: {
                drawable.setColor(Color.TRANSPARENT);
                drawable.setStroke(2, Color.BLACK);
                setTextColor(Color.BLACK);
                break;
            }
            case BLACK: {
                drawable.setColor(Color.BLACK);
                drawable.setStroke(2, Color.BLACK);
                setTextColor(Color.WHITE);
                break;
            }
            case RED: {
                int color = ContextCompat.getColor(context, R.color.cooks_club_red);
                drawable.setColor(color);
                drawable.setStroke(2, color);
                setTextColor(Color.WHITE);
                break;
            }
            case GRAY: {
                drawable.setColor(Color.GRAY);
                drawable.setStroke(2, Color.GRAY);
                setTextColor(Color.WHITE);
                break;
            }
            default: break;
        }
    }

    public enum Style
    {
        TRANSPARENT, BLACK, RED, GRAY
    }

}
