package com.example.notifyapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.editName) EditText editName;
    @BindView(R.id.editEmail) EditText editEmail;
    @BindView(R.id.editPassword) EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

    }

    public void onRegister(View v){
        String name =editName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.length() == 0){
            editEmail.setError("Email is mandatory");
        } else if (password.length() == 0){
            editPassword.setError("Password is mandatory");
        } else if (name.length()==0){
            editName.setError("Name is mandatory");
        }
        else {

            final String url = Utils.getUrl(Constants.PATH_USER+"/register");

            final JsonObject body = new JsonObject();
            body.addProperty("email",email);
            body.addProperty("password",password);
            body.addProperty("name",name);

            Ion.with(this)
                    .load("POST",url)
                    .setJsonObjectBody(body)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String status = result.get("status").getAsString();
                            if (status.equals("success")){
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                String error =result.get("error").getAsString();
                                Toast.makeText(RegistrationActivity.this, error , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }


    }
    public void onCancel(View v){
        finish();
    }

}
