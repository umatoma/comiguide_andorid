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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.KeyValuePairAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.validator.EmptyValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComiketCircleFormFragment extends Fragment {

    public static final String TAG = "ComiketCircleFormFragment";

    private OnComiketCircleUpdateListener mOnUpdateListener;
    private OnComiketCircleCreateListener mOnCreateListener;
    private int mCmapId;
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
    private RadioGroup mFormColor;
    private Button mButtonSubmit;
    private ComiGuideApiClient.HttpClientTask mLoadComiketBlocksTask;
    private ComiGuideApiClient.HttpClientTask mLoadComiketLayoutsTask;
    private ComiGuideApiClient.HttpClientTask mUpdateComiketCircleTask;
    private ComiGuideApiClient.HttpClientTask mCreateComiketCircleTask;

    public ComiketCircleFormFragment() {}

    public ComiketCircleFormFragment(int comiket_id, int day, int cmap_id) {
        mComiketCircle = new ComiketCircle(comiket_id, day);
        mCmapId = cmap_id;
    }

    public ComiketCircleFormFragment(ComiketCircle circle) {
        mComiketCircle = circle;
        mCmapId = circle.getComiketLayout().getComiketBlock().getComiketArea().getCmapId();
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
        mFormColor = (RadioGroup) view.findViewById(R.id.form_comiket_circle_color);
        mButtonSubmit = (Button) view.findViewById(R.id.form_submit);

        mFormComiketBlock.setAdapter(mComiketBlockAdapter);
        mFormComiketLayout.setAdapter(mComiketLayoutAdapter);
        mFormSpaceNoSub.setAdapter(mSpaceNoSubAdapter);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });
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

        return view;
    }

    @Override
    public void onStart () {
        super.onStart();

        mFormSpaceNoSub.setSelection(
                mSpaceNoSubAdapter.getPosition(mComiketCircle.getSpaceNoSub()));
        mFormColor.check(getColorId(mComiketCircle.getColor()));

        mFormCircleName.setText(mComiketCircle.getCircleName());
        mFormCircleUrl.setText(mComiketCircle.getCircleUrl());
        mFormComment.setText(mComiketCircle.getComment());
        mFormCost.setText(mComiketCircle.getCost());

        if (mComiketCircle.isCreated()) {
            mButtonSubmit.setText(getString(R.string.form_comiket_circle_update));
        } else {
            mButtonSubmit.setText(getString(R.string.form_comiket_circle_create));
        }

        loadComiketBlockOptions();
    }

    @Override
    public void onDetach() {
        mOnUpdateListener = null;
        mOnCreateListener = null;
        mLoadComiketBlocksTask = null;
        mLoadComiketLayoutsTask = null;
        super.onDetach();
    }

    private void loadComiketBlockOptions() {
        mLoadComiketBlocksTask = new ComiGuideApiClient(getActivity())
                .callGetTask("api/v1/careas.json");
        mLoadComiketBlocksTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadComiketBlocksTask = null;
                        setComiketBlockAdapterValues(result);
                    }

                    @Override
                    public void onFail() {
                        mLoadComiketBlocksTask = null;
                    }
                })
                .setCache()
                .execute();
    }

    private void loadComiketLayoutOptions(int block_id) {
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
                })
                .setCache()
                .execute();
    }

    private void setComiketBlockAdapterValues(JSONObject result) {
        if (result != null) {
            try {
                JSONArray areas = result.getJSONArray("careas");
                int length = areas.length();

                mComiketBlockAdapter.clear();
                for (int i = 0; i < length; i++) {
                    JSONObject area = areas.getJSONObject(i);
                    if (area.getInt("cmap_id") != mCmapId) {
                        continue;
                    }

                    JSONArray blocks = area.getJSONArray("cblocks");
                    int blocks_length = blocks.length();
                    for (int j = 0; j < blocks_length; j++) {
                        JSONObject block = blocks.getJSONObject(j);
                        Pair<String, String> pair
                                = new Pair<>(block.getString("id"), block.getString("name"));
                        mComiketBlockAdapter.add(pair);
                    }

                    int position = 0;
                    if (mComiketCircle.isCreated()) {
                        int block_id = mComiketCircle.getComiketLayout().getCblockId();
                        position = mComiketBlockAdapter
                                .getPositionFromKey(String.valueOf(block_id), 0);
                    }
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

                mComiketLayoutAdapter.clear();
                for (int i = 0; i < length; i++) {
                    JSONObject layout = layouts.getJSONObject(i);
                    Pair<String, String> pair
                            = new Pair<>(layout.getString("id"), layout.getString("space_no"));
                    mComiketLayoutAdapter.add(pair);
                }

                int position = 0;
                if (mComiketCircle.isCreated() && mComiketLayoutAdapter.getCount() == 0) {
                    position = mComiketLayoutAdapter
                            .getPositionFromKey(String.valueOf(mComiketCircle.getClayoutId()), 0);
                }
                mFormComiketLayout.setSelection(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Fail to load...", Toast.LENGTH_SHORT).show();
        }
    }

    private int getColorId(String color) {
        if (color.equals("gray")) {
            return R.id.color_gray;
        } else if (color.equals("red")) {
            return R.id.color_red;
        } else if (color.equals("green")) {
            return R.id.color_green;
        } else if (color.equals("blue")) {
            return R.id.color_blue;
        } else if (color.equals("yellow")) {
            return R.id.color_yellow;
        } else if (color.equals("orange")) {
            return R.id.color_orange;
        } else {
            return R.id.color_black;
        }
    }

    private String getSelectedColor() {
        switch (mFormColor.getCheckedRadioButtonId()) {
            case R.id.color_gray:
                return "gray";
            case R.id.color_red:
                return "red";
            case R.id.color_green:
                return "green";
            case R.id.color_blue:
                return "blue";
            case R.id.color_yellow:
                return "yellow";
            case R.id.color_orange:
                return "orange";
            default:
                return "black";
        }
    }

    private int getSelectedLayoutId() {
        int position = mFormComiketLayout.getSelectedItemPosition();
        return Integer.parseInt(mComiketLayoutAdapter.getItem(position).first);
    }

    private String getSelectedSpaceNoSub() {
        int position = mFormSpaceNoSub.getSelectedItemPosition();
        return mSpaceNoSubAdapter.getItem(position);
    }

    private void attemptSubmit() {
        int layout_id = getSelectedLayoutId();
        String space_no_sub = getSelectedSpaceNoSub();
        String circle_name = mFormCircleName.getText().toString();
        String circle_url = mFormCircleUrl.getText().toString();
        String comment = mFormComment.getText().toString();
        String cost = mFormCost.getText().toString();
        String color = getSelectedColor();

        if (new EmptyValidator(circle_name).isValid()) {
            if (mComiketCircle.isCreated()) {
                RequestBody formBody = new FormEncodingBuilder()
                        .add("ccircle_checklist[clayout_id]", String.valueOf(layout_id))
                        .add("ccircle_checklist[space_no_sub]", space_no_sub)
                        .add("ccircle_checklist[circle_name]", circle_name)
                        .add("ccircle_checklist[circle_url]", circle_url)
                        .add("ccircle_checklist[comment]", comment)
                        .add("ccircle_checklist[cost]", cost)
                        .add("ccircle_checklist[color]", color)
                        .build();

                updateComiketCircle(formBody);
            } else {
                int comiket_id = mComiketCircle.getComiketId();
                int day = mComiketCircle.getDay();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("ccircle_checklist[comiket_id]", String.valueOf(comiket_id))
                        .add("ccircle_checklist[day]", String.valueOf(day))
                        .add("ccircle_checklist[clayout_id]", String.valueOf(layout_id))
                        .add("ccircle_checklist[space_no_sub]", space_no_sub)
                        .add("ccircle_checklist[circle_name]", circle_name)
                        .add("ccircle_checklist[circle_url]", circle_url)
                        .add("ccircle_checklist[comment]", comment)
                        .add("ccircle_checklist[cost]", cost)
                        .add("ccircle_checklist[color]", color)
                        .build();

                createComiketCircle(formBody);
            }
        } else {
            mFormCircleName.setError(getString(R.string.form_error_empty));
        }
    }

    private void createComiketCircle(RequestBody formBody) {
        String path = "api/v1/ccircle_checklists";
        mCreateComiketCircleTask = new ComiGuideApiClient(getActivity()).callPostTask(path, formBody);
        mCreateComiketCircleTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mCreateComiketCircleTask = null;

                        try {
                            ComiketCircle circle = new ComiketCircle(result.getJSONObject("ccircle_checklist"));
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_success_comiket_circle_create),
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (mOnCreateListener != null) {
                                mOnCreateListener.onComiketCircleCreate(circle);
                            }

                            removeSelf();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_error_common),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFail() {
                        mCreateComiketCircleTask = null;
                        Toast.makeText(
                                getActivity(),
                                getString(R.string.message_fail_comiket_circle_create),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setProgressDialog(getActivity())
                .execute();
    }

    private void updateComiketCircle(RequestBody formBody) {
        String path = String.format("api/v1/ccircle_checklists/%d", mComiketCircle.getId());
        mUpdateComiketCircleTask = new ComiGuideApiClient(getActivity()).callPutTask(path, formBody);
        mUpdateComiketCircleTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mUpdateComiketCircleTask = null;

                        try {
                            ComiketCircle circle = new ComiketCircle(result.getJSONObject("ccircle_checklist"));
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_success_comiket_circle_update),
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (mOnUpdateListener != null) {
                                mOnUpdateListener.onComiketCircleUpdate(circle);
                            }

                            removeSelf();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_error_common),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFail() {
                        mUpdateComiketCircleTask = null;
                        Toast.makeText(
                                getActivity(),
                                getString(R.string.message_fail_comiket_circle_update),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setProgressDialog(getActivity())
                .execute();
    }

    private void removeSelf() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .remove(this)
                .commit();
    }

    public ComiketCircleFormFragment setOnComiketCircleCreateListener(OnComiketCircleCreateListener listener) {
        mOnCreateListener = listener;
        return this;
    }

    public ComiketCircleFormFragment setOnComiketCircleUpdateListener(OnComiketCircleUpdateListener listener) {
        mOnUpdateListener = listener;
        return this;
    }

    private class OnCnacelListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

}
