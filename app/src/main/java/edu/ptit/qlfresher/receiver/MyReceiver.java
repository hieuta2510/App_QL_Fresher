package edu.ptit.qlfresher.receiver;

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

import edu.ptit.qlfresher.activity.MainActivity;
import edu.ptit.qlfresher.R;

public class MyReceiver extends BroadcastReceiver {
    final String CHANNEL_ID = "101";
    @SuppressLint("NotificationPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotifyAddCenter")
                && intent.getStringExtra("acronym")!=null){
            String contentValue = intent.getStringExtra("acronym") + " " + context.getResources().getString(R.string.add_new_center);
            setNotify(context, contentValue, 0);
        } else if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotifyUpdateCenter")
                && intent.getStringExtra("acronym")!=null){
            String contentValue = intent.getStringExtra("acronym") + " " + context.getResources().getString(R.string.edit_center);
            setNotify(context, contentValue, 1);
        } else if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotifyDeleteCenter")
                && intent.getStringExtra("acronym")!=null){
            String contentValue = intent.getStringExtra("acronym") + " " + context.getResources().getString(R.string.delete_center);
            setNotify(context, contentValue, 2);
        } else if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotifyAddFresher")
                && intent.getStringExtra("fresherName")!=null){
            String contentValue = intent.getStringExtra("fresherName") + " " + context.getResources().getString(R.string.add_new_fresher);
            setNotify(context, contentValue, 3);
        }else if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotifyUpdateFresher")
                && intent.getStringExtra("fresherName")!=null){
            String contentValue = intent.getStringExtra("fresherName") + " " + context.getResources().getString(R.string.edit_fresher);
            setNotify(context, contentValue, 4);
        } else if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotifyDeleteFresher")
                && intent.getStringExtra("fresherName")!=null){
            String contentValue = intent.getStringExtra("fresherName") + " " + context.getResources().getString(R.string.delete_fresher);
            setNotify(context, contentValue, 5);
        }
    }

    private void setNotify(Context context, String contentValue, int requestCode){
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
                .setContentTitle("App QL Fresher")
                .setContentText(contentValue)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(Color.RED)
                .setAutoCancel(true);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        requestCode, i,
                        PendingIntent.FLAG_ONE_SHOT
                );
        builder.setContentIntent(pendingIntent);
        manager.notify(12345, builder.build());
    }
}