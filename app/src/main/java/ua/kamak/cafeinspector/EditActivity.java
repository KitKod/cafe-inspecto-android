package ua.kamak.cafeinspector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.kamak.cafeinspector.edit_fragments.EditInfoAboutStaffFragment;
import ua.kamak.cafeinspector.edit_fragments.EditInfoItemMenuFragment;
import ua.kamak.cafeinspector.edit_fragments.EditInfoOrderFragment;
import ua.kamak.cafeinspector.edit_fragments.EditInfoTableFragment;
import ua.kamak.cafeinspector.util.Constants;

public class EditActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        fragmentManager = getSupportFragmentManager();

        key = (String) getIntent().getSerializableExtra(Constants.START_EDIT_ACTIVITY);
        choiceFragment(key);
    }

    private void choiceFragment(String key) {
        switch (key) {
            case Constants.ADD_ITEM_STAFF_KEY:
                Fragment addStaffFragment = EditInfoAboutStaffFragment.getInstance(Constants.ADD_ITEM_STAFF_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoAboutStaffFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, addStaffFragment, EditInfoAboutStaffFragment.TAG)
                            .commit();
                }
                break;
            case Constants.ADD_ITEM_MENU_KEY:
                Fragment addMenuFragment = EditInfoItemMenuFragment.getInstance(Constants.ADD_ITEM_MENU_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoItemMenuFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, addMenuFragment, EditInfoItemMenuFragment.TAG)
                            .commit();
                }
                break;
            case Constants.ADD_ITEM_ORDER_KEY:
                Fragment fragmentAdd = EditInfoOrderFragment.getInstance(Constants.ADD_ITEM_ORDER_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoOrderFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, fragmentAdd, EditInfoOrderFragment.TAG)
                            .commit();
                }
                break;
            case Constants.ADD_ITEM_TABLES_KEY:
                Fragment addTableFragment = EditInfoTableFragment.getInstance(Constants.ADD_ITEM_TABLES_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoTableFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, addTableFragment, EditInfoTableFragment.TAG)
                            .commit();
                }
                break;
            case Constants.EDIT_ITEM_ORDER_KEY:
                Fragment fragmentEdit = EditInfoOrderFragment.getInstance(Constants.EDIT_ITEM_ORDER_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoOrderFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, fragmentEdit, EditInfoOrderFragment.TAG)
                            .commit();
                }
                break;
            case Constants.EDIT_ITEM_TABLES_KEY:
                Fragment editTableFragment = EditInfoTableFragment.getInstance(Constants.EDIT_ITEM_TABLES_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoTableFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, editTableFragment, EditInfoTableFragment.TAG)
                            .commit();
                }
                break;
            case Constants.EDIT_ITEM_MENU_KEY:
                Fragment editMenuFragment = EditInfoItemMenuFragment.getInstance(Constants.EDIT_ITEM_MENU_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoItemMenuFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, editMenuFragment, EditInfoItemMenuFragment.TAG)
                            .commit();
                }
                break;
            case Constants.EDIT_ITEM_STAFF_KEY:
                Fragment editStaffFragment = EditInfoAboutStaffFragment.getInstance(Constants.EDIT_ITEM_STAFF_KEY);
                if (fragmentManager.findFragmentByTag(EditInfoAboutStaffFragment.TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.edit_container, editStaffFragment, EditInfoAboutStaffFragment.TAG)
                            .commit();
                }
                break;
        }
    }
}
