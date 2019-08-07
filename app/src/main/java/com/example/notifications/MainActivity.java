package com.example.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.Person;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button btnSend;

    public static ArrayList<MessageModel> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messages.add(new MessageModel("Sarah", "Hello everyone"));
        messages.add(new MessageModel("Brad", "Hi Sarah!"));
        messages.add(new MessageModel("Tom", "How is everybody?"));

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendNotification();
//                new DownloadAsyncTask().execute();
//                sendLargeTextNotification();
//                sendBigPictureNotification();

//                sendInboxNotification();
//                sendMediaNotification();
//                sendMessagingStyleNotification(MainActivity.this);

//                checkNotificationsSettings();
//                sendAGroupOfNotifications();

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    checkNotificationChannelSettings();
//                }

                sendCustomNotification();
            }
        });
    }

    private void sendCustomNotification () {
        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        expandedView.setOnClickPendingIntent(R.id.billie, pendingIntent);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.billie);

        expandedView.setImageViewBitmap(R.id.image, bitmap);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setContentTitle("Notification")
                .setContentText("This is going to be ignored")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_bell)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }

    @RequiresApi(26)
    private void checkNotificationChannelSettings () {
        NotificationManager manager = getSystemService(NotificationManager.class);
        NotificationChannel channel = manager.getNotificationChannel("channel1");
        if (null != channel) {
            if (channel.getImportance() != NotificationManager.IMPORTANCE_NONE) {

            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Notification channel is not enabled")
                        .setMessage("We need to send notifications on this channel")
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                                intent.putExtra(Settings.EXTRA_CHANNEL_ID, "channel1");
                                startActivity(intent);
                            }
                        });
                builder.show();
            }
        }
    }

    private void checkNotificationsSettings () {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (!manager.areNotificationsEnabled()) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setTitle("Notifications are disabled")
                    .setMessage("Please enable the notifications")
                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        }
                    });
            alertBuilder.show();
        }else {
            Toast.makeText(this, "Notifications are enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendAGroupOfNotifications() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("First Notification")
                .setContentText("This is the first notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("first_group");

        NotificationCompat.Builder secondBuilder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Second Notification")
                .setContentText("This is the second notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("first_group");

        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Summary Notification")
                .setContentText("2 New Notifications")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("first_group")
                .setGroupSummary(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        SystemClock.sleep(2000);
        manager.notify(1, builder.build());

        SystemClock.sleep(2000);
        manager.notify(2, secondBuilder.build());

        SystemClock.sleep(2000);
        manager.notify(3, summaryBuilder.build());
    }

    public static void sendMessagingStyleNotification (Context context) {

        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(new Person.Builder()
                .setName("Meisam").build());
        style.setConversationTitle("Family Group");

        for (MessageModel m: messages) {
            style.addMessage(new NotificationCompat.MessagingStyle.Message(m.getText(), SystemClock.currentThreadTimeMillis(),
                    new Person.Builder().setName(m.getSender()).build()));
        }

        RemoteInput remoteInput = new RemoteInput.Builder("our_remote_input")
                .setLabel("Your answer here")
                .build();

        Intent intent = new Intent(context, MessagesBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_bell,
                "Reply",
                pendingIntent
        ).addRemoteInput(remoteInput).build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Messaging notification")
                .setContentText("Messages")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(style)
                .addAction(action)
                .setColor(Color.GREEN);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());

    }

    private void sendMediaNotification () {

        Bitmap cover = BitmapFactory.decodeResource(getResources(), R.mipmap.billie);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Bad guy")
                .setContentText("By Billie Eilish")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_dislike, "Dislike", null)
                .addAction(R.drawable.ic_previous, "Previous", null)
                .addAction(R.drawable.ic_pause, "Pause", null)
                .addAction(R.drawable.ic_next, "Next", null)
                .addAction(R.drawable.ic_like, "Like", null)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1,2,3))
                .setLargeIcon(cover);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }

    private void sendInboxNotification () {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Inbox Notification")
                .setContentText("Somebody liked your post")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("First Line")
                        .addLine("Second Line")
                        .addLine("Third Line")
                        .addLine("Forth Line")
                        .addLine("Fifth Line")
                        .addLine("Sixth Line")
                        .addLine("Seventh Line")
                        .addLine("Eighth Line")
                );

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }

    private void sendBigPictureNotification () {

        Bitmap moon = BitmapFactory.decodeResource(getResources(), R.mipmap.moon);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Big Picture Notification")
                .setContentText("Somebody liked your post")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(moon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle("Big picture Notification")
                        .bigPicture(moon)
                        .bigLargeIcon(null));

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }

    private void sendLargeTextNotification () {

        String largeText = "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections. The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. \"What's happened to me?\" he thought. It wasn't a dream. His room, a proper human room although a little too small, lay peacefully between its four familiar walls. A collection of textile samples lay spread out on the table - Samsa was a travelling salesman - and above it there hung a picture that he had recently cut out of an illustrated magazine and housed in a nice, gilded frame. It showed a lady fitted out with a fur hat and fur boa who sat upright, raising a heavy fur muff that covered the whole of her lower arm towards the viewer. Gregor then turned to look out the window at the dull weather. Drops";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("Large text Notification")
                .setContentText("This is the content of the notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .setBigContentTitle("Large Text Title")
                    .bigText(largeText)
                    .setSummaryText("This is the content of the notification"));
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }

    private void sendNotification() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, intent, 0);

        Intent actionIntent = new Intent(this, ToastReceiver.class);
        actionIntent.putExtra(getResources().getString(R.string.message), "It's a toast message");
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, 3, actionIntent, 0);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_add,
                "Toast",
                actionPendingIntent
        ).build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setContentTitle("Hello")
                .setSmallIcon(R.drawable.ic_bell)
                .setContentText("This is my first notification")
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setColor(Color.GREEN)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(action);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }

    private class DownloadAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i=0; i<100; i+=10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "channel1")
                    .setContentTitle("Downloading")
                    .setProgress(100, values[0], true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_bell)
                    .setOnlyAlertOnce(true);
            NotificationManagerCompat manager = NotificationManagerCompat.from(MainActivity.this);
            manager.notify(5, builder.build());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "channel1")
                    .setContentTitle("Downloaded")
                    .setContentText("Download Finished")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_bell)
                    .setOnlyAlertOnce(true);
            NotificationManagerCompat manager = NotificationManagerCompat.from(MainActivity.this);
            manager.notify(5, builder.build());
        }
    }
}
