package com.conicskill.app.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.ui.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static com.google.firebase.remoteconfig.FirebaseRemoteConfig.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String ANDROID_CHANNEL_ID_DASHBOARD = BuildConfig.APPLICATION_ID + ".DASHBOARD";
    public static final String ANDROID_CHANNEL_NAME_DASHBOARD = "ANDROID CHANNEL DASHBOARD";
    public static int NOTIFICATION_ID = 100;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        for (Map.Entry mapElement : remoteMessage.getData().entrySet()) {
            String key = (String)mapElement.getKey();
            String value = ((String)mapElement.getValue());

            Log.e(TAG, "onMessageReceived: " + key + " : " + value);
        }
        String title = remoteMessage.getData().get("title");
        String message = (String) remoteMessage.getData().get("message");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
        } else {
            title = String.valueOf(Html.fromHtml(title));
        }

        String smallTitle = "";
        if(remoteMessage.getData().containsKey("smallTitle")) {
             smallTitle = remoteMessage.getData().get("smallTitle");
        }
        String imageUrl = "";
        if(remoteMessage.getData().containsKey("image")) {
            imageUrl = remoteMessage.getData().get("image");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            message = String.valueOf(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            message = String.valueOf(Html.fromHtml(message));
        }

        String isUpdate = "false";
        if (remoteMessage.getData().containsKey("isUpdate")) {
            isUpdate =  remoteMessage.getData().get("isUpdate");
        }

        showNotificationForDashboard(getApplicationContext(), title, message, imageUrl, smallTitle, isUpdate);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        tinyDB.putString(Constants.FCM_TOKEN, s);
    }

    public void showNotificationForDashboard(Context context, String title, String message, String url,
                                             String smallTitle, String isUpdate) {

        // create android channel
        Intent intent = null;
        PendingIntent pIntent = null;
        if(isUpdate.equalsIgnoreCase("false")) {
            intent = new Intent(context, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
            pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        //Toast.makeText(context,"Notification received: "+intent.getStringExtra("message"),Toast.LENGTH_LONG).show();

        PendingIntent finalPIntent = pIntent;
        Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID_DASHBOARD,
                            ANDROID_CHANNEL_NAME_DASHBOARD, NotificationManager.IMPORTANCE_DEFAULT);
                    // Sets whether notifications posted to this channel should display notification lights
                    androidChannel.enableLights(true);
                    // Sets whether notification posted to this channel should vibrate.
                    androidChannel.enableVibration(true);
                    // Sets the notification light color for notifications posted to this channel
                    androidChannel.setLightColor(ContextCompat.getColor(context, R.color.colorAccent));
                    // Sets whether notifications posted to this channel appear on the lockscreen or not
                    androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setSubText(smallTitle)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(androidChannel);
                        notificationManager.notify(NOTIFICATION_ID++, builder.build());
                    }
                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setSubText(smallTitle)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setChannelId(ANDROID_CHANNEL_ID_DASHBOARD)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID++, builder.build());
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID_DASHBOARD,
                            ANDROID_CHANNEL_NAME_DASHBOARD, NotificationManager.IMPORTANCE_DEFAULT);
                    // Sets whether notifications posted to this channel should display notification lights
                    androidChannel.enableLights(true);
                    // Sets whether notification posted to this channel should vibrate.
                    androidChannel.enableVibration(true);
                    // Sets the notification light color for notifications posted to this channel
                    androidChannel.setLightColor(ContextCompat.getColor(context, R.color.colorAccent));
                    // Sets whether notifications posted to this channel appear on the lockscreen or not
                    androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setSubText(smallTitle)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(androidChannel);
                        notificationManager.notify(NOTIFICATION_ID++, builder.build());
                    }
                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setSubText(smallTitle)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setChannelId(ANDROID_CHANNEL_ID_DASHBOARD)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID++, builder.build());
                }
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
            }
        });
    }
}
