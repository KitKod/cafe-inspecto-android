package ua.kamak.cafeinspector.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ua.kamak.cafeinspector.owner_fragment.TablesForOwnerFragment;
import ua.kamak.cafeinspector.terminal_fragment.OrdersTerminalFragment;
import ua.kamak.cafeinspector.waiter_fragment.TablesWaitersFragment;

public class TabsPagerTerminalFragmentAdapter extends TabsPagerFragmentAdapter {

    public TabsPagerTerminalFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OrdersTerminalFragment.getInstance();
            case 1:
                return new TablesForOwnerFragment();
        }
        return null;
    }
}
