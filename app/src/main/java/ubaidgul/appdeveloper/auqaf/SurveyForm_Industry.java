package ubaidgul.appdeveloper.auqaf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;


import ubaidgul.appdeveloper.auqaf.DataSender.Constants;
import ubaidgul.appdeveloper.auqaf.database.DataBaseSQlite;
import ubaidgul.appdeveloper.auqaf.database.Querries;
import ubaidgul.appdeveloper.auqaf.helper.AuqafProperty;
import ubaidgul.appdeveloper.auqaf.helper.CustomAdapter;
import ubaidgul.appdeveloper.auqaf.helper.Helper;
import ubaidgul.appdeveloper.auqaf.helper.TempData;

import static android.graphics.Bitmap.createScaledBitmap;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class SurveyForm_Industry extends AppCompatActivity {

    private View snackbarView;
    private LinearLayout surveyForm;
    private TextInputLayout tilRemarks;
    private EditText etRemarks;
    private LinearLayout llImageScheme;
    private ImageView imageScheme1,ivStartPtPic,ivEndPtPic;
    private TextView tvImageScheme1Hint1;

    private TextView tv_mainHeading_part1;
    private TextInputLayout til_zone;
    private TextInputEditText et_zone;
    private TextInputLayout til_circle;
    private TextInputEditText et_circle;
    private TextInputLayout til_waqf_prop_name;
    private TextInputEditText et_waqf_prop_name;
    private TextInputLayout til_shops_count;
    private TextInputEditText et_shops_count;
    private TextInputLayout til_house_count;
    private TextInputEditText et_house_count;
    private TextInputLayout til_others_count;
    private TextInputEditText et_others_count;
    private TextInputLayout til_nla;
    private TextInputEditText et_nla;
    private TextInputLayout til_cultLa;
    private TextInputEditText et_cultLa;
    private TextInputLayout til_ncultLa;
    private TextInputEditText et_ncultLa;
    private TextInputLayout til_data_remarks;
    private TextInputEditText et_data_remarks;
    private TextInputLayout til_remarks;
    private TextInputEditText et_remarks;
    private ImageView image_start_pt;
    private ImageView image_end_pt;
    private ImageView image_other;
    private ImageView image_other_;

    private Button sendSave;


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
//
//    TextInputLayout til_start_pt_name, til_end_pt_name,til_start_pt_desc, til_end_pt_desc,til_road_width,til_no_of_lanes,til_road_name;
//    EditText et_start_pt_name, et_end_pt_name,et_start_pt_desc, et_end_pt_desc;

    int pos = -1;
    boolean edEmptyCheck = false;
    JSONObject json,road_json;
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
        setContentView(R.layout.android_survey_form_plant);
        context = this;
        helper = new Helper(context);
        TempData.setImageName("");
        TempData.setImageName2("");
        AuqafProperty auqafProperty = (AuqafProperty)getIntent().getExtras().getParcelable("auqaf_property");
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
                findViews(auqafProperty);
//                addingLayout();
                fillDivision();
//                textChange();
//                fillDistricts(divisionIdCheck);
//                fillPlants();
//                fillDedicatedType();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
            if (scheme_roads.length()>0) {
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
                    Toast.makeText(context, "Data has been saved in local database with id: " + id+". You can upload it using 'Pending Records' page.", Toast.LENGTH_LONG).show();
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
                    tilRemarks.getEditText().setText(remarks);

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
//                else {
//                    if (extraOwnerContainer.getChildCount() < 25) {
//                        ExtraOwnerLayoutActivity extraOwner = new ExtraOwnerLayoutActivity(context, extraLanUseCategoryArraylist.size() + 1);
//                        extraOwner.inflate(context, inflater, extraOwnerContainer, extraLanUseCategoryArraylist);
//                        extraLanUseCategoryArraylist.add(extraOwner);
//                        if (extraOwnerContainer.getChildCount() == 25) {
//                            makeAddMoreButtonInvisible();
//                        }
//
//                        extraOwner.til_start_pt_name.getEditText().setText(strtPtName);
//                        extraOwner.til_end_pt_name.getEditText().setText(endPtName);
//                        extraOwner.til_start_pt_desc.getEditText().setText(strtPtDesc);
//                        extraOwner.til_end_pt_desc.getEditText().setText(endPtDesc);
//                        extraOwner.til_road_width.getEditText().setText(String.valueOf(roadWidth));
//                        extraOwner.til_no_of_lanes.getEditText().setText(String.valueOf(noOfLanes));//length of road
//                        extraOwner.til_road_name.getEditText().setText(road_name);
//                        extraOwner.tilRemarks.getEditText().setText(remarks);
//                        extraOwner.setRoadJson(object);
//
//                        if (object.has("image_start_point")) {
//                            String startPointImageName = object.getString("image_start_point");
//                            TempData.setImageName(startPointImageName);
//                            Bitmap bitmapOrg1 = BitmapFactory.decodeFile(path + "/" + startPointImageName, options);
//                            Bitmap thumbnail1 = Bitmap.createScaledBitmap(bitmapOrg1, 50, 50, true);
//                            extraOwner.ivStartPtPic.setImageBitmap(thumbnail1);
//                        }
//
//                        if (object.has("image_end_point")) {
//                            String endPointImageName = object.getString("image_end_point");
//                            TempData.setImageName2(endPointImageName);
//                            Bitmap bitmapOrg2 = BitmapFactory.decodeFile(path + "/" + endPointImageName, options);
//                            Bitmap thumbnail2 = Bitmap.createScaledBitmap(bitmapOrg2, 50, 50, true);
//                            extraOwner.ivEndPtPic.setImageBitmap(thumbnail2);
//                        }
//                    } else {
//                        Toast.makeText(context, "You can't add more than 25 Owners!", Toast.LENGTH_SHORT).show();
//                    }
//                }
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
                LinearLayout ll =  (LinearLayout) rl.getChildAt(i);
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

    private void findViews(AuqafProperty auqafProperty) {
        snackbarView = (View)findViewById( R.id.snackbarView );
        surveyForm = (LinearLayout)findViewById( R.id.surveyForm );

        til_zone = (TextInputLayout) findViewById(R.id.til_zone);
        et_zone = (TextInputEditText) findViewById(R.id.et_zone);
        til_circle = (TextInputLayout) findViewById(R.id.til_circle);
        et_circle = (TextInputEditText) findViewById(R.id.et_circle);
        til_waqf_prop_name = (TextInputLayout) findViewById(R.id.til_waqf_prop_name);
        et_waqf_prop_name = (TextInputEditText) findViewById(R.id.et_waqf_prop_name);
        til_shops_count = (TextInputLayout) findViewById(R.id.til_shops_count);
        et_shops_count = (TextInputEditText) findViewById(R.id.et_shops_count);
        til_house_count = (TextInputLayout) findViewById(R.id.til_house_count);
        et_house_count = (TextInputEditText) findViewById(R.id.et_house_count);
        til_others_count = (TextInputLayout) findViewById(R.id.til_others_count);
        et_others_count = (TextInputEditText) findViewById(R.id.et_others_count);
        til_nla = (TextInputLayout) findViewById(R.id.til_nla);
        et_nla = (TextInputEditText) findViewById(R.id.et_nla);
        til_cultLa = (TextInputLayout) findViewById(R.id.til_cultLa);
        et_cultLa = (TextInputEditText) findViewById(R.id.et_cultLa);
        til_ncultLa = (TextInputLayout) findViewById(R.id.til_ncultLa);
        et_ncultLa = (TextInputEditText) findViewById(R.id.et_ncultLa);
        til_data_remarks = (TextInputLayout) findViewById(R.id.til_data_remarks);
        et_data_remarks = (TextInputEditText) findViewById(R.id.et_data_remarks);
        til_remarks = (TextInputLayout) findViewById(R.id.til_remarks);
        et_remarks = (TextInputEditText) findViewById(R.id.et_remarks);
        image_start_pt = (ImageView) findViewById(R.id.image_start_pt);
        image_end_pt = (ImageView) findViewById(R.id.image_end_pt);
        image_other = (ImageView) findViewById(R.id.image_other);
        image_other_ = (ImageView) findViewById(R.id.image_other_);

        sendSave = (Button)findViewById( R.id.sendSave );

        et_zone.setText(auqafProperty.getZone());
        et_circle.setText(auqafProperty.getCircle());
        et_waqf_prop_name.setText(auqafProperty.getWaqfPropertyName());
        et_shops_count.setText(String.valueOf(auqafProperty.getShops()));
        et_house_count.setText(String.valueOf(auqafProperty.getHouses()));
        et_others_count.setText(String.valueOf(auqafProperty.getOthers()));
        et_cultLa.setText(String.valueOf(Math.round((((auqafProperty.getCultA()*8)+auqafProperty.getCultM())*20)+auqafProperty.getCultM()))+ " Marlas");
        et_ncultLa.setText(String.valueOf(Math.round((((auqafProperty.getNcultA()*8)+auqafProperty.getNcultM())*20)+auqafProperty.getNcultM()))+ " Marlas");
        et_nla.setText(String.valueOf(Math.round((((auqafProperty.getNlaA()*8)+auqafProperty.getNlaM())*20)+auqafProperty.getNlaM()))+ " Marlas");
        et_data_remarks.setText(auqafProperty.getRemarks());
//
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            public void afterTextChanged(Editable editable) {
//                if(editable.toString().contains("."))
//                    editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
//                else
//                    editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
//            }
//        });
//
//        editText1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            public void afterTextChanged(Editable editable) {
//                if(editable.toString().contains("."))
//                    editText1.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
//                else
//                    editText1.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
//            }
//        });
//
//        btn_finish_main_road.setEnabled(false);

        image1Required = true;
        image2Required = true;
//        makeAddMoreButtonInvisible();
//        imageScheme1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ivPublic = (ImageView) view;
//                pictureType = 1;
//                cameraOn(ivPublic.getTag().toString(), true, 1);
//            }
//        });


//        btn_start_tracking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startLocationService();
//                btn_start_tracking.setEnabled(false);
//                btn_stop_tracking.setEnabled(true);
//            }
//        });
//
//
//        btn_stop_tracking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("LocationsArray",LocationService.getLocations().toString());
//                btn_stop_tracking.setEnabled(false);
//                btn_finish_main_road.setEnabled(true);
//                stopLocationService();
//            }
//        });

//        btn_finish_main_road.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("LocationsArray",LocationService.getLocations().toString());
//                road_json = new JSONObject();
//                try {
//                    if (LocationService.getLocations().length()>1) {
//                        if (image1OK && image2OK) {
//                            String strtPtName = til_start_pt_name.getEditText().getText().toString().trim();
//                            String endPtName = til_end_pt_name.getEditText().getText().toString().trim();
//                            String strtPtDesc = til_start_pt_desc.getEditText().getText().toString().trim();
//                            String endPtDesc = til_end_pt_desc.getEditText().getText().toString().trim();
//                            String roadWidth = til_road_width.getEditText().getText().toString().trim();
//                            String noOfLanes = til_no_of_lanes.getEditText().getText().toString().trim();
//                            String road_name = til_road_name.getEditText().getText().toString().trim();
//                            String remarks = tilRemarks.getEditText().getText().toString().trim();
//                            if (!strtPtName.equals("") && !endPtName.equals("") && !strtPtDesc.equals("") && !endPtDesc.equals("")&& !roadWidth.equals("") && !noOfLanes.equals("") && !road_name.equals("")) {
//                                JSONArray jarray = LocationService.getLocations();
//                                String geom = "LINESTRING (";//{\"lat\":34.1618339,\"lng\":73.2375608}
//                                for (int i=0; i<jarray.length();i++) {
//                                    JSONObject object = jarray.getJSONObject(i);
//                                    geom += object.getDouble("lng")+ " "+object.getDouble("lat")+",";
//                                }
//                                geom = geom.substring(0,geom.length()-1);
//                                geom = geom+")";
//                                Log.i("geom",geom);
//                                road_json.put("road_type", "Main Road");
//                                road_json.put("start_pt_name", strtPtName);
//                                road_json.put("end_pt_name", endPtName);
//                                road_json.put("start_pt_desc", strtPtDesc);
//                                road_json.put("end_pt_desc", endPtDesc);
//                                road_json.put("road_width", roadWidth);
//                                road_json.put("road_length", noOfLanes);
//                                road_json.put("road_or_scheme_name", road_name);
//                                road_json.put("road_id", "0");//tv_number_crop.getText().toString()
//                                road_json.put("road_coordinates", geom);
//                                road_json.put("image_start_point", TempData.getImageName());
//                                road_json.put("image_end_point", TempData.getImageName2());
//                                road_json.put("remarks", remarks);
//                                scheme_roads.put(road_json);
//                                btn_finish_main_road.setEnabled(false);
//                                ivStartPtPic.setEnabled(false);
//                                ivEndPtPic.setEnabled(false);
//                                makeAddMoreButtonVisible();
//                            } else {
//                                Toast.makeText(context,"Please fill all empty fields", Toast.LENGTH_LONG).show();
//                            }
//                        } else {
//                           Toast.makeText(context,"Please capture both images", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(context,"Location service didn't capture any coordinates.", Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                /*
                "road_type": "Main Road",
			"start_pt_name": "Lahore",
			"end_pt_name": "Rawalpindi",
			"start_pt_desc": "desc st",
			"end_pt_desc": "desc end",
			"road_id": 0,
			"road_coordinates": [
				[-100, 40],
				[-105, 45],
				[-110, 55]
			],
			"image_start_point": "image_name",
			"image_end_point": "image_name"
                 */
//                btn_start_tracking.setEnabled(false);
//                stopLocationService();
//            }
//        });


//        ivStartPtPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ivPublic = (ImageView) view;
//                pictureType = 1;
//                cameraOn(ivPublic.getTag().toString(), true, 1);
//            }
//        });
//
//        ivEndPtPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ivPublic = (ImageView) view;
//                pictureType = 2;
//                cameraOn(ivPublic.getTag().toString(), true, 2);
//            }
//        });

        sendSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionSendDataFinal(view);
            }
        });

    }

//    private void validateCNICFromDatabase() {
//        final String URL_MIS_Data_JSON = Constants.URL_VALIDATE_RECEIVER_FOR_DONATION;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MIS_Data_JSON, new Response.Listener() {
//            @Override
//            public void onResponse(Object response) {
//                try {
//                    JSONObject response1 = new JSONObject((String) response);
//
//                    if (response1.has("status") && response1.has("message")) {
//                        status = response1.getString("status");
//                        message = response1.getString("message");
//
//                        if(status.equalsIgnoreCase("1") &&
//                                message.equalsIgnoreCase("Receiver valid for donation.")){
//                            etReceiverCnic.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,context.getDrawable(R.drawable.tickg),null);
//                        }else{
//                            helper.dialog(context, message,"Alert!");
//                        }
//                    } else if (response1.has("error") && response1.has("error_description")) {
//
//                    } else {
//
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                helper.stopMyProgressDialog();
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(context, "No Connection/Timeout Error.", Toast.LENGTH_LONG).show();
//                } else if (error instanceof AuthFailureError) {
//                    Toast.makeText(context, "Authentication Failure Error.", Toast.LENGTH_LONG).show();
//                } else if (error instanceof ServerError) {
//                    Toast.makeText(context, "Username and Password is incorrect. ", Toast.LENGTH_LONG).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(context, "Network Error.", Toast.LENGTH_LONG).show();
//                } else if (error instanceof ParseError) {
//                    Toast.makeText(context, "Parse Error.", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(context, "Internet Connection Problem.", Toast.LENGTH_LONG).show();
//                }
//            }
//        }) {
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("receiver_cnic", etReceiverCnic.getText().toString());
//                return params;
//            }
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest);
//    }


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
                    String strPMLong1 ,strPMLat1;
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

//    private String getDivisionName(int divisionIdCheck) {
//        String query = "";
//        String division_name = "NA";
//        SQLiteDatabase db = DataBaseSQlite.connectToDb(context);
//        try {
//            query = "SELECT DISTINCT division_name from divisions where division_id = " + divisionIdCheck + "";
//            Cursor cur = db.rawQuery(query, null);
//
//            if (cur.getCount() > 0) {
//                while (cur.moveToNext()) {
//                    division_name = cur.getString(cur.getColumnIndex("division_name"));
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Exception: " + e.getCause(), Toast.LENGTH_LONG).show();
//        } finally {
//            db.close();
//        }
//
//        return division_name;
//    }


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
            query = "SELECT DISTINCT auto_Pm_Scheme_id, name_Scheme from tbl_schemes WHERE Div_ID=(SELECT DISTINCT Div_ID FROM tbl_Division WHERE Div_Name='"+div+"') AND Distt_ID=(SELECT DISTINCT Distt_ID FROM tbl_Distt WHERE Dist_Name='"+dist+"') ORDER BY name_Scheme ";
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