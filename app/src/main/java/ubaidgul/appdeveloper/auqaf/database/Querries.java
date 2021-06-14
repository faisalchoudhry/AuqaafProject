package ubaidgul.appdeveloper.auqaf.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.helper.TempData;


public class Querries {

    public static long insertIntoLocalDbIndustryData(Context context, JSONObject jsonObject) {
        long _id_pk = 0;
        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
        SharedPreferences prefs = context.getSharedPreferences("epd_shared_prefs", Context.MODE_PRIVATE);
        try {
            ContentValues values = new ContentValues();
            values.put("user_login", prefs.getString("user_login", null));
            values.put("user_id", prefs.getInt("user_id", -1));
            values.put("survey_data", jsonObject.toString());
            values.put("status", "0");
            values.put("image_name", TempData.getImageName());
            values.put("image_name_2", TempData.getImageName2());
            values.put("mobile_date_time", jsonObject.getString("mobile_date_time"));
            values.put("current_date", jsonObject.getString("current_date"));
            values.put("app_version", context.getString(R.string.version));
            try {
                _id_pk = db.insertWithOnConflict(Constants.TABLE_SURVEY_DATA_INDUSTRY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return _id_pk;
    }

}