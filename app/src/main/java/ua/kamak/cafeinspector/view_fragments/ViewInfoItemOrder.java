package ua.kamak.cafeinspector.view_fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.util.LiveDataOrderObject;

public class ViewInfoItemOrder extends Fragment {

    public static final String TAG = "ViewInfoItemOrderTag";

    private TextView tvSum;
    private TextView tvTableNumber;
    private ListView listOrder;
    private List<String> listDish;

    //String keyEdit
    public static ViewInfoItemOrder getInstance() {
        Bundle args = new Bundle();
        //args.putString(Constants.SHOW_ITEM_ORDER_KEY, keyEdit);

        ViewInfoItemOrder fragment = new ViewInfoItemOrder();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDish = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_info_item_order, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listOrder = view.findViewById(R.id.list_view_item_order_view);
        tvTableNumber = view.findViewById(R.id.tv_table_number_item_order_view);
        tvSum = view.findViewById(R.id.tv_suma_item_order_view);

        LiveData<Map<String, Object>> liveData = LiveDataOrderObject.getInstance().getData();
        liveData.observe(getActivity(), new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> stringObjectMap) {
                for (Map.Entry entry : stringObjectMap.entrySet()) {
                    switch (entry.getKey().toString()) {
                        case "keyWaiter":
                            break;
                        case "localKey":
                            break;
                        case "tableNumber":
                            tvTableNumber.setText(entry.getValue().toString());
                            break;
                        case "status":
                            break;
                        case "price":
                            tvSum.setText(entry.getValue().toString());
                            break;
                        default:
                            listDish.add(entry.getValue().toString());
                            break;
                    }
                }
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                listDish
        );

        listOrder.setAdapter(arrayAdapter);
    }
}
