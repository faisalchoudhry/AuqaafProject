package ubaidgul.appdeveloper.auqaf.DataSender;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.helper.Helper;
import ubaidgul.appdeveloper.auqaf.helper.TempData;
import ubaidgul.appdeveloper.auqaf.upload_later.UploaderGui;


public class DataSender {
    Context context;
    Handler mHandler;
    MultipartEntity mEntity = null;
    Http_Data_sender obj_http;
    String strConnectionError;
    Helper helper;
    String tableName;
    String URL;
    String strResponse;
    JSONObject jsonData;
    String picNames = "";

    String part1 = "";
    String part2 = "";

    String LocalID = "";
    String Activity = "";

    public DataSender(Context context, String URL, String tableName, String jsonObject, String local_id, String user_id, String user_login) {
        this.context = context;
        this.tableName = tableName;
        this.URL = URL;
        this.Activity = Activity;
        this.helper = new Helper(context);
        mEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            jsonData = new JSONObject(jsonObject);
            LocalID = local_id;
//            mEntity.addPart("industry_image", new StringBody(TempData.getImageName()));

            mEntity.addPart("local_id", new StringBody(local_id));
            mEntity.addPart("user_id", new StringBody(user_id));
            mEntity.addPart("user_login", new StringBody(user_login));
            mEntity.addPart("app_version", new StringBody(context.getString(R.string.version)));
            mEntity.addPart("imei", new StringBody(helper.getImei()));
            mEntity.addPart("AppCompleteData", new StringBody(jsonObject, "application/json", null));
            mEntity.addPart("image1", new StringBody(TempData.getImageName()));
//            mEntity.addPart("image2", new StringBody(TempData.getImageName2()));

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHandler = new Handler();
    }


    public void androidToServerERS() {
        try {
            String strFolder = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.images_folder) + "/";
            String _path,_path1;// = strFolder + imageName;
            JSONArray jsonArray = jsonData.getJSONArray("scheme_roads");
            boolean imgCheck [] = new boolean[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jobject = jsonArray.getJSONObject(i);
                _path = strFolder + jobject.getString("image_start_point");
                _path1 = strFolder + jobject.getString("image_end_point");
                File imageFile = new File(_path);
                File imageFile2 = new File(_path1);
                if (imageFile.exists() && imageFile.isFile()) {
                    if (imageFile2.exists() && imageFile2.isFile()) {
                        mEntity.addPart("PLACEMARKPHOTO_"+jobject.getString("road_id"), new FileBody(imageFile));
                        mEntity.addPart("PLACEMARKPHOTO2_"+jobject.getString("road_id"), new FileBody(imageFile2));
                        imgCheck [i] = true;
                    } else {
                        helper.dialog(context, "Road End Point Picture Is Missing.", "Alert!");
                        imgCheck [i] = false;
                    }
                } else {
                    helper.dialog(context, "Road Start Point Picture Is Missing.", "Alert!");
                    imgCheck [i] = false;
                }
            }
            if (areAllTrue(imgCheck)) {
                sendMultipartEntityToUUServer();
            } else {
                helper.dialog(context, "Some Pictures Are Missing.", "Alert!");
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error is: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean areAllTrue(boolean myarray []) {
        boolean b = true;
        for (boolean val : myarray){
            if(!val){
                b = false;
                return b;
            }
        }
        return b;
    }

    private void sendMultipartEntityToUUServer() {
        obj_http = new Http_Data_sender();
        Thread updateThrad = new Thread(new Runnable() {
            public void run() {
                try {
                    HttpResponse httpResponse = obj_http.sendMultipartHttpRequestWithURLAndValue(URL, mEntity);
                    strResponse = obj_http.convertHTTPResponseToStringUsingEntityUtils(httpResponse);
                    String[] parts = strResponse.split(":");
                    part1 = parts[0];
                    part2 = parts[1];
                    if (part1.equalsIgnoreCase("200")) { // Successfully Inserted
                        mHandler.post(updateGUIBasedOnResponse);
                    } else if (part1.equalsIgnoreCase("602")) { // Version Response
                        mHandler.post(showConnectionErrorVersion);
                    } else if (part1.equalsIgnoreCase("302")) { // Insert Query
                        mHandler.post(showConnectionError2);
                    } else if (part1.equalsIgnoreCase("420")) { // Image Problem
                        mHandler.post(showConnectionError2);
                    } else if (part1.equalsIgnoreCase("702")) {
                        mHandler.post(showConnectionError3);
                    } else if (part1.equalsIgnoreCase("820")) {
                        mHandler.post(showConnectionError4);
                    } else {
                        mHandler.post(showConnectionError2);
                    }

                    TempData.setImagePath("");
                    TempData.setImageName("");

                    TempData.setImagePath2("");
                    TempData.setImageName2("");

                } catch (Exception e) {
                    strConnectionError = e.getMessage();
                    mHandler.post(showConnectionError2);
                }
            }
        });
        updateThrad.start();
        runProgressDialog();
    }

    final Runnable updateGUIBasedOnResponse = new Runnable() {
        public void run() {
            stopProgressDialog();
            try {
                if (Integer.valueOf(part2) > 0) {
                    helper.dialog(context, "Data Uploaded Successfully,\nDatabase ID is: " + part2, "Congratulations!");
//                    Toast.makeText(context, "Data Uploaded Successfully,\nDatabase ID is: " + part2, Toast.LENGTH_LONG).show();
                    SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
                    try {
                        String q = "UPDATE " + tableName + " set status=1,server_db_id='" + part2 + "' where _id_pk='" + LocalID + "'";
                        db.execSQL(q);

//                      image_name = "";
                        LocalID = "";
                        if (UploaderGui.listView != null) {
                            try {
                                UploaderGui.removeRow(context);
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        db.close();
                    }
                } else {
                    helper.dialog(context, "Data upload failed.", "Alert!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                helper.dialog(context, "Data upload failed.", "Alert!");
            }
        }
    };

    final Runnable showConnectionErrorVersion = new Runnable() {
        public void run() {
            stopProgressDialog();
            helper.dialog(context, "Please Update the Application,\n\nData cannot uploaded because of incorrect application version number.", "Alert!");
        }
    };


    final Runnable showConnectionError2 = new Runnable() {
        public void run() {
            stopProgressDialog();
            helper.dialog(context, "Data Uploading Failed.", "Alert!");
        }
    };

    final Runnable showConnectionError3 = new Runnable() {
        public void run() {
            stopProgressDialog();
            helper.dialog(context, "Please register before uploading.", "Alert!");
        }
    };

    final Runnable showConnectionError4 = new Runnable() {
        public void run() {
            stopProgressDialog();
            helper.dialog(context, "This scheme already surveyed. Ask administrator to delete existing data.", "Alert!");
        }
    };

    protected void runProgressDialog() {
        helper.runMyProgressDialog("Uploading Data...", context);
    }

    public void stopProgressDialog() {
        helper.stopMyProgressDialog();
    }
}