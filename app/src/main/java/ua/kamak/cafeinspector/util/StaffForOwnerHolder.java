package ua.kamak.cafeinspector.util;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kamak.cafeinspector.R;

public class StaffForOwnerHolder extends RecyclerView.ViewHolder {

    private TextView textName, textWorkPosition;
    private ImageView photoWorker;
    private ConstraintLayout layout;

    public StaffForOwnerHolder(View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.tv_item_order_numb_const);
        textWorkPosition = itemView.findViewById(R.id.tv_item_staff_position);
        photoWorker = itemView.findViewById(R.id.iv_item_order_status);
        layout = itemView.findViewById(R.id.staff_item_cons_layout);

    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    public void setLayout(ConstraintLayout layout) {
        this.layout = layout;
    }

    public TextView getTextName() {
        return textName;
    }

    public void setTextName(TextView textName) {
        this.textName = textName;
    }

    public TextView getTextWorkPosition() {
        return textWorkPosition;
    }

    public void setTextWorkPosition(TextView textWorkPosition) {
        this.textWorkPosition = textWorkPosition;
    }

    public ImageView getPhotoWorker() {
        return photoWorker;
    }

    public void setPhotoWorker(ImageView photoWorker) {
        this.photoWorker = photoWorker;
    }
}
