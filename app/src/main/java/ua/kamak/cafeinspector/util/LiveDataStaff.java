package ua.kamak.cafeinspector.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import ua.kamak.cafeinspector.model.MenuForOwnerModel;
import ua.kamak.cafeinspector.model.StaffForOwnerModel;

public class LiveDataStaff {

    private static LiveDataStaff instance;
    private MutableLiveData<StaffForOwnerModel> liveData = new MutableLiveData<>();

    private LiveDataStaff() {
    }

    public static LiveDataStaff getInstance() {
        if (instance == null) {
            instance = new LiveDataStaff();
        }
        return instance;
    }

    public LiveData<StaffForOwnerModel> getData() {
        return liveData;
    }

    public void setData(StaffForOwnerModel model) {
        liveData.setValue(model);
    }
}
