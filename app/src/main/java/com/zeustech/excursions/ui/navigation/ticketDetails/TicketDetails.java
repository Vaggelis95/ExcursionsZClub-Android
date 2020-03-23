package com.zeustech.excursions.ui.navigation.ticketDetails;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.zeustech.excursions.R;
import com.zeustech.excursions.bixolon.manager.BixolonManager;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.customViews.BarcodeGenerator;
import com.zeustech.excursions.models.ExTicketModel;
import com.zeustech.excursions.tools.ScreenDialogFragment;

public class TicketDetails extends ScreenDialogFragment {

    private enum State {
        LOADING,
        ERROR,
        SUCCESS
    }

    private static final String TICKET_INSTRUCTIONS = "ticket_instructions";
    private ExTicketModel model;

    private TextView description, ticket, customer, adults, children, date, hotel,
            room, pickUpPoint, pickUpTime, price, transactionDate, company_details;

    private ImageView barcode_holder, back_arrow;
    private Button printTicketBtn;

    private Dialog loadingDialog, errorDialog;

    public TicketDetails() {
        // Required empty public constructor
    }

    public static TicketDetails newInstance(ExTicketModel ticket) {
        TicketDetails fragment = new TicketDetails();
        Bundle args = new Bundle();
        args.putParcelable(TICKET_INSTRUCTIONS, ticket);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getParcelable(TICKET_INSTRUCTIONS);
        }
        setFullScreen(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        description = view.findViewById(R.id.description);
        ticket = view.findViewById(R.id.ticket);
        customer = view.findViewById(R.id.customer);
        adults = view.findViewById(R.id.adults);
        children = view.findViewById(R.id.children);
        date = view.findViewById(R.id.date);
        hotel = view.findViewById(R.id.hotel);
        room = view.findViewById(R.id.room);
        pickUpPoint = view.findViewById(R.id.pickUpPoint);
        pickUpTime = view.findViewById(R.id.pickUpTime);
        price = view.findViewById(R.id.price);
        transactionDate = view.findViewById(R.id.transactionDate);
        company_details = view.findViewById(R.id.company_details);
        barcode_holder = view.findViewById(R.id.barcode_holder);
        back_arrow = view.findViewById(R.id.back_arrow);
        printTicketBtn = view.findViewById(R.id.print_ticket_btn);
        setUpDialogs(view);
        if (model != null) initView();
    }

    private void setUpDialogs(@NonNull View view) {
        loadingDialog = new Dialog(view.getContext(), R.style.PopUpDialogTheme);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);

        errorDialog = new Dialog(view.getContext(), R.style.PopUpDialogTheme);
        errorDialog.setContentView(R.layout.dialog_error);
    }

    private void initView() {
        description.setText(model.getExcDescr());
        ticket.setText(model.getTicket());
        customer.setText(model.getName());
        adults.setText(String.valueOf(model.getAdults()));
        children.setText(String.valueOf(model.getChildren()));
        hotel.setText(model.getHotel());
        room.setText(model.getRoomCode());
        pickUpTime.setText(model.getPickUpTime());
        pickUpPoint.setText(model.getPickUpPoint());
        price.setText(String.format(getResources().getString(R.string.money_format), model.getFormattedPrice()));
        String transDate = model.getTransDate();
        if (transDate != null) {
            transactionDate.setText(model.getFormattedTransDate());
        }
        date.setText(model.getFormattedExcDate());
        String extraInfo = String.format(getResources().getString(R.string.company_details),
                model.getCompanyDescr(), model.getAddress(), model.getZip(),
                model.getVrn(), model.getDoy(), model.getGnto(), model.getTel(), model.getExtraInfo());
        company_details.setText(extraInfo);
        try {
            barcode_holder.setImageBitmap(BarcodeGenerator.generate(model.getTicket(), 450, 450));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        back_arrow.setOnClickListener(v -> dismiss());
        printTicketBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (model == null) return;
        applyState(view, State.LOADING, null);
        if (getContext() == null) return;
        BixolonManager.getInstance(getContext())
                .connectPrintDisconnect(model, new CompletionHandler<String>() {
                    @Override
                    public void onSuccess(@NonNull String model, int status) {
                        applyState(view, State.SUCCESS, model);
                    }

                    @Override
                    public void onFailure(@Nullable String description, int status) {
                        applyState(view, State.ERROR, description);
                    }
                });
    }

    private void applyState(@NonNull View view, @NonNull State type, @Nullable String message) {
        switch (type) {
            case ERROR: {
                if(loadingDialog.isShowing()) loadingDialog.dismiss();
                view.setEnabled(true);
                TextView tv = errorDialog.findViewById(R.id.text);
                tv.setText(message);
                errorDialog.show();
                break;
            }
            case LOADING: {
                view.setEnabled(false);
                loadingDialog.show();
                break;
            }
            case SUCCESS: {
                if(loadingDialog.isShowing()) loadingDialog.dismiss();
                view.setEnabled(true);
                break;
            }
            default: break;
        }
    }
}
