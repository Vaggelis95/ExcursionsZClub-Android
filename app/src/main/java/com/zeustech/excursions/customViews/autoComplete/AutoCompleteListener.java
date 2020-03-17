package com.zeustech.excursions.customViews.autoComplete;

import android.widget.EditText;

import androidx.annotation.NonNull;

public interface AutoCompleteListener {

    void onTextChanged(@NonNull EditText editText, String text);

    void onFocusChanged(@NonNull EditText editText, String text, boolean hasFocus);

    void onDoneClicked(@NonNull EditText editText, String text);

}
