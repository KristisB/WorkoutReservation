package com.example.workoutreservation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.R;
import com.example.workoutreservation.User;
import com.example.workoutreservation.Workout;
import com.example.workoutreservation.databinding.FragmentCancelReservationBinding;
import com.example.workoutreservation.databinding.FragmentConfirmReservationBinding;
import com.example.workoutreservation.notifications.MessageData;
import com.example.workoutreservation.notifications.MyMessagingService;
import com.example.workoutreservation.notifications.NotificationMessage;
import com.example.workoutreservation.notifications.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelReservation extends Fragment {

    public static final long TIME_TO_CANCEL = 43200000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentCancelReservationBinding binding = FragmentCancelReservationBinding.inflate(inflater, container, false);
        CancelReservationArgs args = CancelReservationArgs.fromBundle(requireArguments());
        MainActivity mainActivity = (MainActivity) requireActivity();
        Workout workout = new Workout();
        int userId = mainActivity.getUser().getUserId();
        workout.setWorkoutId(args.getMyWorkoutId());
        workout.setDateTime(args.getMyWorkoutDate());
        workout.setDescription(args.getMyWorkoutDescription());
        if (workout.getDateTime() - System.currentTimeMillis() < Workout.TIME_TO_CANCEL) {
            binding.cancelReservationPriceText.setText("It is less then 12h left to start of event. Credit wont be returned. Cancel anyway?");
        }
        binding.cancelReservationInfoText.setText(workout.getDateText() + " " + workout.getTimeText() + " " + workout.getDescription());
        binding.cancelReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.getService().cancelReservation(workout.getWorkoutId(), userId).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.d("Cancel Reservation", "workout Id" + workout.getWorkoutId() + " userId " + userId);
                        Log.d("Cancel Reservation", "" + response.message());

                        NavDirections action = CancelReservationDirections.actionCancelReservationToMyWorkouts();
                        Navigation.findNavController(binding.getRoot()).navigate(action);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }

                });

                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("CancelReservation", "device token " + token);
                    }
                });
//                String token = "fG6q0KF5S3-DxpkQCDpVFu:APA91bEwTL1DGt-kYHz5paY_D2Na18ta0GDJLLpr_y57EVgjgcqyUFJgIFo40B8_xrSgtPwoWPaNbTcGRFOIlhYRsaCWf-zzUt8DkjUPOoXFFvoSKN0wSRCCwbK2FOIuo7duQHnHo1qm";
//
//                NotificationMessage msg = new NotificationMessage(token, new NotificationModel("1 bandymas", "rimtaibandom"), new MessageData("vienas", "du"));
//                Log.d("message", msg.toString());
//                mainActivity.getMsgService().sendMessage(msg).enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show();
//                        try {
//                            Log.d("Message", "Message sent " + response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Toast.makeText(requireContext(), "Message not sent", Toast.LENGTH_SHORT).show();
//                        Log.d("Message", "Message not sent");
//                    }
//                });
            }
        });
        return binding.getRoot();
    }


}