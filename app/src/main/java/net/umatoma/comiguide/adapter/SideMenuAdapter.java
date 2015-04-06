package net.umatoma.comiguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.umatoma.comiguide.R;

public class SideMenuAdapter extends ArrayAdapter<SideMenuAdapter.MenuEnum> {

    static class ViewHolder {
        ImageView menuIcon;
        TextView menuText;
    }

    public static enum MenuEnum {
        COMIKET_CIRCLE,
        COMIKET_KIGYO,
        COMIC1,
        BLOG,
        SETTING
    }

    private static final SideMenuAdapter.MenuEnum[] mMenuArray = {
            MenuEnum.COMIKET_CIRCLE,
            MenuEnum.COMIKET_KIGYO,
            MenuEnum.COMIC1,
            MenuEnum.BLOG,
            MenuEnum.SETTING
    };

    private LayoutInflater mInflater;

    public SideMenuAdapter(Context context) {
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
            case BLOG:
                return getContext().getString(R.string.side_menu_text_blog);
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
            case BLOG:
                return R.drawable.ic_link;
            case SETTING:
                return R.drawable.ic_settings;
            default:
                return -1;
        }
    }
}
