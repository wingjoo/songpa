package com.example.wing.workingsongpa;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


/**
 * Created by knightjym on 2017. 3. 9..
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 3000);// 3 초
    }

}
