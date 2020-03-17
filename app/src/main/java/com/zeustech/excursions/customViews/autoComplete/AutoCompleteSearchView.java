package com.zeustech.excursions.customViews.autoComplete;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.zeustech.excursions.customViews.KeyboardManager;

public class AutoCompleteSearchView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    private AutoCompleteListener autoCompleteListener;

    public AutoCompleteSearchView(Context context) {
        super(context);
        setUpListeners();
    }

    public AutoCompleteSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpListeners();
    }

    public AutoCompleteSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpListeners();
    }

    private void setUpListeners() {
        setOnFocusChangeListener((v, focus) -> {
            if (focus) {
                KeyboardManager.showKeyboard(v);
            } else {
                KeyboardManager.hideKeyboard(v);
            }
            if (autoCompleteListener != null) autoCompleteListener.onFocusChanged(this, getText().toString(), focus);
        });
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (autoCompleteListener != null) {
                    autoCompleteListener.onTextChanged(AutoCompleteSearchView.this, s.toString());
                }
            }
        });
        setOnEditorActionListener((textView, i, keyEvent) -> {
            boolean handled = false;
            if (i == EditorInfo.IME_ACTION_DONE && autoCompleteListener != null) {
                String text = textView.getText().toString();
                if (!text.isEmpty()) {
                    autoCompleteListener.onDoneClicked(AutoCompleteSearchView.this,
                            textView.getText().toString());
                    KeyboardManager.hideKeyboard(textView);
                    handled = true;
                }
            }
            return handled;
        });
    }

    public void setAutoCompleteListener(AutoCompleteListener autoCompleteListener) {
        this.autoCompleteListener = autoCompleteListener;
    }

}
