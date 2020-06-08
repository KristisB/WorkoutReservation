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
import com.example.workoutreservation.User;
import com.example.workoutreservation.WaitlistItem;
import com.example.workoutreservation.Workout;
import com.example.workoutreservation.databinding.FragmentMyWaitlistsBinding;
import com.example.workoutreservation.databinding.FragmentMyWorkoutsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWaitlists extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMyWaitlistsBinding binding = FragmentMyWaitlistsBinding.inflate(inflater, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        User user = mainActivity.getUser();
        mainActivity.getService().getMyWaitlists(user.getUserId()).enqueue(new Callback<List<WaitlistItem>>() {
            @Override
            public void onResponse(Call<List<WaitlistItem>> call, Response<List<WaitlistItem>> response) {
                List<WaitlistItem> rawList = new ArrayList<>();
                rawList = response.body();
                Log.d("myWaitlists", "my waitlists received " + rawList.size());
//                for(Workout w:rawList){
//                    Log.d("myWorkoutsList", "my workouts received " + w.getWorkoutId());
//                }

                binding.myWaitlistsRecycleList.setAdapter(new MyWaitlistAdapter(rawList));
            }

            @Override
            public void onFailure(Call<List<WaitlistItem>> call, Throwable t) {
                Log.d("myWorkoutsList", "My workouts not received");
            }
        });

        return binding.getRoot();
    }
}
