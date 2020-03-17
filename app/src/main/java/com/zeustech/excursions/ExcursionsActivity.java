package com.zeustech.excursions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.zeustech.excursions.viewModels.GlobalVM;

public class ExcursionsActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursions);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // programmatically
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getApplication(), this);
        GlobalVM globalVM = new ViewModelProvider(this, factory).get(GlobalVM.class);
    }

}
