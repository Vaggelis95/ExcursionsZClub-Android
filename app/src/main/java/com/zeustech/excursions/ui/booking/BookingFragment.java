package com.zeustech.excursions.ui.booking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustech.excursions.R;

import com.zeustech.excursions.tools.ScreenDialogFragment;
import com.zeustech.excursions.ui.booking.customer.CustomerDetailsFragment;
import com.zeustech.excursions.ui.booking.details.BookingDetailsFragment;
import com.zeustech.excursions.ui.booking.payment.PaymentDetailsFragment;
import com.zeustech.excursions.customViews.CustomViewPager;
import com.zeustech.excursions.customViews.OnBasketClickListener;
import com.zeustech.excursions.viewModels.GlobalVM;

public class BookingFragment extends ScreenDialogFragment {

    private static final String PAGE_INDEX = "index";

    private GlobalVM globalVM;

    private CustomViewPager view_pager;
    private BookingPagerAdapter adapter;
    private ImageView back_arrow, basket_logo;
    private TextView description, basket_counter;

    private int index = 0;

    private OnBasketClickListener onBasketClickListener;

    private View.OnClickListener onClickListener = v -> {
        dismiss();
        if (onBasketClickListener == null) { return; }
        onBasketClickListener.onClick();
    };

    public void setOnBasketClickListener(OnBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    public static BookingFragment newInstance(int index) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(PAGE_INDEX);
        }
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalVM.class);
        setFullScreen(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back_arrow = view.findViewById(R.id.back_arrow);
        description = view.findViewById(R.id.description);
        view_pager = view.findViewById(R.id.view_pager);
        basket_logo = view.findViewById(R.id.basket_logo);
        basket_counter = view.findViewById(R.id.basket_counter);

        adapter = new BookingPagerAdapter(getChildFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.setPagingEnabled(false);
        setUpListeners();
        if (index == 1) { moveNext(); }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof BookingDetailsFragment) {
            BookingDetailsFragment firstFrag = (BookingDetailsFragment) childFragment;
            firstFrag.setOnNextClickListener(this::moveNext);
        } else if (childFragment instanceof CustomerDetailsFragment) {
            CustomerDetailsFragment secondFrag = (CustomerDetailsFragment) childFragment;
            secondFrag.setOnNextClickListener(this::moveNext);
        }
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

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0 : {
                        description.setText(R.string.booking_details);
                        break;
                    }
                    case 1 : {
                        description.setText(R.string.customer_details);
                        break;
                    }
                    case 2 : {
                        description.setText(R.string.payment);
                        break;
                    }
                    default: break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
    }

    private void moveNext() {
        int current_index = view_pager.getCurrentItem();
        if (current_index >= 0 && current_index + 1 < adapter.getCount()) {
            view_pager.setCurrentItem(++current_index);
        }
    }

    private static class BookingPagerAdapter extends FragmentStatePagerAdapter {

        BookingPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1 : {
                    return new CustomerDetailsFragment();
                }
                case 2 : {
                    return new PaymentDetailsFragment();
                }
                default: return new BookingDetailsFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

}
