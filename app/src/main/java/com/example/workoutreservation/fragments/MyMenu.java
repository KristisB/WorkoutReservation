package com.example.workoutreservation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.User;
import com.example.workoutreservation.databinding.FragmentMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMenu extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        FragmentMenuBinding binding = FragmentMenuBinding.inflate(inflater, container, false);
        User user = mainActivity.getUser();
        Log.d("LOGIN", "USER ID: " + user.getUserId() + "USER NAME: " + user.getFirstName());

        //taking token and sendind to DB
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                // Get new Instance ID token
                String token = task.getResult().getToken();
                Log.d("MyMenu", "device token " + token);
                mainActivity.getService().saveToken(user.getUserId(),token).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("MyMenu","token saved in DB "+ response.message());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


            }
        });


        //login Button
        binding.loginButton.setOnClickListener(v -> {
            NavDirections action = MyMenuDirections.actionMyMenuToLogin();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        //my account info Button
        if (user.getUserId() == -1) {
            binding.myAccountButton.setVisibility(View.GONE);
        } else {

            binding.myAccountButton.setOnClickListener(v -> {
                NavDirections action = MyMenuDirections.actionMyMenuToUserData();
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });
        }

        // workouts list Button
        if (user.getUserId() == -1) {
            binding.myWorkoutsButton.setVisibility(View.GONE);
        } else {
            binding.workoutsListButton.setOnClickListener(v -> {
                NavDirections action = MyMenuDirections.actionMyMenuToWorkoutList();
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });
        }

        // add workout Button
        if ((user.getUserId() == -1) && (user.getRights() != 1)) {
            binding.addWorkoutButton.setVisibility(View.GONE);
        } else {
            binding.addWorkoutButton.setOnClickListener(v -> {
                NavDirections action = MyMenuDirections.actionMyMenuToAddWorkout(System.currentTimeMillis());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });
        }
        // add my workout Button
        if ((user.getUserId() == -1) && (user.getRights() != 1)) {
            binding.myWorkoutsButton.setVisibility(View.VISIBLE);
        } else {
            binding.myWorkoutsButton.setOnClickListener(v -> {
                NavDirections action = MyMenuDirections.actionMyMenuToMyWorkouts();
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });
        }
        // add my waitlist Button
        if(user.getUserId()==-1){
            binding.myWaitlistsButton.setVisibility(View.GONE);
        }else{
            binding.myWaitlistsButton.setOnClickListener(v -> {
                NavDirections action=MyMenuDirections.actionMyMenuToMyWaitlists();
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });
        }


        return binding.getRoot();
    }


}
