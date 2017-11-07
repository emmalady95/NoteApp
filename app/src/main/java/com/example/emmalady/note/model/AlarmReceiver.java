package com.example.emmalady.note.model;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.emmalady.note.activity.MainActivity;
import com.example.emmalady.note.model.Notes;
import com.example.emmalady.note.service.AlarmService;

/**
 * Created by Liz Nguyen on 03/11/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String KEY_NOTE = "NOTE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmService.class);
        i.putExtra(KEY_NOTE, (Notes) intent.getExtras().getSerializable(KEY_NOTE));
        context.startService(i);

    }

}
