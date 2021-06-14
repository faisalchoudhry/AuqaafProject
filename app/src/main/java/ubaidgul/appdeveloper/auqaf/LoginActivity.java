package ubaidgul.appdeveloper.auqaf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.app.AppController;
import ubaidgul.appdeveloper.auqaf.helper.Helper;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout surveyForm;
    private TextView tvMainTitle;
    private TextView tvSubTitle;
    private TextInputLayout tilUsername;
    private EditText etUsername;
    //    private TextInputLayout tilPassword;
//    private EditText etPassword;
    private Button btnLogin;

    private Context context;
    private Helper helper;
    private SharedPreferences sharedPreferences;

    private String mUsername;
//    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        helper = new Helper(context);
        sharedPreferences = getSharedPreferences("epd_shared_prefs", MODE_PRIVATE);

        findViews();
        textChange();

    }

    private void findViews() {
        surveyForm = (LinearLayout) findViewById(R.id.surveyForm);
        tvMainTitle = (TextView) findViewById(R.id.tv_main_title);
        tvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
        tilUsername = (TextInputLayout) findViewById(R.id.til_username);
        etUsername = (EditText) findViewById(R.id.et_username);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    private void textChange() {
        helper.TextChangeListenerLooper(surveyForm);
    }


    public void btnLogin(View v) {

//        Intent intent = new Intent(context, MenuScreen.class);
//        startActivity(intent);
//        finish();

        try {
            if (helper.checkInternetConnection()) {

                mUsername = etUsername.getText().toString().trim();
//                mPassword = etPassword.getText().toString().trim();

                if (!mUsername.equalsIgnoreCase("")) {
                    if (helper.isValidMobile(mUsername)) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(surveyForm.getWindowToken(), 0);


                        fetchMisToken();

                    } else {
                        tilUsername.setErrorEnabled(true);
                        tilUsername.setError("Mobile Number must be 11 digits long.");
                    }
                } else {
                    tilUsername.setErrorEnabled(true);
                    tilUsername.setError("Mobile Number cannot be empty.");
                }

            } else {
                helper.dialog(context, "Please check your data connection and try again.", "No Internet!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchMisToken() {

        helper.runMyProgressDialog("Please wait...", context);

        final String URL_MIS_Data_JSON = Constants.URL_LOGIN + "?version=" + context.getString(R.string.version)
                + "&username=" + etUsername.getText().toString()
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
                        editor.putString("user_login", etUsername.getText().toString());
                        editor.putString("user_name", json.getString("user_name"));
                        editor.putInt("user_id", json.getInt("pk_id"));
                        editor.putString("designation", json.getString("designation"));
//                        editor.putString("user_department", json.getString("organization_id"));
                        editor.putString("mob_imei", json.getString("mob_imei"));
                        editor.putString("mobile_no", json.getString("mobile_no"));

                        editor.putBoolean("login", true);

                        editor.apply();
                        editor.commit();

                        Intent intent = new Intent(context, MenuScreen.class);
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

    public void btnSignUp(View view) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        startActivity(intent);
    }
}
