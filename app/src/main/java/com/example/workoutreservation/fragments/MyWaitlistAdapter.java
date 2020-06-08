package com.example.workoutreservation.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutreservation.WaitlistItem;
import com.example.workoutreservation.Workout;
import com.example.workoutreservation.databinding.FragmentWorkoutListItemBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyWaitlistAdapter extends RecyclerView.Adapter<MyWaitlistAdapter.MyWaitlistViewHolder> {
    private List<WaitlistItem> myWaitlist = new ArrayList<WaitlistItem>();

    public MyWaitlistAdapter(List<WaitlistItem> readyList) {
        this.myWaitlist = readyList;
        Log.d("ADAPTER", "Number of MyWaitlists passed: " + myWaitlist.size());
    }

    @NonNull
    @Override
    public MyWaitlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentWorkoutListItemBinding binding = FragmentWorkoutListItemBinding.inflate(inflater, parent, false);
        return new MyWaitlistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWaitlistViewHolder holder, int position) {
        Calendar curentDate = Calendar.getInstance();
        curentDate.setTimeInMillis(myWaitlist.get(position).getDateTime());
        boolean dateVisibility = true;
        if (position > 0) {
            Calendar previousDate = Calendar.getInstance();
            previousDate.setTimeInMillis(myWaitlist.get(position - 1).getDateTime());

            if (curentDate.get(Calendar.DAY_OF_MONTH) == previousDate.get(Calendar.DAY_OF_MONTH)) {
                dateVisibility = false;
            }
        }
        holder.bind(myWaitlist.get(position), dateVisibility);
    }

    @Override
    public int getItemCount() {
        return myWaitlist.size();
    }

    public class MyWaitlistViewHolder extends RecyclerView.ViewHolder {
        private FragmentWorkoutListItemBinding binding;

        public MyWaitlistViewHolder(@NonNull FragmentWorkoutListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WaitlistItem myWaitlistItem, boolean previousDayVisibility) {
            //makes grouping in list by date
            if (previousDayVisibility) {
                binding.date.setVisibility(View.VISIBLE);
            } else {
                binding.date.setVisibility(View.GONE);
            }
//TODO add window to show #which in line
            binding.date.setText(myWaitlistItem.getDateText());
            binding.workoutTime.setText(myWaitlistItem.getTimeText());
            binding.workoutName.setText(myWaitlistItem.getDescription());
            binding.workoutItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("OnItemClick", "Workout ID " + myWaitlistItem.getWorkoutId());
                    NavDirections action = MyWaitlistsDirections.actionMyWaitlistsToRemoveWaitlist(
                            myWaitlistItem.getWaitlistId(),
                            myWaitlistItem.getDateTime(),
                            myWaitlistItem.getDescription());
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            });

        }
    }
}
