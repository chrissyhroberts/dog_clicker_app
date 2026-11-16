package com.example.dogclicker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

final class ClickNotification {
    static final String CHANNEL_ID = "dog_clicker_controls";
    static final int NOTIFICATION_ID = 1001;

    private ClickNotification() { }

    static void ensureChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Dog Clicker", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Lock-screen clicker control");
            channel.setShowBadge(false);
            context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    static void show(Context context) {
        if (Build.VERSION.SDK_INT >= 33
                && context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Notification.Builder builder = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(context, CHANNEL_ID)
                : new Notification.Builder(context);
        RemoteViews compact = buildCompactView(context);
        Notification notification = builder
                .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
                .setContentTitle("Dog Clicker")
                .setContentText("Tap 1, 2, or 3 for training clicks")
                .setCategory(Notification.CATEGORY_STATUS)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setShowWhen(false)
                .setCustomContentView(compact)
                .setCustomBigContentView(compact)
                .setCustomHeadsUpContentView(compact)
                .build();
        context.getSystemService(NotificationManager.class).notify(NOTIFICATION_ID, notification);
    }

    private static RemoteViews buildCompactView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.notification_compact);
        views.setTextViewText(R.id.notification_title, "DOG CLICKER");
        for (int count = 1; count <= 3; count++) {
            Intent clickIntent = new Intent(context, ClickNotificationReceiver.class)
                    .setAction(ClickNotificationReceiver.ACTION_CLICK)
                    .putExtra(ClickNotificationReceiver.EXTRA_COUNT, count);
            PendingIntent pending = PendingIntent.getBroadcast(
                    context, 10 + count, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            int buttonId = count == 1 ? R.id.notification_one
                    : count == 2 ? R.id.notification_two : R.id.notification_three;
            views.setOnClickPendingIntent(buttonId, pending);
        }
        return views;
    }

}
