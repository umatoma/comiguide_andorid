package net.umatoma.comiguide.fragment;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.KeyValuePairAdapter;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ComiketCircleFormFragment extends Fragment {

    public static final String TAG = "ComiketCircleFormFragment";
    private ComiketCircle mComiketCircle;
    private KeyValuePairAdapter mComiketBlockAdapter;
    private KeyValuePairAdapter mComiketLayoutAdapter;
    private KeyValuePairAdapter mSpaceNoSubAdapter;
    private Spinner mFormComiketBlock;
    private Spinner mFormComiketLayout;
    private Spinner mFormSpaceNoSub;
    private EditText mFormCircleName;
    private EditText mFormCircleUrl;
    private EditText mFormComment;
    private EditText mFormCost;
    private Button mButtonSubmit;
    private LoadComiketBlocksTask mLoadComiketCirclesTask;

    public ComiketCircleFormFragment() {
        // Required empty public constructor
    }

    public ComiketCircleFormFragment(ComiketCircle circle) {
        mComiketCircle = circle;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        int resId = android.R.layout.simple_spinner_dropdown_item;
        mComiketBlockAdapter = new KeyValuePairAdapter(getActivity(), resId);
        mComiketLayoutAdapter = new KeyValuePairAdapter(getActivity(), resId);
        mSpaceNoSubAdapter = new KeyValuePairAdapter(getActivity(), resId);

        if (mComiketCircle != null) {
            mLoadComiketCirclesTask = new LoadComiketBlocksTask(getActivity());
            mLoadComiketCirclesTask.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comiket_circle_form, container, false);
        view.setOnTouchListener(new OnCnacelListener()); // Not through touch event.

        mFormComiketBlock = (Spinner) view.findViewById(R.id.form_comiket_block);
        mFormComiketLayout = (Spinner) view.findViewById(R.id.form_comiket_layout);
        mFormSpaceNoSub = (Spinner) view.findViewById(R.id.form_comiket_circle_space_no_sub);
        mFormCircleName = (EditText) view.findViewById(R.id.form_comiket_circle_name);
        mFormCircleUrl = (EditText) view.findViewById(R.id.form_comiket_circle_url);
        mFormComment = (EditText) view.findViewById(R.id.form_comiket_circle_comment);
        mFormCost = (EditText) view.findViewById(R.id.form_comiket_circle_cost);
        mButtonSubmit = (Button) view.findViewById(R.id.form_submit);

        if (mComiketCircle != null) {
            mFormComiketBlock.setAdapter(mComiketBlockAdapter);
            mFormComiketLayout.setAdapter(mComiketLayoutAdapter);
            mFormSpaceNoSub.setAdapter(mSpaceNoSubAdapter);

            mFormCircleName.setText(mComiketCircle.getCircleName());
            mFormCircleUrl.setText(mComiketCircle.getCircleUrl());
            mFormComment.setText(mComiketCircle.getComment());
            mFormCost.setText(mComiketCircle.getCost());

            mButtonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptSubmit();
                }
            });
        }

        return view;
    }

    @Override
    public void onDetach() {
        mLoadComiketCirclesTask = null;
        super.onDetach();
    }

    private void attemptSubmit() {
        Toast.makeText(getActivity(), "attemptSubmit", Toast.LENGTH_SHORT).show();
    }

    private class OnCnacelListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

    private class LoadComiketBlocksTask extends AsyncTask<Void, Void, JSONObject> {

        private static final String TAG = "LoadComiketBlocksTask";
        private User mUser;

        public LoadComiketBlocksTask(Context context) {
            mUser = new User(context);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            String apiToken = mUser.getApiToken();
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .authority("comiguide.net")
                    .path("api/v1/careas.json")
                    .build();
            Request request = new Request.Builder()
                    .url(uri.toString())
                    .addHeader("X-Comiguide-Api-Token", apiToken)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d(TAG, "load success careas");
                    return new JSONObject(response.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "load fail careas");
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {
                    JSONArray areas = result.getJSONArray("careas");
                    int cmap_id = 1;
                    int length = areas.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject area = areas.getJSONObject(i);
                        if (area.getInt("cmap_id") != cmap_id) {
                            break;
                        }

                        JSONArray blocks = area.getJSONArray("cblocks");
                        int blocks_length = blocks.length();
                        for (int j = 0; j < blocks_length; j++) {
                            JSONObject block = blocks.getJSONObject(j);
                            Pair<String, String> pair
                                    = new Pair<>(block.getString("id"), block.getString("name"));
                            mComiketBlockAdapter.add(pair);
                        }
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
