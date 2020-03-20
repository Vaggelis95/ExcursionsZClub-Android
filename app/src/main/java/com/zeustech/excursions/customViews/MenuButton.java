package com.zeustech.excursions.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;

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
