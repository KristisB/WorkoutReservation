package com.example.workoutreservation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workoutreservation.components.AppComponents;
import com.example.workoutreservation.components.ContextModule;
import com.example.workoutreservation.components.DaggerAppComponents;
import com.example.workoutreservation.model.LogDataEntry;
import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.model.User;
import com.example.workoutreservation.databinding.FragmentLogListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLogListBinding binding = FragmentLogListBinding.inflate(inflater, container, false);
        AppComponents appComponents = DaggerAppComponents.builder()
                .contextModule(new ContextModule(getContext()))
                .build();

        binding.logListRecyclerView.setHasFixedSize(true);
        LogListArgs args = LogListArgs.fromBundle(requireArguments());
        int userId = args.getUserId();
        appComponents.getApiService().getUserData(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                binding.userNameText.setText(user.getFirstName()+" "+user.getFamilyName());
                binding.balanceText.setText(Integer.toString(user.getCredits()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        appComponents.getApiService().getLog(userId).enqueue(new Callback<List<LogDataEntry>>() {
            @Override
            public void onResponse(Call<List<LogDataEntry>> call, Response<List<LogDataEntry>> response) {
                List<LogDataEntry> logData = new ArrayList<>();
                logData = response.body();
                binding.logListRecyclerView.setAdapter(new LogListAdapter(logData));
            }

            @Override
            public void onFailure(Call<List<LogDataEntry>> call, Throwable t) {

            }
        });
        return binding.getRoot();
    }
}
