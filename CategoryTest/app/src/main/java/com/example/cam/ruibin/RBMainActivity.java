package com.example.cam.ruibin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cam.categorytest.R;
import com.example.cam.server.RecoreServer;

public class RBMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(), RecoreServer.class));
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
        DeamonService.start(this);
    }
}
