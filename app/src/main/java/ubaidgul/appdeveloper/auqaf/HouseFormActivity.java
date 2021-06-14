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

public class HouseFormActivity extends AppCompatActivity {

    /*Faisal Work*/
    private View snackbarView;
    private LinearLayout surveyForm;
    private ImageView img_part1;
    private TextView tv_mainHeading_part1;
    private TextInputLayout til_lot_no_house_form;
    private TextInputEditText et_lot_no_house_form;
    private TextInputLayout til_lot_area_house_form;
    private TextInputEditText et_lot_area_house_form;
    private Spinner spinner_lease_period_house_form;
    private TextInputLayout til_waqf_house_form;
    private TextInputEditText et_lessee_name_house_form;
    private TextInputLayout til_lessee_f_name_house_form;
    private TextInputEditText et_lessee_f_name_house_form;
    private TextInputLayout til_lessee_address_house_form;
    private TextInputEditText et_lessee_address_house_form;
    private SearchableSpinner spinner_waqf_property_name_house_form;
    private SearchableSpinner spinner_notification_no_house_form;
    private TextInputLayout til_remarks;
    private TextInputEditText et_remarks_house_form;
    private ImageView image_house_house_form;
    private ImageView image_document_house_form;
    private ImageView image_aks_house_form;
    private ImageView image_khasra_girdawari_house_form;
    private Button sendSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_form);
        findViews();
    }

    public void findViews() {

        snackbarView = (View) findViewById(R.id.snackbarView);
        surveyForm = (LinearLayout) findViewById(R.id.surveyForm);
        img_part1 = (ImageView) findViewById(R.id.img_part1);
        tv_mainHeading_part1 = (TextView) findViewById(R.id.tv_mainHeading_part1);
        til_lot_no_house_form = (TextInputLayout) findViewById(R.id.til_lot_no_house_form);
        et_lot_no_house_form = (TextInputEditText) findViewById(R.id.et_lot_no_house_form);
        til_lot_area_house_form = (TextInputLayout) findViewById(R.id.til_lot_area_house_form);
        et_lot_area_house_form = (TextInputEditText) findViewById(R.id.et_lot_area_house_form);
        spinner_lease_period_house_form = (Spinner) findViewById(R.id.spinner_lease_period_house_form);
        til_waqf_house_form = (TextInputLayout) findViewById(R.id.til_waqf_house_form);
        et_lessee_name_house_form = (TextInputEditText) findViewById(R.id.et_lessee_name_house_form);
        til_lessee_f_name_house_form = (TextInputLayout) findViewById(R.id.til_lessee_f_name_house_form);
        et_lessee_f_name_house_form = (TextInputEditText) findViewById(R.id.et_lessee_f_name_house_form);
        til_lessee_address_house_form = (TextInputLayout) findViewById(R.id.til_lessee_address_house_form);
        et_lessee_address_house_form = (TextInputEditText) findViewById(R.id.et_lessee_address_house_form);
        spinner_waqf_property_name_house_form = (SearchableSpinner) findViewById(R.id.spinner_waqf_property_name_house_form);
        spinner_notification_no_house_form = (SearchableSpinner) findViewById(R.id.spinner_notification_no_house_form);
        til_remarks = (TextInputLayout) findViewById(R.id.til_remarks);
        et_remarks_house_form = (TextInputEditText) findViewById(R.id.et_remarks_house_form);
        image_house_house_form = (ImageView) findViewById(R.id.image_house_house_form);
        image_document_house_form = (ImageView) findViewById(R.id.image_document_house_form);
        image_aks_house_form = (ImageView) findViewById(R.id.image_aks_house_form);
        image_khasra_girdawari_house_form = (ImageView) findViewById(R.id.image_khasra_girdawari_house_form);
        sendSave = (Button) findViewById(R.id.sendSave);

    }
}