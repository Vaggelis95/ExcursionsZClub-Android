package com.zeustech.excursions.ui.login;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zeustech.excursions.R;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.customViews.ToastMessage;
import com.zeustech.excursions.customViews.autoComplete.AutoCompleteListener;
import com.zeustech.excursions.customViews.autoComplete.AutoCompleteSearchView;
import com.zeustech.excursions.models.Area;
import com.zeustech.excursions.viewModels.GlobalVM;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    private GlobalVM globalVM;
    private NavController navController;

    private AutoCompleteSearchView areasField, hotelsField;
    private AreasAdapter areasAdapter;
    private HotelsAdapter hotelsAdapter;
    private ProgressBar progressBar;
    private Button loginButton;
    private ToastMessage toastMessage;

    private ImageView clearAreaBtn, clearHotelBtn;

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
        navController = Navigation.findNavController(view);

        areasField = view.findViewById(R.id.areas_field);
        hotelsField = view.findViewById(R.id.hotels_field);

        clearAreaBtn = view.findViewById(R.id.clear_area_btn);
        clearHotelBtn = view.findViewById(R.id.clear_hotel_btn);

        loginButton = view.findViewById(R.id.login_button);
        progressBar = view.findViewById(R.id.progress_bar);

        areasAdapter = new AreasAdapter(view.getContext());
        hotelsAdapter = new HotelsAdapter(view.getContext());

        areasField.setAdapter(areasAdapter);
        hotelsField.setAdapter(hotelsAdapter);

        setUpListeners();
        setUpToast();
    }

    private void setUpListeners() {

        loginButton.setOnClickListener(login -> {
            String hotelName = hotelsField.getText().toString();
            if (hotelName.isEmpty()) return;

            loginButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            globalVM.loginInit(hotelName, new CompletionHandler<Boolean>() {
                @Override
                public void onSuccess(@NonNull Boolean model) {
                    applyResponse(true, null);
                }

                @Override
                public void onFailure(@Nullable String description) {
                    applyResponse(false, description);
                }
            });
        });

        areasField.setAutoCompleteListener(new AutoCompleteListener() {
            @Override
            public void onTextChanged(@NonNull EditText editText, String text) {
                clearAreaBtn.setVisibility(text.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onFocusChanged(@NonNull EditText editText, String text, boolean hasFocus) { /* Ignore */ }

            @Override
            public void onDoneClicked(@NonNull EditText editText, String text) {
                // We Cannot perform click, it can be made only manually
                Area area = areasAdapter.getFirstSuggestion(text);
                if (area == null) return;
                editText.setText(area.getDescription());
                globalVM.getSelectedArea().setValue(area);
            }
        });

        areasAdapter.setOnItemClickListener(areasField, model ->
                globalVM.getSelectedArea().setValue(model));

        globalVM.getAreas().observe(getViewLifecycleOwner(), areas -> {
            if (areas != null && areas.size() > 0) {
                // #1 Set text manually
                areasField.setText(areas.get(0).getDescription());
                // #2 Update Selected Area
                globalVM.getSelectedArea().setValue(areas.get(0));
            }
            // #3 Update DataSet
            areasAdapter.setDataSet(areas != null ? areas : new ArrayList<>());
        });

        globalVM.getHotels().observe(getViewLifecycleOwner(), hotels -> {
            if (hotels != null && hotels.size() > 0) {
                hotelsField.setText(hotels.get(0).getHotelName());
            }
            hotelsAdapter.setDataSet(hotels != null ? hotels : new ArrayList<>());
        });

        clearHotelBtn.setOnClickListener(view -> hotelsField.setText(null));

        clearAreaBtn.setOnClickListener(view -> areasField.setText(null));

        hotelsField.setAutoCompleteListener(new AutoCompleteListener() {
            @Override
            public void onTextChanged(@NonNull EditText editText, String text) {
                clearHotelBtn.setVisibility(text.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onFocusChanged(@NonNull EditText editText, String text, boolean hasFocus) { }

            @Override
            public void onDoneClicked(@NonNull EditText editText, String text) { }
        });
    }

    private void applyResponse(boolean success, @Nullable String message) {
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setEnabled(true);
        if (success) {
            navController.navigate(R.id.action_login_to_main);
        } else {
            if (toastMessage != null) {
                toastMessage.setText(message);
                toastMessage.show();
            }
        }
    }

    private void setUpToast() {
        if (getActivity() != null) {
            toastMessage = new ToastMessage(getActivity());
            toastMessage
                    .setTextColor(Color.WHITE)
                    .setBackgroundColor(Color.parseColor("#003460"))
                    .setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 220);
        }
    }

}
