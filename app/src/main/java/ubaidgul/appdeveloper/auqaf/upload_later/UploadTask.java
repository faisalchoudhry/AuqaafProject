package ubaidgul.appdeveloper.auqaf.upload_later;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.MenuScreen;
import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.helper.Helper;
import ubaidgul.appdeveloper.auqaf.helper.TempData;


public class UploadTask extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Cursor mCursor;
    //    private ProgressBar mProgressBar;
//    private int mItemCounter;
    private HttpClient httpClient;
    HttpContext httpContext;
    private int mFilesCount;
    private int mUploaded;
    private long mTotalSize;
    private String tableName;
    private String imageName,imageName1;
    //            imageName2;
    private ProgressDialog pd;
    String _path = ""; //Environment.getExternalStorageDirectory() + "/" + mContext.getString(R.string.images_folder) + "/";
    Helper helper;
    CustomMultiPartEntity mEntity;
    boolean VersionCheck = true;
    boolean IMEICheck = true;
    private static UploaderGui parent;

    public UploadTask(Context context, Cursor cursor, UploaderGui parent) {
        mContext = context;
        mCursor = cursor;
        mFilesCount = cursor.getCount();
        this.parent = parent;
        helper = new Helper(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _path = Environment.getExternalStorageDirectory() + "/" + mContext.getString(R.string.images_folder) + "/";
        int timeout = 10000;
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
        HttpConnectionParams.setSoTimeout(httpParameters, timeout);
        httpClient = new DefaultHttpClient(httpParameters);
        httpContext = new BasicHttpContext();
        pd = new ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("Uploading Data...");
        pd.setMax(mFilesCount);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {
        while (mCursor.moveToNext()) {
            HttpPost httpPost = new HttpPost(params[0]);//"http://..." URL goes here
            tableName = params[1];
            try {
                mEntity = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
//                        publishProgress(mUploaded);
                        publishProgress((int) (((((float) mUploaded) + ((float) num / (float) mTotalSize)) / (float) mFilesCount)));
                    }
                });
                String strFolder = Environment.getExternalStorageDirectory() + "/"+mContext.getString(R.string.images_folder)+"/";
                imageName = mCursor.getString(mCursor.getColumnIndex("image_name"));
//                imageName1 = mCursor.getString(mCursor.getColumnIndex("imageBuilding"));
                if (addImagesInMultipartEntity(strFolder)) {
                    try {
                        String local_id = String.valueOf(mCursor.getInt(mCursor.getColumnIndex("_id_pk")));
                        TempData.setLocalId(local_id);
                        String json = mCursor.getString(mCursor.getColumnIndex("survey_data"));
                        mEntity.addPart("AppCompleteData", new StringBody(json, "application/json", null));
                        mEntity.addPart("image1", new StringBody(imageName));
                        mEntity.addPart("local_id", new StringBody(local_id));
                        mEntity.addPart("app_version", new StringBody(mContext.getString(R.string.version)));
                        mEntity.addPart("imei", new StringBody(helper.getImei()));
                    } catch (UnsupportedEncodingException e1)
                    {
                        e1.printStackTrace();
                    }
                    mTotalSize = mEntity.getContentLength();
                    httpPost.setEntity(mEntity);
                    HttpResponse response = httpClient.execute(httpPost, httpContext);
                    String result = EntityUtils.toString(response.getEntity());
                    String[] respParts = result.split(":");
                    if (respParts[0].equalsIgnoreCase("200")) {
                        try {
                            if (Integer.valueOf(respParts[1]) > 0) {
                                mUploaded++;
                                SQLiteDatabase db = DataBaseSQlite.connectToDb(mContext);
                                String q = "UPDATE " + tableName + " set status=1 , server_db_id=" + respParts[1] + " where _id_pk='" + TempData.getLocalId()+ "'";
                                db.execSQL(q);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (respParts[0].equalsIgnoreCase("602")) {
                        VersionCheck = false;
                    } else if (respParts[0].equalsIgnoreCase("702")) {
                        IMEICheck = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        int point = progress[0] + 1;
        pd.setMessage("Uploading " + point + " of " + mFilesCount);
        pd.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        int failed = mFilesCount - mUploaded;
        pd.dismiss();
        if (VersionCheck) {
            if (IMEICheck) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(mContext);
                }
                builder.setTitle("Status")
                        .setMessage("Total Records = " + mFilesCount + "\n" + "Total Uploaded = " + mUploaded +
                                "\n" + "Total Failed = " + failed+
                                "\n"+"Upload All Status")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(mContext, MenuScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                                mContext.startActivity(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                helper.dialog(mContext, "Data cannot be uploaded, please register your IMEI to upload the data", "IMEI not Registered");
            }
        } else {
            helper.dialog(mContext, "Please Update the Application,\n\nData cannot be uploaded because of incorrect application version number", "Alert");
        }
    }

    private boolean addImagesInMultipartEntity(String strFolder){
        boolean imagesAdded = false;
        if(tableName.equalsIgnoreCase(Constants.TABLE_SURVEY_DATA_INDUSTRY )) {
            File imageFile = new File(strFolder + imageName);
            if (imageFile.exists() && imageFile.isFile()) {
                mEntity.addPart("PLACEMARKPHOTO", new FileBody(imageFile));
                imagesAdded = true;
            } else {
                parent.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, "Property Unit Picture is missing.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else  {
            parent.runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(mContext, "Picture is missing.", Toast.LENGTH_LONG).show();
                }
            });
        }
        return imagesAdded;
    }
}
