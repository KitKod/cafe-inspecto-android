package ua.kamak.cafeinspector.reg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.WaiterActivity;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.ValidationTextFields;

public class LogInWaiterFragment extends Fragment{

    public static final String TAG = "LogInWaiterFragmentTag";

    private EditText etCodeOwner;
    private EditText etCodeWaiter;
    private Button btnLogin;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private SharedPreferences rememberLogIn;

    private TextInputLayout tlCodeOwner;
    private TextInputLayout tlCodeWaiter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        rememberLogIn = getActivity().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_waiter, container, false);
        return v;
    }

    //  управление элементами фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCodeOwner = view.findViewById(R.id.et_reg_waiter_code_owner);
        etCodeWaiter = view.findViewById(R.id.et_reg_waiter_code_waiter);
        btnLogin = view.findViewById(R.id.btn_login_waiter);

        tlCodeOwner = view.findViewById(R.id.txt_layout_lofin_waiter_code_owner);
        tlCodeWaiter = view.findViewById(R.id.txt_layout_login_waiter_code);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidationTextFields.checkInput(etCodeOwner, tlCodeOwner) & ValidationTextFields.checkInput(etCodeWaiter, tlCodeWaiter)) {

                    Query query = myRef.child(etCodeOwner.getText().toString()).child("waiters").orderByKey().equalTo(etCodeWaiter.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                SharedPreferences.Editor editor = rememberLogIn.edit();
                                editor.putBoolean(Constants.WAITER_IS_LOGIN, true);
                                editor.putString(Constants.CODE_OF_WAITER, etCodeWaiter.getText().toString());
                                editor.putString(Constants.CODE_OF_OWNER, etCodeOwner.getText().toString());
                                editor.apply();

                                Intent intent = new Intent(getContext(), WaiterActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Log.d("WaiterErr", "мы no нашли данные в базе данных");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
