package com.haybankz.medmanager.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.adapter.MedicationRecyclerAdapter;
import com.haybankz.medmanager.adapter.ReminderRecyclerAdapter;
import com.haybankz.medmanager.listener.ClickListener;
import com.haybankz.medmanager.listener.RecyclerTouchListener;
import com.haybankz.medmanager.loader.ReminderLoader;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.model.Reminder;
import com.haybankz.medmanager.util.ReminderDbUtils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link ReminderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Reminder>> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;


    LinearLayout mNoReminderLayout;
    RecyclerView mReminderRecyclerView;

    ReminderRecyclerAdapter mReminderRecyclerAdapter;

    private int REMINDER_LOADER = 1;

    public ReminderListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReminderListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderListFragment newInstance(String param1, String param2) {
        ReminderListFragment fragment = new ReminderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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



    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnFragmentInteractionListener) {
////            mListener = (OnFragmentInteractionListener) context;
////        } else {
//////            throw new RuntimeException(context.toString()
//////                    + " must implement OnFragmentInteractionListener");
////        }
//        if (mReminderRecyclerAdapter != null) {
//
//            mReminderRecyclerAdapter.notifyDataSetChanged();
//        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        mReminderRecyclerAdapter.notifyDataSetChanged();
//    }

    //    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

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
