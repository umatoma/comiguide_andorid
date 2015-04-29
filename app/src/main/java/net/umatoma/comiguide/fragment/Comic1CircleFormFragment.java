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
import net.umatoma.comiguide.api.OnApiClientPostExecuteListener;
import net.umatoma.comiguide.model.Comic1Circle;
import net.umatoma.comiguide.util.FormUtil;
import net.umatoma.comiguide.validator.EmptyValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comic1CircleFormFragment extends Fragment {

    public static final String TAG = "Comic1CircleFormFragment";

    private OnComic1CircleUpdateListener mOnUpdateListener;
    private OnComic1CircleCreateListener mOnCreateListener;
    private boolean mFirstLoad = true;
    private Comic1Circle mComic1Circle;
    private KeyValuePairAdapter mBlockAdapter;
    private KeyValuePairAdapter mLayoutAdapter;
    private ArrayAdapter<String> mSpaceNoSubAdapter;
    private Spinner mFormBlock;
    private Spinner mFormLayout;
    private Spinner mFormSpaceNoSub;
    private EditText mFormCircleName;
    private EditText mFormCircleUrl;
    private EditText mFormComment;
    private EditText mFormCost;
    private RadioGroup mFormColor;
    private Button mButtonSubmit;
    private ComiGuideApiClient.HttpClientTask mLoadBlocksTask;
    private ComiGuideApiClient.HttpClientTask mLoadLayoutsTask;
    private ComiGuideApiClient.HttpClientTask mUpdateComic1CircleTask;
    private ComiGuideApiClient.HttpClientTask mCreateComic1CircleTask;

    public Comic1CircleFormFragment() {}

    public Comic1CircleFormFragment(int comic1_id) {
        mComic1Circle = new Comic1Circle(comic1_id);
    }

    public Comic1CircleFormFragment(Comic1Circle circle) {
        mComic1Circle = circle;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        int resId = android.R.layout.simple_spinner_dropdown_item;
        mBlockAdapter = new KeyValuePairAdapter(getActivity(), resId);
        mLayoutAdapter = new KeyValuePairAdapter(getActivity(), resId);
        mSpaceNoSubAdapter = new ArrayAdapter<>(getActivity(), resId, new String[]{ "a", "b" });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comic1_circle_form, container, false);
        view.setOnTouchListener(new OnCancelListener()); // Not through touch event.

        mFormBlock = (Spinner) view.findViewById(R.id.form_block);
        mFormLayout = (Spinner) view.findViewById(R.id.form_layout);
        mFormSpaceNoSub = (Spinner) view.findViewById(R.id.form_space_no_sub);
        mFormCircleName = (EditText) view.findViewById(R.id.form_circle_name);
        mFormCircleUrl = (EditText) view.findViewById(R.id.form_circle_url);
        mFormComment = (EditText) view.findViewById(R.id.form_comment);
        mFormCost = (EditText) view.findViewById(R.id.form_circle_cost);
        mFormColor = (RadioGroup) view.findViewById(R.id.form_circle_color);
        mButtonSubmit = (Button) view.findViewById(R.id.form_submit);

        mFormBlock.setAdapter(mBlockAdapter);
        mFormLayout.setAdapter(mLayoutAdapter);
        mFormSpaceNoSub.setAdapter(mSpaceNoSubAdapter);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });
        mFormBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Pair<String, String> pair = mBlockAdapter.getItem(position);
                int block_id = Integer.parseInt(pair.first);
                loadLayoutOptions(block_id);
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
                mSpaceNoSubAdapter.getPosition(mComic1Circle.getSpaceNoSub()));
        mFormColor.check(getColorId(mComic1Circle.getColor()));

        mFormCircleName.setText(FormUtil.encodeNullToBlank(mComic1Circle.getCircleName()));
        mFormCircleUrl.setText(FormUtil.encodeNullToBlank(mComic1Circle.getCircleUrl()));
        mFormComment.setText(FormUtil.encodeNullToBlank(mComic1Circle.getComment()));
        mFormCost.setText(FormUtil.encodeNullToBlank(mComic1Circle.getCost()));

        if (mComic1Circle.isCreated()) {
            mButtonSubmit.setText(getString(R.string.form_circle_update));
        } else {
            mButtonSubmit.setText(getString(R.string.form_circle_create));
        }

        loadBlockOptions();
    }

    @Override
    public void onDetach() {
        mOnUpdateListener = null;
        mOnCreateListener = null;
        mLoadBlocksTask = null;
        mLoadLayoutsTask = null;
        super.onDetach();
    }

    private void loadBlockOptions() {
        mLoadBlocksTask = new ComiGuideApiClient(getActivity())
                .callGetTask(String.format("api/v1/comic1s/%d/c1blocks", mComic1Circle.getComic1Id()));
        mLoadBlocksTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadBlocksTask = null;
                        setBlockAdapterValues(result);
                    }

                    @Override
                    public void onFail() {
                        mLoadBlocksTask = null;
                    }
                })
                .setCache()
                .execute();
    }

    private void loadLayoutOptions(int block_id) {
        mLoadLayoutsTask = new ComiGuideApiClient(getActivity())
                .callGetTask(String.format("api/v1/c1blocks/%d/c1layouts", block_id));
        mLoadLayoutsTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadLayoutsTask = null;
                        setComiketLayoutAdapterValues(result);
                    }

                    @Override
                    public void onFail() {
                        mLoadLayoutsTask = null;
                    }
                })
                .setCache()
                .execute();
    }

    private void setBlockAdapterValues(JSONObject result) {
        if (result != null) {
            try {
                JSONArray blocks = result.getJSONArray("c1blocks");
                mBlockAdapter.clear();

                int length = blocks.length();
                for (int i = 0; i < length; i++) {
                    JSONObject block = blocks.getJSONObject(i);
                    Pair<String, String> pair
                            = new Pair<>(block.getString("id"), block.getString("name"));
                    mBlockAdapter.add(pair);
                }

                int position = 0;
                if (mComic1Circle.isCreated()) {
                    int block_id = mComic1Circle.getComic1Layout().getC1blockId();
                    position = mBlockAdapter
                            .getPositionFromKey(String.valueOf(block_id), 0);
                }
                mFormBlock.setSelection(position);
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
                JSONObject block = result.getJSONObject("c1block");
                JSONArray layouts = block.getJSONArray("c1layouts");
                int length = layouts.length();

                mLayoutAdapter.clear();
                for (int i = 0; i < length; i++) {
                    JSONObject layout = layouts.getJSONObject(i);
                    Pair<String, String> pair
                            = new Pair<>(layout.getString("id"), layout.getString("space_no"));
                    mLayoutAdapter.add(pair);
                }

                int position = 0;
                if (mComic1Circle.isCreated() && mFirstLoad) {
                    position = mLayoutAdapter
                            .getPositionFromKey(String.valueOf(mComic1Circle.getC1layoutId()), 0);
                    mFirstLoad = false;
                }
                mFormLayout.setSelection(position);
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
        int position = mFormLayout.getSelectedItemPosition();
        return Integer.parseInt(mLayoutAdapter.getItem(position).first);
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

        EmptyValidator validator = new EmptyValidator(getActivity(), circle_name);
        if (validator.isValid()) {
            if (mComic1Circle.isCreated()) {
                RequestBody formBody = new FormEncodingBuilder()
                        .add("c1circle_checklist[c1layout_id]", String.valueOf(layout_id))
                        .add("c1circle_checklist[space_no_sub]", space_no_sub)
                        .add("c1circle_checklist[circle_name]", circle_name)
                        .add("c1circle_checklist[circle_url]", circle_url)
                        .add("c1circle_checklist[comment]", comment)
                        .add("c1circle_checklist[cost]", cost)
                        .add("c1circle_checklist[color]", color)
                        .build();

                updateComic1Circle(formBody);
            } else {
                int comic1_id = mComic1Circle.getComic1Id();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("c1circle_checklist[comic1_id]", String.valueOf(comic1_id))
                        .add("c1circle_checklist[c1layout_id]", String.valueOf(layout_id))
                        .add("c1circle_checklist[space_no_sub]", space_no_sub)
                        .add("c1circle_checklist[circle_name]", circle_name)
                        .add("c1circle_checklist[circle_url]", circle_url)
                        .add("c1circle_checklist[comment]", comment)
                        .add("c1circle_checklist[cost]", cost)
                        .add("c1circle_checklist[color]", color)
                        .build();

                createComic1Circle(formBody);
            }
        } else {
            mFormCircleName.setError(validator.getErrorMessage());
        }
    }

    private void createComic1Circle(RequestBody formBody) {
        String path = "api/v1/c1circle_checklists";
        mCreateComic1CircleTask = new ComiGuideApiClient(getActivity()).callPostTask(path, formBody);
        mCreateComic1CircleTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mCreateComic1CircleTask = null;

                        try {
                            Comic1Circle circle = new Comic1Circle(result.getJSONObject("c1circle_checklist"));
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_success_circle_create),
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (mOnCreateListener != null) {
                                mOnCreateListener.onComic1CircleCreate(circle);
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
                        mCreateComic1CircleTask = null;
                        Toast.makeText(
                                getActivity(),
                                getString(R.string.message_fail_circle_create),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setProgressDialog(getActivity())
                .execute();
    }

    private void updateComic1Circle(RequestBody formBody) {
        String path = String.format("api/v1/c1circle_checklists/%d", mComic1Circle.getId());
        mUpdateComic1CircleTask = new ComiGuideApiClient(getActivity()).callPutTask(path, formBody);
        mUpdateComic1CircleTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mUpdateComic1CircleTask = null;

                        try {
                            Comic1Circle circle = new Comic1Circle(result.getJSONObject("c1circle_checklist"));
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_success_circle_update),
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (mOnUpdateListener != null) {
                                mOnUpdateListener.onComic1CircleUpdate(circle);
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
                        mUpdateComic1CircleTask = null;
                        Toast.makeText(
                                getActivity(),
                                getString(R.string.message_fail_circle_update),
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

    public Comic1CircleFormFragment setOnComic1CircleCreateListener(OnComic1CircleCreateListener listener) {
        mOnCreateListener = listener;
        return this;
    }

    public Comic1CircleFormFragment setOnComic1CircleUpdateListener(OnComic1CircleUpdateListener listener) {
        mOnUpdateListener = listener;
        return this;
    }

    private class OnCancelListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

}
