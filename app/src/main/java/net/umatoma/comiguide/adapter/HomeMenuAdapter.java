package net.umatoma.comiguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.umatoma.comiguide.R;

import java.util.ArrayList;

public class HomeMenuAdapter extends ArrayAdapter<HomeMenuAdapter.MenuEnum> {

    static class ViewHolder {
        ImageView menuIcon;
        TextView menuText;
    }

    public static enum MenuEnum {
        COMIKET_CIRCLE,
        COMIKET_KIGYO,
        COMIC1,
        COMIKET_INFO,
        SETTING
    }

    private static final HomeMenuAdapter.MenuEnum[] mMenuArray = {
            MenuEnum.COMIKET_CIRCLE,
            MenuEnum.COMIKET_KIGYO,
            MenuEnum.COMIC1,
            MenuEnum.COMIKET_INFO,
            MenuEnum.SETTING
    };

    private LayoutInflater mInflater;

    public HomeMenuAdapter(Context context) {
        super(context, R.layout.adapter_home_menu);
    }

    @Override
    public MenuEnum getItem(int position) {
        return mMenuArray[position];
    }

    @Override
    public int getCount() {
        return mMenuArray.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_home_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.menuIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
            viewHolder.menuText = (TextView) convertView.findViewById(R.id.menu_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MenuEnum menuEnum = getItem(position);
        viewHolder.menuIcon.setImageResource(getIconResource(menuEnum));
        viewHolder.menuText.setText(getMenuString(menuEnum));

        return convertView;
    }

    private String getMenuString(MenuEnum menuEnum) {
        switch (menuEnum) {
            case COMIKET_CIRCLE:
                return getContext().getString(R.string.side_menu_text_comiket_circle);
            case COMIKET_KIGYO:
                return getContext().getString(R.string.side_menu_text_comiket_kigyo);
            case COMIC1:
                return getContext().getString(R.string.side_menu_text_comic1);
            case COMIKET_INFO:
                return getContext().getString(R.string.side_menu_text_comiket_info);
            case SETTING:
                return getContext().getString(R.string.side_menu_text_setting);
            default:
                return null;
        }
    }

    private int getIconResource(MenuEnum menuEnum) {
        switch (menuEnum) {
            case COMIKET_CIRCLE:
            case COMIKET_KIGYO:
            case COMIC1:
                return R.drawable.ic_map_edithing;
            case COMIKET_INFO:
                return R.drawable.ic_question;
            case SETTING:
                return R.drawable.ic_settings;
            default:
                return -1;
        }
    }
}
