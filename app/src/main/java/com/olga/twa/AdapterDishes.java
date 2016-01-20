package com.olga.twa;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterDishes extends CursorAdapter {
    private final String LOG_TAG = AdapterDishes.class.getSimpleName();
    private LayoutInflater inflater;
    private List<EntityDishes> tables = new ArrayList<EntityDishes>();
    public AdapterDishes(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v(LOG_TAG, "newView");
        return LayoutInflater.from(context).inflate(R.layout.dishesdata, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tableprice = (TextView) view.findViewById(R.id.dishprice);
        TextView tablename = (TextView) view.findViewById(R.id.dishename);
        TextView tabledesc = (TextView) view.findViewById(R.id.dishdescription);
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESPRICE));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESNAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(ContractDishes.Columns.DISHESDESCRIPTION));
        tableprice.setText(String.valueOf(price));
        tablename.setText(name);
        tabledesc.setText(description);
    }

 /*   @Override
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

    public void setTables(Cursor data) {
        tables.addAll(data);
        notifyDataSetChanged();
    }*/
}