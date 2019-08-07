package com.example.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

public class MessagesBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = RemoteInput.getResultsFromIntent(intent);
        if (null != bundle) {
            String message = bundle.getString("our_remote_input");
            MessageModel messageModel = new MessageModel("Meisam", message);
            MainActivity.messages.add(messageModel);

            MainActivity.sendMessagingStyleNotification(context);
        }
    }
}
