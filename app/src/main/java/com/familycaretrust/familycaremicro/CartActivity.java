package com.familycaretrust.familycaremicro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //cart data
        final SharedPreferences cart = getSharedPreferences("cart", MODE_PRIVATE);
        //loadString jArray = cart.getString("cart", null);

        TextView one = (TextView) findViewById(R.id.principal);
        one.setText(cart.getString("cart", null));

        String[] cartArray = {"beacon","rice","beans","fufu","eba","beacon","rice","beans","fufu","eba","one","two"};
        ListAdapter cartAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, cartArray);
        ListView cartView = (ListView) findViewById(R.id.cartList);
        cartView.setAdapter(cartAdapter);


        // add to list from json
        //SimpleAdapter simpleAdapter = new SimpleAdapter(this, countryList, android.R.layout.simple_list_item_1, new String[] {"country"}, new int[] {android.R.id.text1});
        //cartView.setAdapter(simpleAdapter);


        try {
            ArrayList<String> items = new ArrayList<String>();
            JSONObject jo = new JSONObject(cart.getString("cart", null));
            JSONArray jsonArray = new JSONArray(jo);

        } catch (JSONException e) {
            e.printStackTrace();
        }





        //add new nominee
        final Button btnAddNom = (Button) findViewById(R.id.btnAddNom);
        btnAddNom.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intentNoms = new Intent(CartActivity.this, beneActivity.class);
                CartActivity.this.startActivity(intentNoms);
            }
        });
        //clear sharepreference and back to homme
        final Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
                progressDialog.show();
                SharedPreferences.Editor editor = cart.edit();
                editor.clear();
                editor.apply();
                Intent intentHome = new Intent(CartActivity.this, HomeActivity.class);
                CartActivity.this.startActivity(intentHome);
            }
        });

    }

    private HashMap<String, String> createEmployee(String name, String age, String packages, String price){
        HashMap<String, String> cartListView = new HashMap<String, String>();
        cartListView.put(name, name);
        cartListView.put(age, age);
        cartListView.put(packages, packages);
        cartListView.put(price, price);
        return cartListView;
    }


}
