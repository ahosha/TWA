package com.olga.twa;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterDishes extends BaseAdapter {
    private LayoutInflater inflater;
    private List<EntityDishes> tables = new ArrayList<EntityDishes>();
    public AdapterDishes(Context context, List<EntityDishes> tables) {
        this.tables = tables;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EntityDishes dish = (EntityDishes) getItem(position);
        if (view == null) {
            view = inflater.inflate(R.layout.dishesdata, null);
        }
        TextView tabelId = (TextView) view.findViewById(R.id.dishename);
        tabelId.setText(dish.Name);
        TextView tablename = (TextView) view.findViewById(R.id.dishprice);
        tablename.setText(dish.Price);
        TextView tabledesc = (TextView) view.findViewById(R.id.dishdescription);
        tabledesc.setText(dish.Description);
        return view;
    }

    @Override
    public Object getItem(int position) {
        return tables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return tables.size();
    }

    public void setTables(List<EntityDishes> data) {
        tables.addAll(data);
        notifyDataSetChanged();
    }
}