package com.olga.twa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterOrders extends BaseAdapter {
    private LayoutInflater inflater;
    private List<EntityOrder> oders = new ArrayList<EntityOrder>();
    public AdapterOrders(Context context, List<EntityOrder> oders) {
        this.oders = oders;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EntityOrder order = (EntityOrder) getItem(position);
        if (view == null) {
            view = inflater.inflate(R.layout.orderdata, null);
        }
        TextView tablename = (TextView) view.findViewById(R.id.ordertime);
        tablename.setText(order.OrderTime);
        TextView dishname = (TextView) view.findViewById(R.id.dishname);
        dishname.setText(order.DishName);
        TextView orderstatus = (TextView) view.findViewById(R.id.orderstatus);
        orderstatus.setText(order.OrderStatus);

        return view;
    }

    @Override
    public Object getItem(int position) {
        return oders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return oders.size();
    }

    public void setTables(List<EntityOrder> data) {
        oders.addAll(data);
        notifyDataSetChanged();
    }
}