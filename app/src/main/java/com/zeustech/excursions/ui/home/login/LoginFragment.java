package com.zeustech.excursions.ui.home.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.zeustech.excursions.R;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.ui.home.HomeFragmentListener;
import com.zeustech.excursions.viewModels.GlobalVM;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private GlobalVM globalVM;
    private HomeFragmentListener homeFragmentListener;

    private EditText usernameField, passwordField;
    private Button loginBtn;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalVM.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameField = view.findViewById(R.id.username_field);
        passwordField = view.findViewById(R.id.password_field);
        progressBar = view.findViewById(R.id.progress_bar);
        loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
    }

    public void setHomeFragmentListener(HomeFragmentListener homeFragmentListener) {
        this.homeFragmentListener = homeFragmentListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            applyUIState(0, null);
            globalVM.login(username, password, new CompletionHandler<Boolean>() {
                @Override
                public void onSuccess(@NonNull Boolean model, int status) {
                    applyUIState(1, null);
                }

                @Override
                public void onFailure(@Nullable String description, int status) {
                    applyUIState(2, description);
                }
            });
        }
    }

    private void applyUIState(int state, @Nullable String message) {
        // state == 0 -> Fetching State
        loginBtn.setEnabled(state != 0);
        progressBar.setVisibility(state == 0 ? View.VISIBLE : View.INVISIBLE);
        switch (state) {
            case 1: { // Success
                if (homeFragmentListener != null) {
                    homeFragmentListener.onSuccessfulLogin();
                }
                break;
            }
            case 2: { // Failure
                if (homeFragmentListener != null) {
                    homeFragmentListener.onErrorMessage(message);
                }
                break;
            }
            default: break;
        }
    }

}
