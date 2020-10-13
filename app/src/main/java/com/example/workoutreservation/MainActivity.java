package com.example.workoutreservation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.workoutreservation.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {

    private int newIntentDirection;
    private NavigationView navigationView;

    public int getNewIntentDirection() {
        return newIntentDirection;
    }

    public void setNewIntentDirection(int newIntentDirection) {
        this.newIntentDirection = newIntentDirection;
    }


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;


    @Override
    protected void onStart() {
//        android.os.Debug.waitForDebugger();

        //if new intent
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if ((intent.getStringExtra("fragment")!=null)&&(intent.getStringExtra("fragment").equals("MyWaitlists")))
                    newIntentDirection = 1;
                Log.d("OnStart", "newIntentDirection = 1");


                if ((intent.getStringExtra("fragment")!=null)&&(intent.getStringExtra("fragment").equals("MyWorkouts"))) {
                    newIntentDirection = 2;
                    Log.d("OnStart", "newIntentDirection = 2");
                }
            }
        }

        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        FirebaseApp.initializeApp(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_menu);


        //setting up drawer menu
        DrawerLayout drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Menu menu = navigationView.getMenu();

        //makes drawer to appear by clicking action bar icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.syncState();

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
                    Log.d("OnNewIntent", "newIntentDirection = 1");
                    super.recreate();
                }
                if (intent.getStringExtra("fragment").equals("MyWorkouts")) {
                    newIntentDirection = 2;
                    Log.d("OnNewIntent", "newIntentDirection = 1");

                    super.recreate();
                }
            }

        }
    }
}