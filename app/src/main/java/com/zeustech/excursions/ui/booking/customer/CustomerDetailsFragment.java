package com.zeustech.excursions.ui.booking.customer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zeustech.excursions.R;
import com.zeustech.excursions.customViews.EventButton;
import com.zeustech.excursions.customViews.HybridEditText;
import com.zeustech.excursions.models.ExCustomerInfo;
import com.zeustech.excursions.viewModels.GlobalVM;

public class CustomerDetailsFragment extends Fragment {

    private FragmentActivity activity;

    private GlobalVM globalVM;
    private OnNextClickListener onNextClickListener;

    private HybridEditText.OnTextDidChangeListener textListener = new HybridEditText.OnTextDidChangeListener() {
        @Override
        public void onTextDidChanged(@NonNull Editable s) {
            next_button.applyStyle(activity, validForm() ?
                    EventButton.Style.BLACK : EventButton.Style.TRANSPARENT);
        }
    };

    private LinearLayout parent_view;
    private HybridEditText first_name, last_name, room_number, remarks;
    private EventButton next_button;

    public interface OnNextClickListener {
        void onNextClicked();
    }

    public CustomerDetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_customer_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent_view = view.findViewById(R.id.parent_view);
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        room_number = view.findViewById(R.id.room_number);
        remarks = view.findViewById(R.id.remarks);
        next_button = view.findViewById(R.id.next_button);
        setUpView();
        setUpListeners();
    }

    private void setUpView() {
        first_name.setMinCharCount(1);
        last_name.setMinCharCount(1);
        room_number.setMinCharCount(1);
        next_button.applyStyle(activity, EventButton.Style.TRANSPARENT);
    }

    private void setUpListeners() {
        first_name.setOnTextDidChangeListener(textListener);
        last_name.setOnTextDidChangeListener(textListener);
        room_number.setOnTextDidChangeListener(textListener);
        next_button.setOnClickListener(v -> {
            parent_view.requestFocus();
            Editable first = first_name.getText();
            Editable last = last_name.getText();
            Editable room = room_number.getText();
            if (validForm() && first != null && last != null && room != null && onNextClickListener != null) {
                ExCustomerInfo customerInfo = new ExCustomerInfo(first.toString() + " " + last.toString(),
                        room.toString(), remarks.getText() != null ? remarks.getText().toString() : "");

                globalVM.setCustomerInfo(customerInfo);
                onNextClickListener.onNextClicked();
            } else {
                applyValidationStyle();
            }
        });
    }

    private boolean validForm() {
        if(!first_name.isValid()) { return false; }
        if(!last_name.isValid()) { return false; }
        return room_number.isValid();
    }

    private void applyValidationStyle() {
        if(!first_name.isValid()) { first_name.setStrokeColor(R.color.red); }
        if(!last_name.isValid()) { last_name.setStrokeColor(R.color.red); }
        if(!room_number.isValid()) { room_number.setStrokeColor(R.color.red); }
    }

    public void setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.onNextClickListener = onNextClickListener;
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
