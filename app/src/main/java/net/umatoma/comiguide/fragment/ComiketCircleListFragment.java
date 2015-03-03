package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.model.ComiketCircle;

public class ComiketCircleListFragment extends Fragment {

    public static final String TAG = "ComiketCircleListFragment";
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ComiketCircleArrayAdapter mAdapter;

    public static ComiketCircleListFragment newInstance(ComiketCircleArrayAdapter adapter) {
        return new ComiketCircleListFragment(adapter);
    }

    public ComiketCircleListFragment() {}

    private ComiketCircleListFragment(ComiketCircleArrayAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comiket_circle_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_reflesh_layout);
        if (mOnRefreshListener != null) {
            mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        }

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onComiketCircleSelected(mAdapter.getItem(position));
            }
        });

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
        mOnRefreshListener = null;
    }

    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public ComiketCircleListFragment setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListener = listener;
        return this;
    }

    public interface OnFragmentInteractionListener {
        public void onComiketCircleSelected(ComiketCircle circle);
    }

}
