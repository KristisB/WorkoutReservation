package com.example.workoutreservation.fragments;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.R;
import com.example.workoutreservation.model.User;
import com.example.workoutreservation.model.Workout;
import com.example.workoutreservation.databinding.FragmentWorkoutListItemBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<Workout> workoutList = new ArrayList<Workout>();
    User user;


    public WorkoutAdapter(List<Workout> readyList, User user) {
        this.workoutList = readyList;
        this.user = user;
        for (Workout workout : readyList) {
            Log.d("ADAPTER", "workout ID" + workout.getWorkoutId());
        }
    }


    @NonNull
    @Override
    public WorkoutAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentWorkoutListItemBinding binding = FragmentWorkoutListItemBinding.inflate(inflater, parent, false);
        return new WorkoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Calendar curentDate = Calendar.getInstance();
        curentDate.setTimeInMillis(workoutList.get(position).getDateTime());
        boolean dateVisibility = true;
        if (position > 0) {
            Calendar previousDate = Calendar.getInstance();
            previousDate.setTimeInMillis(workoutList.get(position - 1).getDateTime());

            if (curentDate.get(Calendar.DAY_OF_MONTH) == previousDate.get(Calendar.DAY_OF_MONTH)) {
                dateVisibility = false;
            }
        }
        holder.bind(workoutList.get(position), user, dateVisibility);


    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private FragmentWorkoutListItemBinding binding;


        public WorkoutViewHolder(@NonNull FragmentWorkoutListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Workout workout, User user, boolean previousDayVisibility) {

            if (previousDayVisibility) {
                binding.date.setVisibility(View.VISIBLE);
            } else {
                binding.date.setVisibility(View.GONE);
            }

            if (workout.getWorkoutId() == -1) {
                binding.workoutItemLayout.setVisibility(View.INVISIBLE);
                binding.date.setText(workout.getDateText());
            } else {
                binding.workoutItemLayout.setVisibility(View.VISIBLE);
                binding.date.setText(workout.getDateText());
                binding.workoutTime.setText(workout.getTimeText());
                binding.workoutName.setText(workout.getDescription());
                binding.freePlacesText.setText("free places " + workout.getFreePlaces() + " / " + workout.getMaxGroupSize());
                if (workout.getExtraInfo1() > 0) {
                    binding.workoutListItemLayout.setBackgroundColor(Color.parseColor("#CC666666"));
                    binding.workoutItemIconImage.setImageResource(R.drawable.ic_check_circle);
                } else {
                    if (workout.getExtraInfo2() > 0) {
                        binding.workoutListItemLayout.setBackgroundColor(Color.parseColor("#CC999999"));
                        binding.workoutItemIconImage.setImageResource(R.drawable.ic_schedule);
                    } else {
                        binding.workoutListItemLayout.setBackgroundColor(Color.parseColor("#CCEEEEEE"));
                        binding.workoutItemIconImage.setImageResource(R.drawable.ic_right);
                    }
                }
            }

//            User user = mainActivity.getUser();

            if (user.getRights() == 1) {
                binding.date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NavDirections action = WorkoutListDirections.actionWorkoutListToAddWorkout(workout.getDateTime());
                        Navigation.findNavController(binding.getRoot()).navigate(action);
                    }
                });
            }
            if (workout.getDateTime() > System.currentTimeMillis()) {
                binding.workoutItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("OnItemClick", "Workout ID " + workout.getWorkoutId());
                        Log.d("OnItemClick", "Workout Extra 1= " + workout.getExtraInfo1() +" Workout Extra 2= "+workout.getExtraInfo2());

                        Log.d("Adapter", "user Id " + user.getUserId());

                        NavDirections actionToBookWorkout = WorkoutListDirections.actionWorkoutListToReservationConfirm2(
                                workout.getWorkoutId(),
                                user.getUserId(),
                                workout.getDateTime(),
                                workout.getDescription(),
                                workout.getFreePlaces());
                        NavDirections actionToCancelBooking = WorkoutListDirections.actionWorkoutListToCancelReservation(
                                workout.getWorkoutId(),
                                workout.getDateTime(),
                                workout.getDescription());
                        NavDirections actionToWaitlists = WorkoutListDirections.actionWorkoutListToMyWaitlists();
                        if (workout.getExtraInfo1() > 0) {
                            Navigation.findNavController(binding.getRoot()).navigate(actionToCancelBooking);
                        } else {
                            if(workout.getExtraInfo2()>0) {
                                Navigation.findNavController(binding.getRoot()).navigate(actionToWaitlists);
                            } else {
                                Navigation.findNavController(binding.getRoot()).navigate(actionToBookWorkout);

                            }
                        }

                    }
                });
            }
        }


    }

}
