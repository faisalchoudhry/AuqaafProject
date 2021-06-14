package ubaidgul.appdeveloper.auqaf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.database.Querries;
import ubaidgul.appdeveloper.auqaf.helper.AttachedProperty;
import ubaidgul.appdeveloper.auqaf.helper.CustomAdapter;
import ubaidgul.appdeveloper.auqaf.helper.Helper;
import ubaidgul.appdeveloper.auqaf.helper.TempData;

import static android.graphics.Bitmap.createScaledBitmap;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class AttachedPropertySurvey extends AppCompatActivity {

    /*Faisal Work*/
    private View snackbarView;
    private LinearLayout surveyForm;
    private ImageView img_part1;
    private TextView tv_mainHeading_part1;
    private SearchableSpinner spinnerWaqfPropertyNameShopForm;
    private SearchableSpinner spinnerNotificationNoShopForm;
    private TextInputLayout til_waqf_prop_name;
    private TextInputEditText et_lessee_name_shop_form;
    private TextInputLayout til_lessee_f_name_shop_form;
    private TextInputEditText et_lessee_f_name_shop_form;
    private TextInputEditText et_lessee_address_shop_form;
    private TextInputLayout til_land_seq_name_shop_form;
    private TextInputEditText et_land_seq_name_shop_form;
    private TextInputLayout til_area_shop_form;
    private TextInputEditText et_area_shop_form;
    private TextView lease_start_date_shop_form;
    private TextView lease_end_date_shop_form;
    private TextInputLayout til_remarks;
    private TextInputEditText et_remarks_shop_form;
    private ImageView image_shop_shop_form;
    private ImageView image_lessee_shop_form;
    private ImageView image_lease_shop_form;
    private ImageView image_other_shop_form;
    private Button sendSave;


    DatePickerDialog datePickerDialog;


    private Location mLocation;

    LocationManager lm;
    LocationListener ls;
    private long timeNow;
    private String provider = null;
    String imageName, currentTime, currentDate;
    private boolean image1OK, image2OK;
    private boolean image1Required = false, image2Required = false;
    Bitmap thumbnail;
    int pictureType;
    ImageView ivPublic;
    Context context;

    TextView tv_number_crop;

    int pos = -1;
    boolean edEmptyCheck = false;
    JSONObject json, road_json;
    JSONArray scheme_roads;
    Helper helper;
    private SharedPreferences sharedPreferences;
    private String userCheck = "NA";
    private static boolean loginStatus = false;
    private int divisionIdCheck;

    private AutoCompleteTextView etScheme;
    private TextInputLayout tilScheme;
    ArrayList<String> division_array = new ArrayList<>();
    ArrayList<String> district_array = new ArrayList<>();
    private ArrayList<String> arrSchemes = new ArrayList<String>();
    private ImageView clearScheme;
    //    private Spinner spnDistrict, spnDivision;
    private boolean checkScheme = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attached_shop_survey);
        context = this;
        helper = new Helper(context);
        TempData.setImageName("");
        TempData.setImageName2("");
        AttachedProperty attachedProperty = (AttachedProperty) getIntent().getExtras().getParcelable("attached_property");
        sharedPreferences = context.getSharedPreferences("epd_shared_prefs", Context.MODE_PRIVATE);
        userCheck = sharedPreferences.getString("user_login", "NA");
        loginStatus = sharedPreferences.getBoolean("login", false);
        divisionIdCheck = sharedPreferences.getInt("division_id", 0);
//        extraLanUseCategoryArraylist = new ArrayList();
        scheme_roads = new JSONArray();
        try {
//            if (userCheck.equalsIgnoreCase("NA") || !loginStatus ) {
//                Intent mInHome = new Intent(context, LoginActivity.class);
//                mInHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(mInHome);
//                finish();
//            } else {
            findViews(attachedProperty);
//                addingLayout();
            fillDivision();
//                textChange();
//                fillDistricts(divisionIdCheck);
//                fillPlants();
//                fillDedicatedType();
//            }
            datePickerCustom();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void datePickerCustom() {
        /*Start Date*/
        leaseStartDateShopForm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AttachedPropertySurvey.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                SeterGeter.setLeaseStartDateShopForm(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                leaseStartDateShopForm.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                return false;
            }


        });

        /*End Date*/
        leaseEndDateShopForm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AttachedPropertySurvey.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                SeterGeter.setLeaseEndDateShopForm(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                leaseEndDateShopForm.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                return false;
            }


        });
    }

//    private void addingLayout() {
//        addOwner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (extraOwnerContainer.getChildCount() < 25) {
//                    ExtraOwnerLayoutActivity extraOwner = new ExtraOwnerLayoutActivity(context, extraLanUseCategoryArraylist.size() + 1);
//                    extraOwner.inflate(context, inflater, extraOwnerContainer, extraLanUseCategoryArraylist);
//                    extraLanUseCategoryArraylist.add(extraOwner);
//                    if (extraOwnerContainer.getChildCount() == 25) {
//                        makeAddMoreButtonInvisible();
//                    }
//                } else {
//                    Toast.makeText(context, "You can't add more than 25 Owners!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    public void makeAddMoreButtonVisible() {
//        addOwner.setVisibility(View.VISIBLE);
//    }
//
//    public void makeAddMoreButtonInvisible() {
//        addOwner.setVisibility(View.GONE);
//    }


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

    private void textChange() {
        helper.TextChangeListenerLooper(surveyForm);
    }

    private void functionSendDataFinal(View view) {
        try {
            if (scheme_roads.length() > 0) {
                if (image1OK && image2OK) {
                    if (checkScheme) {
                        getdata();
                        Log.d("touch", json.toString());
                        send();
                    } else {
                        helper.dialog(context, "Please select scheme from drop down list given in 'Search Scheme'.", "Alert!");
                    }
                } else {
                    helper.dialog(context, "Both pictures are mandatory.", "Alert!");
                }
            } else {
                helper.dialog(context, "Start tracking to get road points.", "Alert!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }


    public void send() {
        try {
            Log.d("touch", json.toString());
            if (!(json.toString().length() == 0)) {
                long id = Querries.insertIntoLocalDbIndustryData(context, json);
                if (id > 0) {
                    Toast.makeText(context, "Data has been saved in local database with id: " + id + ". You can upload it using 'Pending Records' page.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    helper.dialog(context, "Database connection problem, restart application.", "Alert!");
                }
            } else {
                helper.dialog(context, "Your data cannot be saved, restart application.", "Alert!");
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error is" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    //	///////////////////////////////////////Local DataBase insertion////////////////////////////////////////////
    @SuppressLint("SimpleDateFormat")
    public void getdata() {

        json = new JSONObject();
        edEmptyCheck = true;
//        for (int i = 0; i < surveyForm.getChildCount(); i++) {
//            if (surveyForm.getChildAt(i) instanceof LinearLayout) {
//                LinearLayout rl = (LinearLayout) surveyForm.getChildAt(i);
//                getAllLayouts(rl);
//            }
//        }

        try {

            String currentTime = null;
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
            if (timeNow > 0)
                currentTime = dateFormat1.format(new Date(timeNow));

            if (currentTime == null || currentTime.equalsIgnoreCase("")) {
                currentTime = dateFormat1.format(new Date());
                json.put("mobile_date_time", currentTime);
            } else {
                json.put("mobile_date_time", currentTime);
            }

            TempData.setSystemDate(currentTime.split(" ")[0]);

            json.put("scheme_roads", scheme_roads);
            String imei = helper.getImei();
            json.put("imei_surveyed_mobile", imei);

//                json.put("mobile_date_time", TempData.getMobileDateTime());
            json.put("current_date", TempData.getSystemDate());

            json.put("lat", TempData.getLatitude());
            json.put("lng", TempData.getLongitude());

            json.put("lat_end", TempData.getLatitude2());
            json.put("lng_end", TempData.getLongitude2());

            json.put("user_mobile", sharedPreferences.getString("user_login", "NA"));
            json.put("user_id", sharedPreferences.getInt("user_id", 0));

            String[] parts = etScheme.getText().toString().trim().split(" :");
            int schemeID = Integer.parseInt(parts[0].trim());
            json.put("scheme_id", schemeID);

//                json.put("division", spnDivision.getSelectedItem().toString());
//                json.put("district", spnDistrict.getSelectedItem().toString());


//            json.put("tehsil_id", arrTehsilIds.get(spnTehsil.getSelectedItemPosition()));
//            json.put("district_id", arrDistrictIDs.get(spnDistrict.getSelectedItemPosition()));


//            json.put("division_id", sharedPreferences.getInt("division_id", 0));
//            json.put("division", getDivisionName(sharedPreferences.getInt("division_id", 0)));

            String uniqueID = (UUID.randomUUID().toString()).substring(0, 8);
            json.put("unique_code", uniqueID);

            json.put("surveyed_app_version", context.getString(R.string.version));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(savedInstanceState, outPersistentState);
        try {
            savedInstanceState.putString("mobile_date_time", TempData.getMobileDateTime());
            savedInstanceState.putString("current_date", TempData.getSystemDate());
            savedInstanceState.putString("lat", TempData.getLatitude());
            savedInstanceState.putString("lng", TempData.getLongitude());
            savedInstanceState.putString("lat_end", TempData.getLatitude2());
            savedInstanceState.putString("lng_end", TempData.getLongitude2());
            json.put("scheme_roads", scheme_roads);
            savedInstanceState.putString("json_data", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        try {
            String path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.images_folder);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6;

            TempData.setMobileDateTime(savedInstanceState.getString("mobile_date_time"));
            TempData.setSystemDate(savedInstanceState.getString("current_date"));
            TempData.setLatitude(savedInstanceState.getString("lat"));
            TempData.setLongitude(savedInstanceState.getString("lng"));
            TempData.setLatitude2(savedInstanceState.getString("lat_end"));
            TempData.setLongitude2(savedInstanceState.getString("lng_end"));
            json = new JSONObject(savedInstanceState.getString("json_data"));
            scheme_roads = json.getJSONArray("scheme_roads");
            for (int i = 0; i < scheme_roads.length(); i++) {
                JSONObject object = scheme_roads.getJSONObject(i);
                String strtPtName = object.getString("start_pt_name");
                String endPtName = object.getString("end_pt_name");
                String strtPtDesc = object.getString("start_pt_desc");
                String endPtDesc = object.getString("end_pt_desc");
                double roadWidth = object.getDouble("road_width");
                double noOfLanes = object.getDouble("road_length");//length of road
                String road_name = object.getString("road_or_scheme_name");
                String remarks = object.getString("remarks");
                if (i == 0) {
//                    til_start_pt_name.getEditText().setText(strtPtName);
//                    til_end_pt_name.getEditText().setText(endPtName);
//                    til_start_pt_desc.getEditText().setText(strtPtDesc);
//                    til_end_pt_desc.getEditText().setText(endPtDesc);
//                    til_road_width.getEditText().setText(String.valueOf(roadWidth));
//                    til_no_of_lanes.getEditText().setText(String.valueOf(noOfLanes));//length of road
//                    til_road_name.getEditText().setText(road_name);
                    til_remarks.getEditText().setText(remarks);

                    if (object.has("image_start_point")) {
                        String startPointImageName = object.getString("image_start_point");
                        TempData.setImageName(startPointImageName);
                        Bitmap bitmapOrg1 = BitmapFactory.decodeFile(path + "/" + startPointImageName, options);
                        Bitmap thumbnail1 = Bitmap.createScaledBitmap(bitmapOrg1, 50, 50, true);
                        ivStartPtPic.setImageBitmap(thumbnail1);
                    }

                    if (object.has("image_end_point")) {
                        String endPointImageName = object.getString("image_end_point");
                        TempData.setImageName2(endPointImageName);
                        Bitmap bitmapOrg2 = BitmapFactory.decodeFile(path + "/" + endPointImageName, options);
                        Bitmap thumbnail2 = Bitmap.createScaledBitmap(bitmapOrg2, 50, 50, true);
                        ivEndPtPic.setImageBitmap(thumbnail2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    private void getAllLayouts(LinearLayout rl) {
        for (int i = 0; i < rl.getChildCount(); i++) {
            if (rl.getChildAt(i) instanceof TextInputLayout) {
                TextInputLayout til = (TextInputLayout) rl.getChildAt(i);
                EditText et = til.getEditText();
                if (et.getVisibility() == VISIBLE) {
                    if (!(et.getId() == R.id.et_remarks)) {
                        if (helper.isEmpty(et)) {
                            edEmptyCheck = false;
                            til.setErrorEnabled(true);
                            til.setError("Please fill this field");
                        } else {
                            try {
                                json.put(et.getTag().toString(), et.getText().toString());
                                til.setErrorEnabled(false);
                                til.setError(null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            if (helper.isEmpty(et)) {
                                json.put(et.getTag().toString(), "NA");
                            } else {
                                json.put(et.getTag().toString(), et.getText().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        json.put(et.getTag().toString(), "NA");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (rl.getChildAt(i) instanceof Spinner) {
                Spinner spn = (Spinner) rl.getChildAt(i);
                if (spn.getVisibility() == VISIBLE) {
                    try {
                        json.put(spn.getTag().toString(), spn.getSelectedItem().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        json.put(spn.getTag().toString(), "NA");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (rl.getChildAt(i) instanceof RadioGroup) {

                RadioGroup chk = (RadioGroup) rl.getChildAt(i);
                RadioButton rb = (RadioButton) findViewById(chk.getCheckedRadioButtonId());
                RadioButton rbb = (RadioButton) chk.getChildAt(0);
//                RadioButton rbb = (RadioButton) chk.getChildAt(chk.getChildCount() - 1);


                if (chk.getVisibility() == VISIBLE) {
                    if (!(chk.getCheckedRadioButtonId() == -1)) {
                        try {
//                            if ((chk.getId() == R.id.rg_admin_unit) || (chk.getId() == R.id.rg_work_status)) {
                            json.put(chk.getTag().toString(), Integer.parseInt(rb.getTag().toString()));
//                            } else {
//                                json.put(chk.getTag().toString(), rb.getTag().toString());
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    } else {
//                        rbb.setError("Check Anyone");
//                        radioOk = false;
                    }
                } else {
                    try {
                        json.put(chk.getTag().toString(), "NA");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (rl.getChildAt(i) instanceof CheckBox) {
                CheckBox spn = (CheckBox) rl.getChildAt(i);
                if (spn.getVisibility() == VISIBLE) {
                    if (spn.isChecked()) {
                        try {
                            json.put(spn.getTag().toString(), "Yes");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            json.put(spn.getTag().toString(), "No");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        json.put(spn.getTag().toString(), "NA");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (rl.getChildAt(i) instanceof LinearLayout) {
                LinearLayout ll = (LinearLayout) rl.getChildAt(i);
                getAllLayouts(ll);
            }
        }
    }

    public static String addLinebreaks(String input, int maxLineLength) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            if (lineLen + word.length() > maxLineLength) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(" " + word);
            lineLen += word.length();
        }
        return output.toString();
    }

    private void findViews(AttachedProperty auqafProperty) {
        /*Faisal Work*/
        snackbarView = (View) findViewById(R.id.snackbarView);
        surveyForm = (LinearLayout) findViewById(R.id.surveyForm);

        snackbarView = (View) findViewById(R.id.snackbarView);
        surveyForm = (LinearLayout) findViewById(R.id.surveyForm);
        img_part1 = (ImageView) findViewById(R.id.img_part1);
        tv_mainHeading_part1 = (TextView) findViewById(R.id.tv_mainHeading_part1);
        spinnerWaqfPropertyNameShopForm = (SearchableSpinner) findViewById(R.id.spinnerWaqfPropertyNameShopForm);
        spinnerNotificationNoShopForm = (SearchableSpinner) findViewById(R.id.spinnerNotificationNoShopForm);
        til_waqf_prop_name = (TextInputLayout) findViewById(R.id.til_waqf_prop_name);
        et_lessee_name_shop_form = (TextInputEditText) findViewById(R.id.et_lessee_name_shop_form);
        til_lessee_f_name_shop_form = (TextInputLayout) findViewById(R.id.til_lessee_f_name_shop_form);
        et_lessee_f_name_shop_form = (TextInputEditText) findViewById(R.id.et_lessee_f_name_shop_form);
        et_lessee_address_shop_form = (TextInputEditText) findViewById(R.id.et_lessee_address_shop_form);
        til_land_seq_name_shop_form = (TextInputLayout) findViewById(R.id.til_land_seq_name_shop_form);
        et_land_seq_name_shop_form = (TextInputEditText) findViewById(R.id.et_land_seq_name_shop_form);
        til_area_shop_form = (TextInputLayout) findViewById(R.id.til_area_shop_form);
        et_area_shop_form = (TextInputEditText) findViewById(R.id.et_area_shop_form);
        lease_start_date_shop_form = (TextView) findViewById(R.id.lease_start_date_shop_form);
        lease_end_date_shop_form = (TextView) findViewById(R.id.lease_end_date_shop_form);
        til_remarks = (TextInputLayout) findViewById(R.id.til_remarks);
        et_remarks_shop_form = (TextInputEditText) findViewById(R.id.et_remarks_shop_form);
        image_shop_shop_form = (ImageView) findViewById(R.id.image_shop_shop_form);
        image_lessee_shop_form = (ImageView) findViewById(R.id.image_lessee_shop_form);
        image_lease_shop_form = (ImageView) findViewById(R.id.image_lease_shop_form);
        image_other_shop_form = (ImageView) findViewById(R.id.image_other_shop_form);
        sendSave = (Button) findViewById(R.id.sendSave);

        image1Required = true;
        image2Required = true;

        sendSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionSendDataFinal(view);
            }
        });

    }

    public void cameraOn(String prefix, boolean locationMandatory, int pictureNumber) {
        if (locationMandatory) {
            if (helper.checkgps()) {
                if (Build.VERSION.SDK_INT >= 23) { //
                    helper.runMyProgressDialog1("Getting GPS Coordinates...", context);
                    getCurrentLocation(20, "image", prefix, locationMandatory, pictureNumber);
                } else {
                    if (helper.isMockSettingsON(context) && helper.areThereMockPermissionApps(context)) {
                        helper.dialogFake(context, "Please disable mock/fake location.", "Alert!");
                    } else {
                        helper.runMyProgressDialog1("Getting GPS Coordinates...", context);
                        getCurrentLocation(20, "image", prefix, locationMandatory, pictureNumber);
                    }
                }
            }
        } else {
            openCamera(prefix, locationMandatory);
        }
    }

    private void openCamera(String prefix, boolean locationMandatory) {
        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());

        imageName = getPictureName(prefix, locationMandatory);
        String strFolder = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.images_folder) + "/";
        String _path = strFolder + imageName;
        File folder = new File(strFolder);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                File file = new File(_path);
                Uri outputFileUri = Uri.fromFile(file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, 1);
            } else {
                helper.dialog(context, "Cannot create directory.", "Alert!");
            }
        } else {
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MakeMachine", "resultCode: " + resultCode);

        switch (resultCode) {
            case 0:
                imageName = "";
                pos = -1;
                break;
            case -1:
                String path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.images_folder) + "/" + imageName;
                if (pictureType == 1) {
                    if (resizeImage(path)) {
                        image1OK = true;
                        TempData.setImageName(imageName);
                        ivPublic.setImageBitmap(thumbnail);
                    }
                } else if (pictureType == 2) {
                    if (resizeImage(path)) {
                        image2OK = true;
                        TempData.setImageName2(imageName);
                        ivPublic.setImageBitmap(thumbnail);
                    }
                } else if (pictureType == 3) {
                    if (resizeImage(path)) {
                        ivPublic.setTag(imageName);
                        ivPublic.setImageBitmap(thumbnail);
                    }
                }
                break;
        }
    }

    private boolean resizeImage(String path1) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 6;
        setResult(RESULT_OK);
        boolean abc = false;
        File imgFile = new File(path1);
        if (imgFile.exists() && imgFile.isFile()) {
            try {
                Bitmap bitmapOrg = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                bitmapOrg = rotateImage(bitmapOrg, path1);
                int width = bitmapOrg.getWidth();
                int height = bitmapOrg.getHeight();
                FileOutputStream fos = new FileOutputStream(path1);
                createScaledBitmap(bitmapOrg, width, height, true).compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
                fos.close();
                thumbnail = Bitmap.createScaledBitmap(bitmapOrg, 50, 50, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            abc = true;
        } else {
            Toast.makeText(context, "No Image Found", Toast.LENGTH_SHORT).show();
        }
        return abc;
    }

    public static Bitmap rotateImage(Bitmap bitmap, String path) throws IOException {
        int rotate = 0;
        ExifInterface exif;
        exif = new ExifInterface(path);

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private String getPictureName(String prefix, boolean locationMandatory) {
        String uniqueID = (UUID.randomUUID().toString()).substring(0, 4);
        String strDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        if (null != mLocation) {
            strDate = dateFormat.format(new Date(mLocation.getTime())).toString() + ".jpg";
        } else {
            strDate = dateFormat.format(new Date()).toString() + ".jpg";
        }
        return helper.getImei() + "_" + prefix + "_" + uniqueID + "_" + strDate;
    }

    ///////////////////////////////////////Location And GPS////////////////////////////////////////////////////
    public void getCurrentLocation(final int acc, final String event, final String prefix,
                                   final boolean locationMandatory, final int pictureNumber) {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ls = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override

            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                timeNow = location.getTime();
                provider = location.getProvider();

                int MIN_ACCURACY = acc; // 20 in metters
                int val = Math.round(location.getAccuracy());

//                float acc = location.getAccuracy();

                if (val < MIN_ACCURACY) {
                    if (prefix.equals("mr_start")) {
                        TempData.setLatitude(Double.toString(location.getLatitude()));
                        TempData.setLongitude(Double.toString(location.getLongitude()));
                    } else {
                        TempData.setLatitude2(Double.toString(location.getLatitude()));
                        TempData.setLongitude2(Double.toString(location.getLongitude()));
                    }
                    String strPMLong1, strPMLat1;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (null != location /*&& !location.isFromMockProvider()*/) {
                            if (prefix.equals("mr_start")) {
                                strPMLong1 = TempData.getLongitude().toString();
                                strPMLat1 = TempData.getLatitude().toString();
                            } else {
                                strPMLong1 = TempData.getLongitude2().toString();
                                strPMLat1 = TempData.getLatitude2().toString();
                            }
                            if ((strPMLong1.equalsIgnoreCase("") || strPMLong1 == null || strPMLong1.equalsIgnoreCase("0")) ||
                                    (strPMLat1.equalsIgnoreCase("") || strPMLat1 == null || strPMLat1.equalsIgnoreCase("0")) || !helper.checkgps()) {
                                helper.stopMyProgressDialog1();
                                helper.dialog(context, "Sorry, Your Location Not Found.", "Alert!");
                            } else {
                                if (event.equalsIgnoreCase("image")) {
                                    helper.stopMyProgressDialog1();
                                    lm.removeUpdates(ls);
                                    openCamera(prefix, locationMandatory);
                                }
                            }
                        } else {
                            helper.stopMyProgressDialog1();
                            helper.dialogFake(context, "Please disable fake/mock location.", "Alert!");
                        }
                    } else {
                        if (prefix.equals("mr_start")) {
                            strPMLong1 = TempData.getLongitude().toString();
                            strPMLat1 = TempData.getLatitude().toString();
                        } else {
                            strPMLong1 = TempData.getLongitude2().toString();
                            strPMLat1 = TempData.getLatitude2().toString();
                        }
                        if ((strPMLong1.equalsIgnoreCase("") || strPMLong1 == null || strPMLong1.equalsIgnoreCase("0")) ||
                                (strPMLat1.equalsIgnoreCase("") || strPMLat1 == null || strPMLat1.equalsIgnoreCase("0")) || !helper.checkgps()) {
                            helper.stopMyProgressDialog1();
                            helper.dialog(context, "Sorry, Your Location Not Found.", "Alert!");
                        } else {
                            if (event.equalsIgnoreCase("image")) {
                                helper.stopMyProgressDialog1();
                                lm.removeUpdates(ls);
                                openCamera(prefix, locationMandatory);
                            }
                        }
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, ls);
    }


    private void clearOnClickScheme() {
        try {
            clearScheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etScheme.setText("");
                    etScheme.setEnabled(true);
                    etScheme.setFocusable(true);
                    etScheme.setFocusableInTouchMode(true);
                    clearScheme.setVisibility(GONE);
                    checkScheme = false;
                }
            });
        } catch (Exception e) {
            helper.dialog(this, "Exception: " + e.getCause(), "Alert!");
        }
    }

    private void autofillOnSelectScheme() {
        try {
            etScheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    etScheme.setEnabled(false);
                    etScheme.setFocusable(false);
                    clearScheme.setVisibility(VISIBLE);
                    checkScheme = true;
                    String[] parts = etScheme.getText().toString().trim().split(" :");
                    int schemeID = Integer.parseInt(parts[0]);
                    Toast.makeText(context, "Scheme ID: " + schemeID, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            helper.dialog(this, "Exception: " + e.getCause(), "Alert!");
        }
    }

    private void fillDivision() {
        division_array.clear();
        SQLiteDatabase connectToDb = DataBaseSQlite.connectToDb(context);
        Cursor rawQuery = connectToDb.rawQuery("SELECT DISTINCT Div_Name FROM tbl_Division WHERE Div_ID IN (SELECT distinct Div_ID FROM tbl_schemes) ORDER BY Div_Name", (String[]) null);
        while (rawQuery.moveToNext()) {
            division_array.add(rawQuery.getString(rawQuery.getColumnIndex("Div_Name")));
        }


//        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String str = (String) spnDivision.getSelectedItem();
//                clearScheme.setVisibility(GONE);
//                etScheme.setText("");
//                etScheme.setEnabled(true);
//                etScheme.setFocusable(true);
//                etScheme.setFocusableInTouchMode(true);
//                checkScheme = false;
//                fillDistricts(str);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.simple_spinner_item, this.division_array);
//        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down);
//        spnDivision.setAdapter(arrayAdapter);

    }

    public void fillDistricts(final String str) {
        district_array.clear();
        SQLiteDatabase connectToDb = DataBaseSQlite.connectToDb(context);
        Cursor rawQuery = connectToDb.rawQuery("SELECT DISTINCT Dist_Name FROM tbl_Distt WHERE Distt_ID IN (SELECT distinct Distt_ID FROM tbl_schemes WHERE Div_ID=(SELECT DISTINCT Div_ID FROM tbl_Division WHERE Div_Name='" + str + "')) ORDER BY Dist_Name ", (String[]) null);
        while (rawQuery.moveToNext()) {
            district_array.add(rawQuery.getString(rawQuery.getColumnIndex("Dist_Name")));
        }
//        spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                clearScheme.setVisibility(GONE);
//                etScheme.setText("");
//                etScheme.setEnabled(true);
//                etScheme.setFocusable(true);
//                etScheme.setFocusableInTouchMode(true);
//                checkScheme = false;
//                String dist = (String) spnDistrict.getSelectedItem();
////                getRoadsID(str);
//                autofillScheme(str,dist);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.simple_spinner_item, this.district_array);
//        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down);
//        spnDistrict.setAdapter(arrayAdapter);
    }

    private void autofillScheme(String div, String dist) {
        arrSchemes.clear();
        String query = "";
        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
        try {
            query = "SELECT DISTINCT auto_Pm_Scheme_id, name_Scheme from tbl_schemes WHERE Div_ID=(SELECT DISTINCT Div_ID FROM tbl_Division WHERE Div_Name='" + div + "') AND Distt_ID=(SELECT DISTINCT Distt_ID FROM tbl_Distt WHERE Dist_Name='" + dist + "') ORDER BY name_Scheme ";
            Cursor cur = db.rawQuery(query, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String name = cur.getInt(cur.getColumnIndex("auto_Pm_Scheme_id")) + "";
                    name = name + " : " + cur.getString(cur.getColumnIndex("name_Scheme"));
                    arrSchemes.add(addLinebreaks(name, 26));
                }

                etScheme.setText("");
                CustomAdapter arrayAdapter = new CustomAdapter(arrSchemes, this);
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_drop_down, arrSchemes);
                etScheme.setAdapter(arrayAdapter);
                etScheme.setThreshold(1);

                etScheme.setText("");
                etScheme.setEnabled(true);
                etScheme.setFocusable(true);
                etScheme.setFocusableInTouchMode(true);

                tilScheme.setVisibility(VISIBLE);
                etScheme.setVisibility(VISIBLE);

                autofillOnSelectScheme();
                clearOnClickScheme();
            } else {
                helper.dialog(context, "No Scheme Found.", "Alert!");
                tilScheme.setVisibility(GONE);
                etScheme.setVisibility(GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getCause(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit!")
                .setCancelable(false)
                .setMessage("Are you sure you want to leave?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })

                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lm != null && ls != null) {
            lm.removeUpdates(ls);
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
        }
    }

}