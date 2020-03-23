package com.zeustech.excursions.ui.home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.make.dots.dotsindicator.DotsIndicator;
import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.CustomViewPager;
import com.zeustech.excursions.customViews.ToastMessage;
import com.zeustech.excursions.ui.home.login.LoginFragment;
import com.zeustech.excursions.ui.home.placeChooser.PlaceChooserFragment;

public class HomeFragment extends Fragment implements HomeFragmentListener {

    private NavController navController;
    private CustomViewPager viewPager;
    private HomePagerAdapter adapter;
    private ToastMessage tMessage;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        DotsIndicator tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        adapter = new HomePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
        tabLayout.setViewPager(viewPager);
        setUpToast();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof LoginFragment) {
            LoginFragment loginFragment = (LoginFragment) childFragment;
            loginFragment.setHomeFragmentListener(this);
        } else if (childFragment instanceof PlaceChooserFragment) {
            PlaceChooserFragment pcFragment = (PlaceChooserFragment) childFragment;
            pcFragment.setHomeFragmentListener(this);
        }
    }

    @Override
    public void onSuccessfulLogin() {
        if (adapter.getCount() > 1) viewPager.setCurrentItem(1);
    }

    @Override
    public void onSuccessfulPlaceSelection() {
        navController.navigate(R.id.action_home_to_main);
    }

    @Override
    public void onErrorMessage(@Nullable String message) {
        if (tMessage == null) return;
        tMessage.setText(message);
        tMessage.show();
    }

    private void setUpToast() {
        if (getActivity() == null) return;
        tMessage = new ToastMessage(getActivity());
        tMessage.setTextColor(Color.WHITE)
                .setBackgroundColor(Color.parseColor("#003460"))
                .setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 220);
    }

    private static class HomePagerAdapter extends FragmentStatePagerAdapter {

        HomePagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new LoginFragment() : new PlaceChooserFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}
