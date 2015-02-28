package net.umatoma.comiguide.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class KeyValuePairAdapter extends ArrayAdapter<Pair<String, String>> {

    public KeyValuePairAdapter(Context context, int resourceId) {
        super(context, resourceId);
    }

    public KeyValuePairAdapter(Context context, int resourceId, ArrayList<Pair<String, String>> items) {
        super(context, resourceId, items);
    }

    // For Spinner
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(pos, convertView, parent);
        textView.setText(getItem(pos).second);
        return textView;
    }

    // For Spinner Dropdown
    @Override
    public View getDropDownView(int pos, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(pos, convertView, parent);
        textView.setText(getItem(pos).second);
        return textView;
    }
}
