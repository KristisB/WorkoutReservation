package com.example.workoutreservation.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.User;
import com.example.workoutreservation.databinding.FragmentUserDataBinding;
import com.example.workoutreservation.databinding.FragmentUserDataUpdateBinding;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDataUpdate extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentUserDataUpdateBinding binding = FragmentUserDataUpdateBinding.inflate(inflater, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        User user = mainActivity.getUser();
        if (user.getUserId() > -1) {
            binding.userNameEditText.setText(user.getFirstName());
            binding.userFamilyNameEditText.setText(user.getFamilyName());
            binding.userEmailEditText.setText(user.getEmail());
            binding.userPhoneEditText.setText(user.getPhone());
            binding.signUpButton.setVisibility(View.GONE);
            binding.updateUserInfoButton.setVisibility(View.VISIBLE);
            binding.updateUserInfoButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    user.setFirstName(binding.userNameEditText.getText().toString());
                    user.setFamilyName(binding.userFamilyNameEditText.getText().toString());
                    user.setEmail(binding.userEmailEditText.getText().toString());
                    user.setPhone(binding.userPhoneEditText.getText().toString());
                    if (!binding.userPasswordEditText.getText().toString().isEmpty()) {
                        String hashedPass=user.hashPass(binding.userPasswordEditText.getText().toString());
                        user.setPassword(hashedPass);
                    }
                    mainActivity.getService().saveUserData(
                            user.getUserId(),
                            user.getFirstName(),
                            user.getFamilyName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getPhone()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    Toast.makeText(requireContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 201) {
                                    mainActivity.saveUser(user);
                                    NavDirections action= UserDataUpdateDirections.actionUserDataUpdateToUserData();
                                    Navigation.findNavController(binding.getRoot()).navigate(action);
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(requireContext(), "Response failure. Account not updated", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
        } else {
            binding.updateUserInfoButton.setVisibility(View.GONE);
            binding.signUpButton.setVisibility(View.VISIBLE);
            binding.signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.setFirstName(binding.userNameEditText.getText().toString());
                    user.setFamilyName(binding.userFamilyNameEditText.getText().toString());
                    user.setEmail(binding.userEmailEditText.getText().toString());
                    user.setPhone(binding.userPhoneEditText.getText().toString());
                    if (!binding.userPasswordEditText.getText().toString().isEmpty()) {
                        String hashedPass=user.hashPass(binding.userPasswordEditText.getText().toString());
                        user.setPassword(hashedPass);
                    }
                    mainActivity.getService().saveUserData(
                            user.getUserId(),
                            user.getFirstName(),
                            user.getFamilyName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getPhone()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    Toast.makeText(requireContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 201) {
                                    NavDirections action= UserDataUpdateDirections.actionUserDataUpdateToLogin();
                                    Navigation.findNavController(binding.getRoot()).navigate(action);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(requireContext(), "Response failure. Account not created", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }


        return binding.getRoot();
    }
}
