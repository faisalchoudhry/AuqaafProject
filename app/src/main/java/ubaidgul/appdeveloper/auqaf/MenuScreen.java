package ubaidgul.appdeveloper.auqaf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.helper.Helper;
import ubaidgul.appdeveloper.auqaf.upload_later.UploaderGui;


public class MenuScreen extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ScrollView scrollView1;
    private RelativeLayout mainRelativeLayout;
    private LinearLayout titleLayout;
    private TextView tvMainTitle;
    private TextView tvSubTitle;
    private LinearLayout buttonsLayout;
    private TextView tvAppVersion;
    private TextView tvUserCurentData;
    private Button btnIndustryMonitoring;
    //    private Button btnRandomMonitoring;
    private Button btnUpload;
    //    private Button btnDetailedReport;
    private Button btnSync;
    private TextView tvSync;
    private LinearLayout logoLayout;
    private ImageView imageView1;

    private Context context;
    private Helper helper;

    private SharedPreferences sharedPreferences;
    private String userCheck = "NA";
    private static boolean loginStatus = false;
    private int divisionIdCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        context = this;
        helper = new Helper(context);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());

        sharedPreferences = context.getSharedPreferences("epd_shared_prefs", Context.MODE_PRIVATE);
        userCheck = sharedPreferences.getString("user_login", "NA");
        loginStatus = sharedPreferences.getBoolean("login", false);
//        divisionIdCheck = sharedPreferences.getInt("division_id", 0);

        try {
            if (userCheck.equalsIgnoreCase("NA") || !loginStatus ) {
                Intent mInHome = new Intent(context, LoginActivity.class);
                mInHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mInHome);
                finish();
            } else {
                findViews();
//                dataSyncStatus();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dataSyncStatus() {
        int count = helper.checkRecordsInDatabase(Constants.TABLE_PLANT_TYPES_DATA);
        if (count < 1) {
            helper.dialog(context, "Please, Sync the application to start the survey.", "Alert!");

            btnIndustryMonitoring.setClickable(false);
            btnUpload.setClickable(false);

            btnIndustryMonitoring.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDisabled));
            btnUpload.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDisabled));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (userCheck.equalsIgnoreCase("NA") || !loginStatus) {
//            Intent mInHome = new Intent(context, LoginActivity.class);
//            mInHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(mInHome);
//            finish();
//        }

    }


    private void findViews() {

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu Screen");

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        tvMainTitle = (TextView) findViewById(R.id.tv_main_title);
        tvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
        tvUserCurentData = (TextView) findViewById(R.id.tvUserCurentData);
        btnIndustryMonitoring = (Button) findViewById(R.id.btnIndustryMonitoring);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnSync = (Button) findViewById(R.id.btnSync);
        tvSync = (TextView) findViewById(R.id.tvSync);
        logoLayout = (LinearLayout) findViewById(R.id.logoLayout);
        imageView1 = (ImageView) findViewById(R.id.imageView1);

        tvAppVersion.setText("Version " + context.getString(R.string.version));
        tvSync.setText(sharedPreferences.getString("sync", "Last Sync: NA"));

        tvUserCurentData.setText("User Name: " +sharedPreferences.getString("user_name", "NA"));
//        +"\nDepartment/Agency: "+ sharedPreferences.getString("user_department", "NA"));
    }

    public void btnIndustryMonitoring(View v) {
        try {
            Intent intent = new Intent(context, SurveyForm_Industry.class);
            context.startActivity(intent);
        } catch (Exception e) {
            helper.dialog(this, "Please, restart the application.", "Alert!");
        }
    }

    public void btnUpload(View view) {
        try {
            if (helper.showReportAlert(Constants.TABLE_SURVEY_DATA_INDUSTRY, "0", userCheck)) {
                Intent i = new Intent(context, UploaderGui.class);
                i.putExtra("url", Constants.URL_SURVEY_DATA);
                i.putExtra("tblName", Constants.TABLE_SURVEY_DATA_INDUSTRY);
                i.putExtra("userCheck", userCheck);
                startActivity(i);
            } else {
                Toast.makeText(context, "No records to display.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Cannot Access Your Memory card.", Toast.LENGTH_SHORT).show();
        }
    }


/*    public void btnDetailedReport(View v) {
        try {
            if (helper.showReportAlert(Constants.TABLE_SURVEY_DATA_INDUSTRY, "1", userCheck)) {
//                Intent i = new Intent(context, ShowDetailReport.class);
//                i.putExtra("tblName", Constants.TABLE_SURVEY_DATA_INDUSTRY);
//                i.putExtra("userCheck", userCheck);
//                startActivity(i);
                Toast.makeText(context, "Sent Records.", Toast.LENGTH_SHORT).show();
            } else {
                helper.dialog(context, "No records to display.", "Alert!");
            }

        } catch (Exception e) {
            Toast.makeText(context, "Cannot Access Your Memory card.", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void btnSync(View v) {
        try {
            if (!helper.checkInternetConnection()) {
                helper.dialog(context, "Please check your data connection and try again.", "No Internet!");
            } else {
                try {
                    String syncHistory = sharedPreferences.getString("sync", "Last Sync: NA");
                    if (syncHistory.equalsIgnoreCase("Last Sync: NA")) {
                        String serverURL = Constants.URL_DATA_SYNC ;//+ "?tehsil_id=" + tehsilIdCheck;
                        new LongOperation().execute(serverURL);
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setCancelable(false)
                                .setMessage(syncHistory + "\nDo you want to sync data?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String serverURL = Constants.URL_DATA_SYNC ;//+ "?tehsil_id=" + tehsilIdCheck;
                                        new LongOperation().execute(serverURL);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
                            "VALUES (" + json.getInt("pk_id") + ",'" + json.getString("plant_name") + "')";
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
                btnIndustryMonitoring.setClickable(true);
                btnUpload.setClickable(true);

                btnIndustryMonitoring.setBackgroundResource(R.drawable.menu_item_backgrounds);
                btnUpload.setBackgroundResource(R.drawable.menu_item_backgrounds);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy, hh:mm a");
                String formattedDate = df.format(calendar.getTime());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("sync", "Last Sync: " + formattedDate);
                editor.commit();

                tvSync.setText(sharedPreferences.getString("sync", "Last Sync: NA"));

                Toast.makeText(context, "Data Sync Successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.update:
//
//                if (!helper.checkInternetConnection()) {
//                    helper.dialog(context, "Please check your data connection and try again.", "No Internet!");
//                } else {
//                    try {
//                        new updateApplicationTask(context).execute();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;

            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("Exit!")
                        .setCancelable(false)
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_login", null);
                                editor.putString("user_name", null);
                                editor.putInt("user_id", -1);
                                editor.putBoolean("login", false);
                                editor.apply();
                                editor.commit();

                                helper.runMyProgressDialog("Please wait...", context);
                                startLogoutProcess();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void startLogoutProcess() {
        threadProcess();
    }

    private void threadProcess() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                helper.stopMyProgressDialog();

                Intent mInHome = new Intent(context, LoginActivity.class);
                mInHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MenuScreen.this.startActivity(mInHome);
                MenuScreen.this.finish();
            }
        }, 1200);
    }


    private String getDivisionName(int divisionIdCheck) {
        String query = "";
        String division_name = "NA";
        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
        try {
            query = "SELECT DISTINCT division_name from divisions where division_id = " + divisionIdCheck + "";
            Cursor cur = db.rawQuery(query, null);

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    division_name = cur.getString(cur.getColumnIndex("division_name"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getCause(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }

        return division_name;
    }

}