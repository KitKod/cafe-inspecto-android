package ua.kamak.cafeinspector.util;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kamak.cafeinspector.R;

public class TableHolder extends RecyclerView.ViewHolder{

    private CardView cardView;
    private TextView tvNumberOfTable;
    private TextView tvNumberOfSeats;
    private TextView tvStatus;

    public TableHolder(View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.table_cardview_item);
        tvNumberOfTable = itemView.findViewById(R.id.table_title_for_owner_number_table);
        tvNumberOfSeats = itemView.findViewById(R.id.table_seats_for_owner);
        tvStatus = itemView.findViewById(R.id.table_status_for_owner);
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public TextView getTvNumberOfTable() {
        return tvNumberOfTable;
    }

    public void setTvNumberOfTable(TextView tvNumberOfTable) {
        this.tvNumberOfTable = tvNumberOfTable;
    }

    public TextView getTvNumberOfSeats() {
        return tvNumberOfSeats;
    }

    public void setTvNumberOfSeats(TextView tvNumberOfSeats) {
        this.tvNumberOfSeats = tvNumberOfSeats;
    }

    public TextView getTvStatus() {
        return tvStatus;
    }

    public void setTvStatus(TextView tvStatus) {
        this.tvStatus = tvStatus;
    }
}
