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

public class ComiketCircleMenuDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ComiketCircleMenuDialogFragment";

    public static final int MENU_MAP = 1;
    public static final int MENU_EDIT = 2;
    public static final int MENU_DELETE = 3;

    private ComiketCircle mCircle;
    private ListView mMenuList;
    private MenuListAdapter mAdapter;
    private OnMenuDialogSelectListener mOnMenuDialogSelectListener;

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
        mAdapter.add(new MenuListAdapter.MenuOption(
                MENU_MAP, R.drawable.ic_map_marker, getString(R.string.dialog_comiket_circle_menu_show)));
        mAdapter.add(new MenuListAdapter.MenuOption(
                MENU_EDIT, R.drawable.ic_edit, getString(R.string.dialog_comiket_circle_menu_edit)));
        mAdapter.add(new MenuListAdapter.MenuOption(
                MENU_DELETE, R.drawable.ic_delete, getString(R.string.dialog_comiket_circle_menu_delete)));

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
        mOnMenuDialogSelectListener = null;
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnMenuDialogSelectListener != null) {
            mOnMenuDialogSelectListener.onMenuSelect(mAdapter.getItem(position).menuId);
        }
        dismiss();
    }

    public void setOnMenuDialogSelectListener(OnMenuDialogSelectListener listener) {
        mOnMenuDialogSelectListener = listener;
    }
}
