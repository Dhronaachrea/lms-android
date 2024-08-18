package com.skilrock.lms.ui;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.notification.GCMDialogActivity;
import com.skilrock.config.VariableStorage;

import java.util.Random;

/**
 * Created by vishal on 6/1/2015.
 */
public class MyService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private AlertDialog.Builder internetDialog;
    private int index;
    private int notoficationId;
    private int largeIcon;
    private String value;

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString(), "", "");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " +
                // extras.toString(), "", "");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                if (extras.getString("message") != null) {
                    if (extras.getString("isClick") != null && extras.getString("isClick").equals("true")) {
                        value = extras.getString("gameTypeId");
                        VariableStorage.GlobalPref.setBooleanPreferences(this, VariableStorage.GlobalPref.SLE_NOTIFICATION, true);
                    } else {
                        value = "-1";
                        VariableStorage.GlobalPref.setBooleanPreferences(this, VariableStorage.GlobalPref.SLE_NOTIFICATION, false);
                    }
                    sendGCMIntent(getApplicationContext(), extras.getString("message"), extras.getString("title"), extras.getString("isClick") != null ? extras.getString("isClick") : "false", value != null ? value : "-1");
                }
                //sendNotification(extras.getString("message"), extras.getString("service_id"), extras.getString("user_id"));
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendGCMIntent(Context ctx, String message, String title, String isClick, String gameTypeId) {

//        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
//        Uri geoUri = Uri
//                .parse("geo:0,0?q=" + Uri.encode("28.498471,77.093586"));
//        mapIntent.setData(geoUri);
//        PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0,
//                mapIntent, 0);
//
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://www.skilrock.com"));
//        PendingIntent browserPendingIntent = PendingIntent.getActivity(this, 0,
//                browserIntent, 0);

        Intent intent = new Intent(getApplicationContext(),
                GCMDialogActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("isClick", isClick);
        intent.putExtra("gameTypeId", gameTypeId);
        if (isClick.equals("true"))
            largeIcon = R.drawable.ic_launcher;
        else
            largeIcon = R.drawable.ic_launcher;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx,
                new Random().nextInt(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.setBigContentTitle(title);
            style.bigText(message);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(style)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setTicker(title)
                    .setContentText(message)
                    .setAutoCancel(true)
//                .addAction(R.drawable.sports, "Open Web", browserPendingIntent)
//                .addAction(R.drawable.pic, "Locate Us", mapPendingIntent)
                    /*.setLargeIcon(
                            BitmapFactory.decodeResource(getResources(),
                                    largeIcon))*/;
            notificationBuilder.setStyle(style);
            // Get an instance of the NotificationManager service
            NotificationManagerCompat notificationManager = NotificationManagerCompat
                    .from(this);

            // Build the notification and issues it with notification manager.
            notificationManager.notify(notoficationId, notificationBuilder.build());
            notoficationId++;

            playDefaultNotificationSound();
        } catch (Exception e) {

        }
    }

    private void playDefaultNotificationSound() {

        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                notification);
        r.play();
    }

}
