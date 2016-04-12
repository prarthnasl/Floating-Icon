package com.prarthnasl.floatingicon.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.prarthnasl.floatingicon.R;

import com.prarthnasl.floatingicon.constants.Constants;
import com.prarthnasl.floatingicon.controllers.SelectionWindowActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by prarthnasl on 4/3/2016.
 */
public class BackgroundService extends Service {

    private WindowManager windowManager;
    private ImageView floatingIcon;
    private boolean isVisible = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        floatingIcon = new ImageView(this);
        floatingIcon.setImageResource(R.drawable.ic_floating_icon);

        BroadcastReceiver appRunning = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String topActivity = intent.getExtras().getString("top_activity");
                if (topActivity != null && (topActivity.contains(Constants.WHATSAPP_PACKAGE_NAME) || topActivity.contains(SelectionWindowActivity.TAG))) {
                    if (!isVisible) {
                        showIcon();
                    }
                } else {
                    if (floatingIcon.isShown()) {
                        if (floatingIcon != null) windowManager.removeView(floatingIcon);
                        isVisible = false;
                    }
                }
                Log.i("TopActivity", topActivity);
            }
        };

        registerReceiver(appRunning, new IntentFilter("APP_RUNNING"));

        Timer TIMER = new Timer(true);
        TIMER.scheduleAtFixedRate(new BackgroundTimerTask(), 1000, 250);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingIcon != null) windowManager.removeView(floatingIcon);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);
    }

    private class BackgroundTimerTask extends TimerTask {

        @Override
        public void run() {
            Intent i = new Intent(BackgroundService.this, AppAccessibilityService.class);
            startService(i);
        }
    }

    private void showIcon() {
        isVisible = true;
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(floatingIcon, params);

        try {
            floatingIcon.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            long pressTime = System.currentTimeMillis();
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatingIcon, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }

        floatingIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(BackgroundService.this, SelectionWindowActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
}