package ubaidgul.appdeveloper.auqaf;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ExtraOwnerLayoutActivity implements Parcelable {


    ArrayList<String> arrcategoryOne = new ArrayList<String>();
    ArrayList<String> arrcategoryOneCode = new ArrayList<String>();

    ArrayList<String> arrcategoryTwo = new ArrayList<String>();
    ArrayList<String> arrcategoryTwoCode = new ArrayList<String>();


    private boolean image1OK, image2OK;
    private TextView priorityTView;
    private int counter;
    private Context context;
    public void setRoadJson(JSONObject roadJson) {
        this.roadJson = roadJson;
    }

    public JSONObject getRoadJson() {
        return roadJson;
    }

    private JSONObject roadJson;
    private View view;
    TextInputLayout til_start_pt_name, til_end_pt_name,til_start_pt_desc, til_end_pt_desc,til_road_width,til_no_of_lanes,til_road_name,tilRemarks;;
    EditText et_start_pt_name, et_end_pt_name,et_start_pt_desc, et_end_pt_desc;
    Button btn_start_tracking,btn_stop_tracking,btn_finish_main_road;
    RadioGroup radioGender;
    TextView tv_number_crop;
    ImageView ivStartPtPic,ivEndPtPic;
    TextView tv_categories;
    public boolean isOrphan = false;
    int a = 0;

    JSONObject road_json;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getStartPtName());
        parcel.writeString(getEndPtName());
        parcel.writeString(getStartPtDesc());
        parcel.writeString(getEndPtDesc());
        parcel.writeInt(getCounter());
    }

    private ExtraOwnerLayoutActivity(Parcel in) {
        setCounter(in.readInt());
    }

    public ExtraOwnerLayoutActivity(Context context, int index) {
        this.context = context;
        this.setCounter(index);
    }


    public void inflate(final Context context, LayoutInflater inflater, final LinearLayout parent, final ArrayList<ExtraOwnerLayoutActivity> ep) {
        this.context = context;
        view = inflater.inflate(R.layout.add_owner_view, null);
        roadJson =  new JSONObject();
        //Owner Layout ids
        til_start_pt_name = (TextInputLayout)view.findViewById(R.id.til_start_pt_name);
        til_end_pt_name = (TextInputLayout)view.findViewById(R.id.til_end_pt_name);
        til_start_pt_desc = (TextInputLayout)view.findViewById(R.id.til_start_pt_desc);
        til_end_pt_desc = (TextInputLayout)view.findViewById(R.id.til_end_pt_desc);
        til_road_width = (TextInputLayout)view.findViewById(R.id.til_road_width);
        til_no_of_lanes = (TextInputLayout)view.findViewById(R.id.til_no_lanes);
        til_road_name = (TextInputLayout)view.findViewById(R.id.til_road_name);
        tilRemarks = (TextInputLayout)view.findViewById( R.id.til_remarks );
        btn_start_tracking = (Button) view.findViewById(R.id.btn_start_tracking);
        btn_stop_tracking = (Button) view.findViewById(R.id.btn_stop_tracking);
        btn_finish_main_road = (Button) view.findViewById(R.id.btn_finish_main_road);
        tv_number_crop = (TextView) view.findViewById(R.id.tv_number_crop);
        ivStartPtPic = (ImageView) view.findViewById( R.id.image_start_pt );
        ivEndPtPic = (ImageView) view.findViewById( R.id.image_end_pt );
        EditText editText = til_road_width.getEditText();
        EditText editText1 = til_no_of_lanes.getEditText();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {
                if(editable.toString().contains("."))
                    editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                else
                    editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            }
        });

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {
                if(editable.toString().contains("."))
                    editText1.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                else
                    editText1.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            }
        });

        final ImageView deleteImageview = (ImageView) view.findViewById(R.id.delete);

        this.setPriorityTView((TextView) this.view.findViewById(R.id.tv_number_crop));
        this.getPriorityTView().setText("" + this.getCounter());

        deleteImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do You want to Delete this Subroad?")
                        .setTitle("Confirm Deletion!");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            parent.removeView(view);
                        } catch (Exception e) {
                            System.out.println(e);
                        } finally {
                            ep.remove(ExtraOwnerLayoutActivity.this);
                            int p = 1;
                            Iterator it = ep.iterator();
                            while (it.hasNext()) {
                                ((ExtraOwnerLayoutActivity) it.next()).correctPriority(p);
                                p++;
                            }
//                            ((SurveyForm_Industry) context).makeAddMoreButtonVisible();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        btn_finish_main_road.setEnabled(false);
        btn_stop_tracking.setEnabled(false);
//        ((SurveyForm_Industry)context).makeAddMoreButtonInvisible();
//        imageScheme1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((SurveyForm_Industry)context).ivPublic = (ImageView) view;
//                ((SurveyForm_Industry)context).pictureType = 3;
//                ((SurveyForm_Industry)context).cameraOn(((SurveyForm_Industry)context).ivPublic.getTag().toString(), true, 3);
//            }
//        });


        btn_start_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SurveyForm_Industry)context).startLocationService();
                btn_start_tracking.setEnabled(false);
                btn_stop_tracking.setEnabled(true);
            }
        });


        btn_stop_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LocationsArray",LocationService.getLocations().toString());
                btn_stop_tracking.setEnabled(false);
                btn_finish_main_road.setEnabled(true);
                ((SurveyForm_Industry)context).stopLocationService();
            }
        });

        btn_finish_main_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LocationsArray",LocationService.getLocations().toString());
                road_json = new JSONObject();
                try {
                    if (LocationService.getLocations().length()>1) {
                        String startPointPicName = ivStartPtPic.getTag().toString();
                        String endPointPicName = ivEndPtPic.getTag().toString();
                        if (!startPointPicName.equals("sr_start") && startPointPicName.contains(".jpg")){
                            image1OK = true;
                        }
                        if (!endPointPicName.equals("sr_end") && endPointPicName.contains(".jpg")){
                            image2OK = true;
                        }
                        if (image1OK && image2OK) {
                            String strtPtName = til_start_pt_name.getEditText().getText().toString().trim();
                            String endPtName = til_end_pt_name.getEditText().getText().toString().trim();
                            String strtPtDesc = til_start_pt_desc.getEditText().getText().toString().trim();
                            String endPtDesc = til_end_pt_desc.getEditText().getText().toString().trim();
                            String roadWidth = til_road_width.getEditText().getText().toString().trim();
                            String noOfLanes = til_no_of_lanes.getEditText().getText().toString().trim();
                            String road_name = til_road_name.getEditText().getText().toString().trim();
                            String remarks = tilRemarks.getEditText().getText().toString().trim();
                            if (!strtPtName.equals("") && !endPtName.equals("") && !strtPtDesc.equals("") && !endPtDesc.equals("")&& !roadWidth.equals("") && !noOfLanes.equals("")&& !road_name.equals("")) {
                                JSONArray jarray = LocationService.getLocations();
                                String geom = "LINESTRING (";//{\"lat\":34.1618339,\"lng\":73.2375608}
                                for (int i=0; i<jarray.length();i++) {
                                    JSONObject object = jarray.getJSONObject(i);
                                    geom += object.getDouble("lng")+ " "+object.getDouble("lat")+",";
                                }
                                geom = geom.substring(0,geom.length()-1);
                                geom = geom+")";
                                Log.i("geom",geom);
                                road_json.put("road_type", "Sub Road");
                                road_json.put("start_pt_name", strtPtName);
                                road_json.put("end_pt_name", endPtName);
                                road_json.put("start_pt_desc", strtPtDesc);
                                road_json.put("end_pt_desc", endPtDesc);
                                road_json.put("road_width", roadWidth);
                                road_json.put("road_length", noOfLanes);
                                road_json.put("road_or_scheme_name", road_name);
                                road_json.put("road_id", tv_number_crop.getText().toString());
                                road_json.put("road_coordinates", geom);
                                road_json.put("image_start_point", ivStartPtPic.getTag().toString());
                                road_json.put("image_end_point", ivEndPtPic.getTag().toString());
                                road_json.put("remarks", remarks);
                                ((SurveyForm_Industry)context).scheme_roads.put(road_json);
                                btn_finish_main_road.setEnabled(false);
                                ivStartPtPic.setEnabled(false);
                                ivEndPtPic.setEnabled(false);
//                                ((SurveyForm_Industry)context).makeAddMoreButtonVisible();
                                Log.i("JSONArray",((SurveyForm_Industry)context).scheme_roads.toString());
                                deleteImageview.setEnabled(false);
                            } else {
                                Toast.makeText(context,"Please fill all empty fields", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context,"Please capture both images", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context,"Location service didn't capture any coordinates.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                btn_start_tracking.setEnabled(false);
//                stopLocationService();
            }
        });


        ivStartPtPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SurveyForm_Industry)context).ivPublic = (ImageView) view;
                ((SurveyForm_Industry)context).pictureType = 3;
                ((SurveyForm_Industry)context).cameraOn(((SurveyForm_Industry)context).ivPublic.getTag().toString(), false, 3);
            }
        });

        ivEndPtPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SurveyForm_Industry)context).ivPublic = (ImageView) view;
                ((SurveyForm_Industry)context).pictureType = 3;
                ((SurveyForm_Industry)context).cameraOn(((SurveyForm_Industry)context).ivPublic.getTag().toString(), false, 3);
            }
        });
        parent.addView(view);
    }

    public void correctPriority(int priority)
    {
        this.setCounter(priority);
        this.getPriorityTView().setText("" + priority);
    }

    public static final Creator<ExtraOwnerLayoutActivity> CREATOR = new Creator<ExtraOwnerLayoutActivity>() {
        public ExtraOwnerLayoutActivity createFromParcel(Parcel in) {
            return new ExtraOwnerLayoutActivity(in);
        }

        public ExtraOwnerLayoutActivity[] newArray(int size) {
            return new ExtraOwnerLayoutActivity[size];
        }
    };

    public TextView getPriorityTView() {
        return priorityTView;
    }

    public void setPriorityTView(TextView priorityTView) {
        this.priorityTView = priorityTView;
    }

    public String getStartPtName() {
        return this.til_start_pt_name.getEditText().getText().toString();
    }

    public String getEndPtName() {
        return this.til_end_pt_name.getEditText().getText().toString();
    }

    public String getStartPtDesc() {
        return this.til_start_pt_desc.getEditText().getText().toString();
    }

    public String getEndPtDesc() {
        return this.til_end_pt_desc.getEditText().getText().toString();
    }

    public int getCounter() {
        return counter;
    }
    public void setCounter(int counter) {
        this.counter = counter;
    }





}
