package ua.kamak.cafeinspector.util;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ua.kamak.cafeinspector.model.StaffForOwnerModel;

public class StaffViewModel extends ViewModel {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<StaffForOwnerModel> staffList;

    public FirebaseAuth getFirebaseAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public void setMyRef(DatabaseReference myRef) {
        this.myRef = myRef;
    }

    public List<StaffForOwnerModel> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffForOwnerModel> staffList) {
        this.staffList = staffList;
    }
}
