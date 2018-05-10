package com.example.sam.navdrawer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import static java.util.Arrays.asList;

public class CustomAdapter extends BaseAdapter implements Filterable{

    private ArrayList<String> mData;
    private HashMap<Integer, String> headingLocations;
    private TreeSet<Integer> sectionHeader = new TreeSet<>();
    private ArrayList<String> originalData;

    ArrayList<String> sectionTitles;
    ArrayList<String> sectionColors;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private LayoutInflater mInflater;
    private CustomFilter mFilter = new CustomFilter();

    @SuppressLint("UseSparseArrays")
    CustomAdapter(Context context, HashMap<Integer, String> headingLocations1, ArrayList<String> list) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headingLocations = new HashMap<>(headingLocations1);
        mData = list;
        originalData = list;

        sectionTitles = new ArrayList<>(asList(context.getResources().getStringArray(R.array.sectionTitles)));
        sectionColors = new ArrayList<>(asList(context.getResources().getStringArray(R.array.sectionColors)));
    }

    public void addSectionHeaderItem(final int i) {
        sectionHeader.add(i);
        notifyDataSetChanged();
    }

    private void updateHeadingLocations(ArrayList<String> list){
        headingLocations.clear();
        sectionHeader.clear();
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < sectionTitles.size(); j++) {
                if (list.get(i).equals(sectionTitles.get(j))) {
                    sectionHeader.add(i);
                    headingLocations.put(i, sectionColors.get(j));
                }
            }
        }
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

    // Creates different views depending on if they are titles or not
    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int rowType = getItemViewType(position);

        holder = new ViewHolder();

        switch (rowType) {
            case TYPE_ITEM:
                convertView = mInflater.inflate(R.layout.customlayoutlist, null);
                holder.textView = convertView.findViewById(R.id.text);
                break;


            case TYPE_SEPARATOR:
                convertView = mInflater.inflate(R.layout.customlayoutheading, null);
                holder.textView = convertView.findViewById(R.id.textSeparator);
                break;
        }
        if (headingLocations.containsKey(position) && sectionTitles.contains(mData.get(position))) {
            convertView.setBackgroundColor(Color.parseColor(headingLocations.get(position)));
        }
        convertView.setTag(holder);

        holder.textView.setText(mData.get(position));

        return convertView;
    }

    public static class ViewHolder {
        TextView textView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class CustomFilter extends Filter {

        @SuppressWarnings("unchecked")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<String> list = originalData;

            int count = list.size();
            final ArrayList<String> nList = new ArrayList<>(count);

            String filterableString;

            //Checks to see if content has the string searched
            //Keeps all headings regardless of if they have content
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString) && filterableString.contains("-")) {
                    nList.add(filterableString);
                } else if(!filterableString.contains("-")){
                    nList.add(filterableString);
                }
            }

            results.values = nList;
            results.count = nList.size();

            //Shows No Results... when only headings are present
            if (((ArrayList<String>) results.values).size() == 11) {
                nList.clear();
                nList.add(0, "No Results...");
            }

            updateHeadingLocations((ArrayList<String>) results.values);
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
