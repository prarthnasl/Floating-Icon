package com.prarthnasl.floatingicon.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by prarthnasl on 4/2/2016.
 */
public class StartupServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            context.startService(new Intent(context, BackgroundService.class));

        }
    }
}
