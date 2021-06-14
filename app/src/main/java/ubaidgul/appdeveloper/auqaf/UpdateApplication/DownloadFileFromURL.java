package ubaidgul.appdeveloper.auqaf.UpdateApplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.helper.Helper;


public class DownloadFileFromURL extends AsyncTask<String, Integer, Boolean> {
    private int contentLength = -1;
    private int counter = 0;
    private int calculatedProgress = 0;
    String savedImageName = "";
    private ProgressDialog pDialog;
    Helper helper;
    private Context context;
    File file = null;
    Uri fileURI;

    public DownloadFileFromURL(Context context) {
        this.context = context;
        helper = new Helper(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        savedImageName = context.getString(R.string.app_uri_apk_caption);
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean successful = false;
        URL downloadURL = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            downloadURL = new URL(context.getString(R.string.app_uri));
            connection = (HttpURLConnection) downloadURL.openConnection();
            contentLength = connection.getContentLength();
            inputStream = connection.getInputStream();
//                File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File filepath = context.getExternalCacheDir();
            File dir = new File(filepath.getAbsolutePath() + "/" + context.getString(R.string.main_folder) + "/");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file = new File(dir, savedImageName);


            fileURI = Uri.parse("file://" + file.getAbsolutePath());

            if (file.exists()) {
                if (file.delete()) {
                    Log.d("touch", "File Deleted:\n" + file.getAbsolutePath());
                } else {
                    Log.d("touch", "File Not Deleted:\n" + file.getAbsolutePath());
                }
            }

            fileOutputStream = new FileOutputStream(file);
            int read = -1;
            byte[] buffer = new byte[1024];

            while ((read = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
                counter = counter + read;
                publishProgress(counter);
            }

            successful = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return successful;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        pDialog.dismiss();

        if (result) {
            try {
                StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder1.build());


//                File toInstall = new File(appDirectory, appName + ".apk");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", toInstall);
//                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
//                    intent.setData(apkUri);
//                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    context.startActivity(intent);
//                } else {
//                    Uri apkUri = Uri.fromFile(toInstall);
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }


                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(fileURI, "application/vnd.android.package-archive");
                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(installIntent);
            } catch (Exception e) {
                Toast.makeText(context, "Update App Exception:\n" + e.getCause(), Toast.LENGTH_SHORT).show();
            }
        } else {
            helper.dialog(context, "Downloading failed, Try again.", "Alert!");
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        calculatedProgress = (int) (((double) values[0] / contentLength) * 100);
        pDialog.setProgress(calculatedProgress);
    }

}
