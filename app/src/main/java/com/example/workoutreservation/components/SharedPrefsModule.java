package com.example.workoutreservation.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.workoutreservation.SharedData;
import com.example.workoutreservation.model.User;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
class SharedPrefsModule {

    @Provides
     SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    SharedData getSharedData(SharedPreferences sharedPreferences){
        return new SharedData(sharedPreferences);
    }

}
