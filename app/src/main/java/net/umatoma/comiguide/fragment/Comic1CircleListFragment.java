package net.umatoma.comiguide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.umatoma.comiguide.adapter.Comic1CircleAdapter;

public class Comic1CircleListFragment extends CircleListFragment {

    public static final String TAG = "Comic1CircleLIstFragment";

    private OnComic1CircleSelectListener mOnSelectListener;
    private Comic1CircleAdapter mAdapter;

    public static Comic1CircleListFragment newInstance(Comic1CircleAdapter adapter) {
        return new Comic1CircleListFragment(adapter);
    }

    public Comic1CircleListFragment() {}

    private Comic1CircleListFragment(Comic1CircleAdapter adapter) {
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
            mOnSelectListener.onCircleSelect(mAdapter.getItem(position));
        }
    }

    public void setOnComic1CircleSelectListener(OnComic1CircleSelectListener listener) {
        mOnSelectListener = listener;
    }
    
}
