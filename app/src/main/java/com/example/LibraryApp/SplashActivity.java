package com.example.LibraryApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import LibraryApp.R;

import com.example.LibraryApp.Auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView img;
    SharedPreferences sharedpreferences;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        img=findViewById( R.id.image );

        Animation myanim= AnimationUtils.loadAnimation( this,R.anim.mytransition );
        img.startAnimation(myanim);

        //to check current user types
        sharedpreferences= getSharedPreferences("user_details", MODE_PRIVATE);

        isLoggedIn=sharedpreferences.getBoolean("isLoggedIn",false);


        Thread timer= new Thread(){
            public void run(){
                try{
                    sleep( 3000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    if(isLoggedIn) {

                        startActivity(new Intent(SplashActivity.this, MainDrawer.class));
                        finish();

                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        };
        timer.start();


    }
}