package com.example.sam.navdrawer;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String files[];
    AssetManager assetManager;
    TextView text;

    //new idea
    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    ArrayList<String> protocolNumber = new ArrayList<>();
    String name = "";
    String section = "";
    String number = "";
    ActionBarDrawerToggle toggle;

    String[] sectionOrder = { "PI", "UP", "AR", "AC", "AM", "AO", "TB", "PC", "PM", "TE", "SC", "SO" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        loadFiles();
        orderFiles();


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

        //Setting initial screen to base TOCFragmant
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

        number = originalName.substring(1, originalName.indexOf(' '));
        name = name + originalName.substring(0, originalName.indexOf(' ')) + ". ";
        originalName = originalName.substring(originalName.indexOf(' ')+1);

        name = name + originalName.substring(0, originalName.indexOf("Final")-1);

        if (name.indexOf("Protocol") != -1) {
            name = name.substring(0, name.indexOf("Protocol")-1);
        }

        protocolOriginal.add(saveOriginalName);
        protocolName.add(name);
        protocolSection.add(section);
        protocolNumber.add(number);
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

    private void orderFiles(){
        ArrayList<String> newOrderName = new ArrayList<>();
        ArrayList<String> newOrderOriginalName = new ArrayList<>();
        ArrayList<String> newOrderSections = new ArrayList<>();

        for (int i = 1; i < sectionOrder.length; i++){
            ArrayList<String> curSections = new ArrayList<>();
            HashMap<Integer, String> name = new HashMap<>();
            HashMap<Integer, String> ogName = new HashMap<>();

            for (int j = 0; j < protocolName.size(); j++){
                if(protocolSection.get(j).equals(sectionOrder[i])){
                    curSections.add(protocolSection.get(j));
                    name.put(Integer.parseInt(protocolNumber.get(j)), protocolName.get(j));
                    ogName.put(Integer.parseInt(protocolNumber.get(j)), protocolOriginal.get(j));
                }
            }

            for (int j = 0; j < name.size(); j++){
                    if(name.containsKey(j)){
                        newOrderName.add(name.get(j));
                        newOrderOriginalName.add(ogName.get(j));
                        newOrderSections.add(curSections.get(j));
                    }
            }
        }

        protocolName = new ArrayList<>(newOrderName);
        protocolOriginal = new ArrayList<>(newOrderOriginalName);
        protocolSection = new ArrayList<>(newOrderSections);
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
            bundle.putBoolean("TOC",true);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "ListFragment");
            fragmentTransaction.commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        TOCFragment fragment = new TOCFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Names", protocolName);
        bundle.putStringArrayList("Original Names", protocolOriginal);
        bundle.putStringArrayList("Sections", protocolSection);

        if (id == R.id.nav_toc) {
            bundle.putBoolean("TOC",true);
        } else if (id == R.id.nav_PI) {
            bundle.putString("Individual section", "PI");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_UP) {
            bundle.putString("Individual section", "UP");
            bundle.putBoolean("TOC", false);
        } else if (id == R.id.nav_AR) {
            bundle.putString("Individual section", "AR");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_AC) {
            bundle.putString("Individual section", "AC");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_AM) {
            bundle.putString("Individual section", "AM");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_AO) {
            bundle.putString("Individual section", "AO");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_TB) {
            bundle.putString("Individual section", "TB");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_PC) {
            bundle.putString("Individual section", "PC");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_PM) {
            bundle.putString("Individual section", "PM");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_TE) {
            bundle.putString("Individual section", "TE");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_SC) {
            bundle.putString("Individual section", "SC");
            bundle.putBoolean("TOC", false);
        }else if (id == R.id.nav_SO) {
            bundle.putString("Individual section", "SO");
            bundle.putBoolean("TOC", false);
        }

        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
