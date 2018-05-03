package com.legalpedia.android.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.legalpedia.android.app.LoginActivity;
import com.legalpedia.android.app.MainActivity;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class LicenseCheckReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager am= (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);



        Intent i = new Intent();
        i.setClass(context,LoginActivity.class);
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        //i.setFlags(Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


        /**
         *  Uri notifikacijaAlarma = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
         final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notifikacijaAlarma);
         r.play();

         PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
         WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "TRAININGCOUNDOWN");
         wl.acquire();

         Intent i = new Intent(getBaseContext(), GlavniIzbornik.class);
         i.setAction(Intent.ACTION_MAIN);
         i.addCategory(Intent.CATEGORY_LAUNCHER);

         startActivity(i);
         wl.release();
         */
    }
}
