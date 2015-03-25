package net.umatoma.comiguide.fragment;


import android.app.Dialog;
import android.os.Bundle;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.MenuListAdapter;
import net.umatoma.comiguide.model.ComiketCircle;

import java.util.ArrayList;

public class ComiketCircleMenuDialogFragment extends MenuDialogFragment {

    public static final String TAG = "ComiketCircleMenuDialogFragment";

    public static final int MENU_MAP = 1;
    public static final int MENU_EDIT = 2;
    public static final int MENU_DELETE = 3;

    public static ComiketCircleMenuDialogFragment newInstance(ComiketCircle circle) {
        return new ComiketCircleMenuDialogFragment(circle);
    }

    public ComiketCircleMenuDialogFragment() {
        // Required empty public constructor
    }

    private ComiketCircleMenuDialogFragment(ComiketCircle circle) {
        setTitle(circle.getCircleName());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<MenuListAdapter.MenuOption> list = new ArrayList<>();
        list.add(new MenuListAdapter.MenuOption(
                MENU_MAP, R.drawable.ic_map_marker, getString(R.string.dialog_comiket_circle_menu_show)));
        list.add(new MenuListAdapter.MenuOption(
                MENU_EDIT, R.drawable.ic_edit, getString(R.string.dialog_comiket_circle_menu_edit)));
        list.add(new MenuListAdapter.MenuOption(
                MENU_DELETE, R.drawable.ic_delete, getString(R.string.dialog_comiket_circle_menu_delete)));
        setMenuOptionList(list);

        return super.onCreateDialog(savedInstanceState);
    }
}
