package ua.kamak.cafeinspector.view_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.util.Constants;

public class ViewInfoItemMenu extends Fragment {

    private ImageView imgOfDish;
    private TextView tvNameOfDish;
    private TextView tvCategoryOfDish;
    private TextView tvPriceOfDish;
    private TextView tvDescriptionOfDish;

    public static final String TAG = "ViewInfoItemMenuTag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_info_item_menu, container, false);
        return v;
    }

    //  управление элементами фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgOfDish = view.findViewById(R.id.img_item_menu_view);
        tvNameOfDish = view.findViewById(R.id.tv_name_of_dish_item_menu_view);
        tvCategoryOfDish = view.findViewById(R.id.tv_category_item_menu_view);
        tvPriceOfDish = view.findViewById(R.id.tv_price_item_menu_view);
        tvDescriptionOfDish = view.findViewById(R.id.tv_description_item_menu_view);

        String[] categories = getResources().getStringArray(R.array.category_of_dishs);
        switch ((int) getArguments().getSerializable(Constants.INTENT_INFO_ABOUT_CATEGORY_OF_DISH_KEY)) {
            case Constants.DISH_GATEGORY_IS_MAIN_DISH:
                imgOfDish.setImageResource(R.drawable.main_dish);
                tvCategoryOfDish.setText(categories[Constants.DISH_GATEGORY_IS_MAIN_DISH]);
                break;
            case Constants.DISH_GATEGORY_IS_DRINK:
                imgOfDish.setImageResource(R.drawable.drinks2);
                tvCategoryOfDish.setText(categories[Constants.DISH_GATEGORY_IS_DRINK]);
                break;
            case Constants.DISH_GATEGORY_IS_DESSERT:
                break;
        }

        tvNameOfDish.setText((String) getArguments().getSerializable(Constants.INTENT_INFO_ABOUT_NAME_OF_DISH_KEY));
        tvPriceOfDish.setText(String.valueOf((float) getArguments()
                .getSerializable(Constants.INTENT_INFO_ABOUT_PRICE_OF_DISH_KEY)));
        tvDescriptionOfDish.setText((String) getArguments()
                .getSerializable(Constants.INTENT_INFO_ABOUT_DESCRIPTION_OF_DISH_KEY));
    }

    public static ViewInfoItemMenu getInstance(String nameOfdish, int category,
                                               float price, String description) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.INTENT_INFO_ABOUT_NAME_OF_DISH_KEY, nameOfdish);
        args.putSerializable(Constants.INTENT_INFO_ABOUT_CATEGORY_OF_DISH_KEY, category);
        args.putSerializable(Constants.INTENT_INFO_ABOUT_PRICE_OF_DISH_KEY, price);
        args.putSerializable(Constants.INTENT_INFO_ABOUT_DESCRIPTION_OF_DISH_KEY, description);

        ViewInfoItemMenu fragment = new ViewInfoItemMenu();
        fragment.setArguments(args);
        return fragment;
    }
}
