package com.familycaretrust.familycaremicro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //load results
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String mrn = intent.getStringExtra("mrn");

        TextView rName = (TextView) findViewById(R.id.custNameHolder);
        TextView rMrn = (TextView) findViewById(R.id.mrnHolder);

        rName.setText(name);
        rMrn.setText(mrn);


        //Add back button
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Button btnSearch = (Button) findViewById(R.id.btnSearch);
        final ProgressBar preloader = (ProgressBar) findViewById(R.id.preloader);
        preloader.setVisibility(View.GONE);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                preloader.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item  = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intentProfile = new Intent(SearchActivity.this, SearchActivity.class);
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
            Intent intentProfile = new Intent(SearchActivity.this, ProfileActivity.class);
            startActivity(intentProfile);
            return true;
        }
        if(id==R.id.logout){
            Intent intentLogin = new Intent(SearchActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            return true;
        }
        if(id==R.id.help){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
