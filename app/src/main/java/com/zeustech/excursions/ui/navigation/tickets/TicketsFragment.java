package com.zeustech.excursions.ui.navigation.tickets;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustech.excursions.R;
import com.zeustech.excursions.ui.navigation.ticketDetails.TicketDetails;
import com.zeustech.excursions.viewModels.GlobalVM;

public class TicketsFragment extends Fragment {

    private GlobalVM globalVM;
    private FragmentActivity activity;
    private SwipeRefreshLayout swipe_layout;
    private TicketsRecyclerAdapter adapter;
    private final Handler handler = new Handler();

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalVM.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe_layout = view.findViewById(R.id.swipe_layout);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        adapter = new TicketsRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        setUpListeners();
    }

    private void stopRefreshing() {
        if (swipe_layout.isRefreshing()) {
            swipe_layout.setRefreshing(false);
        }
    }

    private void setUpListeners() {
        swipe_layout.setOnRefreshListener(() -> {
            handler.postDelayed(this::stopRefreshing, 5000);
            globalVM.refreshTickets();
        });
        globalVM.getTickets().observe(getViewLifecycleOwner(), ticketModels -> {
            stopRefreshing();
            if (ticketModels == null) { return; }
            adapter.setDataSet(ticketModels);
        });
        adapter.setOnExcursionClickListener(ticket -> TicketDetails.newInstance(ticket)
                .show(activity.getSupportFragmentManager(), TicketDetails.class.getName()));
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
