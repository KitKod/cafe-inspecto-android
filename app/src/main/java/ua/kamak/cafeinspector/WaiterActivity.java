package ua.kamak.cafeinspector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.kamak.cafeinspector.reg.RegActivity;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.ListMenuSigleton;
import ua.kamak.cafeinspector.util.TabsPagerFragmentAdapter;

public class WaiterActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private SharedPreferences preferences;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);
        preferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(preferences.getString(Constants.CODE_OF_OWNER, null)).child("menu");

        tabLayout = findViewById(R.id.tab_layout_waiter);
        viewPager = findViewById(R.id.viewpager_waiter);

        TabsPagerFragmentAdapter tabsAdapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        initToolbar();
        iniMenu();
    }

    private void iniMenu() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                List<Map<String, Long>> listPrice = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, Long> dishPrice = new HashMap<>();

                    GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>(){};
                    HashMap<String, Object> map = ds.getValue(t);
                    dishPrice.put(ds.getKey(), Long.parseLong(map.get("price").toString()));

                    list.add(ds.getKey());
                    listPrice.add(dishPrice);
                }
                ListMenuSigleton menuSigleton = ListMenuSigleton.getInstance();
                menuSigleton.setListMenu(list);
                menuSigleton.setListPrice(listPrice);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_for_waiter);
        toolbar.setTitle(R.string.toolbar_title_waiter);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_for_waiter_sing_out:
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Constants.WAITER_IS_LOGIN, false);
                        editor.remove(Constants.CODE_OF_OWNER);
                        editor.remove(Constants.CODE_OF_WAITER);
                        editor.apply();
                        startActivity(new Intent(WaiterActivity.this, RegActivity.class));
                        finish();
                        break;
                    case R.id.toolbar_for_waiter_add_order:
                        Intent intent = new Intent(WaiterActivity.this, EditActivity.class);
                        intent.putExtra(Constants.START_EDIT_ACTIVITY, Constants.ADD_ITEM_ORDER_KEY);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_menu_for_waiter);
    }
}
