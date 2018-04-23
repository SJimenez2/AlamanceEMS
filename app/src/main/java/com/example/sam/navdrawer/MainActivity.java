package com.example.sam.navdrawer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.github.barteksc.pdfviewer.PDFView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PDFView pdfViewer;
    String files[];
    AssetManager assetManager;
    TextView text;

    //new idea
    ArrayList<String> protocolName = new ArrayList<>();
    ArrayList<String> protocolOriginal = new ArrayList<>();
    ArrayList<String> protocolSection = new ArrayList<>();
    String name = "";
    String section = "";
    //MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        loadFiles();


        //default code
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //searchView = (MaterialSearchView) findViewById(R.id.search_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
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
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "TOCFragment");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_sections) {
            SectionFragment fragment = new SectionFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Names", protocolName);
            bundle.putStringArrayList("Original Names", protocolOriginal);
            bundle.putStringArrayList("Sections", protocolSection);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "SectionFragment");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_f3) {
            Fragment3 fragment = new Fragment3();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Fragment3");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
