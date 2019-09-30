package com.intpro.aero;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class StartActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler pHandler = new Handler();

    Animation bubble_view;
    ImageView think;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        think = findViewById(R.id.bubble);

        bubble_view = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rebounce);
        think.setAnimation(bubble_view);

        progressBar = findViewById(R.id.progressBar1);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.background),PorterDuff.Mode.SRC_IN);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus++;
                    android.os.SystemClock.sleep(20);
                    pHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }

                if(progressStatus == 100)
                {
                    Intent intent = new Intent(StartActivity.this, MainPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }
}
