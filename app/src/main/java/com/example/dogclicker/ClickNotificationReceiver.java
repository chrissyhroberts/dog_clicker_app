package com.example.dogclicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class ClickNotificationReceiver extends BroadcastReceiver {
    static final String ACTION_CLICK = "com.example.dogclicker.NOTIFICATION_CLICK";
    static final String EXTRA_COUNT = "count";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_CLICK.equals(intent.getAction())) {
            ClickSound.playCount(context, intent.getIntExtra(EXTRA_COUNT, 1));
        }
    }
}
