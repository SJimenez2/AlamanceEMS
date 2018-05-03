package com.example.sam.navdrawer;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

public class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private HashMap<Integer, String> headingLocations;
    private ArrayList<String> headingColors;
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public CustomAdapter(Context context, ArrayList<String> colors, HashMap<Integer, String> headingLocations1) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headingColors = new ArrayList<>(colors);
        headingLocations = new HashMap<>(headingLocations1);
    }

    public void addItem (final String item){
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

            holder = new ViewHolder();

                switch (rowType) {
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.customlayoutlist, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.text);
                        break;


                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.customlayoutheading, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                        break;
                }
                if (headingLocations.containsKey(position)) {
                    convertView.setBackgroundColor(Color.parseColor(headingLocations.get(position)));
                }
                convertView.setTag(holder);

            holder.textView.setText(mData.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
