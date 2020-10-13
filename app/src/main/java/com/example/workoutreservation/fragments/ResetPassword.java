package com.example.workoutreservation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.SharedData;
import com.example.workoutreservation.components.AppComponents;
import com.example.workoutreservation.components.ContextModule;
import com.example.workoutreservation.components.DaggerAppComponents;
import com.example.workoutreservation.model.User;
import com.example.workoutreservation.databinding.FragmentResetPasswordBinding;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentResetPasswordBinding binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        AppComponents appComponents = DaggerAppComponents.builder()
                .contextModule(new ContextModule(getContext()))
                .build();

        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate random 8 symbol password
                User user = new User();
                String currentTime = Long.toString(System.currentTimeMillis());
                String newPassword = user.hashPass(currentTime).substring(0, 8);
                String encryptedPassword = user.hashPass(newPassword);
                Log.d("ResetPassword", "new generated password " + newPassword);

                //check if entered e-mail exist in db. If so, update password and send newPassword to user email
                String email = binding.userEmailText.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getContext(), "E-mail not entered", Toast.LENGTH_SHORT).show();
                } else {
                    appComponents.getApiService().resetPassword(email, newPassword, encryptedPassword).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Password not reset. Check connection.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return binding.getRoot();
    }
}
