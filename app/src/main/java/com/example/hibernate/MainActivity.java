package com.example.hibernate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final int REQUEST_ADMIN_ENABLE = 1;

    TextView textView;
    SeekBar gravity_seekBar;
    Switch sleepModeSwitch;
    Switch alarmSwitch;
    Button lock_button;

    ActivityManager activityManager;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;


    Intent sleepModeServiceIntent;
    Intent lockServiceIntent;
    Intent lockServiceIntentWithThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        lockServiceIntent = new Intent(this, LockService.class);
//        startService(lockServiceIntent);


        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, DeviceAdminRec.class);
//        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

//        lock_button = findViewById(R.id.lock);
//        textView = findViewById(R.id.text_tv);
        sleepModeSwitch = findViewById(R.id.lock_service_switch);
        sleepModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!devicePolicyManager.isAdminActive(componentName)) {
                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "SOME BULLSHIT");
                        startActivityForResult(intent, REQUEST_ADMIN_ENABLE);
                    } else {
                        lockServiceIntentWithThread = new Intent(MainActivity.this, LockService.class);
                        startService(lockServiceIntentWithThread);
                        gravity_seekBar.setVisibility(View.VISIBLE);
                    }
                } else {
                    devicePolicyManager.removeActiveAdmin(componentName);
                    gravity_seekBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        initializeSleepModeSeekBar();
//        gravity_seekBar.setVisibility(View.VISIBLE);

//        lock_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sleepModeSwitch.isChecked()) {
//                    devicePolicyManager.lockNow();
//                }
//            }
//        });

        alarmSwitch = findViewById(R.id.alarm_switch);
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    devicePolicyManager.lockNow();
                }
            }
        });
    }

    private void initializeSleepModeSeekBar() {
        gravity_seekBar = findViewById(R.id.gravity_seekbar);
        gravity_seekBar.setVisibility(View.INVISIBLE);
        gravity_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
                Intent intent = new Intent(MainActivity.this, LockService.class);
                intent.putExtra(LockService.NEW_THRESHOLD_VALUE, (double) (90 - progress));
                startService(intent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(lockServiceIntentWithThread);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ADMIN_ENABLE){
            if (resultCode == RESULT_OK){
                Log.d(TAG, "onActivityResult: GOT PERMISSION");
                Toast.makeText(this, "LOCK FEATURE ACTIVATED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LockService.class);
                startService(intent);
            }else{
                Log.d(TAG, "onActivityResult: PERMISSION DENIED");
                Toast.makeText(this, "PERMISSION WOULD BE NEEDED", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
