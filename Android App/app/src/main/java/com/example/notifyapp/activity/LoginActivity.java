package com.example.notifyapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notifyapp.R;
import com.example.notifyapp.utils.Constants;
import com.example.notifyapp.utils.Utils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editEmail) EditText editEmail;
    @BindView(R.id.editPassword) EditText editPassword;
    @BindView(R.id.checkBoxRemember) CheckBox checkBoxRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public void onLogin(View v){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.length() == 0){
            editEmail.setError("Email is mandatory");
        } else if (password.length() == 0){
            editPassword.setError("Password is mandatory");
        } else {
             final String url = Utils.getUrl(Constants.PATH_USER+"/login");

            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
             String device_tocken = preferences.getString("device_token","");
             final JsonObject body = new JsonObject();
             body.addProperty("email", email);
             body.addProperty("password", password);
             body.addProperty("device_token", device_tocken);

            Ion.with(this)
                    .load("POST",url)
                    .setJsonObjectBody(body)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String status = result.get("status").getAsString();
                            if (status.equals("success")){

                                JsonObject object =result.get("data").getAsJsonObject();
                                //login credentials save checkbox attempted
                                SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

                                preferences1.edit()
                                        .putInt("id",object.get("id").getAsInt())
                                        .putString("name", object.get("name").getAsString())
                                        .putString("email",object.get("email").getAsString())
                                        .putBoolean("login_status",true)
                                        .commit();



                                Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                String error = result.get("error").getAsString();
                                Toast.makeText(LoginActivity.this, error , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }



    }

    public void onRegister(View v){
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();

    }
}

