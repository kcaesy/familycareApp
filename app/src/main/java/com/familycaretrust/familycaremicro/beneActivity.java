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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class beneActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    String selectedPackage = "6";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bene);
        final RadioGroup packageGroup = (RadioGroup) findViewById(R.id.nomPackage);
        final RadioButton bronze = (RadioButton) findViewById(R.id.nomPack6);
        final RadioButton silver = (RadioButton) findViewById(R.id.nomPack7);
        final RadioButton gold = (RadioButton) findViewById(R.id.nomPack8);

        final EditText customer_name = (EditText) findViewById(R.id.nomName);
        final EditText customer_rel = (EditText) findViewById(R.id.nomRel);
        final EditText customer_mob = (EditText) findViewById(R.id.nomPhone);
        final EditText customer_addr = (EditText) findViewById(R.id.nomAddress);
        final EditText customer_gender = (EditText) findViewById(R.id.nomGender);
        final EditText customer_dob = (EditText) findViewById(R.id.nomDob);
        final EditText customer_marital = (EditText) findViewById(R.id.nomMarital);
        final Button btnAddNomiees = (Button) findViewById(R.id.btnAddNomiees);
        final Button btnCancel = (Button) findViewById(R.id.btnCancel);
        bronze.setSelected(true);

        //redirect to cart
        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intentRegister = new Intent(beneActivity.this, CartActivity.class);
                beneActivity.this.startActivity(intentRegister);
            }
        });


        //on package select
        packageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.nomPack6){
                    selectedPackage = "6";
                }else if(i==R.id.nomPack7){
                    selectedPackage = "7";
                }else if(i==R.id.nomPack8){
                    selectedPackage = "8";
                }
            }
        });

        // on submit
        btnAddNomiees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(beneActivity.this);
                progressDialog.show();

                final String fullname = customer_name.getText().toString();
                final String relation = customer_rel.getText().toString();
                final String mobile = customer_mob.getText().toString();
                final String address = customer_addr.getText().toString();
                final String gender = customer_gender.getText().toString();
                final String maritalStatus = customer_marital.getText().toString();
                final String dob = customer_dob.getText().toString();
                //current cart data
                SharedPreferences cart = getSharedPreferences("cart", MODE_PRIVATE);
                final String currentCart = cart.getString("cart", null);

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            String mesg = jsonResponse.getString("message");

                            if (status==1) {
                                String data = jsonResponse.getString("data");

                                Intent intent = new Intent(beneActivity.this, CartActivity.class);

                                //shared cart data
                                SharedPreferences.Editor cart = getSharedPreferences("cart", MODE_PRIVATE).edit();
                                //String restoredText = cart.getString("cart", null);
                                cart.putString("cart", currentCart +" "+ data);
                                cart.apply();

                                beneActivity.this.startActivity(intent);
                            }else if(status==5){
                                AlertDialog.Builder builder = new AlertDialog.Builder(beneActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }else if(status==2){
                                AlertDialog.Builder builder = new AlertDialog.Builder(beneActivity.this);
                                builder.setMessage(mesg)
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                beneActivity.NewNomRequest request = new beneActivity.NewNomRequest(fullname, relation, mobile, dob, selectedPackage, gender, maritalStatus, address, currentCart,
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(beneActivity.this);
                queue.add(request);


            }
        });
    }

    public void datePicker(View view){
        RegisterActivity.DatePickerFragment fragment = new RegisterActivity.DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"date");
    }

    public void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.nomDob)).setText(dateFormat.format(calendar.getTime() ));
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
    public class NewNomRequest extends StringRequest {
        private static final String REQUEST_URL = "https://familycaretrust.com/admin/micro-api.php?action=newNominee";
        private Map<String, String> params;


        NewNomRequest(String fullname,String relation, String mobile, String dob, String pack, String gender, String maritalStatus, String address, String cart,
                            Response.Listener<String> listener) {
            super(Method.POST, REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("fullname", fullname);
            params.put("relation", relation);
            params.put("mobile", mobile);
            params.put("dob", dob);
            params.put("package", pack);
            params.put("gender", gender);
            params.put("maritalStatus", maritalStatus);
            params.put("address", address);
            params.put("cart", cart);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }


}
