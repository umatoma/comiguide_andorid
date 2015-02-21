package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.activity.ComiketCircleActivity;
import net.umatoma.comiguide.activity.SettingsActivity;
import net.umatoma.comiguide.adapter.SideMenuAdapter;


public class SideMenuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView mMenuList;
    private SideMenuAdapter mMenuAdapter;

    public static SideMenuFragment newInstance() {
        SideMenuFragment fragment = new SideMenuFragment();
        return fragment;
    }

    public SideMenuFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_side_menu, container, false);
        mMenuAdapter = new SideMenuAdapter(getActivity());
        mMenuList = (ListView)view.findViewById(R.id.menu_list);
        mMenuList.setAdapter(mMenuAdapter);
        mMenuList.setOnItemClickListener(new MenuItemClickListener());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class MenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SideMenuAdapter.MenuEnum menuEnum = mMenuAdapter.getItem(position);
            Intent intent;
            switch (menuEnum) {
                case COMIKET_CIRCLE:
                    intent = new Intent(getActivity(), ComiketCircleActivity.class);
                    startActivity(intent);
                    return;
                case SETTING:
                    intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return;
            }
        }
    }

}
