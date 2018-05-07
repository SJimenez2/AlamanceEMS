package com.example.sam.navdrawer;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class PDFFragment extends Fragment {

    String protocolName = "";
    FrameLayout view;
    PDFView pdfViewer;
    AssetManager assetManager;

    public PDFFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetManager = Objects.requireNonNull(getActivity()).getAssets();
        Bundle bundle = getArguments();
        assert bundle != null;
        protocolName = bundle.getString("Name of PDF");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pdf, container, false);
        view = rootView.findViewById(R.id.fragment_pdf);
        pdfViewer = view.findViewById(R.id.pdfView);


        //pdfViewer.fromAsset(protocolName);
        try {
            pdfViewer.fromStream(assetManager.open("PDFs/" + protocolName)).load();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return rootView;
    }

}
