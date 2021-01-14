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
import ua.kamak.cafeinspector.model.MenuForOwnerModel;
import ua.kamak.cafeinspector.model.TableForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.LiveDataMenu;
import ua.kamak.cafeinspector.util.LiveDataTable;
import ua.kamak.cafeinspector.util.ValidationTextFields;

public class EditInfoItemMenuFragment extends Fragment {

    public static final String TAG = "EditInfoItemMenuFragmentTag";

    private EditText etNameOfDish;
    private EditText etPriceOfDish;
    private EditText etDescrOfDish;
    private Spinner spCategoryOfDish;
    private TextInputLayout ilNameOfDish;
    private Button btnEditItem;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth auth;

    private TextInputLayout layoutPrice;
    private TextInputLayout layoutDescription;

    private String modeWork;

    public static EditInfoItemMenuFragment getInstance(String keyEdit) {
        Bundle args = new Bundle();
        args.putString(Constants.EDIT_ITEM_MENU_KEY , keyEdit);

        EditInfoItemMenuFragment fragment = new EditInfoItemMenuFragment();
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
        View v = inflater.inflate(R.layout.fragment_edit_info_item_menu, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNameOfDish = view.findViewById(R.id.etEditNameOfDish);
        etPriceOfDish = view.findViewById(R.id.etEditPriceOfDish);
        etDescrOfDish = view.findViewById(R.id.etEditDescrOfDish);
        spCategoryOfDish = view.findViewById(R.id.spinnerEditCategoryOfDish);
        btnEditItem = view.findViewById(R.id.btnEditItemMenu);
        ilNameOfDish = view.findViewById(R.id.textInLayoutNameOfDish);
        layoutPrice = view.findViewById(R.id.textInLayoutPriceOfDish);
        layoutDescription = view.findViewById(R.id.textInLayoutDescrOfDish);

        //  настройка спеннера для выбора роли.
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.category_of_dishs,
                android.R.layout.simple_spinner_item
        );
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoryOfDish.setAdapter(spAdapter);

        modeWork = getArguments().getString(Constants.EDIT_ITEM_MENU_KEY);

        if (modeWork.equals(Constants.EDIT_ITEM_MENU_KEY)) {
            LiveData<MenuForOwnerModel> liveData = LiveDataMenu.getInstance().getData();
            liveData.observe(getActivity(), new Observer<MenuForOwnerModel>() {
                @Override
                public void onChanged(@Nullable MenuForOwnerModel menuForOwnerModel) {
                    ilNameOfDish.setVisibility(View.GONE);
                    etNameOfDish.setText(menuForOwnerModel.getNameOfDish());
                    etPriceOfDish.setText(String.valueOf(menuForOwnerModel.getPrice()));
                    etDescrOfDish.setText(menuForOwnerModel.getDescription());
                    switch (menuForOwnerModel.getCategory()) {
                        case Constants.DISH_GATEGORY_IS_MAIN_DISH:
                            spCategoryOfDish.setSelection(0);
                            break;
                        case Constants.DISH_GATEGORY_IS_DRINK:
                            spCategoryOfDish.setSelection(1);
                            break;
                        case Constants.DISH_GATEGORY_IS_DESSERT:
                            spCategoryOfDish.setSelection(2);
                            break;
                    }
                }
            });
        }

        btnEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidationTextFields.checkInput(etNameOfDish, ilNameOfDish)
                        & ValidationTextFields.checkInput(etPriceOfDish, layoutPrice)
                        & ValidationTextFields.checkInput(etDescrOfDish, layoutDescription)) {

                    MenuForOwnerModel model = new MenuForOwnerModel(spCategoryOfDish.getSelectedItemPosition(),
                            Float.parseFloat(etPriceOfDish.getText().toString()), etDescrOfDish.getText().toString());

                    String keyId = etNameOfDish.getText().toString();
                    Map<String, Object> newItemMenu = model.toMap();
                    HashMap<String, Object> sendValue = new HashMap<>();
                    sendValue.put(keyId, newItemMenu);

                    myRef.child("users").child(auth.getUid()).child("menu").updateChildren(sendValue);
                    getActivity().finish();
                }

            }
        });

    }

}
