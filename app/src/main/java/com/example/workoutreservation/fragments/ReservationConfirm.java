package com.example.workoutreservation.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import com.example.workoutreservation.User;
import com.example.workoutreservation.Workout;
import com.example.workoutreservation.databinding.FragmentConfirmReservationBinding;
import com.example.workoutreservation.databinding.FragmentLoginBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationConfirm extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentConfirmReservationBinding binding = FragmentConfirmReservationBinding.inflate(inflater, container, false);
        MainActivity mainActivity = (MainActivity) requireActivity();
        ReservationConfirmArgs args = ReservationConfirmArgs.fromBundle(requireArguments());
        Workout workout = new Workout();
        int userId = mainActivity.getUser().getUserId();
        workout.setWorkoutId(args.getWorkoutID());

        workout.setDateTime(args.getWorkoutDate());
        workout.setDescription(args.getWorkoutDescription());
        workout.setFreePlaces(args.getFreePlaces());
        binding.reservationInfoText.setText(workout.getDateText() + " " + workout.getTimeText() + " " + workout.getDescription());
//TODO add logic if no credits, not possible to book
        Log.d("ReserveConfirmation", "userID " + userId + "; workoutId" + workout.getWorkoutId());
        if (workout.getFreePlaces() > 0) {
            binding.addToWaitlistButton.setVisibility(View.GONE);
            binding.reservationConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mainActivity.getService().reserve(workout.getWorkoutId(), userId).enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            User user = mainActivity.getUser();
                            if (response.isSuccessful()) {
                                //adds reminder into calendar
                                addToCalendar(user, workout);
                                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();

                                NavDirections action = ReservationConfirmDirections.actionReservationConfirmToWorkoutList();
                                Navigation.findNavController(view).navigate(action);
                            } else {
                                Toast.makeText(requireContext(), "Unable to reserve", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("Login", "unknown failure");
                            t.printStackTrace();
                        }

                    });
                }
            });
        } else {
            binding.reservationConfirmButton.setVisibility(View.GONE);
            binding.addToWaitlistButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mainActivity.getService().addWaitlist(workout.getWorkoutId(), userId).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            User user = mainActivity.getUser();
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();

                            NavDirections action = ReservationConfirmDirections.actionReservationConfirmToWorkoutList();
                            Navigation.findNavController(getView()).navigate(action);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("Reservation", "unknown failure");
                            t.printStackTrace();
                        }
                    });
                }
            });
        }
        return binding.getRoot();
    }

    private void addToCalendar(User user, Workout workout) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Calendar")
                .setMessage("Do you want to add to your Calendar?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, workout.getDateText())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, workout.getDateText()+3600000)
                            .putExtra(CalendarContract.Events.TITLE, workout.getDescription())
                            .putExtra(CalendarContract.Events.DESCRIPTION, workout.getDescription())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Uzupio 10")
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                            .putExtra(Intent.EXTRA_EMAIL, user.getEmail());
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .show();
    }
}
