package com.example.emmalady.note.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.emmalady.note.R;
import com.example.emmalady.note.activity.MainActivity;
import com.example.emmalady.note.model.Notes;

/**
 * Created by Liz Nguyen on 03/11/2017.
 */

public class AlarmService extends Service {

    public class ServiceBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }
    public final IBinder mBinder = new ServiceBinder();

    private static final int NOTIFICATION = 123;
    public static final String INTENT_NOTIFY = "com.example.emmalady.note.INTENT_NOTIFY";
    private NotificationManager notificationManager;

    public static final String KEY_NOTE = "NOTE";
    public static String NOTE_NOTIFICATION = "Note Notification!";
    public CharSequence text = "Your notification time is upon us.";

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int startId, int flags){
        if (!(intent == null || intent.getExtras() == null)) {
            Notes note = (Notes) intent.getExtras().getSerializable(KEY_NOTE);
            if (note != null) {
                showNotification(note);
            }
        }
        return Service.START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public void showNotification(Notes note) {
        if (note != null) {
            int icon = R.drawable.ic_alarm_gray_24dp;
            long time = note.getmAlarm().getTime();

            PendingIntent contentIntent = PendingIntent.getActivity(this, note.getmId(), new Intent(this, MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(icon)
                    .setContentTitle(NOTE_NOTIFICATION)
                    .setContentText(text)
                    .setContentIntent(contentIntent)
                    .setWhen(time);
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
            Notification notification = builder.build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(note.getmId(), notification);

            stopSelf();
        }
    }
}
