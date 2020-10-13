package com.example.workoutreservation;

import android.content.SharedPreferences;
import com.example.workoutreservation.model.User;

public class SharedData {
    private SharedPreferences sharedPreferences;
    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstName";
    public static final String FAMILY_NAME = "familyName";
    public static final String PHONE = "phone";
    public static final String RIGHTS = "rights";
    public static final String CREDITS = "credits";

    public SharedData(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }


    public void saveUser(User user) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(USER_ID, user.getUserId());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(PASSWORD, user.getPassword());
        editor.putString(FIRST_NAME, user.getFirstName());
        editor.putString(FAMILY_NAME, user.getFamilyName());
        editor.putString(PHONE, user.getPhone());
        editor.putInt(RIGHTS, user.getRights());
        editor.putInt(CREDITS, user.getCredits());

        editor.commit();
    }

    public User getUser() {
        User user = new User();

        user.setUserId(sharedPreferences.getInt(USER_ID, -1));
        user.setEmail(sharedPreferences.getString(EMAIL, ""));
        user.setPassword(sharedPreferences.getString(PASSWORD, ""));
        user.setFirstName(sharedPreferences.getString(FIRST_NAME, ""));
        user.setFamilyName(sharedPreferences.getString(FAMILY_NAME, ""));
        user.setPhone(sharedPreferences.getString(PHONE, ""));
        user.setRights(sharedPreferences.getInt(RIGHTS, 0));
        user.setCredits(sharedPreferences.getInt(CREDITS, 0));
        return user;
    }
}
