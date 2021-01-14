package ua.kamak.cafeinspector.reg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.util.ValidationTextFields;

public class LogInOwnerFragment extends Fragment {

    public interface onInputDataUserListener {
        public void getUserData(String email, String password, String wayOfEntry);
    }

    private onInputDataUserListener dataUserListener;
    public static final String TAG = "LogInOwnerFragmentTag";

    private RadioGroup radioGroup;
    private FirebaseAuth auth;
    private EditText etEmail;
    private EditText etPassword;
    private Button butReg;

    private TextInputLayout tlEmail;
    private TextInputLayout tlPassword;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dataUserListener = (onInputDataUserListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_owner, container, false);
        return v;
    }

    //  управление элементами фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup = view.findViewById(R.id.reg_radiogroup);

        RadioButton radioButtonLogIn = view.findViewById(R.id.radio_but_login);
        radioButtonLogIn.setChecked(true);

        etEmail = view.findViewById(R.id.et_reg_login_owner_email);
        etPassword = view.findViewById(R.id.et_reg_login_owner_pasw);
        butReg = view.findViewById(R.id.but_reg_login_owner_into);
        tlEmail = view.findViewById(R.id.txt_layout_ling_in_owner_email);
        tlPassword = view.findViewById(R.id.txt_layout_login_owner_pas);

        butReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidationTextFields.checkInput(etEmail, tlEmail) & ValidationTextFields.checkInput(etPassword, tlPassword)) {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radio_but_login:
                            dataUserListener.getUserData(etEmail.getText().toString(), etPassword.getText().toString(), RegActivity.USER_SINGIN);
                            break;
                        case R.id.radio_but_registration:
                            dataUserListener.getUserData(etEmail.getText().toString(), etPassword.getText().toString(), RegActivity.USER_REG);
                            break;
                    }
                }
            }
        });
    }

    private boolean validateFrom() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }
}
