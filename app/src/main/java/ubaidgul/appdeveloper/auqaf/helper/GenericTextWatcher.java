package ubaidgul.appdeveloper.auqaf.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class GenericTextWatcher implements TextWatcher {

  private EditText editText;
  private TextInputLayout textInputLayout;

  public GenericTextWatcher(View textInputLayout, View editText) {
      this.editText = (EditText) editText;
      this.textInputLayout = (TextInputLayout) textInputLayout;

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (!editText.getText().toString().equalsIgnoreCase("")) {
          textInputLayout.setErrorEnabled(false);
          textInputLayout.setError(null);
      }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void afterTextChanged(Editable s) {

  }
}