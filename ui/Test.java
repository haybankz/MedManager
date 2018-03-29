package com.haybankz.medmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haybankz.medmanager.ClickListener;
import com.haybankz.medmanager.MedicationLoader;
import com.haybankz.medmanager.R;
import com.haybankz.medmanager.RecyclerTouchListener;
import com.haybankz.medmanager.adapter.MedicationRecyclerAdapter;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.util.MedicationDbUtils;

import java.util.ArrayList;

public class Test extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<Medication>> {


    LinearLayout mNoMedicationLayout;
    RecyclerView mMedicationRecyclerView;

    MedicationRecyclerAdapter mMedicationRecyclerAdapter;

    private int MED_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Test.this, MainActivity.class);

                startActivity(intent);
            }
        });


        mNoMedicationLayout = findViewById(R.id.linear_layout_no_med);

        mMedicationRecyclerView = findViewById(R.id.recycler_medication);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mMedicationRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mMedicationRecyclerAdapter = new MedicationRecyclerAdapter(Test.this, new ArrayList<Medication>());

        mMedicationRecyclerView.setAdapter(mMedicationRecyclerAdapter);
        mMedicationRecyclerView.addItemDecoration(new DividerItemDecoration(mMedicationRecyclerView.getContext(), StaggeredGridLayoutManager.VERTICAL));

        mMedicationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mMedicationRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(Test.this, mMedicationRecyclerAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getSupportLoaderManager().initLoader(MED_LOADER, null, this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    MenuItem searchViewItem;
    SearchView searchView;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        searchViewItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchViewItem.getActionView();
        ImageView v =  searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        TextView searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchTextView.setBackgroundColor(getResources().getColor(R.color.white));
        searchTextView.setTextColor(getResources().getColor(R.color.black));
        searchTextView.setHintTextColor(getResources().getColor(R.color.darker_gray));



        String searchFor = searchTextView.getText().toString();


        if(!searchFor.isEmpty()){
            searchView.setIconified(false);
            searchView.setQuery(searchFor, false);

        }

        searchView.setQueryHint("Search for medication");

        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Medication> medications = MedicationDbUtils.getMedicationsByName(Test.this, query);
                if(medications != null) {
                    mMedicationRecyclerAdapter.addAll(medications);
                }else{
                    mMedicationRecyclerAdapter.clear();

                }
//                Toast.makeText(Test.this, "Search for "+query, Toast.LENGTH_SHORT).show();
                return false;

//                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
//                    Toast.makeText(Test.this, "Empty search text", Toast.LENGTH_SHORT).show();
                    ArrayList<Medication> medications = MedicationDbUtils.getAllMedications(Test.this);
                    if(medications != null) {
                        mMedicationRecyclerAdapter.addAll(medications);
                    }else{
                        mMedicationRecyclerAdapter.clear();
                    }
                }else{

////                    Toast.makeText(Test.this, "Empty search text", Toast.LENGTH_SHORT).show();
//                    ArrayList<Medication> medications = MedicationDbUtils.getMedicationsByName(Test.this, newText);
//                    if(medications != null) {
//                        mMedicationRecyclerAdapter.addAll(medications);
//                    }else{
//                        mMedicationRecyclerAdapter.clear();
//                    }

                }

                return false;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
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

        if (id == R.id.nav_add_med) {

            Intent addMedicationIntent = new Intent(this, MainActivity.class);
            startActivity(addMedicationIntent);
            // Handle the camera action
        } else if (id == R.id.nav_search) {


            searchView.setPressed(true);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Medication>> onCreateLoader(int id, @Nullable Bundle args) {

            return new MedicationLoader(this);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Medication>> loader, ArrayList<Medication> data) {

        if (data != null) {

            mNoMedicationLayout.setVisibility(View.GONE);

            // add data ro recycler adapter
            mMedicationRecyclerAdapter.addAll(data);

            mMedicationRecyclerView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Medication>> loader) {

        mMedicationRecyclerAdapter.clear();
        mMedicationRecyclerAdapter.notifyDataSetChanged();

    }
}
