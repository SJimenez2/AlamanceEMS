package com.example.sam.navdrawer;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String files[];
    AssetManager assetManager;
    TextView text;

    //new idea
    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    String name = "";
    String section = "";
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        loadFiles();


        //default code
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting initial screen to TOCFragmant
        TOCFragment fragment = new TOCFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Names", protocolName);
        bundle.putStringArrayList("Original Names", protocolOriginal);
        bundle.putStringArrayList("Sections", protocolSection);
        bundle.putBoolean("TOC",true);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "ListFragment");
        fragmentTransaction.commit();
    }

    private void processFile(String originalName) {
        String saveOriginalName = originalName;
        name = originalName.substring(0, originalName.indexOf(' '));
        section = name;
        originalName = '-' + originalName.substring(originalName.indexOf(' ')+1);

        name = name + originalName.substring(0, originalName.indexOf(' ')) + ". ";
        originalName = originalName.substring(originalName.indexOf(' ')+1);

        name = name + originalName.substring(0, originalName.indexOf("Final")-1);

        if (name.indexOf("Protocol") != -1) {
            name = name.substring(0, name.indexOf("Protocol")-1);
        }

        protocolOriginal.add(saveOriginalName);
        protocolName.add(name);
        protocolSection.add(section);
    }

    private void loadFiles(){
        try {
            files = assetManager.list("PDFs");
            for (int i = 0; i < files.length; i++){
                processFile(files[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            TOCFragment fragment = new TOCFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Names", protocolName);
            bundle.putStringArrayList("Original Names", protocolOriginal);
            bundle.putStringArrayList("Sections", protocolSection);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
            fragmentTransaction.commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_toc) {
            TOCFragment fragment = new TOCFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Names", protocolName);
            bundle.putStringArrayList("Original Names", protocolOriginal);
            bundle.putStringArrayList("Sections", protocolSection);
            bundle.putBoolean("TOC",true);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_PI) {
            TOCFragment fragment = new TOCFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Names", protocolName);
            bundle.putStringArrayList("Original Names", protocolOriginal);
            bundle.putStringArrayList("Sections", protocolSection);
            bundle.putString("Individual section", "PI");
            bundle.putBoolean("TOC", false);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
            fragmentTransaction.commit();
        }
//        }else if (id == R.id.nav_UP) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("UP"));
//            bundle.putStringArrayList("Original Names", originalContentNames("UP"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        } else if (id == R.id.nav_AR) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("AR"));
//            bundle.putStringArrayList("Original Names", originalContentNames("AR"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_AC) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("AC"));
//            bundle.putStringArrayList("Original Names", originalContentNames("AC"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_AM) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("AM"));
//            bundle.putStringArrayList("Original Names", originalContentNames("AM"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_AO) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("AO"));
//            bundle.putStringArrayList("Original Names", originalContentNames("AO"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_TB) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("TB"));
//            bundle.putStringArrayList("Original Names", originalContentNames("TB"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_PC) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("PC"));
//            bundle.putStringArrayList("Original Names", originalContentNames("PC"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_PM) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("PM"));
//            bundle.putStringArrayList("Original Names", originalContentNames("PM"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_TE) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("TE"));
//            bundle.putStringArrayList("Original Names", originalContentNames("TE"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_SC) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("SC"));
//            bundle.putStringArrayList("Original Names", originalContentNames("SC"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }else if (id == R.id.nav_SO) {
//            TOCFragment fragment = new TOCFragment();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("Names", sectionContent("SO"));
//            bundle.putStringArrayList("Original Names", originalContentNames("SO"));
//            bundle.putStringArrayList("Sections", protocolSection);
//            fragment.setArguments(bundle);
//            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
//            fragmentTransaction.commit();
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public ArrayList<String> sectionContent(String str){
//        ArrayList<String> secionName = new ArrayList<>();
//        for (int i = 0; i < protocolSection.size(); i++) {
//            if(protocolSection.get(i).equals(str)){
//                secionName.add(protocolName.get(i));
//            }
//        }
//
//        return secionName;
//    }
//
//    public ArrayList<String> originalContentNames(String str){
//        ArrayList<String> originalNameTemp = new ArrayList<>();
//        for (int i = 0; i < protocolSection.size(); i++) {
//            if(protocolSection.get(i).equals(str)){
//                originalNameTemp.add(protocolOriginal.get(i));
//            }
//        }
//
//        return originalNameTemp;
//    }
}
