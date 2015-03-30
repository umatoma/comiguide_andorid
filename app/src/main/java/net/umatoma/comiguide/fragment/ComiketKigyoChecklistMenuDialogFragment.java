package net.umatoma.comiguide.fragment;

import android.app.Dialog;
import android.os.Bundle;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.MenuListAdapter;
import net.umatoma.comiguide.model.ComiketKigyoChecklist;

import java.util.ArrayList;

public class ComiketKigyoChecklistMenuDialogFragment extends MenuDialogFragment {

    public static final String TAG = "ComiketKigyoChecklistMenuDialogFragment";

    public static final int MENU_EDIT = 1;
    public static final int MENU_DELETE = 2;

    public static ComiketKigyoChecklistMenuDialogFragment newInstance(ComiketKigyoChecklist checklist) {
        return new ComiketKigyoChecklistMenuDialogFragment(checklist);
    }

    public ComiketKigyoChecklistMenuDialogFragment() {
        // Required empty public constructor
    }

    private ComiketKigyoChecklistMenuDialogFragment(ComiketKigyoChecklist checklist) {
        setTitle(checklist.getComiketKigyo().getName());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<MenuListAdapter.MenuOption> list = new ArrayList<>();
        list.add(new MenuListAdapter.MenuOption(
                MENU_EDIT, R.drawable.ic_edit, getString(R.string.dialog_menu_edit)));
        list.add(new MenuListAdapter.MenuOption(
                MENU_DELETE, R.drawable.ic_delete, getString(R.string.dialog_menu_delete)));
        setMenuOptionList(list);

        return super.onCreateDialog(savedInstanceState);
    }
}
