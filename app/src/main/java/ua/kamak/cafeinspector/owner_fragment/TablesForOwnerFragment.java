package ua.kamak.cafeinspector.owner_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ua.kamak.cafeinspector.EditActivity;
import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.model.TableForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.LiveDataTable;
import ua.kamak.cafeinspector.util.TableHolder;

public class TablesForOwnerFragment extends Fragment {

    public static final String TAG = "TablesForOwnerFragmentTag";

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseRecyclerAdapter<TableForOwnerModel, TableHolder> adapterFirebase;
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        if (!preferences.getString(Constants.CODE_OF_OWNER, "owner").equals("owner")) {
            myRef = database.getReference("users").child(preferences.getString(Constants.CODE_OF_OWNER, "owner")).child("listtables");
        } else {
            myRef = database.getReference("users").child(auth.getUid()).child("listtables");
        }
        myRef.keepSynced(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tables_for_owner, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_owner_tables);
        fab = view.findViewById(R.id.fab_owner_tables);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        if (!preferences.getString(Constants.CODE_OF_OWNER, "owner").equals("owner")) {
            fab.setVisibility(View.INVISIBLE);
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditActivity.class);
                    intent.putExtra(Constants.START_EDIT_ACTIVITY, Constants.ADD_ITEM_TABLES_KEY);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterFirebase = new FirebaseRecyclerAdapter<TableForOwnerModel, TableHolder>
                (TableForOwnerModel.class, R.layout.item_table, TableHolder.class, myRef) {

            @Override
            protected void populateViewHolder(final TableHolder viewHolder, TableForOwnerModel model, int position) {
                viewHolder.getTvNumberOfTable().setText(adapterFirebase.getRef(viewHolder.getAdapterPosition()).getKey());
                viewHolder.getTvNumberOfSeats().setText(String.valueOf(model.getNumberOfSeats()));
                String[] statusName = getResources().getStringArray(R.array.status_of_table);
                switch (model.getFlag()) {
                    case Constants.TABLE_STATUS_IS_FREE:
                        viewHolder.getCardView().setCardBackgroundColor(
                                getResources().getColor(R.color.tableStatusFreeBackgraund)
                        );
                        viewHolder.getTvStatus().setText(statusName[0]);
                        break;
                    case Constants.TABLE_STATUS_IS_BUSY:
                        viewHolder.getCardView().setCardBackgroundColor(
                                getResources().getColor(R.color.tableStatusBusyBackgraund)
                        );
                        viewHolder.getTvStatus().setText(statusName[1]);
                        break;
                    case Constants.TABLE_STATUS_IS_RESERVED:
                        viewHolder.getCardView().setCardBackgroundColor(
                                getResources().getColor(R.color.tableStatusReservedBackgraund)
                        );
                        viewHolder.getTvStatus().setText(statusName[2]);
                        break;
                }

                viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        if (preferences.getBoolean(Constants.WAITER_IS_LOGIN, false)) {
                            menu.add(viewHolder.getAdapterPosition(), 5, 3, R.string.table_menu_status_free);
                            menu.add(viewHolder.getAdapterPosition(), 6, 4, R.string.table_menu_status_busy);
                            menu.add(viewHolder.getAdapterPosition(), 7, 5, R.string.table_menu_status_reserved);
                        } else if (!preferences.getBoolean(Constants.TERMINAL_IS_LOGIN, false)) {
                            // терминал не имеет прав изменять заначения
                            menu.add(viewHolder.getAdapterPosition(), 0, 0, R.string.edit);
                            menu.add(viewHolder.getAdapterPosition(), 1, 1, R.string.delete);
                        }
                    }
                });

            }
        };
        recyclerView.setAdapter(adapterFirebase);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                updateItemTable(item.getGroupId());
                break;
            case 1:
                removeItemTable(item.getGroupId());
                break;
            case 5:
                updateStatusTable(item.getGroupId(), Constants.TABLE_STATUS_IS_FREE);
                break;
            case 6:
                updateStatusTable(item.getGroupId(), Constants.TABLE_STATUS_IS_BUSY);
                break;
            case 7:
                updateStatusTable(item.getGroupId(), Constants.TABLE_STATUS_IS_RESERVED);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void removeItemTable(int position) {
        String key = adapterFirebase.getRef(position).getKey();
        myRef.child(key).removeValue();
    }

    private void updateItemTable(int position) {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra(Constants.START_EDIT_ACTIVITY, Constants.EDIT_ITEM_TABLES_KEY);

        TableForOwnerModel model = adapterFirebase.getItem(position);
        model.setNumTable(Integer.parseInt(adapterFirebase.getRef(position).getKey()));
        LiveDataTable liveDataTable = LiveDataTable.getInstance();
        liveDataTable.setData(model);

        startActivity(intent);
    }

    private void updateStatusTable(int position, String status) {
        TableForOwnerModel model = adapterFirebase.getItem(position);
        switch (status) {
            case Constants.TABLE_STATUS_IS_FREE:
                model.setFlag(Constants.TABLE_STATUS_IS_FREE);
                break;
            case Constants.TABLE_STATUS_IS_BUSY:
                model.setFlag(Constants.TABLE_STATUS_IS_BUSY);
                break;
            case Constants.TABLE_STATUS_IS_RESERVED:
                model.setFlag(Constants.TABLE_STATUS_IS_RESERVED);
                break;
        }
        Map<String, Object> newItemTable = model.toMap();
        HashMap<String, Object> sendValue = new HashMap<>();
        sendValue.put(adapterFirebase.getRef(position).getKey(), newItemTable);

        myRef.updateChildren(sendValue);
    }

}
