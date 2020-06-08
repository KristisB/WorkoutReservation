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
import com.example.workoutreservation.WaitlistItem;
import com.example.workoutreservation.Workout;
import com.example.workoutreservation.databinding.FragmentCancelReservationBinding;
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
        MainActivity mainActivity = (MainActivity) requireActivity();
        WaitlistItem waitlistItem = new WaitlistItem();
        int userId = mainActivity.getUser().getUserId();
        waitlistItem.setWaitlistId(args.getWaitlistId());
        waitlistItem.setDateTime(args.getWorkoutDateTime());
        waitlistItem.setDescription(args.getWorkoutDescription());
        binding.removeWaitlistInfoText.setText(waitlistItem.getDateText() + " " + waitlistItem.getTimeText() + " " + waitlistItem.getDescription());
        binding.removeWaitlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.getService().quitWaitlist(waitlistItem.getWaitlistId()).enqueue(new Callback<ResponseBody>() {
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