package ua.kamak.cafeinspector.util;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kamak.cafeinspector.R;

public class MenuHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private ImageView imgDish;
    private TextView tvDishTitle;
    private TextView tvDishPrice;

    public MenuHolder(View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.dish_cardview_item);
        imgDish = itemView.findViewById(R.id.dish_image_for_owner);
        tvDishTitle = itemView.findViewById(R.id.dish_title_for_owner);
        tvDishPrice = itemView.findViewById(R.id.dish_price_for_owner);
    }

    public ImageView getImgDish() {
        return imgDish;
    }

    public void setImgDish(ImageView imgDish) {
        this.imgDish = imgDish;
    }

    public TextView getTvDishTitle() {
        return tvDishTitle;
    }

    public void setTvDishTitle(TextView tvDishTitle) {
        this.tvDishTitle = tvDishTitle;
    }

    public TextView getTvDishPrice() {
        return tvDishPrice;
    }

    public void setTvDishPrice(TextView tvDishPrice) {
        this.tvDishPrice = tvDishPrice;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }
}
