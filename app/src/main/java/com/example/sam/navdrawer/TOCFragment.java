package com.example.sam.navdrawer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static java.util.Arrays.asList;

public class TOCFragment extends Fragment implements AdapterView.OnItemClickListener{

    boolean toc = true;
    CustomAdapter mAdapter;
    String section = "";
    ListView listView;

    String[] sectionOrder ;
    String[] sectionTitles;
    ArrayList<String> sectionColors;

    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    ArrayList<String> protocolWHeadings = new ArrayList<>();

    @SuppressLint("UseSparseArrays")
    HashMap<Integer, String> headingLocations = new HashMap<>();

    public TOCFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        protocolName = bundle.getStringArrayList("Names");
            protocolOriginal = bundle.getStringArrayList("Original Names");
            protocolSection = bundle.getStringArrayList("Sections");
            toc = bundle.getBoolean("TOC");

            if(!toc){
                section = bundle.getString("Individual section");
            }

        sectionOrder = getResources().getStringArray(R.array.sectionOrder);
        sectionTitles = getResources().getStringArray(R.array.sectionTitles);
        sectionColors = new ArrayList<>(asList(getResources().getStringArray(R.array.sectionColors)));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toc, container, false);

        FrameLayout view = rootView.findViewById(R.id.fragment_tocView);
        listView = view.findViewById(R.id.ListView1);

        createListArray();

        mAdapter = new CustomAdapter(Objects.requireNonNull(this.getContext()), headingLocations, protocolWHeadings);
        createList();

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        return rootView;
    }

    private void createListArray(){

        if(toc){
            for(int i = 0; i < sectionTitles.length; i++){
                protocolWHeadings.add(sectionTitles[i]);
                headingLocations.put((protocolWHeadings.size()-1), sectionColors.get(i));
                for (int j = 0; j < protocolName.size(); j++){
                        if (protocolSection.get(j).equals(sectionOrder[i])) {
                            protocolWHeadings.add(protocolName.get(j));
                        }
                }
            }

        } else {
            for (int i = 0; i < sectionOrder.length; i++) {
                if(sectionOrder[i].equals(section)){
                    protocolWHeadings.add(sectionTitles[i]);
                    headingLocations.put(protocolWHeadings.size()-1, sectionColors.get(i));
                }
            }
            for (int j = 0; j < protocolName.size(); j++) {
                if (protocolSection.get(j).equals(section)) {
                    protocolWHeadings.add(protocolName.get(j));
                }
            }
        }
    }

    private void createList(){
        for (int i = 0; i < protocolWHeadings.size(); i++){

            for (String sectionTitle : sectionTitles) {
                if (protocolWHeadings.get(i).equals(sectionTitle)) {
                    mAdapter.addSectionHeaderItem(i);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < protocolName.size(); i++){
            if(protocolName.get(i).equals(listView.getItemAtPosition(position))){
                PDFFragment fragment = new PDFFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Name of PDF", protocolOriginal.get(i));
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment, "PDFFragment");
                fragmentTransaction.commit();
            }
        }
    }
}
