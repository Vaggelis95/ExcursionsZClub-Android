package com.zeustech.excursions.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zeustech.excursions.R;

public class ToastMessage extends Toast {

    private GradientDrawable background;
    private TextView textView;

    public ToastMessage(Context context) {
        super(context);
        Activity activity = (Activity) context;
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_message_layout, activity.findViewById(R.id.toast_root));
        textView = layout.findViewById(R.id.toast_message);

        background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        textView.setBackground(background);

        setGravity(Gravity.BOTTOM|Gravity.CENTER, 0,15);
        setDuration(Toast.LENGTH_SHORT);
        setView(layout);
    }

    public ToastMessage setBackgroundColor(int color) {
        background.setColor(color);
        return this;
    }

    public ToastMessage setBackgroundCornerRadius(int radius) {
        background.setCornerRadius(radius);
        return this;
    }

    public ToastMessage setTextColor(int color) {
        textView.setTextColor(color);
        return this;
    }

    public ToastMessage setText(String text) {
        textView.setText(text);
        return this;
    }
}
