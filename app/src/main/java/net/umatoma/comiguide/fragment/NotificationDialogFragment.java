package net.umatoma.comiguide.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.Notification;

public class NotificationDialogFragment extends DialogFragment {

    public static final String TAG = "NotificationDialogFragment";
    private Notification mNotification;

    public static NotificationDialogFragment newInstance(Notification notification) {
        NotificationDialogFragment instance = new NotificationDialogFragment();
        instance.setNotification(notification);
        return instance;
    }

    public NotificationDialogFragment() {}

    private void setNotification(Notification notification) {
        mNotification = notification;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_notification_dialog, null, false);

        if (mNotification != null) {
            TextView titleView = (TextView) view.findViewById(R.id.notification_title);
            TextView contentView = (TextView) view.findViewById(R.id.notification_content);

            titleView.setText(mNotification.getTitle());
            contentView.setText(mNotification.getContent());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onResume () {
        super.onResume();
        if (mNotification == null) {
            dismiss();
        }
    }

}
