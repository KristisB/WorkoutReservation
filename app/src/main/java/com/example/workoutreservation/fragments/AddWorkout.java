package com.example.workoutreservation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.Workout;
import com.example.workoutreservation.databinding.FragmentAddWorkoutBinding;

import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkout extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) requireActivity();

        FragmentAddWorkoutBinding binding = FragmentAddWorkoutBinding.inflate(inflater, container, false);
        binding.addWorkoutTime.setIs24HourView(true);
        AddWorkoutArgs args= AddWorkoutArgs.fromBundle(requireArguments());
        long dateTime=args.getWorkoutDate();
        binding.addWorkoutDate.setMinDate(dateTime);

        binding.submitButton.setOnClickListener(v -> {
            Workout workout = new Workout();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, binding.addWorkoutDate.getYear());
            calendar.set(Calendar.MONTH, binding.addWorkoutDate.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, binding.addWorkoutDate.getDayOfMonth());
            calendar.set(Calendar.HOUR_OF_DAY, binding.addWorkoutTime.getHour());
            calendar.set(Calendar.MINUTE, binding.addWorkoutTime.getMinute());
            workout.setDateTime(calendar.getTimeInMillis());
            workout.setDescription(binding.workoutDescriptionText.getText().toString());
            workout.setMaxGroupSize(Integer.parseInt(binding.maxGroupSizeText.getText().toString()));
            mainActivity.getService().addWorkout(workout.getDateTime(),workout.getMaxGroupSize(),workout.getDescription()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("AddWorkout",response.message() );
                    Toast.makeText(requireContext(), "Workout added", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


//            int maxGroupSize=Integer.getInteger(binding.maxGroupSizeText.getText().toString());
//            String description=binding.workoutDescriptionText.getText().toString();


        });
        return binding.getRoot();
    }
}
