package ubaidgul.appdeveloper.auqaf.upload_later;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;

import java.util.ArrayList;

import ubaidgul.appdeveloper.auqaf.DataSender.DataSender;
import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.helper.Helper;
import ubaidgul.appdeveloper.auqaf.helper.TempData;


public class Upload {
    String imageTreeLoc, imageInProcess, completeAppData, local_id, circle ,srNum,puNum ,locality,ward,block, imei;
//    JSONObject json = new JSONObject();
//    ArrayList<String> indData = new ArrayList<>();
    ArrayList<String> switchData = new ArrayList<>();
    JSONArray jsonArray = new JSONArray();
    Context context;
    String tableName = "";
    private String user_login, mobile_date_time, user_id, picNames,data_version,jsonObject,latitude,longitude;
    Helper helper;

    public void upload(Context context, String s, String tableName) throws HttpHostConnectException {
        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
        try {
            this.context = context;
            this.tableName = tableName;
            helper = new Helper(context);

            jsonArray = new JSONArray();
            switchData.clear();
                String query = "SELECT * FROM " + UploaderGui.tableName + " WHERE status = '0' and _id_pk = '" + UploaderGui.localIDs[Integer.parseInt(s)] + "'";
                Log.d("touch", "" + query);
                Cursor cur = db.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    while (cur.isAfterLast() == false) {
                        imageTreeLoc = cur.getString(cur.getColumnIndex("image_name"));
                        imageInProcess = cur.getString(cur.getColumnIndex("image_name_2"));
                        jsonObject = cur.getString(cur.getColumnIndex("survey_data"));
                        local_id = cur.getString(cur.getColumnIndex("_id_pk"));
                        user_id = cur.getString(cur.getColumnIndex("user_id"));
                        user_login = cur.getString(cur.getColumnIndex("user_login"));
                        cur.moveToNext();
                    }
                    cur.close();
                    final String _path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.images_folder) + "/";
                    TempData.setImagePath(_path + imageTreeLoc);
                    TempData.setImageName(imageTreeLoc);

                    TempData.setImagePath2(_path + imageInProcess);
                    TempData.setImageName2(imageInProcess);

                    try {
                        if (helper.checkInternetConnection()) {
                            DataSender ob = new DataSender(context, UploaderGui.URL, UploaderGui.tableName, jsonObject, local_id,user_id,user_login);
                            ob.androidToServerERS();
                        } else {
                            helper.dialog(context, "Please check your data connection.", "No Internet!");
                        }
                    } catch (Exception e) {
                        helper.dialog(context, "Internet Connection Error.", "Alert!");
                    }
                } else {
                    helper.dialog(context, "Record Already Uploaded.", "Alert!");
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}