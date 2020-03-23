package com.zeustech.excursions.ui.home.placeChooser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.autoComplete.AutoCompleteListener;
import com.zeustech.excursions.customViews.autoComplete.AutoCompleteSearchView;
import com.zeustech.excursions.models.Area;
import com.zeustech.excursions.ui.home.HomeFragmentListener;
import com.zeustech.excursions.viewModels.GlobalVM;

import java.util.ArrayList;

public class PlaceChooserFragment extends Fragment implements View.OnClickListener {

    private GlobalVM globalVM;
    private HomeFragmentListener homeFragmentListener;

    private SwipeRefreshLayout swipeLayout;
    private AutoCompleteSearchView areasField, hotelsField;
    private AreasAdapter areasAdapter;
    private HotelsAdapter hotelsAdapter;
    private ProgressBar progressBar;
    private Button chooseHotelBtn;

    private ImageView clearAreaBtn, clearHotelBtn;

    public PlaceChooserFragment() {
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
        return inflater.inflate(R.layout.fragment_place_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeLayout = view.findViewById(R.id.swipe_layout);
        areasField = view.findViewById(R.id.areas_field);
        hotelsField = view.findViewById(R.id.hotels_field);
        clearAreaBtn = view.findViewById(R.id.clear_area_btn);
        clearHotelBtn = view.findViewById(R.id.clear_hotel_btn);
        chooseHotelBtn = view.findViewById(R.id.choose_hotel_btn);
        progressBar = view.findViewById(R.id.progress_bar);

        areasAdapter = new AreasAdapter(view.getContext());
        hotelsAdapter = new HotelsAdapter(view.getContext());
        areasField.setAdapter(areasAdapter);
        hotelsField.setAdapter(hotelsAdapter);

        globalVM.getAreas(null); // Refresh Areas
        setUpListeners();
    }

    public void setHomeFragmentListener(HomeFragmentListener homeFragmentListener) {
        this.homeFragmentListener = homeFragmentListener;
    }

    private void setUpListeners() {

        swipeLayout.setOnRefreshListener(() -> {
            globalVM.getAreas(null);
            new Handler().postDelayed(() -> {
                if (swipeLayout.isRefreshing()) swipeLayout.setRefreshing(false);
            }, 3000);
        });

        chooseHotelBtn.setOnClickListener(this);

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

        areasAdapter.setOnItemClickListener(areasField, model -> globalVM.getSelectedArea().setValue(model));

        globalVM.getAreas().observe(getViewLifecycleOwner(), areas -> {
            if (swipeLayout.isRefreshing()) swipeLayout.setRefreshing(false);

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.choose_hotel_btn) {
            String hotelName = hotelsField.getText().toString();
            if (hotelName.isEmpty()) return;
            applyUIState(0, null);
            globalVM.initLogin(hotelName, (success, message) -> applyUIState(success ? 1 : 2, message));
        }
    }

    private void applyUIState(int state, @Nullable String message) {
        // state == 0 -> Process State
        chooseHotelBtn.setEnabled(state != 0);
        progressBar.setVisibility(state == 0 ? View.VISIBLE : View.INVISIBLE);
        switch (state) {
            case 1: { // Success
                if (homeFragmentListener != null) {
                    homeFragmentListener.onSuccessfulPlaceSelection();
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
