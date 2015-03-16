package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.model.ComiketCircle;

public class ComiketCircleListFragment extends CircleListFragment {

    public static final String TAG = "ComiketCircleListFragment";
    private OnFragmentInteractionListener mListener;
    private ComiketCircleArrayAdapter mAdapter;

    public static ComiketCircleListFragment newInstance(ComiketCircleArrayAdapter adapter) {
        return new ComiketCircleListFragment(adapter);
    }

    public ComiketCircleListFragment() {}

    private ComiketCircleListFragment(ComiketCircleArrayAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onComiketCircleSelected(mAdapter.getItem(position));
    }

    public interface OnFragmentInteractionListener {
        public void onComiketCircleSelected(ComiketCircle circle);
    }

}
