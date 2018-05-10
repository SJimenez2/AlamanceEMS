package com.example.sam.navdrawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String files[];
    AssetManager assetManager;
    TextView text;

    TOCFragment fragment;
    Boolean playing = false;
    FloatingActionButton fab;
    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    ArrayList<String> protocolNumber = new ArrayList<>();
    String name = "";
    String section = "";
    String number = "";
    ActionBarDrawerToggle toggle;
    SearchView searchView;

    String[] sectionOrder;

    Timer timer;
    MyTimerTask myTimerTask;
    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sectionOrder = getResources().getStringArray(R.array.sectionOrder);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        loadFiles();
        orderFiles();

        //default code
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.start);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playing){
                    if(timer != null) {
                        timer.cancel();
                    }
                    timer = new Timer();
                    myTimerTask = new MyTimerTask();
                    timer.schedule(myTimerTask, 0, 600);

                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    fab.setImageResource(R.drawable.pause);
                    playing = true;
                } else {
                    if (timer != null){
                        timer.cancel();
                        timer = null;
                    }

                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    fab.setImageResource(R.drawable.start);
                    playing = false;
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting initial screen to base TOCFragment
        fragment = new TOCFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("Names", protocolName);
        bundle.putStringArrayList("Original Names", protocolOriginal);
        bundle.putStringArrayList("Sections", protocolSection);
        bundle.putBoolean("TOC",true);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "ListFragment");
        fragmentTransaction.commit();
    }

    //Hides Keyboard when clicking outside of SearchView
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Metronome beep sound
    private void playSound(){
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }

    //Formats and saves the String as a PDF name
    private void processFile(String originalName) {
        String saveOriginalName = originalName;
        name = originalName.substring(0, originalName.indexOf(' '));
        section = name;
        originalName = '-' + originalName.substring(originalName.indexOf(' ')+1);

        number = originalName.substring(1, originalName.indexOf(' '));
        name = name + originalName.substring(0, originalName.indexOf(' ')) + ". ";
        originalName = originalName.substring(originalName.indexOf(' ')+1);

        name = name + originalName.substring(0, originalName.indexOf("Final")-1);

        if (name.contains("Protocol")) {
            name = name.substring(0, name.indexOf("Protocol")-1);
        }

        protocolOriginal.add(saveOriginalName);
        protocolName.add(name);
        protocolSection.add(section);
        protocolNumber.add(number);
    }

    // Loads PDFs into app
    private void loadFiles(){
        try {
            files = assetManager.list("PDFs");
            for (String file : files) {
                processFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reorders PDFs to fix 10 before 2
    private void orderFiles(){
        ArrayList<String> newOrderName = new ArrayList<>();
        ArrayList<String> newOrderOriginalName = new ArrayList<>();
        ArrayList<String> newOrderSections = new ArrayList<>();

        for (String aSectionOrder : sectionOrder) {
            ArrayList<String> curSections = new ArrayList<>();
            @SuppressLint("UseSparseArrays") HashMap<Integer, String> name = new HashMap<>();
            @SuppressLint("UseSparseArrays") HashMap<Integer, String> ogName = new HashMap<>();

            for (int j = 0; j < protocolName.size(); j++) {
                if (protocolSection.get(j).equals(aSectionOrder)) {
                    curSections.add(protocolSection.get(j));
                    name.put(Integer.parseInt(protocolNumber.get(j)), protocolName.get(j));
                    ogName.put(Integer.parseInt(protocolNumber.get(j)), protocolOriginal.get(j));
                }
            }

            for (Integer key : name.keySet()) {
                newOrderName.add(name.get(key));
                newOrderOriginalName.add(ogName.get(key));
                newOrderSections.add(curSections.get(key - 1));
            }
        }

        protocolName = new ArrayList<>(newOrderName);
        protocolOriginal = new ArrayList<>(newOrderOriginalName);
        protocolSection = new ArrayList<>(newOrderSections);
    }

    // Always returns to ToC
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        searchView.onActionViewCollapsed();

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
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "ListFragment");
            fragmentTransaction.commit();
        }
    }

    // Creates title bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) searchItem.getActionView();

        //SearchView window coloring
        LinearLayout ll = (LinearLayout)searchView.getChildAt(0);
        LinearLayout ll2 = (LinearLayout)ll.getChildAt(2);
        LinearLayout ll3 = (LinearLayout)ll2.getChildAt(1);
        SearchView.SearchAutoComplete autoComplete = ((SearchView.SearchAutoComplete)ll3.getChildAt(0));
        autoComplete.setHintTextColor(Color.BLACK);
        autoComplete.setTextColor(Color.BLACK);
        autoComplete.setHint("Search...");
        autoComplete.setBackgroundColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fragment.mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // Opens navigation bar content when clicked
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragment = new TOCFragment();
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
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    playSound();
                }
            });
        }
    }
}
