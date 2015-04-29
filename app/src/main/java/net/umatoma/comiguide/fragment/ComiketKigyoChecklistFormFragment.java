package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import net.umatoma.comiguide.model.ComiketKigyo;
import net.umatoma.comiguide.model.ComiketKigyoChecklist;
import net.umatoma.comiguide.util.FormUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComiketKigyoChecklistFormFragment extends Fragment {

    public static final String TAG = "ComiketKigyoChecklistFormFragment";

    private int mComiketId;
    private OnComiketKigyoChecklistUpdateListener mOnUpdateListener;
    private OnComiketKigyoChecklistCreateListener mOnCreateListener;
    private ComiketKigyoChecklist mComiketKigyoChecklist;
    private KeyValuePairAdapter mKigyoAdapter;
    private Spinner mFormKigyo;
    private EditText mFormComment;
    private EditText mFormCost;
    private RadioGroup mFormColor;
    private Button mButtonSubmit;
    private ComiGuideApiClient.HttpClientTask mLoadCkigyosTask;
    private ComiGuideApiClient.HttpClientTask mUpdateComiketKigyoChecklistTask;
    private ComiGuideApiClient.HttpClientTask mCreateComiketKigyoChecklistTask;

    public static ComiketKigyoChecklistFormFragment newInstance(int comiket_id) {
        ComiketKigyoChecklistFormFragment instance = new ComiketKigyoChecklistFormFragment();
        instance.setComiketId(comiket_id);
        instance.setComiketKigyoChecklist(new ComiketKigyoChecklist());
        return instance;
    }

    public static ComiketKigyoChecklistFormFragment newInstance(ComiketKigyoChecklist checklist) {
        ComiketKigyoChecklistFormFragment instance = new ComiketKigyoChecklistFormFragment();
        instance.setComiketId(checklist.getComiketKigyo().getComiketId());
        instance.setComiketKigyoChecklist(checklist);
        return instance;
    }

    public ComiketKigyoChecklistFormFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        int resId = android.R.layout.simple_spinner_dropdown_item;
        mKigyoAdapter = new KeyValuePairAdapter(getActivity(), resId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comiket_kigyo_checklist_form, container, false);
        view.setOnTouchListener(new OnCancelListener()); // Not through touch event.

        mFormKigyo = (Spinner) view.findViewById(R.id.form_ckigyo);
        mFormComment = (EditText) view.findViewById(R.id.form_comment);
        mFormCost = (EditText) view.findViewById(R.id.form_circle_cost);
        mFormColor = (RadioGroup) view.findViewById(R.id.form_circle_color);
        mButtonSubmit = (Button) view.findViewById(R.id.form_submit);

        mFormKigyo.setAdapter(mKigyoAdapter);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });

        return view;
    }

    @Override
    public void onStart () {
        super.onStart();

        mFormColor.check(getColorId(mComiketKigyoChecklist.getColor()));

        mFormComment.setText(FormUtil.encodeNullToBlank(mComiketKigyoChecklist.getComment()));
        mFormCost.setText(FormUtil.encodeNullToBlank(mComiketKigyoChecklist.getCost()));

        if (mComiketKigyoChecklist.isCreated()) {
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
        mLoadCkigyosTask = null;
        super.onDetach();
    }

    private void setComiketId(int comiket_id) {
        mComiketId = comiket_id;
    }

    private void setComiketKigyoChecklist(ComiketKigyoChecklist checklist) {
        mComiketKigyoChecklist = checklist;
    }

    private void loadBlockOptions() {
        mLoadCkigyosTask = new ComiGuideApiClient(getActivity())
                .callGetTask(String.format("api/v1/comikets/%d/ckigyos", mComiketId));
        mLoadCkigyosTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadCkigyosTask = null;
                        setBlockAdapterValues(result);
                    }

                    @Override
                    public void onFail() {
                        mLoadCkigyosTask = null;
                    }
                })
                .setCache()
                .execute();
    }

    private void setBlockAdapterValues(JSONObject result) {
        if (result != null) {
            try {
                JSONArray kigyos = result.getJSONArray("ckigyos");
                mKigyoAdapter.clear();

                int length = kigyos.length();
                for (int i = 0; i < length; i++) {
                    JSONObject block = kigyos.getJSONObject(i);
                    String value = String.format("[%d] %s", block.getInt("kigyo_no"), block.getString("name"));
                    Pair<String, String> pair
                            = new Pair<>(block.getString("id"), value);
                    mKigyoAdapter.add(pair);
                }

                int position = 0;
                if (mComiketKigyoChecklist.isCreated()) {
                    int kigyo_id = mComiketKigyoChecklist.getCkigyoId();
                    position = mKigyoAdapter
                            .getPositionFromKey(String.valueOf(kigyo_id), 0);
                }
                mFormKigyo.setSelection(position);
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

    private int getSelectedKigyoId() {
        int position = mFormKigyo.getSelectedItemPosition();
        return Integer.parseInt(mKigyoAdapter.getItem(position).first);
    }

    private void attemptSubmit() {
        int kigyo_id = getSelectedKigyoId();
        String comment = mFormComment.getText().toString();
        String cost = mFormCost.getText().toString();
        String color = getSelectedColor();

        RequestBody formBody = new FormEncodingBuilder()
                .add("ckigyo_checklist[ckigyo_id]", String.valueOf(kigyo_id))
                .add("ckigyo_checklist[comment]", comment)
                .add("ckigyo_checklist[cost]", cost)
                .add("ckigyo_checklist[color]", color)
                .build();

        if (mComiketKigyoChecklist.isCreated()) {
            updateComiketKigyoChecklist(formBody);
        } else {
            createComiketKigyoChecklist(formBody);
        }
    }

    private void createComiketKigyoChecklist(RequestBody formBody) {
        String path = "api/v1/ckigyo_checklists";
        mCreateComiketKigyoChecklistTask = new ComiGuideApiClient(getActivity()).callPostTask(path, formBody);
        mCreateComiketKigyoChecklistTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mCreateComiketKigyoChecklistTask = null;

                        try {
                            ComiketKigyoChecklist checklist = new ComiketKigyoChecklist(result.getJSONObject("ckigyo_checklist"));
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_success_circle_create),
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (mOnCreateListener != null) {
                                mOnCreateListener.onComiketKigyoChecklistCreate(checklist);
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
                        mCreateComiketKigyoChecklistTask = null;
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

    private void updateComiketKigyoChecklist(RequestBody formBody) {
        String path = String.format("api/v1/ckigyo_checklists/%d", mComiketKigyoChecklist.getId());
        mUpdateComiketKigyoChecklistTask = new ComiGuideApiClient(getActivity()).callPutTask(path, formBody);
        mUpdateComiketKigyoChecklistTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mUpdateComiketKigyoChecklistTask = null;

                        try {
                            ComiketKigyoChecklist checklist = new ComiketKigyoChecklist(result.getJSONObject("ckigyo_checklist"));
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.message_success_circle_update),
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (mOnUpdateListener != null) {
                                mOnUpdateListener.onComiketKigyoChecklistUpdate(checklist);
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
                        mUpdateComiketKigyoChecklistTask = null;
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

    public ComiketKigyoChecklistFormFragment setOnComiketKigyoChecklistCreateListener(OnComiketKigyoChecklistCreateListener listener) {
        mOnCreateListener = listener;
        return this;
    }

    public ComiketKigyoChecklistFormFragment setOnComiketKigyoChecklistUpdateListener(OnComiketKigyoChecklistUpdateListener listener) {
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
