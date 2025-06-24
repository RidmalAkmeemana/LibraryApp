package com.example.LibraryApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.LibraryApp.Connection.RetroClient;
import com.example.LibraryApp.Screens.Attendance.AttendanceFragment;
import com.example.LibraryApp.Screens.Home.HomeFragment;
import com.example.LibraryApp.Screens.Profile.ProfileFragment;
import com.example.LibraryApp.Screens.Profile.UserModel;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import LibraryApp.R;

public class MainDrawer extends AppCompatActivity {


    String TAG="MainDrawer";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu_icon;
    ActionBarDrawerToggle toggle;
    FrameLayout contentView;
    static final float END_SCALE = 0.7f;

    ImageView nav_profile;
    ImageView tool_bar_profile;
    TextView navBar_name;


    //session for get values
    SharedPreferences sharedpreferences;
    String UserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_activity);

        //declare a session
        sharedpreferences= getSharedPreferences("user_details",MODE_PRIVATE);
        UserName=sharedpreferences.getString("username","");

        initiationView();

        navigationDrawer();

        LoadUserData();
        //Default Fragment
        openFragmentDefault(new HomeFragment());
    }

    @Override
    public void onBackPressed() {
        // Get the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container); // Replace with your container ID

        // Check if the current fragment is ProfileFragment
        if (currentFragment instanceof ProfileFragment) {
            // Replace ProfileFragment with HomeFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment()) // Replace with your container ID
                    .commit();
        } else {
            // If not ProfileFragment, proceed with default back button behavior
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LoadUserData();
    }

    private void LoadUserData() {

        Map<String,String> parameters= new HashMap<>();
        parameters.put("userName",UserName);

        Call<UserModel> getProfileDetails= RetroClient.getInstance().getApi().getProfileDetails(parameters);


        getProfileDetails.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(!response.isSuccessful()) return;

                navBar_name.setText(response.body().getU_name()+"");

                try {
                    Picasso.get().load( ""+response.body().getImgPath()+"")
                            .networkPolicy( NetworkPolicy.NO_CACHE)
                            .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .into( nav_profile );

                    Picasso.get().load( ""+response.body().getImgPath()+"")
                            .networkPolicy( NetworkPolicy.NO_CACHE)
                            .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .into( tool_bar_profile );

                }catch (Exception e){

                    Log.e(TAG,"onFailure :"+e.getMessage()+"");

                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                Log.e(TAG,"onFailure :"+t.getMessage()+"");

            }
        });
    }


    private void initiationView() {

        menu_icon=findViewById(R.id.menu_icon);
        tool_bar_profile=findViewById(R.id.tool_bar_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.container);


        navigationView=findViewById(R.id.navigation_view);
        View headerView=navigationView.getHeaderView( 0 );
        navBar_name=headerView.findViewById( R.id.navBar_name );
        nav_profile=headerView.findViewById( R.id.nav_profile );


        tool_bar_profile.setOnClickListener(view -> {
            openFragment(new ProfileFragment());
        });
    }


    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    navigationView.setCheckedItem(R.id.nav_home);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new HomeFragment());
                    break;

                case R.id.nav_attendance:
                    navigationView.setCheckedItem(R.id.nav_attendance);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new AttendanceFragment());
                    break;

                case R.id.nav_profile:
                    navigationView.setCheckedItem(R.id.nav_profile);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new ProfileFragment());
                    break;

                case R.id.nav_logout:
                    navigationView.setCheckedItem(R.id.nav_logout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    AlertMessageLogout();

                    break;

            }
            return false;
        });

        menu_icon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });

        animateNavigationDrawer();
    }



    private void AlertMessageLogout(){

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle( "Task Status" )
                .setIcon(R.drawable.waning)
                .setMessage("Do You want Logout")
                .setPositiveButton("Ok", (dialog1, which) -> LogOut())
                .setNegativeButton("Cancel", (dialoginterface, i) -> dialoginterface.cancel()).show();

    }

    private void LogOut() {

//        //sessions clear when user log out
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("username","");
        editor.clear();
        editor.commit();


        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }



    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.purple_700));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //Scale the view based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //Translating the view accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslaiton = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslaiton);

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);

        transaction.commit();
    }

    private void openFragmentDefault(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }

}