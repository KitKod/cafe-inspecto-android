package ua.kamak.cafeinspector.model;

import java.util.HashMap;
import java.util.Map;

public class TableForOwnerModel {

    private int numTable;
    private int numberOfSeats;
    private String flag;

    public TableForOwnerModel() {
    }

    public TableForOwnerModel(int numberOfSeats, String flag) {
        this.numberOfSeats = numberOfSeats;
        this.flag = flag;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> itemTableMap = new HashMap<>();
        //itemTableMap.put("numberOfTable", numberOfTable);
        itemTableMap.put("numberOfSeats", numberOfSeats);
        itemTableMap.put("flag", flag);

        return itemTableMap;
    }

    public int getNumTable() {
        return numTable;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
