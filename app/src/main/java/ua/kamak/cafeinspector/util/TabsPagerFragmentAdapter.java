package ua.kamak.cafeinspector.util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ua.kamak.cafeinspector.owner_fragment.MenuCafeForOwnerFragment;
import ua.kamak.cafeinspector.owner_fragment.TablesForOwnerFragment;
import ua.kamak.cafeinspector.waiter_fragment.OrdersWaitersFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<String> tabs;

    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new ArrayList<>();
        tabs.add("Заказы");
        tabs.add("Столы");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OrdersWaitersFragment.getInstance();
            case 1:
                return new TablesForOwnerFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
