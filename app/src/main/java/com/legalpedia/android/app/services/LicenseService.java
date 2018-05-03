package com.legalpedia.android.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by adebayoolabode on 11/7/16.
 */

public class LicenseService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent){
        //do the neccessary release of resources here
        return false;
    }
    public class LicenseBinder extends Binder {
        public LicenseService getService() {
            return LicenseService.this;
        }
    }
}
