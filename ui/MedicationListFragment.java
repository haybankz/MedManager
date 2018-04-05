package com.haybankz.medmanager.ui;

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

import com.haybankz.medmanager.listener.ClickListener;
import com.haybankz.medmanager.loader.MedicationLoader;
import com.haybankz.medmanager.R;
import com.haybankz.medmanager.listener.RecyclerTouchListener;
import com.haybankz.medmanager.adapter.MedicationRecyclerAdapter;
import com.haybankz.medmanager.model.Medication;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicationListFragment} interface
 * to handle interaction events.
 * Use the {@link MedicationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicationListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Medication>>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout mNoMedicationLayout;
    RecyclerView mMedicationRecyclerView;

    MedicationRecyclerAdapter mMedicationRecyclerAdapter;


    private int MED_LOADER = 0;

//    private OnFragmentInteractionListener mListener;

    public MedicationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicationListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicationListFragment newInstance(String param1, String param2) {
        MedicationListFragment fragment = new MedicationListFragment();
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
        View view = inflater.inflate(R.layout.fragment_medication_list, container, false);


        mNoMedicationLayout = view.findViewById(R.id.linear_layout_no_med);

        mMedicationRecyclerView = view.findViewById(R.id.recycler_medication);



        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mMedicationRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mMedicationRecyclerAdapter = new MedicationRecyclerAdapter(getContext(), new ArrayList<Medication>());

        mMedicationRecyclerView.setAdapter(mMedicationRecyclerAdapter);
        mMedicationRecyclerView.addItemDecoration(new DividerItemDecoration(mMedicationRecyclerView.getContext(), StaggeredGridLayoutManager.VERTICAL));

        mMedicationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mMedicationRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(getContext(), mMedicationRecyclerAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getLoaderManager().initLoader(MED_LOADER, null, this);



        return view;
    }


    @NonNull
    @Override
    public Loader<ArrayList<Medication>> onCreateLoader(int id, @Nullable Bundle args) {

        return new MedicationLoader(getContext());

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

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
}
