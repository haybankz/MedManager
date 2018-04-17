package com.haybankz.medmanager.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.adapter.ReminderRecyclerAdapter;
import com.haybankz.medmanager.loader.ReminderLoader;
import com.haybankz.medmanager.model.Reminder;
import com.haybankz.medmanager.util.ReminderDbUtils;

import java.util.ArrayList;



public class ReminderListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Reminder>> {



    LinearLayout mNoReminderLayout;
    RecyclerView mReminderRecyclerView;

    ReminderRecyclerAdapter mReminderRecyclerAdapter;

    private int REMINDER_LOADER = 1;

    public ReminderListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_reminder_list, container, false);

        mNoReminderLayout = view.findViewById(R.id.linear_layout_no_med_rem);

        mNoReminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "View clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mReminderRecyclerView = view.findViewById(R.id.recycler_reminder);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mReminderRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mReminderRecyclerAdapter = new ReminderRecyclerAdapter(getContext(), new ArrayList<Reminder>());




        mReminderRecyclerView.setAdapter(mReminderRecyclerAdapter);
//        mReminderRecyclerView.addItemDecoration(new DividerItemDecoration(mReminderRecyclerView.getContext(), StaggeredGridLayoutManager.VERTICAL));

//        mReminderRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mReminderRecyclerView, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//                Toast.makeText(getContext(), mReminderRecyclerAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));


        getLoaderManager().initLoader(REMINDER_LOADER, null, this);

        return view;
    }





    @NonNull
    @Override
    public Loader<ArrayList<Reminder>> onCreateLoader(int id, @Nullable Bundle args) {
        return new ReminderLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Reminder>> loader, ArrayList<Reminder> data) {

        if (data != null && data.size() > 0) {

            mNoReminderLayout.setVisibility(View.GONE);

            // add data ro recycler adapter
            mReminderRecyclerAdapter.addAll(data);
            int position = 0;
            ArrayList<Reminder> reminders = ReminderDbUtils.getAllRemindersThatRangBeforeCurrentTime(getContext());
            if(reminders != null) {
                position = reminders.size();
                mReminderRecyclerView.scrollToPosition(position);
            }
            mReminderRecyclerAdapter.notifyDataSetChanged();
            mReminderRecyclerView.setVisibility(View.VISIBLE);

        }else{
            mReminderRecyclerView.setVisibility(View.GONE);
            mNoReminderLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Reminder>> loader) {

        mReminderRecyclerAdapter.clear();
        mReminderRecyclerAdapter.notifyDataSetChanged();

    }
}
