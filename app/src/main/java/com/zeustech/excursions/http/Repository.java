package com.zeustech.excursions.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.callbacks.CompletionLogin;
import com.zeustech.excursions.http.requests.BookRequest;
import com.zeustech.excursions.http.requests.ExcursionProgramRequest;
import com.zeustech.excursions.http.requests.ExcursionRequest;
import com.zeustech.excursions.http.requests.ExcursionsRequest;
import com.zeustech.excursions.http.requests.HotelRequest;
import com.zeustech.excursions.http.requests.LanguagesRequest;
import com.zeustech.excursions.http.requests.LoginRequest;
import com.zeustech.excursions.http.requests.PriceRequest;
import com.zeustech.excursions.models.Area;
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
import com.zeustech.excursions.models.ExcursionsServerModel;
import com.zeustech.excursions.models.Hotel;
import com.zeustech.excursions.tools.ExcursionsServerManager;
import com.zeustech.excursions.tools.OkHttpSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class Repository {

    private final String TAG = getClass().getSimpleName();

    private static volatile Repository INSTANCE = null;

    private final String credentials;
    private final LoginRequest loginRequest;

    private final ZenixExcursionsApi api;
    private final Gson gsonHelper;

    public static synchronized Repository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(context);
        }
        return INSTANCE;
    }

    private Repository(@NonNull Context context) {

        ExcursionsServerModel server = new ExcursionsServerManager(context).getModel();
        if (server == null)
            throw new NullPointerException("Server File Error");

        String userName = server.getUsername();
        String password = server.getPassword();

        credentials = Credentials.basic(userName, password);
        loginRequest = new LoginRequest(userName, password);

        OkHttpClient client = OkHttpSingleton.getInstance(context).getClient();
        api = new Retrofit.Builder()
                .baseUrl(server.getDevelopmentHost())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ZenixExcursionsApi.class);

        gsonHelper = new Gson();
    }

    @EverythingIsNonNull
    public void getAreas(CompletionHandler<List<Area>> completion) {
        Type type = new TypeToken<ArrayList<Area>>() {}.getType();
        request(api.getAreas(credentials), type, completion);
    }

    @EverythingIsNonNull
    public void getHotels(String areaCode, CompletionHandler<List<Hotel>> completion) {
        Type type = new TypeToken<ArrayList<Hotel>>() {}.getType();
        request(api.getHotels(credentials, new HotelRequest(areaCode)), type, completion);
    }

    @EverythingIsNonNull
    public void login(LoginRequest request, CompletionHandler<ExLoginModel> completion) {
        request(api.setExcLogin(credentials, request), ExLoginModel.class, completion);
    }

    @EverythingIsNonNull
    public void getExcursions(String hotelCode, String customer, String language,
                              CompletionHandler<List<ExcursionsModel>> completion) {
        // TODO: 202
        Type type = new TypeToken<ArrayList<ExcursionsModel>>() {}.getType();
        ExcursionsRequest request = new ExcursionsRequest(hotelCode, language, customer);
        request(api.getExcursions(credentials, request), type, completion);
    }

    // TODO: Remove it
    @EverythingIsNonNull
    public void init(String hotelCode, String language, CompletionLogin completion) {
        init(loginRequest, hotelCode, language, completion);
    }

    @EverythingIsNonNull
    private void init(LoginRequest request, String hotelCode, String language, CompletionLogin completion) {

        login(request, new CompletionHandler<ExLoginModel>() {
            @Override
            public void onSuccess(@NonNull ExLoginModel login) {

                getExcursions(hotelCode, login.getCustomer(), language, new CompletionHandler<List<ExcursionsModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ExcursionsModel> excursions) {
                        completion.onSuccess(login, excursions);
                    }

                    @Override
                    public void onFailure(@Nullable String description) {
                        completion.onFailure(description);
                    }
                });
            }

            @Override
            public void onFailure(@Nullable String description) {
                completion.onFailure(description);
            }
        });
    }

    @EverythingIsNonNull
    public void getExcursion(String excCode, String language, CompletionHandler<ExcursionModel> completion) {
        ExcursionRequest request = new ExcursionRequest(excCode, language);
        request(api.getExcursion(credentials, request), ExcursionModel.class, completion);
    }

    @EverythingIsNonNull
    public void getLanguages(ExLoginModel login, String exCode,
                             CompletionHandler<List<ExLanguageModel>> completion) {

        LanguagesRequest request = LanguagesRequest.Create(login, exCode);
        Type type = new TypeToken<ArrayList<ExLanguageModel>>() {}.getType();
        request(api.getLanguages(credentials, request), type, completion);
    }

    @EverythingIsNonNull
    public void getProgram(String exCode, String firstDate, String lastDate, String language,
                           ExLoginModel login, CompletionHandler<ExDaysModel> completion) {
        new Thread(() -> {
            ExcursionProgramRequest request = ExcursionProgramRequest.Create(exCode, firstDate, lastDate, language, login);
            Call<ResponseBody> call = api.getExcProgram(credentials, request);
            try {
                Response<ResponseBody> response = call.execute();
                responseHandler(response, ExDaysModel.class, completion);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @EverythingIsNonNull
    public void getPrice(String hotelCode, ExLoginModel login, String excDate, String excCode, String lan, String adults,
                         String children, String infants, CompletionHandler<ExPriceModel> completion) {

        PriceRequest request = PriceRequest.Create(excDate, excCode, lan, login, hotelCode, adults, children, infants);
        request(api.getPrice(credentials, request), ExPriceModel.class, completion);
    }

    @EverythingIsNonNull
    public void setBooking(ExLoginModel login, List<ExPriceModel> basket, ExCustomerInfo customerInfo,
                           ExCardDetails details, final CompletionHandler<List<ExTicketId>> completion) {

        BookRequest request = BookRequest.Create(login, basket, customerInfo, details);
        Type type = new TypeToken<ArrayList<ExTicketId>>() {}.getType();
        request(api.setBooking(credentials, request), type, completion);
    }

    @EverythingIsNonNull
    public void getBookings(List<ExTicketId> ticketIds, final CompletionHandler<List<ExTicketModel>> completion) {

        Type type = new TypeToken<ArrayList<ExTicketModel>>() {}.getType();
        request(api.getBookings(credentials, ticketIds), type, completion);
    }

    @Nullable
    private String getMessage(String text) {
        try {
            return new JSONObject(text).getString("errorDescr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @EverythingIsNonNull
    private <T> void request(Call<ResponseBody> call, Type tClass, CompletionHandler<T> completion) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                responseHandler(response, tClass, completion);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                completion.onFailure(t.getMessage());
            }
        });
    }

    @EverythingIsNonNull
    private <T> void responseHandler(@NonNull Response<ResponseBody> response, Type tClass, CompletionHandler<T> completion) {
        ResponseBody body = response.body();
        ResponseBody errorBody = response.errorBody();
        int status = response.code();
        T model = null;
        String message = null;
        try {
            if (body != null && response.isSuccessful()) {
                String bodyText = body.string();
                Log.i(TAG, "Response: " + bodyText + status);
                if (status == 200) {
                    model = gsonHelper.fromJson(bodyText, tClass);
                } else {
                    message = getMessage(bodyText);
                }
            } else if (errorBody != null) {
                String errorBodyText = errorBody.string();
                Log.i(TAG, "Error Response: " + errorBodyText);
                message = getMessage(errorBodyText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (model != null) {
            completion.onSuccess(model);
        } else {
            completion.onFailure(message);
        }
    }

}
