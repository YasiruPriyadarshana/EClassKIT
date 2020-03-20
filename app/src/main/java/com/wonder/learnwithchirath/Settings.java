package com.wonder.learnwithchirath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wonder.learnwithchirath.Services.BackgroundService;


public class Settings extends AppCompatActivity {

    private boolean aBoolean,n;
    SharedPreferences sharedpreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedpreference= PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        Switch aSwitch=(Switch)findViewById(R.id.notification_switch);

        n=sharedpreference.getBoolean("bool",false);
        aSwitch.setChecked(n);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(Settings.this, "Service is started", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.this, BackgroundService.class);
                    intent.addCategory("MyServiceTag");
                    startService(intent);
                }else {
                    Toast.makeText(Settings.this, "Service is stopped", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.this, BackgroundService.class);
                    intent.addCategory("MyServiceTag");
                    stopService(intent);
                }
                aBoolean=isChecked;
                sharedpreference.edit().putBoolean("bool",aBoolean).apply();
            }
        });

    }


}
