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

import java.util.ArrayList;
import java.util.List;

public class MenuDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "MenuDialogFragment";

    private String mDialogTitle = "MenuDialog";
    private ListView mMenuList;
    private MenuListAdapter mAdapter;
    private List<MenuListAdapter.MenuOption> mMenuOptionList = new ArrayList<>();
    private OnMenuDialogSelectListener mOnMenuDialogSelectListener;

    public MenuDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_comiket_circle_menu_dialog, null, false);

        mAdapter = new MenuListAdapter(getActivity());
        mAdapter.addAll(mMenuOptionList);

        mMenuList = (ListView) view.findViewById(android.R.id.list);
        mMenuList.setAdapter(mAdapter);
        mMenuList.setOnItemClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mDialogTitle);
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setView(view);
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

    protected void setTitle(String title) {
        mDialogTitle = title;
    }

    protected void setMenuOptionList(List<MenuListAdapter.MenuOption> list) {
        mMenuOptionList = list;
    }
}
