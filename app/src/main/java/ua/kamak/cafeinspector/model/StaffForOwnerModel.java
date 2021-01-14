package ua.kamak.cafeinspector.model;

import java.util.HashMap;
import java.util.Map;

public class StaffForOwnerModel {


    private String name;
    private int age;
    private String workPosition;
    private String key;

    public StaffForOwnerModel() {
    }

    public StaffForOwnerModel(String name, String workPosition, int age) {
        this.name = name;
        this.workPosition = workPosition;
        this.age = age;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> workerMap = new HashMap<>();
        workerMap.put("name", name);
        workerMap.put("age", age);
        workerMap.put("workPosition", workPosition);
        //workerMap.put("key", name);
        return workerMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
