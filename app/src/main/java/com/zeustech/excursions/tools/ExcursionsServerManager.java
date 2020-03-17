package com.zeustech.excursions.tools;

import android.content.Context;

import com.zeustech.excursions.R;
import com.zeustech.excursions.models.ExcursionsServerModel;

import androidx.annotation.NonNull;

public class ExcursionsServerManager extends FileReader<ExcursionsServerModel> {

    public ExcursionsServerManager(@NonNull Context context) {
        super(context, R.raw.excursions_server, ExcursionsServerModel.class);
    }

}
