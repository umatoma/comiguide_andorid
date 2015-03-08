package net.umatoma.comiguide.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.MenuListAdapter;
import net.umatoma.comiguide.model.ComiketCircle;

import java.util.ArrayList;

public class ComiketCircleMenuDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ComiketCircleMenuDialogFragment";
    private ComiketCircle mCircle;
    private ListView mMenuList;
    private MenuListAdapter mAdapter;
    private ArrayList<MenuListAdapter.MenuOption> mMenuOptions = new ArrayList<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public static ComiketCircleMenuDialogFragment newInstance(ComiketCircle circle) {
        return new ComiketCircleMenuDialogFragment(circle);
    }

    public ComiketCircleMenuDialogFragment() {
        // Required empty public constructor
    }

    private ComiketCircleMenuDialogFragment(ComiketCircle circle) {
        mCircle = circle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_comiket_circle_menu_dialog, null, false);

        mAdapter = new MenuListAdapter(getActivity());
        mAdapter.addAll(mMenuOptions);

        mMenuList = (ListView) view.findViewById(android.R.id.list);
        mMenuList.setAdapter(mAdapter);
        mMenuList.setOnItemClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mCircle != null) {
            builder.setTitle(mCircle.getCircleName());
            builder.setNegativeButton(R.string.dialog_cancel, null);
            builder.setView(view);
        }
        return builder.create();
    }

    @Override
    public void onResume () {
        super.onResume();
        if (mAdapter.getCount() == 0) {
            dismiss();
        }
    }

    @Override
    public void onDetach () {
        mOnItemClickListener = null;
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(parent, view, position, id);
        }
        dismiss();
    }

    public ComiketCircleMenuDialogFragment setMenuOptions(ArrayList<MenuListAdapter.MenuOption> list) {
        mMenuOptions = list;
        return this;
    }

    public ComiketCircleMenuDialogFragment setOnItemClickListener(AdapterView.OnItemClickListener listener) {
         mOnItemClickListener = listener;
        return this;
    }
}
