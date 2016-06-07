package com.gc.x44_rocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startRocket(View view){
    	startService(new Intent(this, RocketService.class));
    	finish();
    }
  
    public void stopRocket(View view){
    	stopService(new Intent(this, RocketService.class));
    }
}
