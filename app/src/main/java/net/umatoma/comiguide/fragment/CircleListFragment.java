package net.umatoma.comiguide.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.umatoma.comiguide.R;

public class CircleListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

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
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnRefreshListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    protected void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }
}
