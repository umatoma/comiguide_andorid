package net.umatoma.comiguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.Notification;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    public NotificationListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_notification_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) convertView.findViewById(android.R.id.title);
            viewHolder.summaryView = (TextView) convertView.findViewById(android.R.id.summary);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Notification notification = getItem(position);
        viewHolder.titleView.setText(notification.getTitle());
        viewHolder.summaryView.setText(notification.getSummary());

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleView;
        public TextView summaryView;
    }
}
