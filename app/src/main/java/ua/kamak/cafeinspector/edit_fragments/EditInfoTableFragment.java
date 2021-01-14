package ua.kamak.cafeinspector.edit_fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.model.TableForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.LiveDataOrderObject;
import ua.kamak.cafeinspector.util.LiveDataTable;
import ua.kamak.cafeinspector.util.ValidationTextFields;

public class EditInfoTableFragment extends Fragment {

    public static final String TAG = "EditInfoTableFragmentTag";

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private TextInputLayout inputLayoutNumTable;
    private EditText etNumberTable;
    private EditText etNumberSeats;
    private Spinner spStatus;
    private Button btnSendInfo;

    private TextInputLayout tlNumber;
    private TextInputLayout tlSeats;

    private String modeWork;

    public static EditInfoTableFragment getInstance(String keyEdit) {
        Bundle args = new Bundle();
        args.putString(Constants.EDIT_ITEM_TABLES_KEY , keyEdit);

        EditInfoTableFragment fragment = new EditInfoTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_teble, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNumberTable = view.findViewById(R.id.et_edit_num_table_table_info);
        etNumberSeats = view.findViewById(R.id.et_edit_num_seats_table_info);
        spStatus = view.findViewById(R.id.sp_edit_status_table_info);
        btnSendInfo = view.findViewById(R.id.btn_edit_send_table_info);

        inputLayoutNumTable = view.findViewById(R.id.txtLayout_edit_num_table_info);
        tlSeats = view.findViewById(R.id.textInputLayout_edit_num_seats);

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.status_of_table,
                android.R.layout.simple_spinner_item
        );
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(spAdapter);

        modeWork = getArguments().getString(Constants.EDIT_ITEM_TABLES_KEY);

        if (modeWork.equals(Constants.EDIT_ITEM_TABLES_KEY)) {
            LiveData<TableForOwnerModel> liveData = LiveDataTable.getInstance().getData();
            liveData.observe(getActivity(), new Observer<TableForOwnerModel>() {
                @Override
                public void onChanged(@Nullable TableForOwnerModel tableForOwnerModel) {
                    inputLayoutNumTable.setVisibility(View.GONE);
                    etNumberTable.setText(String.valueOf(tableForOwnerModel.getNumTable()));
                    etNumberSeats.setText(String.valueOf(tableForOwnerModel.getNumberOfSeats()));
                    switch (tableForOwnerModel.getFlag()) {
                        case Constants.TABLE_STATUS_IS_FREE:
                            spStatus.setSelection(0);
                            break;
                        case Constants.TABLE_STATUS_IS_BUSY:
                            spStatus.setSelection(1);
                            break;
                        case Constants.TABLE_STATUS_IS_RESERVED:
                            spStatus.setSelection(2);
                            break;
                    }
                }
            });
        }

        btnSendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidationTextFields.checkInput(etNumberTable, inputLayoutNumTable) & ValidationTextFields.checkInput(etNumberSeats, tlSeats)) {
                    TableForOwnerModel model = new TableForOwnerModel(
                            Integer.parseInt(etNumberSeats.getText().toString()),
                            spStatus.getSelectedItem().toString()
                    );

                    String key = etNumberTable.getText().toString();

                    Map<String, Object> newItemTable = model.toMap();
                    HashMap<String, Object> sendValue = new HashMap<>();
                    sendValue.put(key, newItemTable);

                    myRef.child("users").child(auth.getUid()).child("listtables").updateChildren(sendValue);
                    getActivity().finish();
                }
            }
        });

    }
}
