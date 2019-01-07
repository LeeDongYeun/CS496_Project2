package com.example.q.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<list_item> list;

    private TextView nameView;
    private TextView phoneNumberView;

    public CustomListAdapter(Context context, ArrayList<list_item> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
            this.nameView = (TextView)convertView.findViewById(android.R.id.text1);
            this.phoneNumberView = (TextView)convertView.findViewById(android.R.id.text2);
        }

        this.nameView.setText(list.get(position).getName());
        this.phoneNumberView.setText(list.get(position).getPhoneNumber());

        return convertView;
    }
}
