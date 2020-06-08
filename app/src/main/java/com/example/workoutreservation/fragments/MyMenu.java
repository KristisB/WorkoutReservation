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

import java.util.Calendar;

//TODO make menu reachable from anywhere
//TODO make notifications

public class MyMenu extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        FragmentMenuBinding binding = FragmentMenuBinding.inflate(inflater, container, false);
        User user = mainActivity.getUser();
        Log.d("LOGIN", "USER ID: " + user.getUserId() + "USER NAME: " + user.getFirstName());

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
