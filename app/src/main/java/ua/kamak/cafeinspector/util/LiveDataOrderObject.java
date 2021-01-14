package ua.kamak.cafeinspector.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Map;

public class LiveDataOrderObject {

    private static LiveDataOrderObject instance;

    private LiveDataOrderObject() {
    }

    public static LiveDataOrderObject getInstance() {
        if (instance == null) {
            instance = new LiveDataOrderObject();
        }
        return instance;
    }

    private MutableLiveData<Map<String, Object>> liveData = new MutableLiveData<>();

    public LiveData<Map<String, Object>> getData() {
        return liveData;
    }

    public void setData(Map<String, Object> map) {
        liveData.setValue(map);
    }
}
