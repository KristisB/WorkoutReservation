package com.example.workoutreservation.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutreservation.User;
import com.example.workoutreservation.databinding.FragmentUserListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder> {
    private List<User> usersList = new ArrayList<User>();

    public UsersListAdapter(List<User> usersList){
        this.usersList=usersList;
    }

    @NonNull
    @Override
    public UsersListAdapter.UsersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FragmentUserListItemBinding binding=FragmentUserListItemBinding.inflate(inflater,parent,false);
        return new UsersListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListViewHolder holder, int position) {
        holder.bind(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UsersListViewHolder extends RecyclerView.ViewHolder {
        private FragmentUserListItemBinding binding;

        public UsersListViewHolder(@NonNull FragmentUserListItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
        //todo fix layout
        //todo add reservation count
        public void bind(User user){
            binding.userNameText.setText(user.getFirstName()+" "+user.getFamilyName());
            binding.userEmailText.setText(user.getEmail());
            binding.userPhoneText.setText(user.getPhone());
            binding.availableCreditsText.setText(Integer.toString(user.getCredits()));
            String rights="";
            if (user.getRights()==1){
                rights="User rights: Administrator";
            }else {
                rights="User rights: User";
            }
            binding.userRightsText.setText(rights);
            binding.userDataInsideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavDirections action =UsersListDirections.actionUsersListToUserBalanceUpdate(user.getUserId());
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }
            });
        }

    }

}
