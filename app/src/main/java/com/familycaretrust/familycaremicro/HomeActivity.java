package com.familycaretrust.familycaremicro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //load logged in user
        TextView aName = (TextView) findViewById(R.id.username);
        TextView aEmail = (TextView) findViewById(R.id.email);

        //shared data
        SharedPreferences user_info = getSharedPreferences("logUser", MODE_PRIVATE);
        String restoredText = user_info.getString("text", null);
        aName.setText(user_info.getString("name", null));
        aEmail.setText(user_info.getString("email", null));
        int userId = user_info.getInt("id", 0);


        final ImageView linkProfile = (ImageView) findViewById(R.id.linkProfile);
        final LinearLayout layoutNewCust = (LinearLayout) findViewById(R.id.layoutNewCust);


        linkProfile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                HomeActivity.this.startActivity(intentProfile);
            }
        });

        //redirect to new registration
        layoutNewCust.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intentRegister = new Intent(HomeActivity.this, RegisterActivity.class);
                HomeActivity.this.startActivity(intentRegister);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar);
        progressbar.setVisibility(View.GONE);

        MenuItem item  = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        //Search Query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            String mesg = jsonResponse.getString("message");

                            if (status==1) {
                                String name = jsonResponse.getString("name");
                                String mrn = jsonResponse.getString("mrn");
                                String packages = jsonResponse.getString("package");
                                double fee = jsonResponse.getDouble("fee");
                                int id = jsonResponse.getInt("user_id");

                                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("mrn", mrn);
                                intent.putExtra("package", packages);
                                intent.putExtra("id", id);


                                HomeActivity.this.startActivity(intent);
                            }else if(status==6){
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                progressbar.setVisibility(View.VISIBLE);
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        super.run();
                        for (int i=0;i<=100;){
                            try{
                                sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            progressbar.setProgress(i);
                            i=i+10;
                        }
                    }
                };
                thread.start();

                SearchRequest request = new SearchRequest(query, responseListener);
                RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                queue.add(request);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.profile){
            Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intentProfile);
            return true;
        }
        if(id==R.id.logout){
            Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            return true;
        }
        if(id==R.id.help){

            return true;
        }

        return true;
    }






}
