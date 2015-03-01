package net.umatoma.comiguide.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.KeyValuePairAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.model.ComiketCircle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComiketCircleFormFragment extends Fragment {

    public static final String TAG = "ComiketCircleFormFragment";
    private ComiketCircle mComiketCircle;
    private KeyValuePairAdapter mComiketBlockAdapter;
    private KeyValuePairAdapter mComiketLayoutAdapter;
    private ArrayAdapter<String> mSpaceNoSubAdapter;
    private Spinner mFormComiketBlock;
    private Spinner mFormComiketLayout;
    private Spinner mFormSpaceNoSub;
    private EditText mFormCircleName;
    private EditText mFormCircleUrl;
    private EditText mFormComment;
    private EditText mFormCost;
    private Button mButtonSubmit;
    private ComiGuideApiClient.HttpClientTask mLoadComiketBlocksTask;
    private ComiGuideApiClient.HttpClientTask mLoadComiketLayoutsTask;

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
        mSpaceNoSubAdapter = new ArrayAdapter<>(getActivity(), resId, new String[]{ "a", "b" });
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

            mButtonSubmit.setText(getString(R.string.form_comiket_circle_update));
            mButtonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptSubmit();
                }
            });

            mFormSpaceNoSub.setSelection(
                    mSpaceNoSubAdapter.getPosition(mComiketCircle.getSpaceNoSub()));
            mFormComiketBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Pair<String, String> pair = mComiketBlockAdapter.getItem(position);
                    int block_id = Integer.parseInt(pair.first);
                    loadComiketLayoutOptions(block_id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            int block_id = mComiketCircle.getComiketLayout().getComiketBlock().getId();
            loadComiketBlockOptions(block_id);
        }

        return view;
    }

    @Override
    public void onDetach() {
        mLoadComiketBlocksTask = null;
        mLoadComiketLayoutsTask = null;
        super.onDetach();
    }

    private void loadComiketBlockOptions(final int block_id) {
        mLoadComiketBlocksTask = new ComiGuideApiClient(getActivity())
                .callGetTask("api/v1/careas.json");
        mLoadComiketBlocksTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadComiketBlocksTask = null;
                        setComiketBlockAdapterValues(result, block_id);
                    }

                    @Override
                    public void onFail() {
                        mLoadComiketBlocksTask = null;
                    }
                }).execute();
    }

    private void loadComiketLayoutOptions(int block_id) {
        final int layout_id = mComiketCircle.getComiketLayout().getId();
        mLoadComiketLayoutsTask = new ComiGuideApiClient(getActivity())
                .callGetTask(String.format("api/v1/cblocks/%d/clayouts.json", block_id));
        mLoadComiketLayoutsTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadComiketLayoutsTask = null;
                        setComiketLayoutAdapterValues(result);
                    }

                    @Override
                    public void onFail() {
                        mLoadComiketLayoutsTask = null;
                    }
                }).execute();
    }

    private void setComiketBlockAdapterValues(JSONObject result, int comiket_block_id) {
        if (result != null) {
            try {
                JSONArray areas = result.getJSONArray("careas");
                int cmap_id = 1;
                int length = areas.length();

                mComiketBlockAdapter.clear();
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
                    int position = mComiketBlockAdapter
                            .getPositionFromKey(String.valueOf(comiket_block_id));
                    mFormComiketBlock.setSelection(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Fail to load...", Toast.LENGTH_SHORT).show();
        }
    }

    private void setComiketLayoutAdapterValues(JSONObject result) {
        if (result != null) {
            try {
                JSONObject block = result.getJSONObject("cblock");
                JSONArray layouts = block.getJSONArray("clayouts");
                int length = layouts.length();
                int layout_id = -1;
                if (mComiketLayoutAdapter.getCount() == 0) {
                    layout_id = mComiketCircle.getComiketLayout().getId();
                }

                mComiketLayoutAdapter.clear();
                for (int i = 0; i < length; i++) {
                    JSONObject layout = layouts.getJSONObject(i);
                    Pair<String, String> pair
                            = new Pair<>(layout.getString("id"), layout.getString("space_no"));
                    mComiketLayoutAdapter.add(pair);
                }
                int position = mComiketLayoutAdapter
                        .getPositionFromKey(String.valueOf(layout_id), 0);
                mFormComiketLayout.setSelection(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Fail to load...", Toast.LENGTH_SHORT).show();
        }
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

}
