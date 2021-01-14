package ua.kamak.cafeinspector.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Map;

import ua.kamak.cafeinspector.model.TableForOwnerModel;

public class LiveDataTable {

    private static LiveDataTable instance;
    private MutableLiveData<TableForOwnerModel> liveData = new MutableLiveData<>();

    private LiveDataTable() {
    }

    public static LiveDataTable getInstance() {
        if (instance == null) {
            instance = new LiveDataTable();
        }
        return instance;
    }

    public LiveData<TableForOwnerModel> getData() {
        return liveData;
    }

    public void setData(TableForOwnerModel model) {
        liveData.setValue(model);
    }
}
