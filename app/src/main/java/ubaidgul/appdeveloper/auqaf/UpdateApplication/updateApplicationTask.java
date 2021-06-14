package ubaidgul.appdeveloper.auqaf.UpdateApplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.helper.Helper;


public class updateApplicationTask extends AsyncTask<String, Integer, Boolean> {

    private Context context;
    boolean VersionCheck = true;
    String server_response;

    private ProgressDialog pd;
    Helper helper = new Helper(context);

    public updateApplicationTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setMessage("Checking App...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean successful = false;
        URL url = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String VersionCode = context.getString(R.string.version);

        String strUpdateURL = Constants.URL_Menu_Update_App + "" + VersionCode;
        Log.d("touch", strUpdateURL);

        try {
            url = new URL(strUpdateURL);
            connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                server_response = readStream(inputStream);
                Log.v("touch", server_response);

                if (!server_response.equalsIgnoreCase("") && server_response != null && !server_response.equalsIgnoreCase("null")) {
                    if (server_response.equalsIgnoreCase("true")) {
                        VersionCheck = true;
                    } else {
                        VersionCheck = false;
                    }
                } else {
                    VersionCheck = false;
                }

                successful = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return successful;
    }

    // Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(Boolean response) {
        pd.dismiss();

        if (response) {
            if (VersionCheck) {
                helper.dialog(context, "You already have the latest version of application.", "Alert!");
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setCancelable(false)
                        .setMessage("There is a newer version available of " + context.getResources().getString(R.string.app_name) + " Application.\nAre you sure, you want to Update?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(context, "Downloading Started...", Toast.LENGTH_LONG).show();
//                                    CallToBroadcast();
                                new DownloadFileFromURL(context).execute();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        } else {
            helper.dialog(context, "Internet Connection Problem, Application cannot be updated.", "Alert!");
        }
    }
}
