package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ComiketCircleListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LoadComiketCirclesTask mLoadComiketCirclesTask;
    private AbsListView mListView;
    private ComiketCircleArrayAdapter mAdapter;

    public ComiketCircleListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ComiketCircleArrayAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comiket_circle_list, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
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

        mLoadComiketCirclesTask = new LoadComiketCirclesTask(getActivity());
        mLoadComiketCirclesTask.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mLoadComiketCirclesTask = null;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onComiketCircleSelected(ComiketCircle circle);
    }

    private class LoadComiketCirclesTask extends AsyncTask<Void, Void, JSONObject> {

        private User mUser;

        public LoadComiketCirclesTask(Context context) {
            mUser = new User(context);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://comiguide.net/api/v1/comikets/87/ccircle_checklists.json")
                    .addHeader("X-Comiguide-Api-Token", mUser.getApiToken())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return new JSONObject(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {
                    JSONArray comiketCircles = result.getJSONArray("ccircle_checklists");
                    int length = comiketCircles.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject comiketCircle = comiketCircles.getJSONObject(i);
                        mAdapter.add(new ComiketCircle(comiketCircle));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Fail to load...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mLoadComiketCirclesTask = null;
        }
    }

}
