package com.familycaretrust.familycaremicro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        final String TAG = this.getClass().getName();

        final EditText txtemail = (EditText) findViewById(R.id.email);
        final EditText txtpassword = (EditText) findViewById(R.id.password);
        final Button bLogin = (Button) findViewById(R.id.btnLogin);
        final ProgressBar preloader = (ProgressBar) findViewById(R.id.preloader);
        preloader.setVisibility(View.GONE);

        final SharedPreferences.Editor user_session = getSharedPreferences("logUser", MODE_PRIVATE).edit();



        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = txtemail.getText().toString();
                final String pwd = txtpassword.getText().toString();


                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int success = jsonResponse.getInt("status");
                            String mesg = jsonResponse.getString("message");

                            if (success==1) {
                                String name = jsonResponse.getString("name");
                                int id = jsonResponse.getInt("user_id");

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                //shared data
                                user_session.putString("name", name);
                                user_session.putString("email", mail);
                                user_session.putInt("id", id);
                                user_session.apply();

                                LoginActivity.this.startActivity(intent);
                            }else if(success==5){
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }else if(success==2){
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }
                            preloader.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                preloader.setVisibility(View.VISIBLE);
                LoginRequest loginRequest = new LoginRequest(mail, pwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

                /*Log.d(TAG, shareData.getString("shared_name",""));
                Log.d(TAG, shareData.getString("shared_email",""));*/


            }
        });

    }
}
