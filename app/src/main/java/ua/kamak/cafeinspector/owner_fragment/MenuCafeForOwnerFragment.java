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
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ua.kamak.cafeinspector.EditActivity;
import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.ViewActivity;
import ua.kamak.cafeinspector.model.MenuForOwnerModel;
import ua.kamak.cafeinspector.model.TableForOwnerModel;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.LiveDataMenu;
import ua.kamak.cafeinspector.util.LiveDataTable;
import ua.kamak.cafeinspector.util.MenuHolder;

public class MenuCafeForOwnerFragment extends Fragment{

    public static final String TAG = "MenuCafeForOwnerFragmentTag";

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseRecyclerAdapter<MenuForOwnerModel, MenuHolder> adapterFirebase;
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference("users").child(auth.getUid()).child("menu");

        myRef.keepSynced(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_cafe_for_owner, container, false);
        return v;
    }

    //  управление элементами фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_owner_menu);
        fab = view.findViewById(R.id.fab_owner_manu);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra(Constants.START_EDIT_ACTIVITY, Constants.ADD_ITEM_MENU_KEY);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        adapterFirebase = new FirebaseRecyclerAdapter<MenuForOwnerModel, MenuHolder>
                (MenuForOwnerModel.class, R.layout.item_menu, MenuHolder.class, myRef) {

            @Override
            protected void populateViewHolder(final MenuHolder viewHolder, final MenuForOwnerModel model, int position) {
                // поменял позицию на позицию адаптера
                viewHolder.getTvDishTitle().setText(adapterFirebase.getRef(viewHolder.getAdapterPosition()).getKey());
                viewHolder.getTvDishPrice().setText(String.valueOf(model.getPrice()));
                switch (model.getCategory()) {
                    case Constants.DISH_GATEGORY_IS_MAIN_DISH:
                        viewHolder.getImgDish().setImageResource(R.drawable.main_dish);
                        break;
                    case Constants.DISH_GATEGORY_IS_DRINK:
                        viewHolder.getImgDish().setImageResource(R.drawable.drinks2);
                        break;
                    case Constants.DISH_GATEGORY_IS_DESSERT:
                        break;
                }

                viewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ViewActivity.class);
                        intent.putExtra(Constants.START_VIEW_ACTIVITY, Constants.SHOW_ITEM_MENU_KEY);

                        intent.putExtra(Constants.INTENT_INFO_ABOUT_NAME_OF_DISH_KEY, adapterFirebase.getRef(viewHolder.getAdapterPosition()).getKey());
                        intent.putExtra(Constants.INTENT_INFO_ABOUT_CATEGORY_OF_DISH_KEY, model.getCategory());
                        intent.putExtra(Constants.INTENT_INFO_ABOUT_PRICE_OF_DISH_KEY, model.getPrice());
                        intent.putExtra(Constants.INTENT_INFO_ABOUT_DESCRIPTION_OF_DISH_KEY, model.getDescription());
                        startActivity(intent);
                    }
                });

                viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                        menu.add(viewHolder.getAdapterPosition(), 1, 0, R.string.edit);
                        menu.add(viewHolder.getAdapterPosition(), 0, 1, R.string.delete);

                    }
                });
            }
        };

        recyclerView.setAdapter(adapterFirebase);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                removeItemMenu(item.getGroupId());
                break;
            case 1:
                updateItemMenu(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void removeItemMenu(int pisition) {
        String key = adapterFirebase.getRef(pisition).getKey();
        myRef.child(key).removeValue();
    }

    private void updateItemMenu(int position) {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra(Constants.START_EDIT_ACTIVITY, Constants.EDIT_ITEM_MENU_KEY);

        MenuForOwnerModel model = adapterFirebase.getItem(position);
        model.setNameOfDish(adapterFirebase.getRef(position).getKey());

        LiveDataMenu liveDataMenu = LiveDataMenu.getInstance();
        liveDataMenu.setData(model);

        startActivity(intent);
    }
}
