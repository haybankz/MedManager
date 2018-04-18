package com.haybankz.medmanager.ui;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.adapter.MedicationCursorAdapter;
import com.haybankz.medmanager.data.medication.MedicationContract.MedicationEntry;




public class MedicationListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    ListView medicationList;
    MedicationCursorAdapter adapter;
    View emptyView;




    public MedicationListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medication_list, container, false);


        medicationList = view.findViewById(R.id.list_view_medication);
        adapter = new MedicationCursorAdapter(getContext(), null);
        medicationList.setAdapter(adapter);
        getLoaderManager().initLoader(122, null, this);

        medicationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Uri uri = ContentUris.withAppendedId(MedicationEntry.CONTENT_URI, id);

                Intent openViewMedicationActivityIntent = new Intent(getContext(), ViewMedicationActivity.class);
                openViewMedicationActivityIntent.setData(uri);
                startActivity(openViewMedicationActivityIntent);


            }
        });

        emptyView = view.findViewById(R.id.linear_layout_no_med);
        medicationList.setEmptyView(emptyView);

        return view;
    }




    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {MedicationEntry._ID,
                MedicationEntry.COLUMN_MEDICATION_NAME,
                MedicationEntry.COLUMN_MEDICATION_DESCRIPTION,
                MedicationEntry.COLUMN_MEDICATION_DOSAGE,
                MedicationEntry.COLUMN_MEDICATION_FREQUENCY,
                MedicationEntry.COLUMN_MEDICATION_START_DATE,
                MedicationEntry.COLUMN_MEDICATION_END_DATE,
                MedicationEntry.COLUMN_MEDICATION_ACTIVE
        };

        String sortOrder = MedicationEntry._ID + " DESC";

        return new CursorLoader(getContext(),
               MedicationEntry.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);
    }
}
