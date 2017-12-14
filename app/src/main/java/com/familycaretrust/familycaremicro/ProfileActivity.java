package com.familycaretrust.familycaremicro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Add back button
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item  = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intentProfile = new Intent(ProfileActivity.this, SearchActivity.class);
                startActivity(intentProfile);
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
            Intent intentProfile = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intentProfile);
            return true;
        }
        if(id==R.id.logout){
            Intent intentLogin = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            return true;
        }
        if(id==R.id.help){

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
