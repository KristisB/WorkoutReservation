package com.example.workoutreservation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.SharedData;
import com.example.workoutreservation.components.AppComponents;
import com.example.workoutreservation.components.ContextModule;
import com.example.workoutreservation.components.DaggerAppComponents;
import com.example.workoutreservation.model.User;
import com.example.workoutreservation.model.Workout;
import com.example.workoutreservation.databinding.FragmentMyWorkoutsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWorkouts extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMyWorkoutsBinding binding = FragmentMyWorkoutsBinding.inflate(inflater, container, false);
        AppComponents appComponents = DaggerAppComponents.builder()
                .contextModule(new ContextModule(getContext()))
                .build();
        User user = appComponents.getSharedData().getUser();
        appComponents.getApiService().getMyWorkouts(user.getUserId()).enqueue(new Callback<List<Workout>>() {
            @Override
            public void onResponse(Call<List<Workout>> call, Response<List<Workout>> response) {
                List<Workout> rawList = new ArrayList<>();
                rawList = response.body();
                Log.d("myWorkoutsList", "my workouts received " + rawList.size());
//                for(Workout w:rawList){
//                    Log.d("myWorkoutsList", "my workouts received " + w.getWorkoutId());
//                }

                binding.myWorkoutsRecycleList.setAdapter(new MyWorkoutAdapter(rawList));
            }

            @Override
            public void onFailure(Call<List<Workout>> call, Throwable t) {
                Log.d("myWorkoutsList", "My workouts not received");
            }
        });

        return binding.getRoot();
    }
}
