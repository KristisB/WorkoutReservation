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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.SharedData;
import com.example.workoutreservation.components.AppComponents;
import com.example.workoutreservation.components.ContextModule;
import com.example.workoutreservation.components.DaggerAppComponents;
import com.example.workoutreservation.model.WaitlistItem;
import com.example.workoutreservation.databinding.FragmentRemoveWaitlistBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoveWaitlist extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentRemoveWaitlistBinding binding = FragmentRemoveWaitlistBinding.inflate(inflater, container, false);
        RemoveWaitlistArgs args = RemoveWaitlistArgs.fromBundle(requireArguments());
        AppComponents appComponents = DaggerAppComponents.builder()
                .contextModule(new ContextModule(getContext()))
                .build();

        WaitlistItem waitlistItem = new WaitlistItem();
        int userId = appComponents.getSharedData().getUser().getUserId();
        waitlistItem.setWaitlistId(args.getWaitlistId());
        waitlistItem.setDateTime(args.getWorkoutDateTime());
        waitlistItem.setDescription(args.getWorkoutDescription());
        waitlistItem.setWorkoutId(args.getWorkoutId());
        waitlistItem.setFreePlaces(args.getFreePlaces());
        binding.removeWaitlistInfoText.setText(waitlistItem.getDateText() + " " + waitlistItem.getTimeText() + " " + waitlistItem.getDescription());

        //if free place appeared, button appears which directs to ReservationConfirm fragment
        if(waitlistItem.getFreePlaces()>0){
            binding.bookButton.setVisibility(View.VISIBLE);
            binding.bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavDirections action = RemoveWaitlistDirections.actionRemoveWaitlistToReservationConfirm(
                            waitlistItem.getWorkoutId(),userId, waitlistItem.getDateTime(),waitlistItem.getDescription(),waitlistItem.getFreePlaces() );
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            });

        }else {
            binding.bookButton.setVisibility(View.GONE);
        }

        binding.removeWaitlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appComponents.getApiService().quitWaitlist(waitlistItem.getWaitlistId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.d("Quit waitlist", "waitlist Id"+waitlistItem.getWaitlistId()+" userId "+ userId);
                        Log.d("Quit waitlist", ""+response.message());

                        NavDirections action = RemoveWaitlistDirections.actionRemoveWaitlistToMyWaitlists();
                        Navigation.findNavController(binding.getRoot()).navigate(action);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }

                });
            }



        });
        return binding.getRoot();
    }
}