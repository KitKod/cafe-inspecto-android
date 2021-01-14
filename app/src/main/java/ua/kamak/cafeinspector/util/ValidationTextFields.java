package ua.kamak.cafeinspector.util;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

public class ValidationTextFields {

    public static boolean checkInput(EditText et, TextInputLayout layout) {
        if (et.getText().toString().length() == 0) {
            layout.setError(Constants.ERR_FOR_TEXT_LAYOUT);
            return false;
        }
        return true;
    }
}
