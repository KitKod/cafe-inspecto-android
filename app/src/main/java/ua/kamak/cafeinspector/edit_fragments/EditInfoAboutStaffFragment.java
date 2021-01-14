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
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.model.StaffForOwnerModel;
import ua.kamak.cafeinspector.model.TableForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.LiveDataStaff;
import ua.kamak.cafeinspector.util.LiveDataTable;
import ua.kamak.cafeinspector.util.ValidationTextFields;

public class EditInfoAboutStaffFragment extends Fragment {

    public static final String TAG = "EditInfoAboutStaffFragmentTag";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth auth;

    private EditText etName;
    private EditText etWorkPosition;
    private EditText etAge;

    private TextInputLayout tlName;
    private TextInputLayout tlJob;
    private TextInputLayout tlAge;

    private Button btnAdd;
    private String modeWork;
    private String keyId;

    public static EditInfoAboutStaffFragment getInstance(String keyEdit) {
        Bundle args = new Bundle();
        args.putString(Constants.EDIT_ITEM_STAFF_KEY, keyEdit);

        EditInfoAboutStaffFragment fragment = new EditInfoAboutStaffFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_info_about_staff, container, false);
        return v;
    }

    //  управление элементами фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_editstaff_name);
        etWorkPosition = view.findViewById(R.id.et_editstaff_work_position);
        etAge = view.findViewById(R.id.et_editstaff_age);
        btnAdd = view.findViewById(R.id.btn_add_new_position);

        tlName = view.findViewById(R.id.txt_layout_staff_name);
        tlJob = view.findViewById(R.id.txt_layout_staff_job);
        tlAge = view.findViewById(R.id.txt_layout_staff_age);

        modeWork = getArguments().getString(Constants.EDIT_ITEM_STAFF_KEY);

        if (modeWork.equals(Constants.EDIT_ITEM_STAFF_KEY)) {
            LiveData<StaffForOwnerModel> liveData = LiveDataStaff.getInstance().getData();
            liveData.observe(getActivity(), new Observer<StaffForOwnerModel>() {
                @Override
                public void onChanged(@Nullable StaffForOwnerModel model) {
                    etName.setText(model.getName());
                    etWorkPosition.setText(model.getWorkPosition());
                    etAge.setText(String.valueOf(model.getAge()));
                    keyId = model.getKey();
                }
            });
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidationTextFields.checkInput(etName , tlName)
                        & ValidationTextFields.checkInput(etWorkPosition, tlJob)
                        & ValidationTextFields.checkInput(etAge, tlAge)) {

                    StaffForOwnerModel model = new StaffForOwnerModel(etName.getText().toString(), etWorkPosition.getText().toString(),
                            Integer.parseInt(etAge.getText().toString()));

                    if (!modeWork.equals(Constants.EDIT_ITEM_STAFF_KEY)) {
                        keyId = myRef.push().getKey();
                    }

                    Map<String, Object> newWorker = model.toMap();
                    HashMap<String, Object>  sendValue = new HashMap<>();
                    sendValue.put(keyId, newWorker);

                    myRef.child("users").child(auth.getUid()).child("waiters").updateChildren(sendValue);
                    getActivity().finish();

                }
            }
        });
    }
}
