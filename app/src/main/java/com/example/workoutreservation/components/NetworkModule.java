package com.example.workoutreservation.components;

import com.example.workoutreservation.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
class NetworkModule {

    @Provides
    @Singleton
    Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://workout-reservation.herokuapp.com")
//                .baseUrl("http://10.0.2.2")            // realybeje nurodomas serverio adresas
                .addConverterFactory(MoshiConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiService getApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
