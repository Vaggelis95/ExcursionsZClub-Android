package com.zeustech.excursions.customViews;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.zeustech.excursions.R;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

public class MenuButton extends androidx.appcompat.widget.AppCompatButton {

    private PopupMenu popupMenu;
    private OnMenuClickedListener listener;

    public MenuButton(Context context) {
        super(context);
        init(context);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnMenuClickedListener {
        void onClick(View view);
    }

    public void setOnMenuClickedListener(OnMenuClickedListener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        popupMenu = new PopupMenu(context, this);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.keyboard_arrow_down);
        if (bitmapDrawable != null) {
            setCompoundDrawablesWithIntrinsicBounds(null, null,
                    new BitmapDrawableResizer(context).resize(bitmapDrawable, Gravity.CENTER, 45, 45), null);
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            setText(menuItem.getTitle().toString());
            if (listener != null) {
                listener.onClick(MenuButton.this);
            }
            return false;
        });
        setOnClickListener(v -> popupMenu.show());
    }

    public PopupMenu getMenu() {
        return this.popupMenu;
    }
}
