package ua.kamak.cafeinspector.model;

import java.util.HashMap;
import java.util.Map;

public class MenuForOwnerModel {

    private String nameOfDish;
    private int category;
    private float price;
    private String description;

    public MenuForOwnerModel() {
    }

    public MenuForOwnerModel(int category, float price, String description) {
        //this.nameOfDish = nameOfDish;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> itemMenuMap = new HashMap<>();
        itemMenuMap.put("category", category);
        itemMenuMap.put("price", price);
        itemMenuMap.put("description", description);

        return itemMenuMap;
    }

    public String getNameOfDish() {
        return nameOfDish;
    }

    public void setNameOfDish(String nameOfDish) {
        this.nameOfDish = nameOfDish;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
