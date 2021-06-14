package ubaidgul.appdeveloper.auqaf.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.helper.AttachedProperty;
import ubaidgul.appdeveloper.auqaf.helper.AuqafProperty;


public class DataBaseSQlite {
    private String folderName;
    private static String folder;
    private String strInputDBFileName;
    private String strInputDBFileName1;
    private String ac_name;
    private String dbName = "/uu.db";
    private String dbName1 = "/TrainingManual.pdf"; // Manual
    private Context context;


    public DataBaseSQlite(String folderName, Context context) {
        this.strInputDBFileName = "/" + folderName + dbName;
        this.strInputDBFileName1 = "/" + folderName + dbName1; // Manual
        DataBaseSQlite.folder = folderName;
        this.folderName = "/" + folderName;
        this.context = context;
        		 dataBase();
    }

    public static SQLiteDatabase connectToDb(Context context) {
        String folderName = context.getString(R.string.db_name);
        String dbPath = Environment.getExternalStorageDirectory() + "/" + folderName + "/uu.db";//rwp_citysurvey_dp/uu.db";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        return db;
    }

    public boolean dataBase() {
        boolean b = false;
        boolean isExist = checkingDatabaseFileOnSdCard();
        if (isExist == true) {

            /**************** Read Database File or do Nothing ********************/
            ReadWriteDatabase();
            b = true;
        } else {
            /*********************** Copy DB File From Assets Folder to SD Card *******************/
            copyFromAssetsToSDCard("uu.db");
//            copyManualFromAssetsToSdCard("TrainingManual.pdf");  // Manual
            ReadWriteDatabase();
            b = true;
        }
        return b;
    }//end of dataBase

    private void copyManualFromAssetsToSdCard(String fileName)
    {
        try{
            //Open your local db as the input stream
            InputStream myInput = context.getAssets().open(fileName);

            //Open the empty db as the output stream
            String fPath = Environment.getExternalStorageDirectory() +folderName;
            File f = new File(fPath);
            if(!f.exists()){
                f.mkdirs();
            }
            String dbFilePath = Environment.getExternalStorageDirectory() +strInputDBFileName1;
            File dbFile = new File(dbFilePath);
            OutputStream myOutput = new FileOutputStream(dbFile);
            //transfer bytes from the input-file to the output-file
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }catch(Exception ex){
            Toast.makeText(context, "Error Copying Training Manual file = "+ex.getMessage()+"", Toast.LENGTH_LONG).show();
        }
    }

    private void copyFromAssetsToSDCard(String strDBFile) {
        try {
            //Open your local db as the input stream
            InputStream myInput = context.getAssets().open(strDBFile);

            //Open the empty db as the output stream
            String fPath = Environment.getExternalStorageDirectory() + folderName;
            File f = new File(fPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            String dbFilePath = Environment.getExternalStorageDirectory() + strInputDBFileName;
            File dbFile = new File(dbFilePath);
            OutputStream myOutput = new FileOutputStream(dbFile);
            //transfer bytes from the input-file to the output-file
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception ex) {
            Toast.makeText(context, "Error Copying Database file = " + ex.getMessage() + "", Toast.LENGTH_LONG).show();
        }
    }//end of copyFromAssetsToSDCard

    private String ReadWriteDatabase() {
        try {


        } catch (Exception ex) {
            Toast.makeText(context, "Error Reading Database file = " + ex.getMessage() + "", Toast.LENGTH_LONG).show();
        }//end of try-catch
        return ac_name;
    }//end of ReadWriteDatabase

    private boolean checkingDatabaseFileOnSdCard() {
        boolean isExist = false;
        try {
            String strFilePath = Environment.getExternalStorageDirectory() + strInputDBFileName;
            File dbFile = new File(strFilePath);
            if (dbFile.exists()) {
                isExist = true;
            } else {
                isExist = false;
            }
        } catch (Exception ex) {
            Toast.makeText(context, "Error Reading Database file = " + ex.getMessage() + "", Toast.LENGTH_LONG).show();
            return false;
        }//end of try-catch
        return isExist;
    }//end of checkingDatabaseFileOnSdCard

    public static List<AuqafProperty> findAll(Context context) {
        List<AuqafProperty> contacts = null;
        try {
            SQLiteDatabase db = connectToDb(context);
            String query = "SELECT * FROM tbl_notifications";
            Cursor cur = db.rawQuery(query, null);
            if (cur.moveToFirst()) {
                contacts = new ArrayList<AuqafProperty>();
                do {
                    cur.moveToFirst();
                    while (cur.isAfterLast() == false) {
                        AuqafProperty landmarks = new AuqafProperty();
                        landmarks.setCircle(cur.getString(cur.getColumnIndex("circle")));
                        landmarks.setZone(cur.getString(cur.getColumnIndex("zone")));
                        landmarks.setWaqfPropertyName(cur.getString(cur.getColumnIndex("waqf_property_name")));
                        landmarks.setLocation(cur.getString(cur.getColumnIndex("area_name")));
                        landmarks.setNotificationNo(cur.getString(cur.getColumnIndex("notification_no")));
                        landmarks.setCategory(cur.getString(cur.getColumnIndex("property_main_category_name")));
                        landmarks.setNlaA(cur.getDouble(cur.getColumnIndex("notified_land_acre")));
                        landmarks.setNlaK(cur.getDouble(cur.getColumnIndex("notified_land_kanal")));
                        landmarks.setNlaM(cur.getDouble(cur.getColumnIndex("notified_land_marla")));
                        landmarks.setCultA(cur.getDouble(cur.getColumnIndex("cultivable_land_acre")));
                        landmarks.setCultK(cur.getDouble(cur.getColumnIndex("cultivable_land_kanal")));
                        landmarks.setCultM(cur.getDouble(cur.getColumnIndex("cultivable_land_marla")));
                        landmarks.setNcultA(cur.getDouble(cur.getColumnIndex("non_cultivalbe_land_acre")));
                        landmarks.setNcultK(cur.getDouble(cur.getColumnIndex("non_cultivalbe_land_kanal")));
                        landmarks.setNcultM(cur.getDouble(cur.getColumnIndex("non_cultivalbe_land_marla")));
                        landmarks.setShops(cur.getInt(cur.getColumnIndex("total_shops")));
                        landmarks.setHouses(cur.getInt(cur.getColumnIndex("total_houes")));
                        landmarks.setOthers(cur.getInt(cur.getColumnIndex("total_other_properties")));
                        landmarks.setRemarks(cur.getString(cur.getColumnIndex("data_team_remarks")));
                        landmarks.setIsSurveyed(cur.getInt(cur.getColumnIndex("is_surveyed")));
                        contacts.add(landmarks);
                        cur.moveToNext();
                    }
                    cur.close();
                    db.close();
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            contacts = null;
        }
        return contacts;
    }
    public static List<AttachedProperty> findAllAttachedProperties(Context context,AuqafProperty auqafProperty) {
        List<AttachedProperty> contacts = null;
        try {
            SQLiteDatabase db = connectToDb(context);
            String query = "SELECT * FROM tbl_attached_properties Where waqf_property_name='"+auqafProperty.getWaqfPropertyName()+"'";
            Cursor cur = db.rawQuery(query, null);
            if (cur.moveToFirst()) {
                contacts = new ArrayList<AttachedProperty>();
                do {
                    cur.moveToFirst();
                    while (cur.isAfterLast() == false) {
                        AttachedProperty landmarks = new AttachedProperty();
                        landmarks.setCircle(cur.getString(cur.getColumnIndex("circle")));
                        landmarks.setZone(cur.getString(cur.getColumnIndex("zone")));
                        landmarks.setWaqfPropertyName(cur.getString(cur.getColumnIndex("waqf_property_name")));
                        landmarks.setAreaName(cur.getString(cur.getColumnIndex("area_name")));
                        landmarks.setNotificationNo(cur.getString(cur.getColumnIndex("notification_no")));
                        landmarks.setLanduseType(cur.getString(cur.getColumnIndex("landuse_type")));
                        landmarks.setRegisterSrNo(cur.getInt(cur.getColumnIndex("register_sr_no")));
                        landmarks.setLandSequence(cur.getString(cur.getColumnIndex("land_sequence")));
                        landmarks.setAttachedPropertyAdress(cur.getString(cur.getColumnIndex("attached_property_adress")));
                        landmarks.setAreaMF(cur.getString(cur.getColumnIndex("area_m_f")));
                        landmarks.setLeaseeName(cur.getString(cur.getColumnIndex("leasee_name")));
                        landmarks.setLeaseeFName(cur.getString(cur.getColumnIndex("leasee_f_name")));
                        landmarks.setLeaseeAddress(cur.getString(cur.getColumnIndex("leasee_address")));
                        landmarks.setRentalValuePerMonth(cur.getInt(cur.getColumnIndex("rental_value_per_month")));
                        landmarks.setLeaseStartDate(cur.getString(cur.getColumnIndex("lease_start_date")));
                        landmarks.setLeaseEndDate(cur.getString(cur.getColumnIndex("lease_end_date")));
                        landmarks.setDataTeamRemarks(cur.getString(cur.getColumnIndex("data_team_remarks")));
                        landmarks.setIsSurveyed(cur.getInt(cur.getColumnIndex("is_surveyed")));
                        contacts.add(landmarks);
                        cur.moveToNext();
                    }
                    cur.close();
                    db.close();
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            contacts = null;
        }
        return contacts;
    }
}