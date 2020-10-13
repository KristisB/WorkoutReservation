package com.example.workoutreservation.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.workoutreservation.ApiService;
import com.example.workoutreservation.SharedData;
import com.example.workoutreservation.model.User;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (modules = {NetworkModule.class, SharedPrefsModule.class})
public interface AppComponents {
    ApiService getApiService();
    SharedData getSharedData();

}
