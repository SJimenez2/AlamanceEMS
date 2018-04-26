package com.example.sam.navdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TOCFragment extends Fragment {

    String pdfName = "";

    private CustomAdapter mAdapter;

    String[] sectionColor = {"#0c0c0c", "#92d050", "#00afef", "#001f5f", "#50622a", "#6f2f9f", "#ff0000",
            "#538ad3", "#4aacc5", "#ffc000", "#0c0c0c", "#ffc000"};

    ArrayList<String> colorArray = new ArrayList<String>(Arrays.asList(sectionColor));

    FrameLayout view;
    ListView listView;
    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    //ArrayList<Integer> headingLocations = new ArrayList<>();
    HashMap<Integer, String> headingLocations = new HashMap<Integer, String>();

    ArrayList<String> protocolWHeadings = new ArrayList<>();

    String[] sectionOrder = { "PI", "UP", "AR", "AC", "AM", "AO", "TB", "PC", "PM", "TE", "SC", "SO" };
    String[] sectionTitles = { "Protocol Introduction PI", "Universal Protocols UP", "Airway Respiratory Section AR",
            "Adult Cardiac Section AC", "Adult Medical Section AM", "Adult Obstetrical Section AO",
            "Trauma and Burn Section TB", "Pediatric Cardiac Section PC", "Pediatric Medical Section PM",
            "Toxin-Environmental Section TE", "Special Circumstances Section SC", "Special Operations SectionSO" };

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toc, container, false);

        view = (FrameLayout) rootView.findViewById(R.id.fragment_tocView);
        listView = view.findViewById(R.id.ListView1);

        createListArray();
        mAdapter = new CustomAdapter(this.getContext(), headingLocations);
        createList();
        listView.setAdapter(mAdapter);

        return rootView;
    }

    private void createListArray(){

        for(int i = 0; i < sectionOrder.length; i++){
            protocolWHeadings.add(sectionTitles[i]);

            headingLocations.put(protocolWHeadings.size()-1, colorArray.get(0));
            colorArray.remove(0);

            for (int j = 0; j < protocolName.size(); j++){
                if(protocolSection.get(j).equals(sectionOrder[i])){
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

}
