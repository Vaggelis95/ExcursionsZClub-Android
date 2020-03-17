package com.zeustech.excursions.ui.navigation.basket;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zeustech.excursions.R;
import com.zeustech.excursions.ui.booking.BookingFragment;
import com.zeustech.excursions.models.CartModel;
import com.zeustech.excursions.customViews.ToastMessage;
import com.zeustech.excursions.viewModels.GlobalVM;

import java.util.List;

public class BasketFragment extends Fragment {

    private GlobalVM globalVM;
    private FragmentActivity activity;

    private BasketRecyclerAdapter adapter;
    private Button book_button;
    private ToastMessage toastMessage;

    public BasketFragment() {
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
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        book_button = view.findViewById(R.id.book_button);

        adapter = new BasketRecyclerAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        toastMessage = new ToastMessage(activity);
        toastMessage.setTextColor(Color.WHITE).setBackgroundColor(Color.BLACK)
                .setText(activity.getResources().getString(R.string.empty_basket));
        toastMessage.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 240);
        setUpListeners();
    }

    private void setUpListeners() {
        globalVM.getCart().observe(getViewLifecycleOwner(), cart -> {
            if (cart == null) { return; }
            adapter.submitList(cart);
        });
        adapter.setOnDeleteListener(exCode -> globalVM.removeFromCart(exCode));
        book_button.setOnClickListener(v -> {
            List<CartModel> basketList = globalVM.getCart().getValue();
            if (basketList == null || basketList.size() == 0) {
                toastMessage.show();
            } else {
                globalVM.clearData(); // SOS
                BookingFragment.newInstance(1).show(getChildFragmentManager(), BookingFragment.class.getSimpleName());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}
