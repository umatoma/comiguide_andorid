package net.umatoma.comiguide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.umatoma.comiguide.adapter.ComiketKigyoChecklistAdapter;

public class ComiketKigyoChecklistListFragment extends CircleListFragment {

    public static final String TAG = "ComiketKigyoChecklistListFragment";

    private OnComiketKigyoChecklistSelectListener mOnSelectListener;
    private ComiketKigyoChecklistAdapter mAdapter;

    public static ComiketKigyoChecklistListFragment newInstance(ComiketKigyoChecklistAdapter adapter) {
        return new ComiketKigyoChecklistListFragment(adapter);
    }

    public ComiketKigyoChecklistListFragment() {}

    private ComiketKigyoChecklistListFragment(ComiketKigyoChecklistAdapter adapter) {
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
    public void onDetach() {
        mOnSelectListener = null;

        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnSelectListener != null) {
            mOnSelectListener.onChecklistSelect(mAdapter.getItem(position));
        }
    }

    public void setOnComiketKigyoChecklistSelectListener(OnComiketKigyoChecklistSelectListener listener) {
        mOnSelectListener = listener;
    }
}
