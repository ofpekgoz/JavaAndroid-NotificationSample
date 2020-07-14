package com.omerfpekgoz.notifications_durumabaglbildirimolusturma;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BildirimYakalayici extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification bildirim=intent.getParcelableExtra("nesne");
        notificationManager.notify(1,bildirim);

    }
}
