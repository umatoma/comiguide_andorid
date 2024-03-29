package net.umatoma.comiguide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;

public class ComiketCircleListFragment extends CircleListFragment {

    public static final String TAG = "ComiketCircleListFragment";
    private OnComiketCircleSelectListener mListener;
    private ComiketCircleArrayAdapter mAdapter;

    public static ComiketCircleListFragment newInstance(ComiketCircleArrayAdapter adapter) {
        ComiketCircleListFragment instance = new ComiketCircleListFragment();
        instance.setComiketCircleArrayAdapter(adapter);
        return instance;
    }

    public ComiketCircleListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        setAdapterToListView(mAdapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onCircleSelect(mAdapter.getItem(position));
        }
    }

    public void setOnComiketCircleSelectListener(OnComiketCircleSelectListener listener) {
        mListener = listener;
    }

    private void setComiketCircleArrayAdapter(ComiketCircleArrayAdapter adapter) {
        mAdapter = adapter;
    }

}
