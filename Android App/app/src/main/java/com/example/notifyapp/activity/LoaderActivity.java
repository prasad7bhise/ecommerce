package com.example.notifyapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.notifyapp.R;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check user already login

        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
         if(preferences1.getBoolean("login_status", false) == true){
             Intent intent =  new Intent(this,ProductListActivity.class);
             startActivity(intent);

         } else {
             Intent intent =  new Intent(this,LoginActivity.class);
             startActivity(intent);
         }
         finish();

    }

}
