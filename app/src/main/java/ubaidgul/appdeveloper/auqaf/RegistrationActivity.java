package ubaidgul.appdeveloper.auqaf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.app.AppController;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.helper.Helper;


public class RegistrationActivity extends AppCompatActivity {

    private LinearLayout surveyForm;
    private LinearLayout bannerForm;
    private TextView tvMainTitle;
    private TextView tvSubTitle;
//    private AppCompatCheckBox chkDivision;
//    private Spinner spnOrganizations;
    private TextInputLayout tilOrganizationName;
    private EditText etOrganizationName;
//    private RadioGroup rgMultidistrict;
//    private RadioButton rbYes;
//    private RadioButton rbNo;
    private TextInputLayout tilName;
    private EditText etName;
    private TextInputLayout tilMobile;
    private EditText etMobile;
    private TextInputLayout tilDesignation;
    private EditText etDesignation;
    private Button btnRegister;

    Context context;

    Helper helper;

    String mControllingAuthority;
    int mDivision;
    String mName;
    String multiDistrictVal;
    String mMobile;
    String mDesignation;
    String mControllingAuthorityOther;

    String loginDataStatus = "false";

    SharedPreferences sharedPreferences;
    ProgressDialog pd;

    int pictureType;//1 = Surveyor Image,
    String imageName, imageNameSurveyor;
    Bitmap thumbnail;
    boolean imageSurveyorOK;
    boolean imageSurveyorRequired = false;

    long timeNow;
    String provider = null;
    LocationManager lm;
    LocationListener ls;
    Location mLocation;

    String status;
    String message;
    ProgressDialog progressDialog;

    private ArrayList<String> arrDepartmentNames = new ArrayList<String>();
    private ArrayList<Integer> arrDepartmentIds = new ArrayList<Integer>();

    private ArrayList<String> arrDivisionNames = new ArrayList<String>();
    private ArrayList<Integer> arrDivisionIDs = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        sharedPreferences = context.getSharedPreferences("epd_shared_prefs", Context.MODE_PRIVATE);
        helper = new Helper(context);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        btnSync();
    }

//    private void defaultVisibilities(){
//        tilOrganizationName.setVisibility(View.GONE);
//        etOrganizationName.setVisibility(View.GONE);
//    }

//    private void fillDivisions() {
//        arrDivisionNames.clear();
//        arrDivisionIDs.clear();
//        String query = "";
//        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
//        try {
//            query = "SELECT DISTINCT division_id, division_name from divisions ORDER BY division_name ASC";
//            Cursor cur = db.rawQuery(query, null);
//
//            arrDivisionNames.add("Select an option..");
//            arrDivisionIDs.add(0);
//            while (cur.moveToNext()) {
//                int division_id = cur.getInt(cur.getColumnIndex("division_id"));
//                String division_name = cur.getString(cur.getColumnIndex("division_name"));
//
//                arrDivisionIDs.add(division_id);
//                arrDivisionNames.add(division_name);
//            }
//            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, arrDivisionNames);
//            aa.setDropDownViewResource(R.layout.spinner_item_drop_down);
//            spnDivision.setAdapter(aa);
//
//            spnDivision.setSelection(arrDivisionNames.indexOf("Lahore"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Exception: " + e.getCause(), Toast.LENGTH_LONG).show();
//        } finally {
//            db.close();
//        }
//    }

//    private void fillDepartments() {
//        arrDepartmentNames.clear();
//        arrDepartmentIds.clear();
//        String query = "";
//        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
//        try {
//            query = "SELECT DISTINCT pk_id, plant_name from plant_types ORDER BY plant_name ASC";
//            Cursor cur = db.rawQuery(query, null);
//
//            arrDepartmentNames.add("Select an option..");
//            arrDepartmentIds.add(0);
//
//            while (cur.moveToNext()) {
//                int pk_id = cur.getInt(cur.getColumnIndex("pk_id"));
//                String plant_name = cur.getString(cur.getColumnIndex("plant_name"));
//
//                arrDepartmentIds.add(pk_id);
//                arrDepartmentNames.add(plant_name);
//            }
//
////            arrDepartmentNames.add("Other Controlling Authority");
//            int tempSize = arrDepartmentIds.size() + 1;
//            arrDepartmentIds.add(tempSize);
//
//            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, arrDepartmentNames);
//            aa.setDropDownViewResource(R.layout.spinner_item_drop_down);
//            spnOrganizations.setAdapter(aa);
//
//            spnOrganizations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if (position > 0 && spnOrganizations.getSelectedItem().toString().equalsIgnoreCase("Not Listed")) {
//                        tilOrganizationName.setVisibility(View.VISIBLE);
//                        etOrganizationName.setVisibility(View.VISIBLE);
//                    } else {
//                        tilOrganizationName.setVisibility(View.GONE);
//                        etOrganizationName.setVisibility(View.GONE);
//                    }
//
////                    etControllingAuthorityOther.setText("");
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Exception: " + e.getCause(), Toast.LENGTH_LONG).show();
//        } finally {
//            db.close();
//        }
//    }


    private void sendSave() {

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean dcPermission = chkDivision.isChecked();
//                mControllingAuthority = spnOrganizations.getSelectedItem().toString();
                mControllingAuthorityOther = etOrganizationName.getText().toString().trim();
//                int id = rgMultidistrict.getCheckedRadioButtonId();
//                RadioButton radioButton = findViewById(id);
//                multiDistrictVal = radioButton.getText().toString();
                mName = etName.getText().toString().trim();
                mDesignation = etDesignation.getText().toString().trim();
                mMobile = etMobile.getText().toString().trim();
//                mDivision = arrDivisionIDs.get(spnDivision.getSelectedItemPosition());

//                if(spnDivision.getSelectedItemPosition()> 0){
                    if (!mName.equalsIgnoreCase("")) {
                        if (!mMobile.equalsIgnoreCase("")) {
                            if (mMobile.length() == 11) {
                                if (!mDesignation.equalsIgnoreCase("")) {

//                                    if(spnOrganizations.getSelectedItemPosition()> 0){

//                                        if((etOrganizationName.getVisibility() == View.VISIBLE && !helper.isEmpty(etOrganizationName)) ||
//                                                etOrganizationName.getVisibility() == View.GONE){

//                                            if (etOrganizationName.getVisibility() == View.VISIBLE){
                                                mControllingAuthority = etOrganizationName.getText().toString();
//                                            }

                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(surveyForm.getWindowToken(), 0);

                                            success(mDivision, mName, mMobile, mDesignation,mControllingAuthority);

//                                        }else{
//                                            tilName.setErrorEnabled(true);
//                                            tilName.setError("Enter Organization Name");
//                                        }
//
//                                    }else{
//                                        helper.dialog(context, "Please select valid Organization Name from the list.", "Alert!");
//                                    }

                                } else {
                                    helper.dialog(context, "Unsuccessful, Try Again.", "Alert!");
                                }
                            } else {
                                tilMobile.setErrorEnabled(true);
                                tilMobile.setError("Mobile Number must be 11 digits long.");
                            }
                        } else {
                            tilMobile.setErrorEnabled(true);
                            tilMobile.setError("Mobile Number cannot be empty.");
                        }
                    } else {
                        tilName.setErrorEnabled(true);
                        tilName.setError("Name cannot be empty.");
                    }
//                }else{
//                    helper.dialog(context, "Please select valid Division from the list.", "Alert!");
//                }

            }
        });
    }

    private void success(final int mDivision, final String mName, final String mMobile, final String mDesignation, final String mControllingAuthority) {

        helper.runMyProgressDialog("Please wait...", context);

        final String URL_MIS_Data_JSON = Constants.URL_REGISTRATION;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MIS_Data_JSON, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject response1 = new JSONObject((String) response);

                    if (response1.has("status") && response1.has("message")) {
                        status = response1.getString("status");
                        message = response1.getString("message");

                        if(status.equalsIgnoreCase("1") && message.equalsIgnoreCase("User has been added successfully.")){
                            fetchMisToken(mMobile);
                        }else{
                            helper.stopMyProgressDialog();
                            helper.dialog(RegistrationActivity.this, message, "Alert!");
                        }

                    } else if (response1.has("error") && response1.has("error_description")) {
                        helper.stopMyProgressDialog();
                        String errorDescription = response1.getString("error_description");
                        helper.dialog(RegistrationActivity.this, errorDescription, "Alert!");

                    } else {
                        helper.stopMyProgressDialog();
                        helper.dialog(RegistrationActivity.this, "Server is down! Please try again later.", "Alert!");
                    }


                } catch (Exception e) {
                    helper.stopMyProgressDialog();
                    Toast.makeText(context, "Data parsing error.", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helper.stopMyProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "No Connection/Timeout Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Authentication Failure Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Username and Password is incorrect. ", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Internet Connection Problem.", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", mName);
                params.put("designation", mDesignation);
                params.put("mobile", mMobile);
//                params.put("dc_permission", String.valueOf(chkDivision.isChecked()));
                params.put("mob_imei", mControllingAuthority);
//                params.put("organization_other", mControllingAuthorityOther);
//                params.put("multi_district", multiDistrictVal);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void fetchMisToken(String mMobile ) {

        final String URL_MIS_Data_JSON = Constants.URL_LOGIN + "?version=" + context.getString(R.string.version)
                + "&username=" + mMobile
                + "&imei=" + helper.getImei();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_MIS_Data_JSON, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {

                    if (!response.toString().equals("Login Failed.")) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        String currentTime = null;
                        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
                        currentTime = dateFormat1.format(new Date());
                        editor.putString("date_time_mobile", currentTime);


                        JSONObject json = new JSONObject((String) response);
//                        {"pk_id":8,"user_name":"ubaid gul","designation":"test","mobile":"03214567891","organization_id":16}
                        editor.putString("user_login", etMobile.getText().toString());
                        editor.putString("user_name", json.getString("user_name"));
                        editor.putInt("user_id", json.getInt("pk_id"));
                        editor.putString("designation", json.getString("designation"));
                        editor.putString("mob_imei", json.getString("mob_imei"));
                        editor.putString("mobile_no", json.getString("mobile_no"));
                        editor.putString("user_department", json.getString("organization_name"));
                        editor.putBoolean("login", true);

                        editor.apply();
                        editor.commit();

                        Intent intent = new Intent(context, MenuScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(context, "Login Failed.", Toast.LENGTH_SHORT).show();
                    }

                    helper.stopMyProgressDialog();

                } catch (Exception e) {
                    helper.stopMyProgressDialog();
                    Toast.makeText(context, "Data parsing error.", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helper.stopMyProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "No Connection/Timeout Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Authentication Failure Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context, "Username and Password is incorrect. ", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error.", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, "Parse Error.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Internet Connection Problem.", Toast.LENGTH_LONG).show();
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void textChange() {
        helper.TextChangeListenerLooper(surveyForm);
    }

    private void findViews() {
        surveyForm = (LinearLayout)findViewById( R.id.surveyForm );
        bannerForm = (LinearLayout)findViewById( R.id.bannerForm );
        tvMainTitle = (TextView)findViewById( R.id.tv_main_title );
        tvSubTitle = (TextView)findViewById( R.id.tv_sub_title );
//        chkDivision = (AppCompatCheckBox)findViewById( R.id.tv_division );
//        spnOrganizations = (Spinner)findViewById( R.id.spn_organizations );
        tilOrganizationName = (TextInputLayout)findViewById( R.id.til_organization_name );
        etOrganizationName = (EditText)findViewById( R.id.et_organization_name );
//        rgMultidistrict = (RadioGroup)findViewById( R.id.rg_multidistrict );
//        rbYes = (RadioButton)findViewById( R.id.rb_yes );
//        rbNo = (RadioButton)findViewById( R.id.rb_no );
        tilName = (TextInputLayout)findViewById( R.id.til_name );
        etName = (EditText)findViewById( R.id.et_name );
        tilMobile = (TextInputLayout)findViewById( R.id.til_mobile );
        etMobile = (EditText)findViewById( R.id.et_mobile );
        tilDesignation = (TextInputLayout)findViewById( R.id.til_designation );
        etDesignation = (EditText)findViewById( R.id.et_designation );
        btnRegister = (Button)findViewById( R.id.btnRegister );

    }


    public void btnSync() {
        try {
            setContentView(R.layout.activity_registration);
            findViews();
            sendSave();
//            if (!helper.checkInternetConnection()) {
//                helper.dialog(context, "Please check your data connection and try again.", "No Internet!");
//            } else {
//                try {
//                    String syncHistory = sharedPreferences.getString("sync", "Last Sync: NA");
//                    if (syncHistory.equalsIgnoreCase("Last Sync: NA")) {
//                        String serverURL = Constants.URL_DATA_SYNC ;//+ "?tehsil_id=" + tehsilIdCheck;
//                        new LongOperation().execute(serverURL);
//                    } else {
//                        new AlertDialog.Builder(context)
//                                .setTitle("Alert!")
//                                .setCancelable(false)
//                                .setMessage(syncHistory + "\nDo you want to sync data?")
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        String serverURL = Constants.URL_DATA_SYNC ;//+ "?tehsil_id=" + tehsilIdCheck;
//                                        new LongOperation().execute(serverURL);
//                                    }
//                                })
//                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    public void onClick(final DialogInterface dialog, final int id) {
//                                        dialog.cancel();
//                                    }
//                                })
//                                .show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;

        protected void onPreExecute() {
            helper.runMyProgressDialog("Getting Data...", context);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {
                HttpGet httpget = new HttpGet(urls[0]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);
            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
                helper.stopMyProgressDialog();
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
                helper.stopMyProgressDialog();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
            // Close progress dialog
            helper.stopMyProgressDialog();
            if (Error != null) {
                helper.dialog(context, "No Data Found!", "Alert!");
            } else {
//                helper.dialog(context,Content,"Alert!");
                if (Content.equals("") || Content.equalsIgnoreCase("No Record Found") || Content.contains("No Record Found")) {
                    helper.dialog(context, "No Data Found!", "Alert!");
                } else {
                    new JsonToSqlite().execute(Content);
                }
            }
        }
    }

    private class JsonToSqlite extends AsyncTask<String, Void, Void> {
        private String responseString;
        private boolean status = true;

        protected void onPreExecute() {
            helper.runMyProgressDialog("Saving Data...", context);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {
                responseString = urls[0];

                JSONArray jsonArray = new JSONArray(responseString);
                SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
                db.beginTransaction();
                String delete = "DELETE from plant_types";
                db.execSQL(delete);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);

                    String query = "INSERT into plant_types(pk_id,plant_name) " +
                            "VALUES (" + json.getInt("pk_id") + ",'" + json.getString("organization_name") + "')";
                    db.execSQL(query);
                    System.out.println(query);

                }
                String q2 = "select * from plant_types";
                Cursor cur = db.rawQuery(q2, null);
                int count = cur.getCount();

                if (count != jsonArray.length()) {
                    status = false;
                }
                cur.close();
                db.setTransactionSuccessful();
                db.endTransaction();

//                editor.commit();
            } catch (Exception e) {
                cancel(true);
                helper.stopMyProgressDialog();
                status = false;
            }
            return null;
        }

        protected void onPostExecute(Void unused) {

            helper.stopMyProgressDialog();

            if (status) {
//                btnIndustryMonitoring.setClickable(true);
//                btnUpload.setClickable(true);
//
//                btnIndustryMonitoring.setBackgroundResource(R.drawable.menu_item_backgrounds);
//                btnUpload.setBackgroundResource(R.drawable.menu_item_backgrounds);

                setContentView(R.layout.activity_registration);

                findViews();
//                defaultVisibilities();
                textChange();
//                fillDepartments();
                sendSave();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy, hh:mm a");
                String formattedDate = df.format(calendar.getTime());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("sync", "Last Sync: " + formattedDate);
                editor.commit();

//                tvSync.setText(sharedPreferences.getString("sync", "Last Sync: NA"));

                Toast.makeText(context, "Data Sync Successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
