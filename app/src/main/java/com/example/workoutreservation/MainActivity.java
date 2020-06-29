package com.example.workoutreservation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutreservation.databinding.ActivityMainBinding;
import com.example.workoutreservation.fragments.AddWorkout;
import com.example.workoutreservation.fragments.Login;
import com.example.workoutreservation.fragments.MyMenuDirections;
import com.example.workoutreservation.fragments.MyWaitlists;
import com.example.workoutreservation.fragments.MyWorkouts;
import com.example.workoutreservation.fragments.UserData;
import com.example.workoutreservation.fragments.WorkoutList;
import com.example.workoutreservation.fragments.WorkoutListDirections;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstName";
    public static final String FAMILY_NAME = "familyName";
    public static final String PHONE = "phone";
    public static final String RIGHTS = "rights";
    public static final String CREDITS = "credits";
    private int newIntentDirection;

    public int getNewIntentDirection() {
        return newIntentDirection;
    }

    public void setNewIntentDirection(int newIntentDirection) {
        this.newIntentDirection = newIntentDirection;
    }

    private ApiService service;
    private SharedPreferences sharedPref;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                if (intent.getStringExtra("fragment").equals("MyWaitlists")) {
                    newIntentDirection = 1;
                }
                if (intent.getStringExtra("fragment").equals("MyWorkouts")) {
                    newIntentDirection = 2;
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        String fragmentName=getIntent().getStringExtra("fragment");
//        if(fragmentName!=null){
//            Fragment fragment = new MyWaitlists();
//            Log.d("Main Activity", "onCreate: INTENT EXTRA RECEIVED"+ fragmentName);
//            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
//
//        }

        FirebaseApp.initializeApp(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_menu);


        //setting up drawer menu
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //makes drawer to appear by clicking action bar icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.syncState();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2")            // realybeje nurodomas serverio adresas
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("onSupportNavigateUp", "onSupportNavigateUp method started");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.getExtras() != null) {
                if (intent.getStringExtra("fragment").equals("MyWaitlists")) {
                    newIntentDirection = 1;
                    super.recreate();
                }
                if (intent.getStringExtra("fragment").equals("MyWorkouts")) {
                    newIntentDirection = 2;
                    super.recreate();
                }
            }

        }
    }


    public ApiService getService() {
        return service;
    }
//    public MsgApiService getMsgService(){return msgService;}

    public void saveUser(User user) {

        SharedPreferences.Editor editor = sharedPref.edit();

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

        user.setUserId(sharedPref.getInt(USER_ID, -1));
        user.setEmail(sharedPref.getString(EMAIL, ""));
        user.setPassword(sharedPref.getString(PASSWORD, ""));
        user.setFirstName(sharedPref.getString(FIRST_NAME, ""));
        user.setFamilyName(sharedPref.getString(FAMILY_NAME, ""));
        user.setPhone(sharedPref.getString(PHONE, ""));
        user.setRights(sharedPref.getInt(RIGHTS, 0));
        user.setCredits(sharedPref.getInt(CREDITS, 0));
        return user;
    }
}