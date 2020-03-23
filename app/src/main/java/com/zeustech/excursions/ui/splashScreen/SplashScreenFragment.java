package com.zeustech.excursions.ui.splashScreen;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zeustech.excursions.R;
import com.zeustech.excursions.viewModels.GlobalVM;

public class SplashScreenFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private GlobalVM globalVM;
    private Dialog connectionDialog;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalVM.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        setUpDialog(view);
        init();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            init();
        }
    }

    private void init() {
        if (connectionDialog.isShowing()) { connectionDialog.dismiss(); }
        globalVM.initSplash(status -> {
            switch (status) {
                case 1: { // Success
                    navController.navigate(R.id.action_splashScreen_to_home);
                    break;
                }
                case 2: { // Missing Hotel-Login
                    navController.navigate(R.id.action_splashScreen_to_login);
                    break;
                }
                case 3: { // Connection Error
                    connectionDialog.show();
                    break;
                }
            }
        });
    }

    private void setUpDialog(@NonNull View view) {
        connectionDialog = new Dialog(view.getContext(), R.style.PopUpDialogTheme);
        connectionDialog.setContentView(R.layout.dialog_internet_connection);
        connectionDialog.setCancelable(false);
        Button btn = connectionDialog.findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }
}
