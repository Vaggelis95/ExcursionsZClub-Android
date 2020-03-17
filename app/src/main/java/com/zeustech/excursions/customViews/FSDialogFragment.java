package com.zeustech.excursions.customViews;

import android.os.Bundle;

import com.zeustech.excursions.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FSDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FadeFragmentTheme);
    }
}
