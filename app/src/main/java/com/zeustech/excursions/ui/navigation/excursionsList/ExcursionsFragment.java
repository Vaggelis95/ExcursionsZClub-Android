package com.zeustech.excursions.ui.navigation.excursionsList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustech.excursions.R;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.customViews.autoComplete.AutoCompleteListener;
import com.zeustech.excursions.customViews.autoComplete.AutoCompleteSearchView;
import com.zeustech.excursions.models.ExcursionModel;
import com.zeustech.excursions.models.ExcursionsModel;
import com.zeustech.excursions.ui.navigation.excursionDetails.ExcursionDetails;
import com.zeustech.excursions.viewModels.GlobalVM;

import java.util.ArrayList;
import java.util.List;

public class ExcursionsFragment extends Fragment {

    private GlobalVM globalVM;
    private ExcursionRecyclerAdapter adapter;
    private SwipeRefreshLayout swipe_layout;
    private TextView noExField;

    private ConstraintLayout searchContainer;
    private AutoCompleteSearchView searchField;
    private SearchAdapter searchAdapter;
    private ImageView clearSearchBtn;

    private Handler handler = new Handler();

    public ExcursionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalVM.class);
        //excursionsVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(ExcursionsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_excursions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        searchContainer = view.findViewById(R.id.search_container);
        swipe_layout = view.findViewById(R.id.swipe_layout);
        noExField = view.findViewById(R.id.no_ex_field);
        searchField = view.findViewById(R.id.search_field);
        clearSearchBtn = view.findViewById(R.id.clear_search_btn);

        adapter = new ExcursionRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        searchAdapter = new SearchAdapter(view.getContext());
        searchField.setAdapter(searchAdapter);

        setUpListeners();
    }

    private void stopRefreshing() {
        if (swipe_layout.isRefreshing()) {
            swipe_layout.setRefreshing(false);
        }
    }

    private void setDataSet(@NonNull List<ExcursionsModel> dataSet, boolean updateSearchBar) {
        if (swipe_layout.isRefreshing()) {
            swipe_layout.setRefreshing(false);
        }
        noExField.setVisibility(dataSet.size() > 0 ? View.GONE : View.VISIBLE);
        adapter.setDataSet(dataSet);
        if (updateSearchBar) searchAdapter.setDataSet(dataSet);
    }

    private void setUpListeners() {

        clearSearchBtn.setOnClickListener(view -> searchField.setText(null));

        globalVM.getSearchTrigger().observe(getViewLifecycleOwner(), aBoolean -> {
            boolean alreadyVisible = searchContainer.getVisibility() == View.VISIBLE;
            boolean visible = aBoolean != null ? aBoolean : true;
            if (alreadyVisible != visible) {
                searchContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
                if (visible) searchField.requestFocus();
            }
        });

        searchField.setAutoCompleteListener(new AutoCompleteListener() {
            @Override
            public void onTextChanged(@NonNull EditText editText, String text) {
                globalVM.getFilterText().setValue(text);
                clearSearchBtn.setVisibility(text.isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onFocusChanged(@NonNull EditText editText, String text, boolean hasFocus) { }

            @Override
            public void onDoneClicked(@NonNull EditText editText, String text) {
                globalVM.getFilterText().setValue(text);
            }
        });

        globalVM.getFilteredExcursions().observe(getViewLifecycleOwner(), excursions ->
                setDataSet(excursions != null ? excursions : new ArrayList<>(), false));

        globalVM.getExcursions().observe(getViewLifecycleOwner(), excursions ->
                setDataSet(excursions != null ? excursions : new ArrayList<>(), true));

        swipe_layout.setOnRefreshListener(() -> {
            handler.postDelayed(this::stopRefreshing, 3000);
            globalVM.refreshExcursions();
        });

        adapter.setOnExcursionClickListener((exc_code, exc_name) -> {
            adapter.setUserInteractionEnabled(false);
            globalVM.getExcursion(exc_code, new CompletionHandler<ExcursionModel>() {
                @Override
                public void onSuccess(@NonNull ExcursionModel model) {
                    if (getParentFragment() != null) {
                        ExcursionDetails
                                .newInstance(exc_name, model)
                                .show(getParentFragment().getChildFragmentManager(), ExcursionDetails.class.getSimpleName());
                    }
                    new Handler().postDelayed(() -> adapter.setUserInteractionEnabled(true), 1000);
                }

                @Override
                public void onFailure(@Nullable String description) {
                    new Handler().postDelayed(() -> adapter.setUserInteractionEnabled(true), 1000);
                }
            });
        });
    }
}
