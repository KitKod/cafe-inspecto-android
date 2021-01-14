package ua.kamak.cafeinspector.owner_fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ua.kamak.cafeinspector.EditActivity;
import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.ViewActivity;
import ua.kamak.cafeinspector.model.StaffForOwnerModel;
import ua.kamak.cafeinspector.model.TableForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.LiveDataStaff;
import ua.kamak.cafeinspector.util.LiveDataTable;
import ua.kamak.cafeinspector.util.StaffForOwnerHolder;

public class StaffForOwnerFragment extends Fragment {

    public static final String TAG = "StaffForOwnerFragmentTag";

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseRecyclerAdapter<StaffForOwnerModel, StaffForOwnerHolder> adapterFirebase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(auth.getUid()).child("waiters");
        myRef.keepSynced(true);//!!!!!
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterFirebase = new FirebaseRecyclerAdapter<StaffForOwnerModel, StaffForOwnerHolder>
                (StaffForOwnerModel.class, R.layout.item_staff, StaffForOwnerHolder.class, myRef) {
            @Override
            protected void populateViewHolder(final StaffForOwnerHolder viewHolder, StaffForOwnerModel model, int position) {
                viewHolder.getTextName().setText(model.getName());
                viewHolder.getTextWorkPosition().setText(model.getWorkPosition());

                viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(viewHolder.getAdapterPosition(), 0, 2, R.string.delete);
                        menu.add(viewHolder.getAdapterPosition(), 1, 1, R.string.code_workers_menu);
                        menu.add(viewHolder.getAdapterPosition(), 2, 0, R.string.edit);
                    }
                });

                viewHolder.getLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ViewActivity.class);
                        intent.putExtra(Constants.START_VIEW_ACTIVITY, Constants.SHOW_ITEM_STAFF_KEY);

                        StaffForOwnerModel model = adapterFirebase.getItem(viewHolder.getAdapterPosition());
                        LiveDataStaff liveDataStaff = LiveDataStaff.getInstance();
                        liveDataStaff.setData(model);

                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapterFirebase);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_staff_for_owner, container, false);
        return v;
    }

    //  управление элементами фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.fab_staff_owner);
        recyclerView = view.findViewById(R.id.recycler_owner_staff);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra(Constants.START_EDIT_ACTIVITY, Constants.ADD_ITEM_STAFF_KEY);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                removeWorker(item.getGroupId());
                break;
            case 1:
                clipBordCodeOfWaiter(adapterFirebase.getRef(item.getGroupId()).getKey());
                break;
            case 2:
                changeWorker(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    //  удаление сотрудника из списка
    private void removeWorker(int pisition) {
        String key = adapterFirebase.getRef(pisition).getKey();
        myRef.child(key).removeValue();
    }


    private void changeWorker(int position) {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra(Constants.START_EDIT_ACTIVITY, Constants.EDIT_ITEM_STAFF_KEY);

        StaffForOwnerModel model = adapterFirebase.getItem(position);
        model.setKey(adapterFirebase.getRef(position).getKey());
        LiveDataStaff liveDataStaff = LiveDataStaff.getInstance();
        liveDataStaff.setData(model);

        startActivity(intent);
    }

    private void clipBordCodeOfWaiter(String code) {
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("waiter_cod", code);
        clipboardManager.setPrimaryClip(data);
        Toast.makeText(getContext(), getString(R.string.code_workers_toast), Toast.LENGTH_LONG).show();
    }
}
