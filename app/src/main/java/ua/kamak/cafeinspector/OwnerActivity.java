package ua.kamak.cafeinspector;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ua.kamak.cafeinspector.owner_fragment.MenuCafeForOwnerFragment;
import ua.kamak.cafeinspector.owner_fragment.StaffForOwnerFragment;
import ua.kamak.cafeinspector.owner_fragment.StatisticsForOwnerFragment;
import ua.kamak.cafeinspector.owner_fragment.TablesForOwnerFragment;
import ua.kamak.cafeinspector.reg.RegActivity;

public class OwnerActivity extends AppCompatActivity {

    private Fragment statisticsFragment;
    private Fragment staffFragment;
    private Fragment menuCafeFragment;
    private Fragment tablesFragment;
    private FragmentManager fragmentManager;

    private BottomNavigationView bnv;
    private Toolbar toolbar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        auth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();

        statisticsFragment = new StatisticsForOwnerFragment();
        staffFragment = new StaffForOwnerFragment();
        menuCafeFragment = new MenuCafeForOwnerFragment();
        tablesFragment = new TablesForOwnerFragment();

        bnv = findViewById(R.id.main_screen_btn_navigation_menu);
        bnv.setOnNavigationItemSelectedListener(getBottomNavigationListener());

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.main_screen_container, statisticsFragment, StatisticsForOwnerFragment.TAG)
                    .commit();
        }

        initToolbar();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener getBottomNavigationListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_statistics:
                        if (fragmentManager.findFragmentByTag(StatisticsForOwnerFragment.TAG) == null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_screen_container, statisticsFragment, StatisticsForOwnerFragment.TAG)
                                    .commit();
                        }
                        break;
                    case R.id.action_menu_cafe:
                        if (fragmentManager.findFragmentByTag(MenuCafeForOwnerFragment.TAG) == null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_screen_container, menuCafeFragment, MenuCafeForOwnerFragment.TAG)
                                    .commit();
                        }
                        break;
                    case R.id.action_staff:
                        if (fragmentManager.findFragmentByTag(StaffForOwnerFragment.TAG) == null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_screen_container, staffFragment, StaffForOwnerFragment.TAG)
                                    .commit();
                        }
                        break;
                    case R.id.action_tables:
                        if (fragmentManager.findFragmentByTag(TablesForOwnerFragment.TAG) == null) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.main_screen_container, tablesFragment, TablesForOwnerFragment.TAG)
                                    .commit();
                        }
                        break;
                }
                return true;
            }
        };
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.toolbar_title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_sing_out:
                        auth.signOut();
                        startActivity(new Intent(OwnerActivity.this, RegActivity.class));
                        finish();
                        break;
                    case R.id.toolbar_code_owner:
                        clipBordCodeOfOwner(auth.getUid());
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_menu);
    }

    private void clipBordCodeOfOwner(String cod) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("owner_code", cod);
        clipboardManager.setPrimaryClip(data);
        Toast.makeText(this, R.string.code_owners_toast, Toast.LENGTH_LONG).show();
    }
}
