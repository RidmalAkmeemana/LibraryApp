package com.example.LibraryApp.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.LibraryApp.Connection.RetroClient;
import com.example.LibraryApp.MainDrawer;
import LibraryApp.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;

public class LoginActivity extends AppCompatActivity {

    //Debug TAG
    private ProgressDialog progressDialog;
    private  static  final String  TAG ="LoginActivity";
    EditText userNameEdit;
    EditText passwordEdit;
    Button loginBtn;

    //session preparing for Set the values
    SharedPreferences sharedpreferences;

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initiationView();

        //Login Button

        loginBtn.setOnClickListener(view -> {

            LoginAPICall();
            Log.e(TAG,"Click on Login Button");
        });
    }

    private void LoginAPICall() {
        //Validations
        if (TextUtils.isEmpty(userNameEdit.getText().toString())) {
            userNameEdit.setError("Username is Required !");
            return;
        }

        if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
            passwordEdit.setError("Password is Required !");
            return;
        }

        //API Call
        Map<String,String> parameters= new HashMap<>();

        parameters.put("userName",userNameEdit.getText().toString());
        parameters.put("password",passwordEdit.getText().toString());

        showProgressDialog("Loading...");

        Call<UserCredential> auth= RetroClient.getInstance().getApi().auth(parameters);

        auth.enqueue(new Callback<UserCredential>() {
            @Override
            public void onResponse(Call<UserCredential> call, Response<UserCredential> response) {

                hideProgressDialog();
                if(!response.isSuccessful()) return;

                Log.e(TAG,"onResponse :"+response.body().getSuccess()+"");

                    if(response.body().getSuccess().equals("true")) {


                        //set the session
                        sharedpreferences =getSharedPreferences("user_details", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("isLoggedIn",true);
                        editor.putString("st_username",response.body().getSt_username());
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, MainDrawer.class));
                        finish();


                    }else{
                        dialogBox("Username or Password is Incorrect !! ","Login");
                    }

                }

            @Override
            public void onFailure(Call<UserCredential> call, Throwable t) {

                hideProgressDialog();
                Log.e(TAG,"onFailure :"+t.getMessage()+"");
                dialogBox(t.getMessage(),"Login");
            }
        });

    }

    private void initiationView() {
        userNameEdit= findViewById(R.id.userNameEdit);
        passwordEdit= findViewById(R.id.passwordEdit);
        loginBtn= findViewById(R.id.loginBtn);
    }


    public void dialogBox(String message,String title) {


        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.waning);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();


    }
}