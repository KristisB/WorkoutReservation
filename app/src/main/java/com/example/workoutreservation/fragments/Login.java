package com.example.workoutreservation.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.example.workoutreservation.ApiService;
import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.User;
import com.example.workoutreservation.databinding.FragmentLoginBinding;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
//todo implement safe password encoding

public class Login extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
        MainActivity mainActivity = (MainActivity) requireActivity();
        User userLoging = new User();

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String email = binding.userEmailText.getText().toString();
                String password = binding.userPasswordText.getText().toString();
                String hashedPass= userLoging.hashPass(password);

                mainActivity.getService().login(email, hashedPass).enqueue(new Callback<User>() {


                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        if (response.isSuccessful()) {
                            mainActivity.saveUser(user);
                            NavDirections action = LoginDirections.actionLoginToWorkoutList();
                            Navigation.findNavController(view).navigate(action);
                            Log.d("Login", user.getFirstName() + " " + user.getFamilyName() + " login rights are " + user.getRights());
                        } else {
                            Log.d("Login", "wrong e-mail or password");
                            Toast.makeText(requireContext(), "Unable to login", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Login", "unknown failure");
                        t.printStackTrace();
                    }


                });

//        return binding.getRoot();
            }
        });
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action =LoginDirections.actionLoginToUserDataUpdate();
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });

        return binding.getRoot();
    }
}
