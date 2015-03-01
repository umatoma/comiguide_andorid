package net.umatoma.comiguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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
            viewHolder.mColorLayout = (RelativeLayout) convertView.findViewById(R.id.color);
            viewHolder.mSpaceInfoView = (TextView) convertView.findViewById(R.id.space_info);
            viewHolder.mCircleNamaView = (TextView) convertView.findViewById(R.id.circle_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ComiketCircle circle = getItem(position);
        viewHolder.mColorLayout.setBackgroundColor(circle.getColorCode());
        viewHolder.mSpaceInfoView.setText(circle.getSpaceInfo());
        viewHolder.mCircleNamaView.setText(circle.getCircleName());

        return convertView;
    }

    public void updateItem(ComiketCircle new_circle) {
        int count = getCount();
        for (int i = 0; i < count; i++) {
            ComiketCircle circle = getItem(i);
            if (circle.getId() == new_circle.getId()) {
                insert(new_circle, i);
                remove(circle);
                notifyDataSetChanged();
                break;
            }
        }
    }

    private static class ViewHolder {
        public RelativeLayout mColorLayout;
        public TextView mSpaceInfoView;
        public TextView mCircleNamaView;
    }
}
