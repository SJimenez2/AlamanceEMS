package com.example.sam.navdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TOCFragment extends Fragment implements AdapterView.OnItemClickListener{

    private CustomAdapter mAdapter;

    String section = "";
    boolean toc = true;

    ListView listView;

    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    HashMap<Integer, String> headingLocations = new HashMap<Integer, String>();
    ArrayList<String> protocolWHeadings = new ArrayList<>();

    String[] sectionOrder = { "PI", "UP", "AR", "AC", "AM", "AO",
            "TB", "PC", "PM", "TE", "SC", "SO" };

    String[] sectionTitles = { "Protocol Introduction PI", "Universal Protocols UP",
            "Airway Respiratory Section AR", "Adult Cardiac Section AC", "Adult Medical Section AM",
            "Adult Obstetrical Section AO", "Trauma and Burn Section TB",
            "Pediatric Cardiac Section PC", "Pediatric Medical Section PM",
            "Toxin-Environmental Section TE", "Special Circumstances Section SC",
            "Special Operations SectionSO" };

    ArrayList<String> sectionColors = new ArrayList<String>(Arrays.asList(new String[] {"#0c0c0c",
            "#92d050", "#00afef", "#001f5f", "#50622a", "#6f2f9f", "#ff0000",
            "#538ad3", "#4aacc5", "#ffc000", "#0c0c0c", "#ffc000"}));

    public TOCFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
            protocolName = bundle.getStringArrayList("Names");
            protocolOriginal = bundle.getStringArrayList("Original Names");
            protocolSection = bundle.getStringArrayList("Sections");
            toc = bundle.getBoolean("TOC");

            if(!toc){
                section = bundle.getString("Individual section");
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toc, container, false);

        FrameLayout view = (FrameLayout) rootView.findViewById(R.id.fragment_tocView);
        listView = view.findViewById(R.id.ListView1);

        createListArray();

        mAdapter = new CustomAdapter(this.getContext(), sectionColors, headingLocations);
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
            boolean title = false;

            for (int j = 0; j < sectionTitles.length; j++) {
                if (protocolWHeadings.get(i).equals(sectionTitles[j])) {
                    mAdapter.addSectionHeaderItem(protocolWHeadings.get(i));
                    title = true;
                }
            }

            if(!title){
                mAdapter.addItem(protocolWHeadings.get(i));
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
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment, "PDFFragment");
                fragmentTransaction.commit();
            }
        }
    }
}
