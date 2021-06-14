package ubaidgul.appdeveloper.auqaf.upload_later;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.helper.Helper;


public class UploaderGui extends AppCompatActivity {
    public static ListView listView;

    public static String[] path = null;
    public static int[] localIDs;
    public static String[] DateTimeIDS = null;

    public static String[] array1 = null;
    public static String[] array2 = null;
    public static String[] array3 = null;
    public static String[] array4 = null;
    public static String[] array5 = null;
    public static String[] array6 = null;


    public static String tableName = null;
    public static String URL = null;

    private int totalarecords;
    Context context;

    public static ItemAdapter adapter;
    Helper helper;

    private static LinearLayout noRecordLayout;
    private static TextView noRecordText;
    private static Button btnGoBack;

    private static String clickedButton = "industry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaderlayout);
        context = this;
        helper = new Helper(context);

        Intent intent = getIntent();
        tableName = intent.getExtras().getString("tblName");
        URL = intent.getExtras().getString("url");

        getSupportActionBar().setTitle("Send My Data");

        noRecordLayout = (LinearLayout) findViewById(R.id.noRecordLayout);
        noRecordText = (TextView) findViewById(R.id.noRecordText);
        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        listView = (ListView) findViewById(R.id.listView1);

        noRecordLayout.setVisibility(View.GONE);
        noRecordText.setVisibility(View.GONE);
        btnGoBack.setVisibility(View.GONE);

        ShowPendingRecords();
    }


    private void ShowPendingRecords() {

        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
        try {
            int i = 1;

            String query = "SELECT * FROM " + Constants.TABLE_SURVEY_DATA_INDUSTRY + " WHERE status = '0' order by current_date desc";

            Log.d("touch", query.toString());
            Cursor cur = db.rawQuery(query, null);
            int ct = cur.getCount();
            if (ct > 0) {

                path = new String[ct + 1];
                localIDs = new int[ct + 1];
                DateTimeIDS = new String[ct + 1];

                array1 = new String[ct + 1];
                array2 = new String[ct + 1];
                array3 = new String[ct + 1];
                array4 = new String[ct + 1];
                array5 = new String[ct + 1];
                array6 = new String[ct + 1];

                while (cur.moveToNext()) {
                    path[i] = cur.getString(cur.getColumnIndex("image_name"));
                    localIDs[i] = cur.getInt(cur.getColumnIndex("_id_pk"));
                    DateTimeIDS[i] = cur.getString(cur.getColumnIndex("mobile_date_time"));

                    array1[i] = cur.getString(cur.getColumnIndex("survey_data"));
//                    array2[i] = cur.getString(cur.getColumnIndex("ind_code"));
//                    array3[i] = cur.getString(cur.getColumnIndex("ind_type"));
//                    array4[i] = cur.getString(cur.getColumnIndex("head_propo_name"));
//                    array5[i] = cur.getString(cur.getColumnIndex("head_propo_mobile"));
//                    array6[i] = cur.getString(cur.getColumnIndex("head_propo_email"));
                    i++;
                }

                Model.LoadModel(path, localIDs, DateTimeIDS, array1, array2, array3, array4, array5, array6);

                ArrayList<String> ids = new ArrayList<String>();
                for (int j = 0; j < ct; j++) {
                    ids.add(Integer.toString(j + 1));
                }
                try {
                    adapter = new ItemAdapter(this, R.layout.row, ids, tableName, URL, clickedButton);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    listView.setVisibility(View.VISIBLE);
                    noRecordLayout.setVisibility(View.GONE);
                    noRecordText.setVisibility(View.GONE);
                    btnGoBack.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                listView.setVisibility(View.GONE);
                noRecordLayout.setVisibility(View.VISIBLE);
                noRecordText.setVisibility(View.VISIBLE);
                btnGoBack.setVisibility(View.VISIBLE);
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void btnGoBack(View v) {
        try {
            finish();
        } catch (Exception e) {
            helper.dialog(this, "Cannot access memory storage, please restart the application.", "Alert!");
        }
    }



    public static void removeRow(Context context) {
        ItemAdapter.Ids.remove(ItemAdapter.Ids.indexOf(ItemAdapter.pos + ""));
        adapter.notifyDataSetChanged();
        checkCurrentPendingStatus(context);
    }


    private static void checkCurrentPendingStatus(final Context context) {

        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
        try {
            String query = "SELECT * FROM " + Constants.TABLE_SURVEY_DATA_INDUSTRY + " WHERE status = '0' order by current_date desc";

            Cursor cur = db.rawQuery(query, null);
            cur.moveToFirst();
            if (cur.getCount() == 0) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        listView.setVisibility(View.GONE);
                        noRecordLayout.setVisibility(View.VISIBLE);
                        noRecordText.setVisibility(View.VISIBLE);
                        btnGoBack.setVisibility(View.VISIBLE);
                    }
                }, 50);
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.upload_all, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_upload_all) {
//            if (!helper.checkInternetConnection()) {
//                helper.dialog(context, "Please check your data connection.", "No Internet!");
//            } else {
//                try {
//                    SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
//                    String query = "SELECT * FROM "+tableName+" WHERE status = 0";
//                    Cursor cursor = db.rawQuery(query,null);
//                    new UploadTask(context, cursor,new UploaderGui()).execute(URL,tableName);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.getMemoryInfo(mi);
            long availableMemory = (mi.availMem / 1048576L);
            if (availableMemory < 50) {
                helper.dialog(this, "Your available memory is " + availableMemory + " MB, Please free some space for using the app.", "Alert!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

