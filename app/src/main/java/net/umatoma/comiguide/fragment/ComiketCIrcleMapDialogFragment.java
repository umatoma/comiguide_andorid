package net.umatoma.comiguide.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.umatoma.comiguide.R;

public class ComiketCIrcleMapDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ComiketCIrcleMapDialogFragment";

    private OnComiketCircleMapSelectListener mOnSelectListener;

    public static ComiketCIrcleMapDialogFragment newInstance() {
        return new ComiketCIrcleMapDialogFragment();
    }

    public ComiketCIrcleMapDialogFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    onCreateDialogでDialogを指定するときはonCreateViewでnullを返さないと落ちる。
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_comiket_circle_map_dialog, container, false);
//        return view;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_comiket_circle_map_dialog, null, false);

        view.findViewById(R.id.button_comiket_circle_map_d1_e123).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d1_e456).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d1_w12).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d2_e123).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d2_e456).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d2_w12).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d3_e123).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d3_e456).setOnClickListener(this);
        view.findViewById(R.id.button_comiket_circle_map_d3_w12).setOnClickListener(this);

        return new AlertDialog
                .Builder(getActivity())
                .setTitle(R.string.dialog_comiket_circle_map_title)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(view)
                .create();
    }

    public ComiketCIrcleMapDialogFragment setOnComiketCircleMapSelectListener(
            OnComiketCircleMapSelectListener listener) {

        mOnSelectListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mOnSelectListener == null){
            return;
        }

        switch (v.getId()) {
            case R.id.button_comiket_circle_map_d1_e123:
                mOnSelectListener.onSelect(1, 1);
                break;
            case R.id.button_comiket_circle_map_d1_e456:
                mOnSelectListener.onSelect(1, 2);
                break;
            case R.id.button_comiket_circle_map_d1_w12:
                mOnSelectListener.onSelect(1, 3);
                break;
            case R.id.button_comiket_circle_map_d2_e123:
                mOnSelectListener.onSelect(2, 1);
                break;
            case R.id.button_comiket_circle_map_d2_e456:
                mOnSelectListener.onSelect(2, 2);
                break;
            case R.id.button_comiket_circle_map_d2_w12:
                mOnSelectListener.onSelect(2, 3);
                break;
            case R.id.button_comiket_circle_map_d3_e123:
                mOnSelectListener.onSelect(3, 1);
                break;
            case R.id.button_comiket_circle_map_d3_e456:
                mOnSelectListener.onSelect(3, 3);
                break;
            case R.id.button_comiket_circle_map_d3_w12:
                mOnSelectListener.onSelect(3, 3);
                break;
            default:
                return;
        }

        Dialog dialog = getDialog();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public interface OnComiketCircleMapSelectListener {
        public void onSelect(int day, int cmap_id);
    }

}
