package edu.ptit.ql_fresher;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {
    final String CHANNEL_ID = "101";
    @SuppressLint("NotificationPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotify")
                && intent.getStringExtra("Name")!=null
                && intent.getStringExtra("Price")!=null
                && intent.getStringExtra("Description")!=null ){
            Log.e("Rev","rev");
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(
                        CHANNEL_ID,
                        "Channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel1.setDescription("This is Channel 1");
                manager.createNotificationChannel(channel1);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_add_task_black_18dp)
                    .setContentTitle(intent.getStringExtra("Name"))
                    .setContentText(intent.getStringExtra("Price") + " " + intent.getStringExtra("Description"))
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setColor(Color.RED)
                    .setAutoCancel(true);
            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0, i,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            builder.setContentIntent(pendingIntent);
            manager.notify(12345, builder.build());
        }
    }
}