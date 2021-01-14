package ua.kamak.cafeinspector.util;

import java.util.List;
import java.util.Map;

public class ListMenuSigleton {

    private List<String> listMenu;
    private List<Map<String, Long>> listPrice;
    private static ListMenuSigleton instance;

    private ListMenuSigleton() {

    }

    public static ListMenuSigleton getInstance() {
        if (instance == null) {
            instance = new ListMenuSigleton();
        }
        return instance;
    }

    public List<String> getListMenu() {
        return listMenu;
    }

    public void setListMenu(List<String> listMenu) {
        this.listMenu = listMenu;
    }

    public List<Map<String, Long>> getListPrice() {
        return listPrice;
    }

    public void setListPrice(List<Map<String, Long>> listPrice) {
        this.listPrice = listPrice;
    }
}
