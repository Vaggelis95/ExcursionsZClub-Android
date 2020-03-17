package com.zeustech.excursions.ui.login;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.autoComplete.AutoCompleteAdapter;
import com.zeustech.excursions.models.Area;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AreasAdapter extends AutoCompleteAdapter<Area> {

    AreasAdapter(@NonNull Context context) {
        super(context, R.layout.spinner_child_cell);
    }

    @Override
    public void onBindView(@Nullable Area model, @Nullable SpannableString text, @NonNull View viewHolder) {
        TextView tv = viewHolder.findViewById(R.id.text);
        if (text != null) {
            tv.setText(text);
        } else {
            tv.setText(model != null ? model.getDescription() : null);
        }
    }
}
