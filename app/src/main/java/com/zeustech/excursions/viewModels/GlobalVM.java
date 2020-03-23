package com.zeustech.excursions.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import com.couchbase.lite.CouchbaseLiteException;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.zeustech.excursions.DAO.AreaDAO;
import com.zeustech.excursions.DAO.CartDAO;
import com.zeustech.excursions.DAO.ExcursionsDAO;
import com.zeustech.excursions.DAO.HotelDAO;
import com.zeustech.excursions.DAO.LoginDataDAO;
import com.zeustech.excursions.DAO.TicketsDAO;
import com.zeustech.excursions.callbacks.Completion;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.callbacks.CompletionTransaction;
import com.zeustech.excursions.customViews.DateManager;
import com.zeustech.excursions.database.CouchbaseDAO;
import com.zeustech.excursions.database.DatabaseManager;
import com.zeustech.excursions.http.Repository;
import com.zeustech.excursions.http.requests.LoginRequest;
import com.zeustech.excursions.models.Area;
import com.zeustech.excursions.models.CartModel;
import com.zeustech.excursions.models.ExBookingData;
import com.zeustech.excursions.models.ExCardDetails;
import com.zeustech.excursions.models.ExCustomerInfo;
import com.zeustech.excursions.models.ExDaysModel;
import com.zeustech.excursions.models.ExLanguageModel;
import com.zeustech.excursions.models.ExLoginModel;
import com.zeustech.excursions.models.ExPriceModel;
import com.zeustech.excursions.models.ExTicketId;
import com.zeustech.excursions.models.ExTicketModel;
import com.zeustech.excursions.models.ExcursionModel;
import com.zeustech.excursions.models.ExcursionsModel;
import com.zeustech.excursions.models.Hotel;
import com.zeustech.excursions.models.LoginData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class GlobalVM extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();

    private final static String DEFAULT_LAN = "EN";

    private SavedStateHandle savedStateHandle;
    private final Repository repository;

    private final LoginDataDAO loginDataDAO;
    private final AreaDAO areaDAO;
    private final HotelDAO hotelDAO;

    private final ExcursionsDAO excursionsDAO;

    private final CartDAO cartDAO;
    private final TicketsDAO ticketsDAO;

    private MutableLiveData<List<Area>> areas;
    private MutableLiveData<Area> selectedArea;
    private LiveData<List<Hotel>> hotels;

    private LoginData loginData;
    private MutableLiveData<Hotel> selectedHotel;

    private MutableLiveData<List<ExcursionsModel>> excursions;
    private MutableLiveData<String> filterText;
    private LiveData<List<ExcursionsModel>> filteredExcursions;

    private MutableLiveData<Boolean> searchTrigger;

    private MutableLiveData<ArrayList<CartModel>> cart;
    private LiveData<ArrayList<ExPriceModel>> price;

    private MutableLiveData<List<ExTicketId>> ticketIds;
    private MutableLiveData<List<ExTicketModel>> tickets;

    /*Booking*/
    private final static String FORMAT = "yyyyMMdd";
    private final static String EX_DATA = "ex_data";
    private final static String CURRENT_PRICE = "current_price";
    private final static String CUSTOMER_INFO = "customer_info";

    private MutableLiveData<ExBookingData> exData;
    private MutableLiveData<ExPriceModel> currentPrice;

    private MutableLiveData<List<ExLanguageModel>> languages;

    private MutableLiveData<List<String>> dates;
    private LiveData<List<CalendarDay>> calendarDays;

    private MutableLiveData<ExCustomerInfo> customerInfo;

    {
        areas = new MutableLiveData<>();
        selectedArea = new MutableLiveData<>();
        hotels = Transformations.switchMap(selectedArea, this::getHotelsByArea);

        loginData = null;
        selectedHotel = new MutableLiveData<>();

        excursions = new MutableLiveData<>();
        filterText = new MutableLiveData<>();

        searchTrigger = new MutableLiveData<>();

        filteredExcursions = Transformations.map(filterText, text -> {
            List<ExcursionsModel> excursions = this.excursions.getValue();
            List<ExcursionsModel> filteredExcursions = new ArrayList<>();
            if (text != null && excursions != null) {
                String validText = text.toLowerCase().trim();
                for (ExcursionsModel excursion : excursions) {
                    String description = excursion.getDescription();
                    if (description == null) continue;
                    String validDescription = description.toLowerCase().trim();
                    if (validDescription.contains(validText)) {
                        filteredExcursions.add(excursion);
                    }
                }
            }
            return filteredExcursions;
        });

        cart = new MutableLiveData<>();
        price = Transformations.map(cart, cart -> {
            ArrayList<ExPriceModel> price = new ArrayList<>();
            if (cart != null) {
                for(CartModel model : cart) {
                    price.add(model.getPriceModel());
                }
            }
            return price;
        });

        ticketIds = new MutableLiveData<>();
        tickets = (MutableLiveData<List<ExTicketModel>>) Transformations.switchMap(ticketIds, this::fetchTickets);

        languages = new MutableLiveData<>();

        dates = new MutableLiveData<>();
        calendarDays = Transformations.map(dates, dates -> {
            List<CalendarDay> calendarDays = new ArrayList<>();
            for (String date : dates) {
                calendarDays.add(DateManager.textToCalendarDay(date, "yyyy-MM-dd"));
            }
            return calendarDays;
        });
    }

    public GlobalVM(@NonNull Application application, SavedStateHandle savedStateHandle) {
        super(application);
        this.savedStateHandle = savedStateHandle;
        repository = Repository.getInstance(application);
        loginDataDAO = new LoginDataDAO(application);
        areaDAO = new AreaDAO(application);
        hotelDAO = new HotelDAO(application);
        excursionsDAO = new ExcursionsDAO(application);
        cartDAO = new CartDAO(application);
        ticketsDAO = new TicketsDAO(application);

        exData = savedStateHandle.getLiveData(EX_DATA);
        currentPrice = savedStateHandle.getLiveData(CURRENT_PRICE);
        customerInfo = savedStateHandle.getLiveData(CUSTOMER_INFO);
        setUpTriggers(true);
    }

    private void setUpTriggers(boolean enable) {
        if (enable) {
            areaDAO.addChangeListener(change -> areas.postValue(change));
            loginDataDAO.addChangeListener(change -> {
                Log.i(TAG, "Login Live Query Triggered: " + change);
                loginData = change.size() > 0 ? change.get(0) : null;
            });
            // Selected Hotel Query
            hotelDAO.addChangeListener(change -> {
                Log.i(TAG, "Hotel Live Query Triggered: " + change);
                selectedHotel.postValue(change.size() > 0 ? change.get(0) : null);
            });
            excursionsDAO.addChangeListener(change -> {
                Log.i(TAG, "Excursions Live Query Triggered");
                excursions.postValue(new ArrayList<>(change));
            });
            cartDAO.addChangeListener(change -> {
                Log.i(TAG, "Cart Live Query Triggered");
                cart.postValue(new ArrayList<>(change));
            });
            ticketsDAO.addChangeListener(change -> {
                Log.i(TAG, "Tickets Live Query Triggered");
                ticketIds.postValue(new ArrayList<>(change));
            });
        } else {
            areaDAO.removeChangeListener();
            loginDataDAO.removeChangeListener();
            hotelDAO.removeChangeListener();
            excursionsDAO.removeChangeListener();
            cartDAO.removeChangeListener();
            ticketsDAO.removeChangeListener();
        }
    }

    public MutableLiveData<List<Area>> getAreas() {
        return areas;
    }

    public MutableLiveData<Area> getSelectedArea() {
        return selectedArea;
    }

    public LiveData<List<Hotel>> getHotels() {
        return hotels;
    }

    public MutableLiveData<List<ExcursionsModel>> getExcursions() {
        return excursions;
    }

    public MutableLiveData<String> getFilterText() {
        return filterText;
    }

    public LiveData<List<ExcursionsModel>> getFilteredExcursions() {
        return filteredExcursions;
    }

    public MutableLiveData<Boolean> getSearchTrigger() {
        return searchTrigger;
    }

    public MutableLiveData<ArrayList<CartModel>> getCart() { return cart; }

    public LiveData<ArrayList<ExPriceModel>> getCartPrice() { return price; }

    public LiveData<List<ExTicketModel>> getTickets() {
        return tickets;
    }

    /*Booking*/
    public LiveData<ExBookingData> getExData() {
        return exData;
    }

    public LiveData<ExPriceModel> getCurrentPrice() {
        return currentPrice;
    }

    private LiveData<ExCustomerInfo> getCustomerInfo() {
        return customerInfo;
    }

    public MutableLiveData<List<ExLanguageModel>> getLanguages() {
        return languages;
    }

    public LiveData<List<CalendarDay>> getCalendarDays() {
        return calendarDays;
    }

    public void setExData(ExBookingData exData) {
        savedStateHandle.set(EX_DATA, exData);
    }

    private void setCurrentPrice(ExPriceModel price) {
        savedStateHandle.set(CURRENT_PRICE, price);
    }

    public void setCustomerInfo(ExCustomerInfo customerInfo) {
        savedStateHandle.set(CUSTOMER_INFO, customerInfo);
    }

    public void updatePrice(String date, String language, String adults, String children, String infants, CompletionTransaction completion) {
        ExBookingData exData = this.exData.getValue();
        Hotel hotel = selectedHotel.getValue();
        if (exData != null && loginData != null && hotel != null) {
            repository.getPrice(hotel.getHotelCode(), loginData, date, exData.getCode(), getLanCode(language), adults, children, infants,
                    new CompletionHandler<ExPriceModel>() {
                        @Override
                        public void onSuccess(@NonNull ExPriceModel model, int status) {
                            setCurrentPrice(model);
                            completion.onResult(true, null);
                        }

                        @Override
                        public void onFailure(@Nullable String description, int status) {
                            completion.onResult(false, description);
                        }
                    });
        }
    }

    public void fetchAvailableLanguages() {
        ExBookingData exData = this.exData.getValue();
        if (exData == null || loginData == null) return;
        repository.getLanguages(loginData, exData.getCode(), new CompletionHandler<List<ExLanguageModel>>() {
            @Override
            public void onSuccess(@NonNull List<ExLanguageModel> model, int status) {
                languages.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description, int status) { }
        });
    }

    public boolean containsDate(@NonNull CalendarDay calendarDay) {
        List<CalendarDay> numericDates = this.calendarDays.getValue();
        return numericDates != null && numericDates.contains(calendarDay);
    }

    @NonNull
    private String getLanCode(@NonNull String language) {
        List<ExLanguageModel> languages = this.languages.getValue();
        if (languages != null) {
            String lanCode = ExLanguageModel.findLanCode(language, languages);
            if (lanCode != null) return lanCode;
        }
        return DEFAULT_LAN;
    }

    public void onLanguageChanged(@NonNull String language, int displayingMonth) {
        setCurrentPrice(null); // #1 Erase previous Price
        dates.setValue(new ArrayList<>()); // #2 Initialize Dates
        fetchAvailableDatesOf(displayingMonth, language); // #3 fetch Dates from current displaying month
    }

    public void initAvailableDates() {
        Date today = new Date();
        getAvailableDatesOf(
                DateManager.convertDate(today, FORMAT),
                DateManager.getLastDateOfMonth(today, FORMAT),
                DEFAULT_LAN
        );
    }

    public void fetchAvailableDatesOf(int month, @NonNull String language) {
        getAvailableDatesOf(
                DateManager.getFirstDateOfMonth(month, FORMAT),
                DateManager.getLastDateOfMonth(month, FORMAT),
                getLanCode(language)
        );
    }

    private void getAvailableDatesOf(@NonNull String firstDate, @NonNull String lastDate, @NonNull String language) {
        ExBookingData exData = this.exData.getValue();
        if (exData == null || loginData == null) return;
        repository.getProgram(exData.getCode(), firstDate, lastDate, language, loginData, new CompletionHandler<ExDaysModel>() {
            @Override
            public void onSuccess(@NonNull ExDaysModel model, int status) {
                setDates(model.getDays() != null ? model.getDays() : new ArrayList<>());
            }

            @Override
            public void onFailure(@Nullable String description, int status) {
                setDates(new ArrayList<>());
            }
        });
    }

    public void bookNow(@NonNull ExCardDetails cardDetails, List<ExPriceModel> basket, CompletionHandler<List<ExTicketId>> completion) {
        ExCustomerInfo customerInfo = getCustomerInfo().getValue();
        if (customerInfo != null && loginData != null) {
            repository.setBooking(loginData, basket, customerInfo, cardDetails, completion);
        } else {
            completion.onFailure(null, -1);
        }
    }

    private void setDates(@NonNull List<String> nDates) {
        List<String> oldDates = new ArrayList<>(dates.getValue() != null ? dates.getValue() : new ArrayList<>());
        nDates.addAll(oldDates);
        List<String> newList = new ArrayList<>(new HashSet<>(nDates));
        dates.postValue(newList);
    }

    public void clearData() {
        setCurrentPrice(null);
        setCustomerInfo(null);
        languages.setValue(new ArrayList<>());
        dates.setValue(new ArrayList<>());
    }

    /*Home-SplashScreen Fragments*/
    public void login(@NonNull String username, @NonNull String password,
                      @NonNull CompletionHandler<Boolean> completion) {
        repository.login(new LoginRequest(username, password), new CompletionHandler<ExLoginModel>() {
            @Override
            public void onSuccess(@NonNull ExLoginModel model, int status) {
                try {
                    loginDataDAO.replaceAll(new LoginData(model, username, password));
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                completion.onSuccess(true, status);
            }

            @Override
            public void onFailure(@Nullable String description, int status) {
                completion.onFailure(description, status);
            }
        });
    }

    public void getAreas(@Nullable CompletionTransaction completion) {
        repository.getAreas(new CompletionHandler<List<Area>>() {
            @Override
            public void onSuccess(@NonNull List<Area> model, int status) {
                try {
                    areaDAO.replaceAll(model);
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                if (completion != null) completion.onResult(true, null);
            }

            @Override
            public void onFailure(@Nullable String description, int status) {
                if (completion != null) completion.onResult(false, description);
            }
        });
    }

    private LiveData<List<Hotel>> getHotelsByArea(@Nullable Area area) {
        MutableLiveData<List<Hotel>> hotels = new MutableLiveData<>();
        if (area != null) {
            repository.getHotels(area.getAreaCode(), new CompletionHandler<List<Hotel>>() {
                @Override
                public void onSuccess(@NonNull List<Hotel> model, int status) {
                    hotels.setValue(model);
                }

                @Override
                public void onFailure(@Nullable String description, int status) {
                    hotels.setValue(new ArrayList<>());
                }
            });
        } else {
            hotels.setValue(new ArrayList<>());
        }
        return hotels;
    }

    private void getExcursions(@NonNull String hotelCode, @Nullable CompletionTransaction completion) {
        if (loginData != null) {
            repository.getExcursions(hotelCode, loginData.getCustomer(), DEFAULT_LAN,
                    new CompletionHandler<List<ExcursionsModel>>() {
                        @Override
                        public void onSuccess(@NonNull List<ExcursionsModel> model, int status) {
                            try {
                                excursionsDAO.replaceAll(model);
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }
                            if (completion != null) completion.onResult(true, null);
                        }

                        @Override
                        public void onFailure(@Nullable String description, int status) {
                            if (status == 202) { // Work Around
                                try {
                                    excursionsDAO.replaceAll(new ArrayList<>());
                                } catch (CouchbaseLiteException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (completion != null) completion.onResult(status == 202, description);
                        }
                    });
        } else if (completion != null) {
            completion.onResult(false, null);
        }
    }

    public void refreshExcursions() {
        Hotel hotel = selectedHotel.getValue();
        if (hotel != null) getExcursions(hotel.getHotelCode(), null);
    }

    public void getExcursion(String code, CompletionHandler<ExcursionModel> completion) {
        repository.getExcursion(code, DEFAULT_LAN, completion);
    }

    /**Used by Login (1. Save Hotel, 2. Fetch Excursions)*/
    public void initLogin(@NonNull String hotelName, @NonNull CompletionTransaction completion) {
        Hotel hotel = getHotelWith(hotelName);
        if (hotel != null) {
            getExcursions(hotel.getHotelCode(), (success, message) -> {
                if (success) {
                    try {
                        hotelDAO.replaceAll(hotel);
                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                    }
                }
                completion.onResult(success, message);
            });
        } else {
            completion.onResult(false, null);
        }
    }

    /* 1: Success
     * 2: Missing or Invalid Hotel-Login
     * 3: Connection Error
     * */
    /**Used by SplashScreen (1. Check Hotel & Login Database data, 2. Login)*/
    public void initSplash(@NonNull Completion<Integer> completion) {
        Hotel hotel = hotelDAO.getSelectedHotel();
        LoginData data = loginDataDAO.getLoginData();
        if (hotel != null && data != null) {
            login(data.getUsername(), data.getPassword(), new CompletionHandler<Boolean>() {
                @Override
                public void onSuccess(@NonNull Boolean model, int status) {
                    completion.onResult(1);
                }

                @Override
                public void onFailure(@Nullable String description, int status) {
                    boolean connectionError = (status == -1);
                    completion.onResult(connectionError ? 3 : 2);
                }
            });
        } else {
            completion.onResult(2);
        }
    }

    public void eraseDatabase() {
        try {
            DatabaseManager.getInstance(getApplication()).eraseDatabase();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private Hotel getHotelWith(@NonNull String name) {
        List<Hotel> hotels = this.hotels.getValue();
        if (hotels != null) {
            for (Hotel hotel : hotels) {
                if (!hotel.getHotelName().equals(name.trim())) continue;
                return hotel;
            }
        }
        return null;
    }

    public void triggerSearch() {
        Boolean enabled = searchTrigger.getValue();
        searchTrigger.setValue(enabled == null || !enabled);
    }

    /*Tickets*/
    public void addTickets(@NonNull List<ExTicketId> ticketIds) {
        try {
            ticketsDAO.batchOperation(CouchbaseDAO.Operation.SAVE, ticketIds);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    private MutableLiveData<List<ExTicketModel>> fetchTickets(@Nullable List<ExTicketId> ids) {
        MutableLiveData<List<ExTicketModel>> tickets = new MutableLiveData<>();
        if (ids != null) {
            repository.getBookings(ids, new CompletionHandler<List<ExTicketModel>>() {
                @Override
                public void onSuccess(@NonNull List<ExTicketModel> model, int status) {
                    tickets.setValue(model);
                }

                @Override
                public void onFailure(@Nullable String description, int status) {
                    tickets.setValue(new ArrayList<>());
                }
            });
        } else {
            tickets.setValue(new ArrayList<>());
        }
        return tickets;
    }

    public void refreshTickets() {
        List<ExTicketId> ids = ticketIds.getValue();
        if (ids == null) return;
        repository.getBookings(ids, new CompletionHandler<List<ExTicketModel>>() {
            @Override
            public void onSuccess(@NonNull List<ExTicketModel> model, int status) {
                tickets.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description, int status) {
                tickets.setValue(new ArrayList<>());
            }
        });
    }

    /*Cart*/

    public void addToCart(@NonNull CartModel model) {
        if (!model.isValid()) return;
        try {
            cartDAO.updateDocument(model);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void removeFromCart(@NonNull String exCode) {
        ArrayList<CartModel> cart = this.cart.getValue();
        if (cart == null) return;
        for(CartModel model : cart) {
            if (model.getCode().equals(exCode)) {
                try {
                    cartDAO.deleteDocument(model);
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        setUpTriggers(false);
    }
}
