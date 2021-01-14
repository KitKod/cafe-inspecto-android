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

import ua.kamak.cafeinspector.reg.RegActivity;
import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.util.TabsPagerFragmentAdapter;
import ua.kamak.cafeinspector.util.TabsPagerTerminalFragmentAdapter;

public class TerminalActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        preferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);

        tabLayout = findViewById(R.id.tab_layout_terminal);
        viewPager = findViewById(R.id.viewpager_terminal);

        //Поменять на терминал фрагменты
        TabsPagerTerminalFragmentAdapter tabsAdapter = new TabsPagerTerminalFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        initToolbar();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_for_terminal);
        toolbar.setTitle(R.string.toolbar_title_terminal);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_for_terminal_sing_out:
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Constants.TERMINAL_IS_LOGIN, false);
                        editor.remove(Constants.CODE_OF_OWNER);
                        editor.apply();
                        startActivity(new Intent(TerminalActivity.this, RegActivity.class));
                        finish();
                        break;
                    case R.id.toolbar_for_waiter_add_order:
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_menu_for_terminal);
    }
}
