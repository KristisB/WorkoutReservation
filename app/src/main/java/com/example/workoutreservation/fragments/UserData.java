package com.example.workoutreservation.fragments;

import android.os.Bundle;
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
import com.example.workoutreservation.databinding.FragmentUserDataBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserData extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentUserDataBinding binding = FragmentUserDataBinding.inflate(inflater, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        User user = mainActivity.getUser();
        mainActivity.getService().getUserData(user.getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (response.isSuccessful()) {
                    mainActivity.saveUser(user);
                    binding.userNameText.setText(user.getFirstName() + " " + user.getFamilyName());
                    binding.userEmailText.setText(user.getEmail());
                    binding.balanceText.setText(Integer.toString(user.getCredits()) + " Credits");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
//        user=mainActivity.getUser();
        binding.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUserId(-1);
                user.setEmail("");
                user.setPassword("");
                user.setFirstName("");
                user.setFamilyName("");
                user.setPhone("");
                user.setRights(0);
                user.setCredits(0);
                mainActivity.saveUser(user);
                NavDirections action = UserDataDirections.actionUserDataToMyMenu();
                Navigation.findNavController(binding.getRoot()).navigate(action);

            }
        });
        binding.updateUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = UserDataDirections.actionUserDataToUserDataUpdate();
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });

        binding.reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = UserDataDirections.actionUserDataToLogList(user.getUserId());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });

        return binding.getRoot();

    }
}
