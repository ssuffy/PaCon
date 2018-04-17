package com.sofi.pacon.listener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.sofi.pacon.NewDayActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //you might want to check what's inside the Intent
        if (intent.getStringExtra("Notification") != null &&
                intent.getStringExtra("Notification").equals("mDoNotify")) {
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "PaconNotification")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setContentTitle("It's time")
                    .setContentText("Don't forget!")
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Intent i = new Intent(context, NewDayActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            i,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            // example for blinking LED
            mBuilder.setLights(0xFFb71c1c, 1000, 2000);
            mBuilder.setContentIntent(pendingIntent);
            mNotifyMgr.notify(12345, mBuilder.build());

        }

    }
}
