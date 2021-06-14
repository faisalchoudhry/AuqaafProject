package ubaidgul.appdeveloper.auqaf;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class LandSurveyActivity extends AppCompatActivity {

    /*Faisal Work*/
    private View snackbarView;
    private LinearLayout surveyForm;
    private ImageView img_part1;
    private TextView tv_mainHeading_part1;
    private TextInputLayout til_lot_no_land_form;
    private TextInputEditText et_lot_no_land_form;
    private TextInputLayout til_lot_area_land_form;
    private TextInputEditText et_lot_area_land_form;
    private Spinner spinner_lease_period_land_form;
    private TextInputLayout til_waqf_land_form;
    private TextInputEditText et_lessee_name_land_form;
    private TextInputLayout til_lessee_f_name_land_form;
    private TextInputEditText et_lessee_f_name_land_form;
    private TextInputLayout til_lessee_address_land_form;
    private TextInputEditText et_lessee_address_land_form;
    private SearchableSpinner spinner_waqf_property_name_land_form;
    private SearchableSpinner spinner_notification_no_land_form;
    private TextInputLayout til_remarks;
    private TextInputEditText et_remarks_land_form;
    private ImageView image_land_land_form;
    private ImageView image_document_land_form;
    private ImageView image_aks_land_form;
    private ImageView image_khasra_girdawari_land_form;
    private Button sendSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_survey);
        findViews();
    }

    public void findViews() {
        snackbarView = (View) findViewById(R.id.snackbarView);
        surveyForm = (LinearLayout) findViewById(R.id.surveyForm);
        img_part1 = (ImageView) findViewById(R.id.img_part1);
        tv_mainHeading_part1 = (TextView) findViewById(R.id.tv_mainHeading_part1);
        til_lot_no_land_form = (TextInputLayout) findViewById(R.id.til_lot_no_land_form);
        et_lot_no_land_form = (TextInputEditText) findViewById(R.id.et_lot_no_land_form);
        til_lot_area_land_form = (TextInputLayout) findViewById(R.id.til_lot_area_land_form);
        et_lot_area_land_form = (TextInputEditText) findViewById(R.id.et_lot_area_land_form);
        spinner_lease_period_land_form = (Spinner) findViewById(R.id.spinner_lease_period_land_form);
        til_waqf_land_form = (TextInputLayout) findViewById(R.id.til_waqf_land_form);
        et_lessee_name_land_form = (TextInputEditText) findViewById(R.id.et_lessee_name_land_form);
        til_lessee_f_name_land_form = (TextInputLayout) findViewById(R.id.til_lessee_f_name_land_form);
        et_lessee_f_name_land_form = (TextInputEditText) findViewById(R.id.et_lessee_f_name_land_form);
        til_lessee_address_land_form = (TextInputLayout) findViewById(R.id.til_lessee_address_land_form);
        et_lessee_address_land_form = (TextInputEditText) findViewById(R.id.et_lessee_address_land_form);
        spinner_waqf_property_name_land_form = (SearchableSpinner) findViewById(R.id.spinner_waqf_property_name_land_form);
        spinner_notification_no_land_form = (SearchableSpinner) findViewById(R.id.spinner_notification_no_land_form);
        til_remarks = (TextInputLayout) findViewById(R.id.til_remarks);
        et_remarks_land_form = (TextInputEditText) findViewById(R.id.et_remarks_land_form);
        image_land_land_form = (ImageView) findViewById(R.id.image_land_land_form);
        image_document_land_form = (ImageView) findViewById(R.id.image_document_land_form);
        image_aks_land_form = (ImageView) findViewById(R.id.image_aks_land_form);
        image_khasra_girdawari_land_form = (ImageView) findViewById(R.id.image_khasra_girdawari_land_form);
        sendSave = (Button) findViewById(R.id.sendSave);

    }
}