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
        add(new Notification(1, "Androidアプリを公開しました", "Androidアプリを公開しました。\nよろしくお願いします。", "content"));
        add(new Notification(2, "バグ修正", "バグ修正を行いました。", "content"));
        add(new Notification(4, "アップデート", "新しく機能が追加されました。", "content"));
        add(new Notification(5, "バグ修正", "バグ修正を行いました。", "content"));
        add(new Notification(6, "アップデート", "新しく機能が追加されました。", "content"));
        add(new Notification(7, "バグ修正", "バグ修正を行いました。", "content"));
        add(new Notification(8, "アップデート", "新しく機能が追加されました。", "content"));
        add(new Notification(9, "バグ修正", "バグ修正を行いました。", "content"));
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
