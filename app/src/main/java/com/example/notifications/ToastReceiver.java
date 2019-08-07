package com.example.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class ToastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            String message = intent.getStringExtra(context.getResources().getString(R.string.message));
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
//            manager.cancel(1);
            manager.cancelAll();
        }
    }
}
