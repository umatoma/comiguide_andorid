package net.umatoma.comiguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.umatoma.comiguide.R;

public class MenuListAdapter extends ArrayAdapter<MenuListAdapter.MenuOption> {
    public MenuListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_menu_list, null);
            viewHolder = new ViewHolder();
            viewHolder.menuIcon = (ImageView) convertView.findViewById(android.R.id.icon);
            viewHolder.menuText = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MenuOption option = getItem(position);
        viewHolder.menuIcon.setImageResource(option.menuIconId);
        viewHolder.menuText.setText(option.menuText);

        return convertView;
    }

    public static class MenuOption {

        private int menuIconId;
        private String menuText;

        public MenuOption(int icon_id, String menu_text) {
            menuIconId = icon_id;
            menuText = menu_text;
        }
    }

    private final static class ViewHolder {
        public ImageView menuIcon;
        public TextView menuText;
    }
}
