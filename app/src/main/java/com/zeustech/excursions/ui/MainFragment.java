package com.zeustech.excursions.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.OnBasketClickListener;
import com.zeustech.excursions.database.DatabaseManager;
import com.zeustech.excursions.ui.navigation.basket.BasketFragment;
import com.zeustech.excursions.ui.navigation.excursionDetails.ExcursionDetails;
import com.zeustech.excursions.ui.navigation.excursionsList.ExcursionsFragment;
import com.zeustech.excursions.ui.navigation.tickets.TicketsFragment;
import com.zeustech.excursions.viewModels.GlobalVM;

public class MainFragment extends Fragment implements OnBasketClickListener {

    private GlobalVM globalVM;

    private ViewPager view_pager;
    private ExPagerAdapter adapter;
    private TextView headline;
    private BottomNavigationView navigation_bar;
    private NavController navController;

    private ImageView logoutImg, searchBtn;

    private Dialog logoutDialog;
    private Button logoutBtn;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalVM.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view_pager = view.findViewById(R.id.view_pager);
        headline = view.findViewById(R.id.headline);
        logoutImg = view.findViewById(R.id.logout_img);
        searchBtn = view.findViewById(R.id.search_btn);
        navigation_bar = view.findViewById(R.id.navigation_bar);

        logoutDialog = new Dialog(view.getContext(), R.style.PopUpDialogTheme);
        logoutDialog.setContentView(R.layout.dialog_logout);
        logoutBtn = logoutDialog.findViewById(R.id.logout_btn);
        Button cancelBtn = logoutDialog.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(cancel ->
                logoutDialog.dismiss());
        adapter = new ExPagerAdapter(getChildFragmentManager());
        view_pager.setAdapter(adapter);
        setUpListeners();
    }

    private void setUpListeners() {
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0 : {
                        navigation_bar.setSelectedItemId(R.id.excursions_home);
                        headline.setText(R.string.your_next_adventure);
                        searchBtn.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 1 : {
                        navigation_bar.setSelectedItemId(R.id.basket);
                        headline.setText(R.string.my_shopping_cart);
                        searchBtn.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case 2 : {
                        navigation_bar.setSelectedItemId(R.id.tickets);
                        headline.setText(R.string.my_tickets);
                        searchBtn.setVisibility(View.INVISIBLE);
                        break;
                    }
                    default: break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        navigation_bar.setOnNavigationItemSelectedListener(menuItem -> {
            int index = view_pager.getCurrentItem();
            switch (menuItem.getItemId()) {
                case R.id.excursions_home: {
                    if (index != 0 && adapter.getCount() > 0) {
                        view_pager.setCurrentItem(0);
                    }
                    break;
                }
                case R.id.basket: {
                    if (index != 1 && adapter.getCount() > 1) {
                        view_pager.setCurrentItem(1);
                    }
                    break;
                }
                case R.id.tickets: {
                    if (index != 2 && adapter.getCount() > 2) {
                        view_pager.setCurrentItem(2);
                    }
                    break;
                }
                default: break;
            }
            return true;
        });

        searchBtn.setOnClickListener(view -> globalVM.triggerSearch());

        logoutImg.setOnClickListener(view -> logoutDialog.show());
        logoutBtn.setOnClickListener(view -> {
            try {
                DatabaseManager.getInstance(getContext()).eraseDatabase();
                logoutDialog.dismiss();
                navController.navigate(R.id.action_main_to_splashScreen);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick() {
        if (adapter.getCount() < 1) { return; }
        view_pager.setCurrentItem(1);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof ExcursionDetails) {
            ExcursionDetails fragment = (ExcursionDetails) childFragment;
            fragment.setOnBasketClickListener(this);
        }
    }

    private static class ExPagerAdapter extends FragmentStatePagerAdapter {

        ExPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1 : {
                    return new BasketFragment();
                }
                case 2 : {
                    return new TicketsFragment();
                }
                default: return new ExcursionsFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

}
