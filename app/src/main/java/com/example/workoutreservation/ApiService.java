package com.example.workoutreservation;

import com.example.workoutreservation.model.LogDataEntry;
import com.example.workoutreservation.model.User;
import com.example.workoutreservation.model.WaitlistItem;
import com.example.workoutreservation.model.Workout;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("home")
    Call<ResponseBody> getHome();

    @FormUrlEncoded
    @POST("login")
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("save_token")
    Call<ResponseBody> saveToken(@Field("userId") int userId, @Field("token") String token);

    //todo change parameter to User
    @FormUrlEncoded
    @POST("save_user_data")
    Call<ResponseBody> saveUserData (@Field("userId") int userId, @Field("firstName") String firstName,
                                    @Field("familyName") String familyName, @Field("email") String email,
                                    @Field("password") String password,@Field("phone") String phone);

    @FormUrlEncoded
    @POST("get_user_data")
    Call<User> getUserData(@Field("userId") int userId);

    @FormUrlEncoded
    @POST("get_workouts_on_day")
    Call<List<Workout>> getWorkouts(@Field("date") String date);

    @FormUrlEncoded
    @POST("get_workouts_with_extra")
    Call<List<Workout>> getWorkouts(@Field("date") String date, @Field("userId") int userId);

    //todo fix method to pass Workout instance
    @FormUrlEncoded
    @POST("add_workout")
    Call<ResponseBody> addWorkout(@Field("date") long date, @Field("maxGroupSize") int maxGroupSize, @Field("description") String description);

    @FormUrlEncoded
    @POST("get_my_workouts")
    Call<List<Workout>> getMyWorkouts(@Field("userId") int userId);

    @FormUrlEncoded
    @POST("reserve")
    Call<ResponseBody> reserve(@Field("workoutId") int workoutId, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("cancel_reservation")
    Call<ResponseBody> cancelReservation(@Field("workoutId") int workoutId, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("get_my_waitlists")
    Call<List<WaitlistItem>> getMyWaitlists(@Field("userId") int userId);

    @FormUrlEncoded
    @POST("add_waitlist")
    Call<ResponseBody> addWaitlist(@Field("workoutId") int workoutId, @Field("userId") int userId);

    @FormUrlEncoded
    @POST("quit_waitlist")
    Call<ResponseBody> quitWaitlist(@Field("waitlistId") int waitlistId);

    @FormUrlEncoded
    @POST("get_all_users")
    Call<List<User>> getAllUsers(@Field("userRights") int rights);

    @FormUrlEncoded
    @POST("add_credits")
    Call<Integer> updateBalance(@Field("userId")int userId, @Field("addCredits") int addCredits, @Field("referenceId") int referenceId);

    @FormUrlEncoded
    @POST("change_rights")
    Call<Integer> changeRights(@Field("userId")int userId, @Field("newRights")int newRights);

    @FormUrlEncoded
    @POST("get_operation_log")
    Call<List<LogDataEntry>> getLog(@Field("userId") int userId);

    @FormUrlEncoded
    @POST("reset_password")
    Call<ResponseBody> resetPassword(@Field("email") String email,
                                     @Field("newPassword") String newPassword,
                                     @Field("encryptedPassword") String encryptedPassword);
}
