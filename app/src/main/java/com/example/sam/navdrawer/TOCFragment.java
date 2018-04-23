package com.example.sam.navdrawer;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TOCFragment extends Fragment {

    String pdfName = "";

    FrameLayout view;
    LinearLayout linearLayout;

    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    ArrayList<Button> protocolButtons = new ArrayList<>();

    String[] sectionColor = {"#0c0c0c", "#92d050", "#00afef", "#001f5f", "#50622a", "#6f2f9f", "#ff0000",
            "#538ad3", "#4aacc5", "#ffc000", "#0c0c0c", "#ffc000"};
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_toc, container, false);

        view = (FrameLayout) rootView.findViewById(R.id.fragment_tocView);
        linearLayout = view.findViewById(R.id.linearLayout);

        createButtons();
        addButtons();

        return rootView;
    }

    private void createButtons() {
        for (int j = 0; j < protocolName.size(); j++) {
            Button btn = new Button(getActivity());
            btn.setText(protocolName.get(j).toString());
            btn.setBackground(null);
            btn.setTextSize(12);
            btn.setGravity(Gravity.LEFT);
            btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
            btn.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {
                   for(int i = 0; i < protocolName.size(); i++){
                       if(((Button) v).getText().toString().equals(protocolName.get(i))) {
                           pdfName = protocolOriginal.get(i);
                       }
                   }


                   PDFFragment fragment = new PDFFragment();
                   Bundle bundle = new Bundle();
                   bundle.putString("Name of PDF", pdfName);
                   fragment.setArguments(bundle);
                   android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                   fragmentTransaction.replace(R.id.fram, fragment, "PDFFragment");
                   fragmentTransaction.commit();
               }
            });
            protocolButtons.add(btn);
        }
    }

    private void addButtons(){
        for(int i = 0; i < sectionOrder.length; i++){
            TextView text = new TextView(this.getActivity());
            text.setText(sectionTitles[i]);
            text.setTypeface(Typeface.DEFAULT_BOLD);
            text.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//            if(sectionOrder[i].equals("PI") || sectionOrder[i].equals("AC")
//                    || sectionOrder[i].equals("AM") || sectionOrder[i].equals("SC")
//                    || sectionOrder[i].equals("AO") || sectionOrder[i].equals("TB")){
                text.setTextColor(Color.WHITE);
//            } else {
//                text.setTextColor(Color.BLACK);
//            }

            text.setBackgroundColor(Color.parseColor(sectionColor[i]));
            linearLayout.addView(text);

            for (int j = 0; j < protocolButtons.size(); j++){
                if(protocolSection.get(j).equals(sectionOrder[i])){
                    Button btn = protocolButtons.get(j);
                    linearLayout.addView(btn);
                }
            }
        }
    }

}
