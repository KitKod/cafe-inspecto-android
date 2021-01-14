package ua.kamak.cafeinspector.edit_fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.model.MenuForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.ListMenuSigleton;
import ua.kamak.cafeinspector.util.LiveDataOrderObject;
import ua.kamak.cafeinspector.util.ValidationTextFields;
import ua.kamak.cafeinspector.view_fragments.ViewInfoItemMenu;

public class EditInfoOrderFragment extends Fragment {

    public static final String TAG = "EditInfoOrderFragmentTag";

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private SharedPreferences preferences;
    private String modeWork;
    private String localKeyForEdit;

    private ListMenuSigleton menuSigleton;
    private List<String> itemInListView;
    private List<Map<String, Long>> itemWithPrice;
    private Long sumPrice;

    private ListView listView;
    private AutoCompleteTextView autoChoiceDish;
    private ImageButton btnAddNewItem;
    private Button btnSendToDatabase;
    private ArrayAdapter<String> autoAdapter;
    private TextView tvSumPrice;
    private EditText etNumberTable;
    private TextInputLayout tlTableNumber;
    private TextInputLayout tlTableAutoDish;

    public static EditInfoOrderFragment getInstance(String keyEdit) {
        Bundle args = new Bundle();
        args.putString(Constants.EDIT_ITEM_ORDER_KEY , keyEdit);

        EditInfoOrderFragment fragment = new EditInfoOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        preferences = getActivity().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        menuSigleton = ListMenuSigleton.getInstance();
        itemWithPrice = menuSigleton.getListPrice();

        itemInListView = new ArrayList<>();
        sumPrice = 0L;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_info_order, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listv_edit_item_order);
        autoChoiceDish = view.findViewById(R.id.auto_tv_choice_dish_edit_order);
        btnAddNewItem = view.findViewById(R.id.btn_edit_add_order);
        btnSendToDatabase = view.findViewById(R.id.btn_edit_send_database_order);
        tvSumPrice = view.findViewById(R.id.tv_sum_price_edit_order);
        etNumberTable = view.findViewById(R.id.et_number_table_edit_order);
        tlTableNumber = view.findViewById(R.id.et_layout_number_table_edit_order);
        tlTableAutoDish = view.findViewById(R.id.Input_layout_auto_text);

        modeWork = getArguments().getString(Constants.EDIT_ITEM_ORDER_KEY);
        if (modeWork.equals(Constants.EDIT_ITEM_ORDER_KEY)) {
            LiveData<Map<String, Object>> liveData = LiveDataOrderObject.getInstance().getData();
            liveData.observe(getActivity(), new Observer<Map<String, Object>>() {
                @Override
                public void onChanged(@Nullable Map<String, Object> stringObjectMap) {
                    for (Map.Entry entry : stringObjectMap.entrySet()) {
                        switch (entry.getKey().toString()) {
                            case "keyWaiter":
                                break;
                            case "localKey":
                                localKeyForEdit = entry.getValue().toString();
                                break;
                            case "tableNumber":
                                etNumberTable.setText(entry.getValue().toString());
                                break;
                            case "status":
                                break;
                            case "price":
                                sumPrice = Long.parseLong(entry.getValue().toString());
                                tvSumPrice.setText(entry.getValue().toString());
                                break;
                            default:
                                itemInListView.add(entry.getValue().toString());
                                break;
                        }
                    }
                }
            });
        }

        autoAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, menuSigleton.getListMenu());
        autoChoiceDish.setAdapter(autoAdapter);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                itemInListView);

        listView.setAdapter(arrayAdapter);

        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidationTextFields.checkInput(autoChoiceDish, tlTableAutoDish)) {
                    itemInListView.add(0, autoChoiceDish.getText().toString());
                    for (int i = 0; i < itemWithPrice.size(); i++) {
                        Long temp = itemWithPrice.get(i).get(autoChoiceDish.getText().toString());
                        if (temp != null) {
                            sumPrice += temp;
                            tvSumPrice.setText(sumPrice.toString());
                            break;
                        }
                    }

                    arrayAdapter.notifyDataSetChanged();
                    autoChoiceDish.setText("");
                }
            }
        });

        btnSendToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidationTextFields.checkInput(etNumberTable, tlTableNumber) & itemInListView.size() != 0) {
                    String keyId = myRef.push().getKey();

                    Map<String, Object> newItemOrder = new HashMap<>();

                    for (int i = 0; i < itemInListView.size(); i++) {
                        newItemOrder.put(String.valueOf(i), itemInListView.get(i));
                    }
                    newItemOrder.put("price", sumPrice);
                    newItemOrder.put("keyWaiter", preferences.getString(Constants.CODE_OF_WAITER, null));
                    newItemOrder.put("status", "ordered");
                    newItemOrder.put("tableNumber", etNumberTable.getText().toString());

                    if (modeWork.equals(Constants.ADD_ITEM_ORDER_KEY)) {

                        newItemOrder.put("localKey", keyId);
                        HashMap<String, Object> sendValue = new HashMap<>();
                        sendValue.put(keyId, newItemOrder);

                        myRef.child("users").child(preferences.getString(Constants.CODE_OF_OWNER, null)).child("orders").updateChildren(sendValue);
                    } else {
                        newItemOrder.put("localKey", localKeyForEdit);
                        HashMap<String, Object> sendValue = new HashMap<>();
                        sendValue.put(localKeyForEdit, newItemOrder);
                        myRef.child("users").child(preferences.getString(Constants.CODE_OF_OWNER, null)).child("orders").updateChildren(sendValue);
                    }
                    getActivity().finish();
                }
            }
        });

    }
}
