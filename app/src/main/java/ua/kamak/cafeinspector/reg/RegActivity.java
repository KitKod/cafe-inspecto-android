package ua.kamak.cafeinspector.reg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ua.kamak.cafeinspector.OwnerActivity;
import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.TerminalActivity;
import ua.kamak.cafeinspector.WaiterActivity;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.DelayedProgressDialog;

public class RegActivity extends AppCompatActivity implements LogInOwnerFragment.onInputDataUserListener {

    public static final int INDEX_USER_EMAIL = 0;
    public static final int INDEX_USER_PASSWORD = 1;
    public static final int INDEX_SINGIN_OR_REG = 2;
    public static final String USER_SINGIN = "singin";
    public static final String USER_REG = "reg";


    private LogInOwnerFragment fragmentLogInOwner;
    private LogInWaiterFragment fragmentLogInWaiter;
    private LogInTerminalFragment fragmentLogInTerminal;

    private FragmentManager fragmentManager;

    private Spinner spWhoIsYou;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefDatabase = database.getReference();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        preferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);

        spWhoIsYou = findViewById(R.id.spinner_reg_who_is_you);

        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        fragmentLogInOwner = new LogInOwnerFragment();
        fragmentLogInWaiter = new LogInWaiterFragment();
        fragmentLogInTerminal = new LogInTerminalFragment();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.reg_container, fragmentLogInOwner, LogInOwnerFragment.TAG)
                    .commit();
        }

        //  настройка спеннера для выбора роли.
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.who_is_you,
                android.R.layout.simple_spinner_item
        );
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWhoIsYou.setAdapter(spAdapter);
        spWhoIsYou.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chooseTypeLogIn(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mAuth.getCurrentUser()) {
            Intent intent = new Intent(this, OwnerActivity.class);
            startActivity(intent);
            finish();
        } else if (preferences.getBoolean(Constants.WAITER_IS_LOGIN, false)) {
            Intent intent = new Intent(this, WaiterActivity.class);
            startActivity(intent);
            finish();
        } else if (preferences.getBoolean(Constants.TERMINAL_IS_LOGIN, false)) {
            Intent intent = new Intent(this, TerminalActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void chooseTypeLogIn(int position) {
        switch (position) {
            case 0:
                if (fragmentManager.findFragmentByTag(LogInOwnerFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.reg_container, fragmentLogInOwner, LogInOwnerFragment.TAG)
                            .commit();
                }
                break;
            case 1:
                if (fragmentManager.findFragmentByTag(LogInWaiterFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.reg_container, fragmentLogInWaiter, LogInWaiterFragment.TAG)
                            .commit();
                }
                break;
            case 2:
                if (fragmentManager.findFragmentByTag(LogInTerminalFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.reg_container, fragmentLogInTerminal, LogInTerminalFragment.TAG)
                            .commit();
                }
                break;
        }
    }

    private void createAccount(String email, String password) {
        final DelayedProgressDialog progressDialog = new DelayedProgressDialog();
        progressDialog.show(fragmentManager, "tag");

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //initNewUserInDataBase();
                    startActivity(new Intent(RegActivity.this, OwnerActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        });
    }

    private void singInAccount(String email, String password) {
        final DelayedProgressDialog progressDialog = new DelayedProgressDialog();
        progressDialog.show(fragmentManager, "tag");

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(RegActivity.this, OwnerActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegActivity.this, "SingIn failed.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        });
    }

    private void initNewUserInDataBase() {
        myRefDatabase.child(mAuth.getCurrentUser().getUid()).child("staff").child("oly").setValue("1");
    }

    @Override
    public void getUserData(String email, String password, String wayOfEntry) {
        if (wayOfEntry.equals(USER_REG)) {
            createAccount(email, password);
        } else {
            singInAccount(email, password);
        }
    }
}
