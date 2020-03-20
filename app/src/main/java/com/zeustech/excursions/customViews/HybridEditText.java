package com.zeustech.excursions.customViews;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.zeustech.excursions.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class HybridEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Context context;
    private GradientDrawable drawable;
    private Integer minCharCount = null;

    private OnTextDidChangeListener onTextDidChangeListener;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if (onTextDidChangeListener != null) {
                if (s != null) {
                    onTextDidChangeListener.onTextDidChanged(s);
                }
            }
        }
    };

    private Integer minNumber = null, maxNumber = null;

    // Create a Custom Interface
    public interface OnTextDidChangeListener {
        void onTextDidChanged(@NonNull Editable s);
    }

    public HybridEditText(Context context) {
        super(context);
        init(context);
    }

    public HybridEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HybridEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        drawable = new GradientDrawable();
        setBackground(drawable);
        setStrokeColor(R.color.black);
        setUpListeners();
    }

    private void setUpListeners() {
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setStrokeColor(R.color.blue);
                return;
            }
            setStrokeColor(isValid() ? R.color.black : R.color.red);
        });

        addTextChangedListener(textWatcher);
    }

    public void setOnTextDidChangeListener(OnTextDidChangeListener listener) {
        onTextDidChangeListener = listener;
    }

    public void reloadView() {
        removeTextChangedListener(textWatcher); // #1 remove text listener
        setText(""); // #2 set text to empty String
        clearFocus(); // #3 clear focus
        addTextChangedListener(textWatcher); // #4 add text listener
        setStrokeColor(R.color.black); // #5 set stroke color
    }

    public boolean isValid() {
        Editable editable = getText();
        if (editable == null) { return false; } // #1 Empty String
        if (minNumber != null && maxNumber != null) {
            try {
                int currentNumber = Integer.valueOf(editable.toString());
                return currentNumber >= minNumber && currentNumber <= maxNumber;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (minCharCount == null) { return true; } // No Validation
        return getText().length() >= minCharCount;
    }

    public void setValidNumbers(@NonNull Integer minNumber, @NonNull Integer maxNumber) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
    }

    public void setMinCharCount(Integer minCharCount) {
        this.minCharCount = minCharCount;
    }

    public void setStrokeColor(int strokeColor) {
        drawable.setStroke(1, ContextCompat.getColor(context, strokeColor));
    }
}
