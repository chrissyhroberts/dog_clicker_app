package com.example.dogclicker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

abstract class ClickerWidgetBase extends AppWidgetProvider {
    private static final String ACTION_WIDGET_CLICK = "com.example.dogclicker.WIDGET_CLICK";

    protected abstract int clickCount();

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        int count = clickCount();
        for (int id : ids) {
            Intent intent = new Intent(context, getClass())
                    .setAction(ACTION_WIDGET_CLICK)
                    .putExtra(ClickNotificationReceiver.EXTRA_COUNT, count);
            PendingIntent pending = PendingIntent.getBroadcast(
                    context, id, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clicker_widget);
            views.setTextViewText(R.id.clicker_widget_button, String.valueOf(count));
            views.setOnClickPendingIntent(R.id.clicker_widget_button, pending);
            manager.updateAppWidget(id, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_WIDGET_CLICK.equals(intent.getAction())) {
            ClickSound.playCount(context, intent.getIntExtra(ClickNotificationReceiver.EXTRA_COUNT, clickCount()));
        }
    }
}
