package com.zeustech.excursions.ui.booking.payment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeustech.excursions.R;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.models.ExTicketId;
import com.zeustech.excursions.customViews.EventButton;
import com.zeustech.excursions.customViews.HybridEditText;
import com.zeustech.excursions.customViews.MenuButton;
import com.zeustech.excursions.customViews.SpanConstructor;
import com.zeustech.excursions.models.ExCardDetails;
import com.zeustech.excursions.models.ExPriceModel;
import com.zeustech.excursions.viewModels.GlobalVM;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;

public class PaymentDetailsFragment extends Fragment {

    private GlobalVM globalVM;
    private FragmentActivity activity;

    private PaymentRecyclerAdapter adapter;
    private LinearLayout parent_view;
    private RecyclerView recycler_view;
    private TextView total_payment;
    private EventButton payment_button;
    private HybridEditText card_name, card_number, cvv, email_field;
    private MenuButton exp_month, exp_year;

    private Dialog loadingDialog, successDialog, errorDialog;

    private HybridEditText.OnTextDidChangeListener textListener = new HybridEditText.OnTextDidChangeListener() {
        @Override
        public void onTextDidChanged(@NonNull Editable s) {
            payment_button.applyStyle(activity, validForm() ?
                    EventButton.Style.BLACK : EventButton.Style.TRANSPARENT);
        }
    };

    private MenuButton.OnMenuClickedListener menuListener = new MenuButton.OnMenuClickedListener() {
        @Override
        public void onClick(View view) {
            payment_button.applyStyle(activity, validForm() ?
                    EventButton.Style.BLACK : EventButton.Style.TRANSPARENT);
        }
    };

    public PaymentDetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent_view = view.findViewById(R.id.parent_view);
        recycler_view = view.findViewById(R.id.recycler_view);
        total_payment = view.findViewById(R.id.total_payment);
        payment_button = view.findViewById(R.id.payment_button);
        card_name = view.findViewById(R.id.card_name);
        card_number = view.findViewById(R.id.card_number);
        cvv = view.findViewById(R.id.cvv);
        email_field = view.findViewById(R.id.email_field);
        exp_month = view.findViewById(R.id.exp_month);
        exp_year = view.findViewById(R.id.exp_year);

        setUpView();
        setUpDialogs();
        setUpListeners();
        globalVM.getCartPrice().observe(getViewLifecycleOwner(), this::updateView);
    }

    private void updateView(ArrayList<ExPriceModel> data) {
        adapter.setDataSet(data);
        float sumPrice = 0;
        for (ExPriceModel priceModel : data) {
            sumPrice += priceModel.getPrice();
        }
        try {
            String formattedPrice = String.format(Locale.US, "%.2f", sumPrice);
            total_payment.setText(SpanConstructor.apply(
                    getResources().getString(R.string.total_payment),
                    String.format(getResources().getString(R.string.money_format), formattedPrice),
                    ContextCompat.getColor(activity, R.color.cooks_club_red)));
            payment_button.setText(String.format(getResources().getString(R.string.pay), formattedPrice));
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        }
    }

    private void setUpYearMenu() {
        Calendar calendar = Calendar.getInstance();
        List<String> years = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            years.add(String.valueOf(calendar.get(Calendar.YEAR)));
            calendar.add(Calendar.YEAR, 1);
        }
        for (String year : years) {
            exp_year.getMenu().getMenu().add(year);
        }
    }

    private void setUpView() {
        adapter = new PaymentRecyclerAdapter(activity);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        recycler_view.setHasFixedSize(true);
        email_field.setMinCharCount(1);
        card_number.setMinCharCount(9);
        card_name.setMinCharCount(1);
        cvv.setMinCharCount(3);
        setUpYearMenu();
        exp_month.getMenu().inflate(R.menu.month_list);
        payment_button.applyStyle(activity, EventButton.Style.TRANSPARENT);
    }

    private void setUpDialogs() {
        loadingDialog = new Dialog(activity);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.dialog_loading);
        TextView textView = loadingDialog.findViewById(R.id.text);
        textView.setText(getResources().getString(R.string.processing));

        successDialog = new Dialog(activity);
        successDialog.setContentView(R.layout.dialog_success);

        TextView top = successDialog.findViewById(R.id.text_top);
        top.setText(getResources().getString(R.string.thank_you));
        top.setVisibility(View.VISIBLE);

        TextView text = successDialog.findViewById(R.id.text);
        text.setText(getResources().getString(R.string.find_your_ticket));

        TextView bottom = successDialog.findViewById(R.id.text_bottom);
        bottom.setText(getResources().getString(R.string.have_fun));
        bottom.setVisibility(View.VISIBLE);

        DialogFragment parent = (DialogFragment) getParentFragment();
        if (parent != null) {
            successDialog.setOnDismissListener(dialog -> parent.dismiss());
        }

        errorDialog = new Dialog(activity);
        errorDialog.setContentView(R.layout.dialog_error);
    }

    private void setUpListeners() {

        exp_month.setOnMenuClickedListener(menuListener);
        exp_year.setOnMenuClickedListener(menuListener);
        card_name.setOnTextDidChangeListener(textListener);
        card_number.setOnTextDidChangeListener(textListener);
        cvv.setOnTextDidChangeListener(textListener);
        email_field.setOnTextDidChangeListener(textListener);

        payment_button.setOnClickListener( v -> {
            parent_view.requestFocus();
            if (validForm() && getCardDetails() != null) {
                ArrayList<ExPriceModel> sumPrice = globalVM.getCartPrice().getValue();
                if (sumPrice == null) { return; }
                fetchingStateUI(v, false);
                globalVM.bookNow(getCardDetails(), sumPrice, new CompletionHandler<List<ExTicketId>>() {
                    @Override
                    public void onSuccess(@NonNull List<ExTicketId> model) {
                        fetchingStateUI(v, true);
                        for (ExPriceModel price : sumPrice) {
                            globalVM.removeFromCart(price.getCode()); // 1# remove from basket
                        }
                        globalVM.addTickets(model); // #2 add tickets in database
                        serverResponseUI(true, null);
                    }

                    @Override
                    public void onFailure(@Nullable String description) {
                        fetchingStateUI(v, true);
                        serverResponseUI(false, description);
                    }
                });
            } else {
                applyValidationStyle();
            }
        });
    }

    private void fetchingStateUI(final View view, final boolean enabled) {
        activity.runOnUiThread(() -> {
            view.setEnabled(enabled);
            if (enabled) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            } else {
                loadingDialog.show();
            }
        });
    }

    private void serverResponseUI(final boolean success, final String message) {
        activity.runOnUiThread(() -> {
            if (success) {
                successDialog.show();
            } else {
                TextView textView = errorDialog.findViewById(R.id.text);
                textView.setText(message);
                errorDialog.show();
            }
        });
    }

    private boolean validForm() {
        if(!card_name.isValid()) { return false; }
        if(!card_number.isValid()) { return false; }
        if(!cvv.isValid()) { return false; }
        if(exp_month.getText().toString().isEmpty()) { return false; }
        if(exp_year.getText().toString().isEmpty()) { return false; }
        return email_field.isValid();
    }

    private void applyValidationStyle() {
        if(!card_name.isValid()) { card_name.setStrokeColor(R.color.red); }
        if(!card_number.isValid()) { card_number.setStrokeColor(R.color.red); }
        if(!cvv.isValid()) { cvv.setStrokeColor(R.color.red); }
        if(!email_field.isValid()) { email_field.setStrokeColor(R.color.red); }
    }

    private ExCardDetails getCardDetails() {
        Editable card_name = this.card_name.getText();
        Editable card_number = this.card_number.getText();
        Editable cvv = this.cvv.getText();
        Editable email = this.email_field.getText();
        String year = this.exp_year.getText().toString();
        String month = this.exp_month.getText().toString();
        if (card_name != null && card_number != null && cvv != null && email != null) {
            return new ExCardDetails(card_name.toString(), card_number.toString(),
                    cvv.toString(), year, month, email.toString().trim());
        }
        return null;
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
