package com.sportspartner.util.gcm_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.sportspartner.R;
import com.sportspartner.activity.NotificationActivity;
import com.sportspartner.models.Sport;
import com.sportspartner.util.NotificationDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * Created by yujiaxiao on 11/3/17.
 */

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
// [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String detail = data.getString("detail");
        String sender = data.getString("sender");
        String type  = data.getString("type");
        String timeString  = data.getString("time");
        String priorityString = data.getString("priority");
        Date date = new Date(Long.valueOf(timeString));
        int priority = Integer.valueOf(priorityString);
        Log.d(TAG, "From FROM: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Detail: " + detail);
        Log.d(TAG, "Sender: " + sender);
        Log.d(TAG, "Type: " + type);
        Log.d(TAG, "Time: " + date.toString());
        Log.d(TAG, "Priority: " + priorityString);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        //parse time
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String timeDbString = format.format(date);

        //insert the notification in SQL
        NotificationDBHelper dbHelper = NotificationDBHelper.getInstance(this);
        String uuid = UUID.randomUUID().toString();
        dbHelper.insert(uuid,title,detail,sender,type,timeDbString,priority);

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(title, detail);
        // [END_EXCLUDE]
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     * @param title GCM title.
     * @param content GCM content.
     */
    private void sendNotification(String title, String content) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.edit)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
