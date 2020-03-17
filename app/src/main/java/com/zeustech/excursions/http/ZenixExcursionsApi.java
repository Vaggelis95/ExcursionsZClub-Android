package com.zeustech.excursions.http;

import com.zeustech.excursions.http.requests.BookRequest;
import com.zeustech.excursions.http.requests.ExcursionProgramRequest;
import com.zeustech.excursions.http.requests.ExcursionRequest;
import com.zeustech.excursions.http.requests.ExcursionsRequest;
import com.zeustech.excursions.http.requests.HotelRequest;
import com.zeustech.excursions.http.requests.LanguagesRequest;
import com.zeustech.excursions.http.requests.LoginRequest;
import com.zeustech.excursions.http.requests.PriceRequest;
import com.zeustech.excursions.models.ExTicketId;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ZenixExcursionsApi {

    @POST("GetAreas")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getAreas(@Header ("Authorization") String auth);

    @POST("GetHotels")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getHotels(@Header ("Authorization") String auth,
                                 @Body HotelRequest request);

    @POST("SetExcLogin")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> setExcLogin(@Header ("Authorization") String auth,
                                   @Body LoginRequest request);

    @POST("GetExcursions")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getExcursions(@Header ("Authorization") String auth,
                                     @Body ExcursionsRequest request);

    @POST("GetExcursion")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getExcursion(@Header ("Authorization") String auth,
                                    @Body ExcursionRequest request);

    @POST("GetExcProgramm")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getExcProgram(@Header ("Authorization") String auth,
                                     @Body ExcursionProgramRequest request);

    @POST("GetLanguages")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getLanguages(@Header ("Authorization") String auth,
                                    @Body LanguagesRequest request);

    @POST("GetPrice")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getPrice(@Header ("Authorization") String auth,
                                @Body PriceRequest request);

    @POST("SetBooking")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> setBooking(@Header ("Authorization") String auth,
                                  @Body BookRequest request);

    @POST("GetBookings")
    @Headers({"Content-Type: text/json"})
    Call<ResponseBody> getBookings(@Header ("Authorization") String auth,
                                   @Body List<ExTicketId> request);

}
