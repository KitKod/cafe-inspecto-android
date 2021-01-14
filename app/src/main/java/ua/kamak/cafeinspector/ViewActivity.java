package ua.kamak.cafeinspector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.kamak.cafeinspector.util.Constants;
import ua.kamak.cafeinspector.view_fragments.ViewInfoItemMenu;
import ua.kamak.cafeinspector.view_fragments.ViewInfoItemOrder;
import ua.kamak.cafeinspector.view_fragments.ViewInfoItemStaff;

public class ViewActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment viewItemMenuFragment;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        fragmentManager = getSupportFragmentManager();
        key = (String) getIntent().getSerializableExtra(Constants.START_VIEW_ACTIVITY);

        choiceFragment(key);
    }

    private void choiceFragment(String key) {
        switch (key) {
            case Constants.SHOW_ITEM_MENU_KEY:
                String nameOfDish = (String) getIntent().getSerializableExtra(Constants.INTENT_INFO_ABOUT_NAME_OF_DISH_KEY);
                int gategoryOfDish = (int) getIntent().getSerializableExtra(Constants.INTENT_INFO_ABOUT_CATEGORY_OF_DISH_KEY);
                float priceOfDish = (float) getIntent().getSerializableExtra(Constants.INTENT_INFO_ABOUT_PRICE_OF_DISH_KEY);
                String descrOfDish = (String) getIntent().getSerializableExtra(Constants.INTENT_INFO_ABOUT_DESCRIPTION_OF_DISH_KEY);
                viewItemMenuFragment = ViewInfoItemMenu.getInstance(nameOfDish, gategoryOfDish, priceOfDish, descrOfDish);

                if (fragmentManager.findFragmentByTag(ViewInfoItemMenu.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.view_container, viewItemMenuFragment, ViewInfoItemMenu.TAG)
                            .commit();
                }
                break;
            case Constants.SHOW_ITEM_ORDER_KEY:
                Fragment viewOrderFragment = ViewInfoItemOrder.getInstance();
                if (fragmentManager.findFragmentByTag(ViewInfoItemOrder.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.view_container, viewOrderFragment, ViewInfoItemOrder.TAG)
                            .commit();
                }
                break;
            case Constants.SHOW_ITEM_STAFF_KEY:
                Fragment viewStaffFragment = new ViewInfoItemStaff();
                if (fragmentManager.findFragmentByTag(ViewInfoItemStaff.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.view_container, viewStaffFragment, ViewInfoItemStaff.TAG)
                            .commit();
                }
                break;
        }
    }
}
