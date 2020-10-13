package com.example.workoutreservation.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutreservation.model.LogDataEntry;
import com.example.workoutreservation.databinding.FragmentLogItemBinding;

import java.util.ArrayList;
import java.util.List;

class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogListViewHolder> {

    private List<LogDataEntry> logDataEntries = new ArrayList<LogDataEntry>();

    public LogListAdapter(List<LogDataEntry> logDataEntries) {
        this.logDataEntries = logDataEntries;
    }

    @NonNull
    @Override
    public LogListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentLogItemBinding binding=FragmentLogItemBinding.inflate(inflater,parent,false);
        return new LogListAdapter.LogListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LogListViewHolder holder, int position) {
        boolean dateVisible = true;
        if(position>0){
            if(logDataEntries.get(position).getMonthYearText().equals
                    (logDataEntries.get(position-1).getMonthYearText())) {
                dateVisible=false;
            }
        }
        holder.bind(logDataEntries.get(position), dateVisible);
    }

    @Override
    public int getItemCount() {
        return logDataEntries.size();
    }

    public class LogListViewHolder extends RecyclerView.ViewHolder {
        private FragmentLogItemBinding binding;

        public LogListViewHolder(@NonNull FragmentLogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LogDataEntry logDataEntry, boolean dateVisible) {
            if(dateVisible){
                binding.date.setVisibility(View.VISIBLE);
            }else {
                binding.date.setVisibility(View.GONE);

            }
            binding.date.setText(logDataEntry.getMonthYearText());
            binding.balanceChangeText.setText(Integer.toString(logDataEntry.getBalanceChange()));
            binding.operationDate.setText(logDataEntry.getShortDateText());
            binding.operationTime.setText(logDataEntry.getTimeText());
            binding.operationInfo.setText(logDataEntry.getOperationInfo());
            binding.referenceName.setText(logDataEntry.getReferenceName() + " " + logDataEntry.getReferenceFamilyName());
        }
    }
}
