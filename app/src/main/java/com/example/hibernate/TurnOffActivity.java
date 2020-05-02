package com.example.hibernate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.WindowManager;


public class TurnOffActivity extends AppCompatActivity {
    ScreenStatusReceiver screenStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_off);

//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.screenBrightness = 0;
//        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getWindow().setAttributes(params);
//
//        screenStatusReceiver = getIntent().getParcelableExtra("receiver");
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = null;
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LockServiceWithThread : wake Lock");
            try {
                wakeLock.release();
            } catch (Exception e) {

            }
            wakeLock.acquire();
            try {
                wakeLock.release();
            }catch (Exception e){

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(screenStatusReceiver, intentFilter);
//        Intent intent = new Intent(Intent.ACTION_SCREEN_ON);
////        sendBroadcast(intent);
//        screenStatusReceiver.onReceive(this, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
