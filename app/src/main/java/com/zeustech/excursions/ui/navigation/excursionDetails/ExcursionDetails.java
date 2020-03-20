package com.zeustech.excursions.ui.navigation.excursionDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeustech.excursions.R;
import com.zeustech.excursions.tools.ScreenDialogFragment;
import com.zeustech.excursions.ui.booking.BookingFragment;
import com.zeustech.excursions.customViews.OnBasketClickListener;
import com.zeustech.excursions.customViews.EventButton;
import com.zeustech.excursions.models.ExBookingData;
import com.zeustech.excursions.models.ExcursionModel;
import com.zeustech.excursions.viewModels.GlobalVM;

import java.util.ArrayList;
import java.util.Arrays;

public class ExcursionDetails extends ScreenDialogFragment {

    private static final String EXC = "exc";
    private static final String EXC_NAME = "exc_name";

    private GlobalVM globalVM;

    private String exc_name;
    private ExcursionModel exc;

    private ImageView back_arrow, main_pick, basket_logo;
    private TextView exc_name_tv, exc_description, basket_counter;
    private RecyclerView recycler_view;
    private DetailsRecyclerAdapter adapter;
    private EventButton book_button;
    private View separator_line;
    private TextView gallery_header;

    private OnBasketClickListener onBasketClickListener;

    private View.OnClickListener onClickListener = v -> onBasketClicked();

    public void setOnBasketClickListener(OnBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    private void onBasketClicked() {
        dismiss();
        if (onBasketClickListener == null) { return; }
        onBasketClickListener.onClick();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof BookingFragment) {
            BookingFragment dialogFragment = (BookingFragment) childFragment;
            dialogFragment.setOnBasketClickListener(this::onBasketClicked);
        }
    }

    public ExcursionDetails() {
        // Required empty public constructor
    }

    public static ExcursionDetails newInstance(String exc_name, ExcursionModel exc) {
        ExcursionDetails fragment = new ExcursionDetails();
        Bundle args = new Bundle();
        args.putString(EXC_NAME, exc_name);
        args.putParcelable(EXC, exc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exc_name = getArguments().getString(EXC_NAME);
            exc = getArguments().getParcelable(EXC);
        }
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalVM.class);
        setFullScreen(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_excursion_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back_arrow = view.findViewById(R.id.back_arrow);
        main_pick = view.findViewById(R.id.main_pick);
        exc_name_tv = view.findViewById(R.id.exc_name);
        exc_description = view.findViewById(R.id.exc_description);
        recycler_view = view.findViewById(R.id.recycler_view);
        book_button = view.findViewById(R.id.book_button);
        basket_counter = view.findViewById(R.id.basket_counter);
        basket_logo = view.findViewById(R.id.basket_logo);
        separator_line = view.findViewById(R.id.separator_line);
        gallery_header = view.findViewById(R.id.gallery_header);

        adapter = new DetailsRecyclerAdapter(recycler_view.getContext(), (exc.getPicGallery() != null) ?
                Arrays.asList(exc.getPicGallery()) : new ArrayList<>());
        recycler_view.setAdapter(adapter);

        recycler_view.setLayoutManager(new LinearLayoutManager(recycler_view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        try {
            setUpView();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setUpListeners();
    }

    private void setUpListeners() {

        basket_counter.setOnClickListener(onClickListener);
        basket_logo.setOnClickListener(onClickListener);

        back_arrow.setOnClickListener(v -> dismiss());

        globalVM.getCart().observe(getViewLifecycleOwner(), basketModels -> {
            if (basketModels == null || basketModels.size() == 0) {
                basket_counter.setVisibility(View.INVISIBLE);
                basket_logo.setVisibility(View.INVISIBLE);
            } else {
                basket_counter.setText(String.valueOf(basketModels.size()));
                basket_counter.setVisibility(View.VISIBLE);
                basket_logo.setVisibility(View.VISIBLE);
            }
        });

        adapter.setOnImageClickListener((position, pickPath) -> {
            recycler_view.smoothScrollToPosition(position);
            ImageDisplayFragment.newInstance(pickPath).show(
                    getChildFragmentManager(),
                    ImageDisplayFragment.class.getSimpleName()
            );
        });

        book_button.setOnClickListener(v -> {
            v.setEnabled(false);
            moveToBookingFragment();
            new Handler().postDelayed(() -> v.setEnabled(true), 2000);
        });
    }

    private void moveToBookingFragment() {
        globalVM.clearData(); // SOS
        globalVM.setExData(new ExBookingData(exc.getCode(), exc_name, exc.getMainPicPath()));
        BookingFragment.newInstance(0).show(getChildFragmentManager(), BookingFragment.class.getSimpleName());
    }

    private void setUpView() throws NullPointerException {
        book_button.applyStyle(book_button.getContext(), EventButton.Style.BLACK);
        Glide.with(main_pick.getContext()).load(exc.getMainPicPath()).into(main_pick);
        exc_name_tv.setText(exc_name);
        exc_description.setText(exc.getDescription());
        separator_line.setVisibility((exc.getPicGallery() != null) ? View.VISIBLE : View.GONE);
        gallery_header.setVisibility((exc.getPicGallery() != null) ? View.VISIBLE : View.GONE);
    }
}
