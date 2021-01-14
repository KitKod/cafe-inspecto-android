package ua.kamak.cafeinspector.view_fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.model.StaffForOwnerModel;
import ua.kamak.cafeinspector.util.LiveDataStaff;

public class ViewInfoItemStaff extends Fragment {

    public static final String TAG = "ViewInfoItemStaffTag";
    private TextView tvName;
    private TextView tvJob;
    private TextView tvAge;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_info_item_staff, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.view_staff_item_name);
        tvJob = view.findViewById(R.id.view_staff_item_job);
        tvAge = view.findViewById(R.id.view_staff_item_age);

        LiveData<StaffForOwnerModel> liveData = LiveDataStaff.getInstance().getData();
        liveData.observe(getActivity(), new Observer<StaffForOwnerModel>() {
            @Override
            public void onChanged(@Nullable StaffForOwnerModel model) {
                tvName.setText(model.getName());
                tvJob.setText(model.getWorkPosition());
                tvAge.setText(String.valueOf(model.getAge()));
            }
        });
    }
}
