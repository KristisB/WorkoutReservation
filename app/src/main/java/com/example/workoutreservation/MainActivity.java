package com.example.workoutreservation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.workoutreservation.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

import retrofit2.Retrofit;
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
    private NavigationView navigationView;

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

//        Menu menu = navigationView.getMenu();
//        if (user1.getRights() != 1) {
//            menu.findItem(R.id.usersList).setVisible(false);
//        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = getPreferences(Context.MODE_PRIVATE);


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
        User logedUser = getUser();
        if (logedUser.getRights() != 1) {
            menu.findItem(R.id.usersList).setVisible(false);
        }

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