package com.zeustech.excursions.tools;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zeustech.excursions.R;

public class ScreenDialogFragment extends DialogFragment {

    private boolean fullScreenSize = false;

    private boolean wrapContentHeight = false;
    private boolean wrapContentWidth = false;

    private double widthPercentage = -1;
    private double heightPercentage = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Override
    public void onResume() {
        applyScreenSize();
        // Call super onResume after sizing
        super.onResume();
    }

    protected void setWidthPercentage(double widthPercentage) {
        this.widthPercentage = widthPercentage;
    }

    protected void setHeightPercentage(double heightPercentage) {
        this.heightPercentage = heightPercentage;
    }

    protected void wrapContentHeight(boolean enable) {
        wrapContentHeight = enable;
    }

    protected void setWrapContentWidth(boolean enable) {
        wrapContentWidth = enable;
    }

    protected void setFullScreen(boolean enable) {
        fullScreenSize = enable;
    }

    private void applyScreenSize() {
        if (getDialog() == null || getDialog().getWindow() == null) return;
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width

        int width;
        if (fullScreenSize) {
            width = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (wrapContentWidth) {
            width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            width = (int) (size.x * ((widthPercentage != -1) ? widthPercentage : 0.85));
        }

        int height;
        if (fullScreenSize) {
            height = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (wrapContentHeight) {
            height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            height = (int) (size.y * ((heightPercentage != -1) ? heightPercentage : 0.60));
        }

        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
    }

/*    private void setFullScreen() {
        if (getDialog() == null || getDialog().getWindow() == null) return;
        // Get existing layout params for the window
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
    }*/

}
