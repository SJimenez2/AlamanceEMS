package com.example.sam.navdrawer;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SectionFragment extends Fragment {

    String[] sectionColor = {"#0c0c0c", "#92d050", "#00afef", "#001f5f", "#50622a", "#6f2f9f", "#ff0000",
            "#538ad3", "#4aacc5", "#ffc000", "#0c0c0c", "#ffc000"};
    String[] sectionOrder = { "PI", "UP", "AR", "AC", "AM", "AO", "TB", "PC", "PM", "TE", "SC", "SO" };
    String[] sectionTitles = { "Protocol Introduction PI", "Universal Protocols UP", "Airway Respiratory Section AR",
            "Adult Cardiac Section AC", "Adult Medical Section AM", "Adult Obstetrical Section AO",
            "Trauma and Burn Section TB", "Pediatric Cardiac Section PC", "Pediatric Medical Section PM",
            "Toxin-Environmental Section TE", "Special Circumstances Section SC", "Special Operations SectionSO" };

    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    FrameLayout view;
    LinearLayout linearLayout;

    int height = 0;
    int width = 0;

    public SectionFragment() {
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_section, container, false);

        view = (FrameLayout) rootView.findViewById(R.id.fragment_sectionView);
        linearLayout = view.findViewById(R.id.linearLayout2);

        //addSections();

        return rootView;
    }

    public void addSections(){
        for(int i = 0; i < sectionOrder.length; i++){
            TextView text = new TextView(this.getActivity());
            text.setGravity(Gravity.CENTER_VERTICAL);
            text.setText(sectionTitles[i]);
            text.setTypeface(Typeface.DEFAULT_BOLD);
            text.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            text.setTextColor(Color.WHITE);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String section = ((TextView) v).getText().toString();
                    SpecificFragment fragment = new SpecificFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Chosen Section", section);
                    bundle.putStringArrayList("Names", protocolName);
                    bundle.putStringArrayList("Original Names", protocolOriginal);
                    bundle.putStringArrayList("Sections", protocolSection);
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fram, fragment, "SpecificFragment");
                    fragmentTransaction.commit();
                }
            });

            text.setHeight(150);

            text.setBackgroundColor(Color.parseColor(sectionColor[i]));
            linearLayout.addView(text);
        }
    }

}
