package com.olga.twa;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterTables extends BaseAdapter {
    private LayoutInflater inflater;
    private List<TableEntity> tables = new ArrayList<TableEntity>();
    public AdapterTables(Context context, List<TableEntity> tables) {
        this.tables = tables;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TableEntity table = (TableEntity) getItem(position);
        if (view == null) {
            view = inflater.inflate(R.layout.tabledata, null);
        }
/*        TextView tabelId = (TextView) view.findViewById(R.id.tableid);
        tabelId.setText(table.tableid);*/
        TextView tablename = (TextView) view.findViewById(R.id.tablename);
        tablename.setText(table.tablename);
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

    public void setTables(List<TableEntity> data) {
        tables.addAll(data);
        notifyDataSetChanged();
    }
}