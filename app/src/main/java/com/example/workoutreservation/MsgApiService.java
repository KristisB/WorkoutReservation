package com.example.workoutreservation;

import com.example.workoutreservation.notifications.NotificationMessage;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface MsgApiService {

//    @Headers({"Authorization: key="  +
//            "AAAA5KXl5YQ:APA91bG6YQiIQVeQ3XknpceIuNKkcgrcw_RmDS82ulk1j_lH9nKZzNyZDMb0Rvh9o_Sr3nyucnigL" +
//            "rtSBnR0CqIG_z7GXc7dxU3KEIsj-Qiy-IC5j2fpxus9cgp2lKg-dPoJOgPA5Pox",
//            "Content-Type:application/json"})
//    @POST("fcm/send")
//    Call<ResponseBody> sendMessage(@Body NotificationMessage message);

    @FormUrlEncoded
    @POST("fcm/send")
    Call<ResponseBody> sendMessage (@Field("token") String token,
                                    @Field("title") String title,
                                    @Field("body") String body);

}
