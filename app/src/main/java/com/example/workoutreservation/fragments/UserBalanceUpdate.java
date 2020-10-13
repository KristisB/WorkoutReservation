package com.example.workoutreservation.fragments;

import android.os.Bundle;
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
import com.example.workoutreservation.SharedData;
import com.example.workoutreservation.components.AppComponents;
import com.example.workoutreservation.components.ContextModule;
import com.example.workoutreservation.components.DaggerAppComponents;
import com.example.workoutreservation.model.User;
import com.example.workoutreservation.databinding.FragmentUserBalanceUpdateBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBalanceUpdate extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentUserBalanceUpdateBinding binding = FragmentUserBalanceUpdateBinding.inflate(inflater, container, false);
        AppComponents appComponents = DaggerAppComponents.builder()
                .contextModule(new ContextModule(getContext()))
                .build();

        UserBalanceUpdateArgs args = UserBalanceUpdateArgs.fromBundle(requireArguments());
        int referenceId = appComponents.getSharedData().getUser().getUserId();
        User userA = new User();
        userA.setUserId(args.getUserId());
        appComponents.getApiService().getUserData(userA.getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (response.isSuccessful()) {
                    userA.setCredits(user.getCredits());
                    userA.setRights(user.getRights());
                    binding.userNameText.setText(user.getFirstName() + " " + user.getFamilyName());
                    binding.userEmailText.setText(user.getEmail());
                    binding.balanceText.setText(Integer.toString(user.getCredits()) + " Credits");
                    binding.rightsCheckBox.setChecked(user.getRights() == 1);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        binding.addCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addCredits = 0;
                if (!binding.addCreditsEditText.getText().toString().isEmpty()) {
                    addCredits = Integer.parseInt(binding.addCreditsEditText.getText().toString());
                    appComponents.getApiService().updateBalance(userA.getUserId(), addCredits, referenceId).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Toast.makeText(requireContext(), "New balance " + response.body(), Toast.LENGTH_SHORT).show();
                            binding.balanceText.setText(Integer.toString(response.body()));
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }
                int newBalance = userA.getCredits() + addCredits;

//                Toast.makeText(requireContext(), "New balance" + newBalance, Toast.LENGTH_SHORT).show();
                int newRights = userA.getRights();
                if (binding.rightsCheckBox.isChecked()) {
                    newRights = 1;
                } else {
                    newRights = 0;
                }

                if (!(binding.rightsCheckBox.isChecked() == (userA.getRights() == 1))) {  //checks if rights were changed
                    appComponents.getApiService().changeRights(userA.getUserId(), newRights).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Toast.makeText(requireContext(), "Rights changed", Toast.LENGTH_SHORT).show();
                            userA.setRights(response.body());
                            binding.rightsCheckBox.setChecked(response.body() == 1);


                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });

                }
            }
        });
        binding.reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = UserBalanceUpdateDirections.actionUserBalanceUpdateToLogList(userA.getUserId());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });

        return binding.getRoot();

    }
}
