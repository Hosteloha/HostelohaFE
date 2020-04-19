package com.hosteloha.app.utils;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.hosteloha.R;
import com.hosteloha.app.MainActivity;
import com.hosteloha.app.log.HostelohaLog;

import androidx.core.app.NotificationCompat;

/**
 * {@link AppNotificationManager} is a class to manage all the activities related to application notifications.
 */
public class AppNotificationManager {
    private static NotificationManager mNotificationManager = null;
    private static NotificationCompat.Builder mNotificationBuilder_Files = null;

    private static NotificationManager getNotificationManager(Context context) {
        if (mNotificationManager == null) {
            HostelohaLog.debugOut("creating Hosteloha NotificationManager");
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    /**
     * To clear all the notifications, from the Notification bar.
     *
     * @param context
     */
    public static void clearAllNotification(Context context) {
        if (getNotificationManager(context) != null) {
            getNotificationManager(context).cancelAll();
        }
    }

    /**
     * updateFileUploadNotification to indicate the progress of the file uploaded in the notification bar.
     *
     * @param context
     * @param fileUploadStatus SUCCESS, IN-PROGRESS, FAILURE
     * @param fileProgress
     * @param filesStatus
     * @param completion
     */
    public static void updateFileUploadNotification(Context context, AppFileUploader.fileUploadStatus fileUploadStatus, double fileProgress, String filesStatus, String completion) {
        // If default notification builder is created.
        if (mNotificationBuilder_Files == null) {
            createFileNotificationBuilder(context);
        }
        // Update notification builder, based on file status
        if (fileUploadStatus == AppFileUploader.fileUploadStatus.SUCCESS) {
            mNotificationBuilder_Files.setContentTitle("Product images " + filesStatus + " uploaded")
                    .setProgress(100, 100, false)
                    .setContentText("success");
        } else if (fileUploadStatus == AppFileUploader.fileUploadStatus.PROGRESS) {
            mNotificationBuilder_Files.setContentText("Files:" + filesStatus + fileProgress + "%" + "-" + completion);
            mNotificationBuilder_Files.setProgress(100, (int) fileProgress, false);
        } else if (fileUploadStatus == AppFileUploader.fileUploadStatus.FAILURE) {
            mNotificationBuilder_Files.setContentTitle("Product images upload")
                    .setProgress(100, 100, false)
                    .setContentText("failed");
        }
        // Notify the updated builder.
        getNotificationManager(context).notify(Define.NOTIFICATION_PRODUCT_IMAGE, mNotificationBuilder_Files.build());
    }

    private static void createFileNotificationBuilder(Context context) {
        String channelID = Define.NOTIFICATION_CHANNEL_FILE_ID;
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            // In IMP_LOW NO VIBRATION IS ENABLED
            mChannel = new NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_LOW);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100});
            getNotificationManager(context).createNotificationChannel(mChannel);
        }
        // OnClick of notification, where to redirect, bundle and add product ID
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder_Files = new NotificationCompat.Builder(context, channelID);
        mNotificationBuilder_Files.setContentTitle("Product images uploading")
                .setContentText("in progress")
                .setSmallIcon(R.drawable.ic_cloud_upload)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setProgress(100, 0, true)
                .setOngoing(true)
                .build();
    }
}
