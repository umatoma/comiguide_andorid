package net.umatoma.comiguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.ComiketCircle;

public class ComiketCircleArrayAdapter
        extends ArrayAdapter<ComiketCircle> {

    public ComiketCircleArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_comiket_circle, null);
            viewHolder = new ViewHolder();
            viewHolder.mCircleNamaView = (TextView) convertView.findViewById(R.id.circle_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ComiketCircle comiketCircle = getItem(position);
        viewHolder.mCircleNamaView.setText(comiketCircle.getCircleName());

        return convertView;
    }

    private static class ViewHolder {
        public TextView mCircleNamaView;
    }
}
