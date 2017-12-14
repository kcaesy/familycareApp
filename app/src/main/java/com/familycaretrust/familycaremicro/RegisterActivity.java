package com.familycaretrust.familycaremicro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String selectedPackage = "6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final RadioGroup packageGroup = (RadioGroup) findViewById(R.id.custPackage);
        final RadioButton bronze = (RadioButton) findViewById(R.id.pack6);
        final RadioButton silver = (RadioButton) findViewById(R.id.pack7);
        final RadioButton gold = (RadioButton) findViewById(R.id.pack8);

        final EditText customer_name = (EditText) findViewById(R.id.custName);
        final EditText customer_mob = (EditText) findViewById(R.id.custPhone);
        final EditText customer_addr = (EditText) findViewById(R.id.custAddress);
        final EditText customer_gender = (EditText) findViewById(R.id.custGender);
        final EditText customer_dob = (EditText) findViewById(R.id.custDob);
        final EditText customer_marital = (EditText) findViewById(R.id.custMarital);
        final Button btnReg = (Button) findViewById(R.id.btnReg);
        bronze.setSelected(true);
        //customer_dob.setEnabled(false);

        final SharedPreferences.Editor new_customer = getSharedPreferences("cart", MODE_PRIVATE).edit();

        //on package select
        packageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.pack6){
                    selectedPackage = "6";
                }else if(i==R.id.pack7){
                    selectedPackage = "7";
                }else if(i==R.id.pack8){
                    selectedPackage = "8";
                }
            }
        });

        // on submit
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.show();

                final String fullname = customer_name.getText().toString();
                final String mobile = customer_mob.getText().toString();
                final String address = customer_addr.getText().toString();
                final String gender = customer_gender.getText().toString();
                final String maritalStatus = customer_marital.getText().toString();
                final String dob = customer_dob.getText().toString();
                //final String fullname = customer_name.getText().toString();

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            String mesg = jsonResponse.getString("message");

                            if (status==1) {
                                String principal = jsonResponse.getString("principal");
                                //int id = jsonResponse.getInt("user_id");

                                Intent intent = new Intent(RegisterActivity.this, CartActivity.class);

                                //shared data
                                new_customer.putString("cart", principal);
                                new_customer.apply();

                                RegisterActivity.this.startActivity(intent);
                            }else if(status==5){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }else if(status==2){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }
                            //preloader.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                NewPrincipalRequest request = new NewPrincipalRequest(fullname, mobile, dob, selectedPackage, gender, maritalStatus, address,
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(request);
            };
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

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
                                String packages = jsonResponse.getString("package");
                                double fee = jsonResponse.getDouble("fee");
                                int id = jsonResponse.getInt("user_id");

                                Intent intent = new Intent(RegisterActivity.this, SearchActivity.class);
                                intent.putExtra("name", name);
                                //intent.putExtra("age", age);
                                intent.putExtra("id", id);
                                RegisterActivity.this.startActivity(intent);
                            }else if(status==6){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                SearchRequest request = new SearchRequest(query, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
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
            Intent intentProfile = new Intent(RegisterActivity.this, ProfileActivity.class);
            startActivity(intentProfile);
            return true;
        }
        if(id==R.id.logout){
            Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            return true;
        }
        if(id==R.id.help){

            return true;
        }

        return true;
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"date");
    }

    public void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.custDob)).setText(dateFormat.format(calendar.getTime() ));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }


    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day );

        }
    }

    /*
     http request class
     */
    public class NewPrincipalRequest extends StringRequest {
        private static final String REQUEST_URL = "https://familycaretrust.com/admin/micro-api.php?action=newPrincipal";
        private Map<String, String> params;

        NewPrincipalRequest(String fullname, String mobile, String dob, String pack, String gender, String maritalStatus, String address,
                            Response.Listener<String> listener) {
            super(Method.POST, REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("fullname", fullname);
            params.put("mobile", mobile);
            params.put("dob", dob);
            params.put("package", pack);
            params.put("gender", gender);
            params.put("maritalStatus", maritalStatus);
            params.put("address", address);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
