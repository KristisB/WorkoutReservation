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
import com.example.workoutreservation.databinding.FragmentUserListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentUserListBinding binding = FragmentUserListBinding.inflate(inflater, container, false);
        binding.usersRecyclerView.setHasFixedSize(true);
        MainActivity mainActivity = (MainActivity) getActivity();
        List<User> userList=new ArrayList<>();

        User user = mainActivity.getUser();
        if (user.getRights() == 1) {
            mainActivity.getService().getAllUsers(user.getRights()).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    List<User> usersList = new ArrayList<>();
                    usersList = response.body();
                    Log.d("UsersList", "users list received " + usersList.size());
                    binding.usersRecyclerView.setAdapter(new UsersListAdapter(usersList));

                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.d("USERS LIST", t.getMessage());
                }
            });
        }
        return binding.getRoot();
    }

}
