package com.example.moviecatalogue4.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.moviecatalogue4.Model.Movie;
import com.example.moviecatalogue4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class ReleaseAlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION = 102;
    private static final  String NOTIFICATION_CHANNEL = "102";
    private static int notificationId = 2;
    private static final String INTENT_ID = "intent_id";
    private static final String INTENT_TITLE = "intent_title";

    public ReleaseAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String NOTIF_ID = "NotifId";
        int notifId = intent.getIntExtra(INTENT_ID, 0);
        Log.e(NOTIF_ID, String.valueOf(notifId));
        String title = intent.getStringExtra(INTENT_TITLE);
        Log.e(NOTIF_ID, title);
        showAlarmNotification(context, title, notifId);
    }

    public void showAlarmNotification(Context context, String title, int notifId){
        String message = "Movie Release Today";
        String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, BottomNavigationView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmRingtone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(NOTIFICATION_CHANNEL);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notifId, builder.build());
    }

    public void setRepeatingAlarm(Context context, List<Movie> movies){
        int delay = 0;
        String TITLE = "title";
        String RELEASE_SETUP = "Release alarm set up";

        for(Movie movie: movies){
            cancelAlarm(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReleaseAlarmReceiver.class);
            intent.putExtra(INTENT_TITLE, movie.getTitle());
            Log.e(TITLE, movie.getTitle());
            intent.putExtra(INTENT_ID, notificationId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + delay, pendingIntent);
            } else if (SDK_INT > Build.VERSION_CODES.KITKAT && SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + delay, AlarmManager.INTERVAL_DAY, pendingIntent);
            } else if (SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + delay, pendingIntent);
            }
            notificationId += 1;
            delay += 5000;
        }
        Toast.makeText(context, RELEASE_SETUP, Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent alarmIntent = new Intent(context, ReleaseAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION, alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
