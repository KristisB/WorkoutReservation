package com.example.workoutreservation.fragments;

import android.content.SharedPreferences;
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
import com.example.workoutreservation.databinding.FragmentWorkoutListBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentWorkoutListBinding binding = FragmentWorkoutListBinding.inflate(inflater, container, false);
        binding.workoutsRecyclerView.setHasFixedSize(true);

        Log.d("WorkoutList", "WorkoutList fragment inflated");
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        String dateS = Long.toString(today.getTimeInMillis());

        AppComponents appComponents = DaggerAppComponents.builder()
                .contextModule(new ContextModule(getContext()))
                .build();

        User user= appComponents.getSharedData().getUser();
        appComponents.getApiService().getWorkouts(dateS, user.getUserId()).enqueue(new Callback<List<Workout>>() {
            List<Workout> workouts = new ArrayList<>();

            @Override
            public void onResponse(Call<List<Workout>> call, Response<List<Workout>> response) {
                List<Workout> rawList = new ArrayList<>();
                rawList = response.body();
                if (response.isSuccessful()) {
                    Log.d("workoutsList", "workouts received " + rawList.size());
                    List<Workout> readyList = new ArrayList<>(listForRecycler(rawList));
                    binding.workoutsRecyclerView.setAdapter(new WorkoutAdapter(readyList,user));
                } else {
                    Log.d("workoutsList", "workouts not received");
                }

            }

            @Override
            public void onFailure(Call<List<Workout>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return binding.getRoot();
    }

    //makes ajusted list sorted each day and with empty entries if day is empty
    public ArrayList<Workout> listForRecycler(List<Workout> rawList) {
        ArrayList<Workout> ajustedList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        for (int i = 0; i < 365; i++) {
            Workout emptyWorkout = new Workout();
            emptyWorkout.setWorkoutId(-1);
            boolean isEmptyDay = true;

            for (int j = 0; j < rawList.size(); j++) {
                Workout currentWorkout = rawList.get(j);
                Calendar workoutDate = Calendar.getInstance();
                workoutDate.setTimeInMillis(currentWorkout.getDateTime());
                if (calendar.get(Calendar.DAY_OF_YEAR) == workoutDate.get(Calendar.DAY_OF_YEAR)) {
                    ajustedList.add(currentWorkout);
                    isEmptyDay = false;
                }
            }
            if (isEmptyDay) {
                emptyWorkout.setDateTime(calendar.getTimeInMillis());
                ajustedList.add(emptyWorkout);
            }
            calendar.roll(Calendar.DAY_OF_YEAR, 1);
        }

        return ajustedList;
    }

}

