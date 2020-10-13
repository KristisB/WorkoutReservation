package com.example.workoutreservation.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutreservation.model.Workout;
import com.example.workoutreservation.databinding.FragmentWorkoutListItemBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyWorkoutAdapter extends RecyclerView.Adapter<MyWorkoutAdapter.MyWorkoutViewHolder> {
    private List<Workout> myWorkoutList = new ArrayList<Workout>();

    public MyWorkoutAdapter(List<Workout> readyList) {
        this.myWorkoutList = readyList;
        Log.d("ADAPTER", "Number of MyWorkouts passed: " + myWorkoutList.size());
    }

    @NonNull
    @Override
    public MyWorkoutAdapter.MyWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentWorkoutListItemBinding binding = FragmentWorkoutListItemBinding.inflate(inflater, parent, false);
        return new MyWorkoutAdapter.MyWorkoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWorkoutAdapter.MyWorkoutViewHolder holder, int position) {
        Calendar curentDate = Calendar.getInstance();
        curentDate.setTimeInMillis(myWorkoutList.get(position).getDateTime());
        boolean dateVisibility = true;
        if (position > 0) {
            Calendar previousDate = Calendar.getInstance();
            previousDate.setTimeInMillis(myWorkoutList.get(position - 1).getDateTime());

            if (curentDate.get(Calendar.DAY_OF_MONTH) == previousDate.get(Calendar.DAY_OF_MONTH)) {
                dateVisibility = false;
            }
        }
        holder.bind(myWorkoutList.get(position), dateVisibility);
    }

    @Override
    public int getItemCount() {
        return myWorkoutList.size();
    }

    public class MyWorkoutViewHolder extends RecyclerView.ViewHolder {
        private FragmentWorkoutListItemBinding binding;

        public MyWorkoutViewHolder(@NonNull FragmentWorkoutListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Workout myWorkout, boolean previousDayVisibility) {
            //makes grouping in list by date
            if (previousDayVisibility) {
                binding.date.setVisibility(View.VISIBLE);
            } else {
                binding.date.setVisibility(View.GONE);
            }

            binding.date.setText(myWorkout.getDateText());
            binding.workoutTime.setText(myWorkout.getTimeText());
            binding.workoutName.setText(myWorkout.getDescription());
            binding.workoutItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OnItemClick", "Workout ID " + myWorkout.getWorkoutId());
                    NavDirections action = MyWorkoutsDirections.actionMyWorkoutsToCancelReservation(
                            myWorkout.getWorkoutId(),
                            myWorkout.getDateTime(),
                            myWorkout.getDescription());
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            });

        }
    }
}
