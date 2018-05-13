package com.example.hoshiko.gamemyself;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(4000);
                    Intent startGame = new Intent(getApplicationContext(), MainActivity .class);
                    startActivity(startGame);
                    finish();

                }catch (InterruptedException e){e.printStackTrace();};
            }
        };  // end Thread
        myThread.start();




    }
};
