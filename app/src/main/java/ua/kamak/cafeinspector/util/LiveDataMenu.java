package ua.kamak.cafeinspector.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import ua.kamak.cafeinspector.model.MenuForOwnerModel;
import ua.kamak.cafeinspector.model.TableForOwnerModel;

public class LiveDataMenu {

    private static LiveDataMenu instance;
    private MutableLiveData<MenuForOwnerModel> liveData = new MutableLiveData<>();

    private LiveDataMenu() {
    }

    public static LiveDataMenu getInstance() {
        if (instance == null) {
            instance = new LiveDataMenu();
        }
        return instance;
    }

    public LiveData<MenuForOwnerModel> getData() {
        return liveData;
    }

    public void setData(MenuForOwnerModel model) {
        liveData.setValue(model);
    }
}
