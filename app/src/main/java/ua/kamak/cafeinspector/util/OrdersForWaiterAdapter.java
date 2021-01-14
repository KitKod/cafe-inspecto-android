package ua.kamak.cafeinspector.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ua.kamak.cafeinspector.R;
import ua.kamak.cafeinspector.ViewActivity;

public class OrdersForWaiterAdapter extends RecyclerView.Adapter<OrdersForWaiterAdapter.OrdersForWaiterHolder> {

    private List<Map<String, Object>> dataList;
    private SharedPreferences preferences;
    private Context context;
    private int myPos;

    public OrdersForWaiterAdapter(List<Map<String, Object>> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        preferences = context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OrdersForWaiterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersForWaiterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final OrdersForWaiterHolder holder, int position) {
        holder.tvNumberOrder.setText(dataList.get(position).get("tableNumber").toString());
        holder.tvPriceOrder.setText(dataList.get(position).get("price").toString());
        final String status = dataList.get(position).get("status").toString();
        switch (status) {
            case "ordered":
                holder.imgStatusOrder.setImageResource(R.drawable.ic_ordered_status);
                break;
            case "cooked":
                holder.imgStatusOrder.setImageResource(R.drawable.ic_cooked_status);
                break;
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewActivity.class);

                LiveDataOrderObject object = LiveDataOrderObject.getInstance();
                object.setData(dataList.get(holder.getAdapterPosition()));

                intent.putExtra(Constants.START_VIEW_ACTIVITY, Constants.SHOW_ITEM_ORDER_KEY);

                context.startActivity(intent);
            }
        });

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (preferences.getBoolean(Constants.WAITER_IS_LOGIN, false)) {
                    if (status.equals("cooked")) {
                        menu.add(holder.getAdapterPosition(), 2, 0, R.string.ready);
                    } else {
                        menu.add(holder.getAdapterPosition(), 12, 2, R.string.delete);
                        menu.add(holder.getAdapterPosition(), 1, 1, R.string.edit);
                    }

                } else if (preferences.getBoolean(Constants.TERMINAL_IS_LOGIN, false)) {
                    if (status.equals("ordered")) {
                        menu.add(holder.getAdapterPosition(), 3, 0, R.string.cooked);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class OrdersForWaiterHolder extends RecyclerView.ViewHolder {

        ImageView imgStatusOrder;
        TextView tvNumberOrder;
        TextView tvPriceOrder;
        ConstraintLayout constraintLayout;

        public OrdersForWaiterHolder(View itemView) {
            super(itemView);

            imgStatusOrder = itemView.findViewById(R.id.iv_item_order_status);
            tvNumberOrder = itemView.findViewById(R.id.tv_item_order_number);
            tvPriceOrder = itemView.findViewById(R.id.tv_item_order_price);
            constraintLayout = itemView.findViewById(R.id.const_layout_item_order);
        }
    }
}
