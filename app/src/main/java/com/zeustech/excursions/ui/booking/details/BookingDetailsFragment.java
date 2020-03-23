package com.zeustech.excursions.ui.booking.details;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.zeustech.excursions.R;
import com.zeustech.excursions.models.CartModel;
import com.zeustech.excursions.customViews.EventButton;
import com.zeustech.excursions.customViews.MenuButton;
import com.zeustech.excursions.customViews.SpanConstructor;
import com.zeustech.excursions.customViews.ToastMessage;
import com.zeustech.excursions.models.ExLanguageModel;
import com.zeustech.excursions.models.ExPriceModel;
import com.zeustech.excursions.customViews.DateManager;
import com.zeustech.excursions.tools.ColorManager;
import com.zeustech.excursions.viewModels.GlobalVM;

import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.Locale;

public class BookingDetailsFragment extends Fragment implements MenuButton.OnMenuClickedListener {

    private GlobalVM globalVM;
    private FragmentActivity activity;
    private OnNextClickListener onNextClickListener;

    private TextView includedLabel;
    private IncludedRecyclerAdapter includedAdapter;

    private MaterialCalendarView calendarView;
    private ImageView backButton, forwardButton;
    private TextView currentMonthHeadline;

    private TextView selectedDateField, excursionNameField, pickUpPointField, pickUpTimeField, priceField;
    private MenuButton adultsBtn, childrenBtn, infantsBtn, languageBtn;
    private EventButton cartBtn, nextBtn;
    private ToastMessage toastMessage;
    private Dialog messageDialog;

    private String mainPickPath;

    public interface OnNextClickListener {
        void onNextClicked();
    }

    public BookingDetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_booking_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView includedRecyclerView = view.findViewById(R.id.included_recycler_view);
        includedLabel = view.findViewById(R.id.included_label);

        calendarView = view.findViewById(R.id.calendar_view);
        backButton = view.findViewById(R.id.back_button);
        forwardButton = view.findViewById(R.id.forward_button);
        currentMonthHeadline = view.findViewById(R.id.current_month_headline);

        selectedDateField = view.findViewById(R.id.selected_days);
        excursionNameField = view.findViewById(R.id.exc_name);
        pickUpPointField = view.findViewById(R.id.pickup_point);
        pickUpTimeField = view.findViewById(R.id.pickup_time);
        priceField = view.findViewById(R.id.price);
        adultsBtn = view.findViewById(R.id.adults);
        childrenBtn = view.findViewById(R.id.children);
        infantsBtn = view.findViewById(R.id.infants);
        languageBtn = view.findViewById(R.id.language);
        cartBtn = view.findViewById(R.id.cart_button);
        nextBtn = view.findViewById(R.id.next_button);

        includedAdapter = new IncludedRecyclerAdapter();
        includedRecyclerView.setLayoutManager(new LinearLayoutManager(includedRecyclerView.getContext()));
        includedRecyclerView.setAdapter(includedAdapter);

        toastMessage = new ToastMessage(activity);
        toastMessage.setTextColor(Color.WHITE).setBackgroundColor(Color.BLACK);

        messageDialog = new Dialog(view.getContext(), R.style.PopUpDialogTheme);
        messageDialog.setContentView(R.layout.dialog_error);

        adultsBtn.getMenu().inflate(R.menu.adults_list);
        childrenBtn.getMenu().inflate(R.menu.children_list);
        infantsBtn.getMenu().inflate(R.menu.infants_list);

        applyDesign(false); // Disable button on start up

        globalVM.fetchAvailableLanguages();
        globalVM.initAvailableDates();

        ColorManager.setTint(
                new ArrayList<ImageView>() {{
                    add(backButton);
                    add(forwardButton);
                }},
                R.color.calendar_arrows_color);

        initCalendar();
        setUpListeners();
    }

    private void initCalendar() {

        calendarView.setTopbarVisible(false);
        calendarView.setPagingEnabled(false);

        calendarView.setTitleFormatter(day -> day.getDate().getMonth().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US).toUpperCase());
        calendarView.setWeekDayFormatter(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.US).toUpperCase());

        calendarView.state().edit()
                .setMinimumDate(DateManager.getFirstDateOfMonth())
                .setMaximumDate(DateManager.getLastDateOfYear())
                .commit();

        calendarView.addDecorators(drawableDecorator, disableDecorator);

        calendarView.setOnMonthChangedListener((widget, date) -> {
            enableDateChange(false);
            changeMonthHeadline(date.getMonth());
            globalVM.fetchAvailableDatesOf(date.getMonth(), languageBtn.getText().toString());
        });

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            selectedDateField.setText(SpanConstructor.apply(activity.getResources().getString(R.string.selected_date),
                    date.getDay() + "th of " + date.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.US),
                    ContextCompat.getColor(activity, R.color.softRed)));
            updatePrice();
        });

        changeMonthHeadline(calendarView.getCurrentDate().getMonth());
    }

    private void updatePrice() {
        CalendarDay date = calendarView.getSelectedDate();
        if (date == null) return;
        String d = String.format(Locale.US, "%02d", date.getYear()) +
                String.format(Locale.US, "%02d", date.getMonth()) +
                String.format(Locale.US, "%02d", date.getDay());

        globalVM.updatePrice(d, languageBtn.getText().toString(), adultsBtn.getText().toString(),
                childrenBtn.getText().toString(), infantsBtn.getText().toString(),
                (success, message) -> {
                    if (!success) showDialogMessage(message);
                });
    }

    private void showDialogMessage(@Nullable String text) {
        TextView tv = messageDialog.findViewById(R.id.text);
        tv.setText(text);
        messageDialog.show();
    }

    private void changeMonthHeadline(int month) {
        String[] months = getResources().getStringArray(R.array.months);
        String monthText = (months.length > month - 1) ? months[month - 1] : "";
        String threeChars = (monthText.length() > 3) ? monthText.substring(0, 3) : monthText;
        currentMonthHeadline.setText(threeChars);
    }

    private void moveCalendar(boolean back) {
        CalendarDay currentDate = calendarView.getCurrentDate();
        int monthNum = currentDate.getMonth();
        int toMonthNum = back ? (monthNum - 1) : (monthNum + 1);
        if (toMonthNum >= 0 && toMonthNum <= 12) {
            CalendarDay toDate = CalendarDay.from(currentDate.getYear(), toMonthNum, currentDate.getDay());
            calendarView.setCurrentDate(toDate, true);
        }
    }

    private void enableDateChange(boolean enable) {
        new Handler(Looper.getMainLooper()).post(() -> {
            backButton.setEnabled(enable);
            forwardButton.setEnabled(enable);
        });
    }

    private void setUpListeners() {

        globalVM.getCurrentPrice().observe(getViewLifecycleOwner(), price -> {
            applyDesign(price != null); // #1 Enable button
            includedLabel.setVisibility((price != null && price.getIncluded() != null && price.getIncluded().size() != 0) ? View.VISIBLE : View.INVISIBLE);
            includedAdapter.setDataSet(price != null ? price.getIncluded() : new ArrayList<>());
            updateInfo(price != null ? price.getPickUpPoint() : "",
                    price != null ? price.getPickUpTime() : "",
                    price != null ? price.getFormattedPrice() : "",
                    price != null ? price.getDescription() : ""
            ); // #2 Update excursion information
        });

        globalVM.getExData().observe(getViewLifecycleOwner(), exData -> {
            if (exData == null) { return; }
            mainPickPath = exData.getMainPickPath(); // #1 Keep mainPickPath as variable
            excursionNameField.setText(exData.getName()); // #2 Update Excursion name
        });

        globalVM.getCalendarDays().observe(getViewLifecycleOwner(), calendarDays -> {
            calendarView.post(() -> calendarView.invalidateDecorators());
            enableDateChange(true);
        });

        globalVM.getLanguages().observe(getViewLifecycleOwner(), languages -> {
            for (ExLanguageModel language : languages) {
                this.languageBtn.getMenu().getMenu().add(language.getDescription());
            }
        });

        cartBtn.setOnClickListener(v -> {
            ExPriceModel exPrice = globalVM.getCurrentPrice().getValue();
            if (exPrice == null) {
                toastMessage.show();
            } else { // Add to Cart
                globalVM.addToCart(new CartModel(exPrice, mainPickPath != null ? mainPickPath : ""));
                toastMessage.setText(getResources().getString(R.string.added_to_cart));
                toastMessage.show();
            }
        });

        nextBtn.setOnClickListener(v -> {
            ExPriceModel exPrice = globalVM.getCurrentPrice().getValue();
            if(exPrice == null) {
                toastMessage.show();
            } else {
                globalVM.addToCart(new CartModel(exPrice, mainPickPath != null ? mainPickPath : ""));
                onNextClickListener.onNextClicked();
            }
        });

        backButton.setOnClickListener(view -> moveCalendar(true));
        forwardButton.setOnClickListener(view -> moveCalendar(false));

        adultsBtn.setOnMenuClickedListener(this);
        childrenBtn.setOnMenuClickedListener(this);
        infantsBtn.setOnMenuClickedListener(this);
        languageBtn.setOnMenuClickedListener(this);
    }

    private final DayViewDecorator drawableDecorator = new DayViewDecorator() {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
            if (getContext() != null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.calendar_selection_drawable);
                if (drawable != null) view.setSelectionDrawable(drawable);
            }
        }
    };

    private final DayViewDecorator disableDecorator = new DayViewDecorator() {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return !globalVM.containsDate(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    };

    private void updateInfo(@NonNull String pickUpPoint, @NonNull String pickUpTime,
                            @NonNull String price, @NonNull String excursionName) {
        if (!pickUpPoint.isEmpty()) {
            pickUpPointField.setText(SpanConstructor.apply(
                    getResources().getString(R.string.pickup_point),
                    pickUpPoint,
                    ContextCompat.getColor(activity, R.color.softRed))
            );
        } else {
            pickUpPointField.setText(R.string.pickup_point);
        }
        if (!pickUpTime.isEmpty()) {
            pickUpTimeField.setText(SpanConstructor.apply(
                    getResources().getString(R.string.pickup_time),
                    pickUpTime,
                    ContextCompat.getColor(activity, R.color.softRed))
            );
        } else {
            pickUpTimeField.setText(R.string.pickup_time);
        }
        if (!price.isEmpty()) {
            this.priceField.setText(SpanConstructor.apply(
                    getResources().getString(R.string.excursion_price),
                    String.format(getResources().getString(R.string.money_format), price),
                    ContextCompat.getColor(activity, R.color.softRed))
            );
        } else {
            this.priceField.setText(R.string.excursion_price);
        }
        this.excursionNameField.setText(excursionName);
    }

    private void applyDesign(boolean enabled) {
        cartBtn.applyStyle(activity, enabled ? EventButton.Style.RED : EventButton.Style.GRAY);
        nextBtn.applyStyle(activity, enabled ? EventButton.Style.BLACK : EventButton.Style.TRANSPARENT);
        toastMessage.setText(enabled ? getResources().getString(R.string.added_to_cart) : getResources().getString(R.string.select_date));
    }

    public void setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.onNextClickListener = onNextClickListener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.language) {
            CalendarDay currentDay = calendarView.getCurrentDate();
            if (currentDay != null) {
                globalVM.onLanguageChanged(languageBtn.getText().toString(), currentDay.getMonth());
            }
            CalendarDay day = calendarView.getSelectedDate();
            if (day != null) calendarView.setDateSelected(day, false);
            selectedDateField.setText(R.string.selected_date);
        } else {
            updatePrice();
        }
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
