package ua.kamak.cafeinspector.terminal_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.ViewActivity;
import ua.kamak.cafeinspector.model.MenuForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.MenuHolder;
import ua.kamak.cafeinspector.util.OrdersForWaiterAdapter;
import ua.kamak.cafeinspector.waiter_fragment.OrdersWaitersFragment;

public class OrdersTerminalFragment extends Fragment {

    private RecyclerView recyViewListOrders;
    private OrdersForWaiterAdapter adapter;
    private List<Map<String, Object>> dataMap;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private SharedPreferences preferences;

    public static OrdersTerminalFragment getInstance() {
        Bundle args = new Bundle();
        OrdersTerminalFragment fragment = new OrdersTerminalFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(preferences.getString(Constants.CODE_OF_OWNER, null)).child("orders");
        dataMap = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_waiter, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyViewListOrders = view.findViewById(R.id.recycler_waiter_list_order);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayout.VERTICAL);
        recyViewListOrders.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyViewListOrders.getContext(), llm.getOrientation());
        recyViewListOrders.addItemDecoration(dividerItemDecoration);

        adapter = new OrdersForWaiterAdapter(dataMap, getActivity());
        recyViewListOrders.setAdapter(adapter);

        updateList();
    }

    private void updateList() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>(){};
                HashMap<String, Object> order = dataSnapshot.getValue(t);
                String key = dataSnapshot.getKey();
                order.put("localKey", key);

                dataMap.add(order);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>(){};
                HashMap<String, Object> order = dataSnapshot.getValue(t);

                String key = dataSnapshot.getKey();
                int index = getIndexByKey(key);

                dataMap.set(index, order);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>(){};
                HashMap<String, Object> order = dataSnapshot.getValue(t);

                String key = dataSnapshot.getKey();
                int index = getIndexByKey(key);
                dataMap.remove(index);
                adapter.notifyItemRemoved(index);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 3:
                Map<String, Object> updateStatusOrder = dataMap.get(item.getGroupId());
                updateStatusOrder.put("status", "cooked");
                changeOrder(updateStatusOrder);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private int getIndexByKey(String key) {
        int index = -1;
        for (int i = 0; i < dataMap.size(); i++) {
            if (dataMap.get(i).get("localKey").equals(key)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private void changeOrder(Map<String, Object> newOrder) {
        Map<String, Object> newWorker = new HashMap<>();
        newWorker.put(newOrder.get("localKey").toString(), newOrder);

        myRef.updateChildren(newWorker);
    }

}
