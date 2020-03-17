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
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.viewModels.GlobalVM;

public class SplashScreenFragment extends Fragment {

    private NavController navController;
    private GlobalVM globalVM;
    private Dialog connectionDialog;
    private boolean loggedIn = false;

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
        connectionDialog = new Dialog(view.getContext());
        setUpDialog();
        loggedIn = (globalVM.getSelectedHotel() != null);
        if (loggedIn) { // Already Logged in
            init();
        } else {
            getAreas();
        }
    }

    private void getAreas() {
        if (connectionDialog.isShowing()) { connectionDialog.dismiss(); }
        globalVM.getAreas(new CompletionHandler<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean model) {
                if (connectionDialog.isShowing()) { connectionDialog.dismiss(); }
                navController.navigate(R.id.action_splashScreen_to_login);
            }

            @Override
            public void onFailure(@Nullable String description) {
                connectionDialog.show();
            }
        });
    }

    private void init() {
        if (connectionDialog.isShowing()) { connectionDialog.dismiss(); }
        globalVM.splashScreenInit(new CompletionHandler<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean model) {
                if (connectionDialog.isShowing()) { connectionDialog.dismiss(); }
                navController.navigate(R.id.action_splashScreen_to_main);
            }

            @Override
            public void onFailure(@Nullable String description) {
                connectionDialog.show();
            }
        });
    }

    private void setUpDialog() {
        connectionDialog.setContentView(R.layout.dialog_internet_connection);
        connectionDialog.setCancelable(false);
        Button btn = connectionDialog.findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            if (loggedIn) {
                init();
            } else {
                getAreas();
            }
        });
    }
}
