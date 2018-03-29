package com.haybankz.medmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.haybankz.medmanager.MedicationLoader;
import com.haybankz.medmanager.R;
import com.haybankz.medmanager.adapter.MedicationRecyclerAdapter;
import com.haybankz.medmanager.model.Medication;

import java.util.ArrayList;

public class MedicationListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Medication>>{

    LinearLayout mNoMedicationLayout;
    RecyclerView mMedicationRecyclerView;

    MedicationRecyclerAdapter mMedicationRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNoMedicationLayout = findViewById(R.id.linear_layout_no_med);
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent = new Intent(MedicationListActivity.this, MainActivity.class);

                startActivity(intent);


            }
        });

        mMedicationRecyclerView = findViewById(R.id.recycler_medication);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mMedicationRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mMedicationRecyclerAdapter = new MedicationRecyclerAdapter(MedicationListActivity.this, new ArrayList<Medication>());

        mMedicationRecyclerView.setAdapter(mMedicationRecyclerAdapter);

        getSupportLoaderManager().initLoader(1, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
