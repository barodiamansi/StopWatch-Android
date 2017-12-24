package com.chromatic.marion.stopwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static Boolean running = false;
    private static Boolean paused = false;
    private static long startTime = 0;
    private static long pauseTime = 0;
    private static TextView timerTV;
    private Handler timerHandler;
    private static String pauseTimerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.timerHandler = new Handler();

        final Button resetButton = findViewById(R.id.resetButton);
        resetButton.setVisibility(View.GONE);

        final Button startButton = findViewById(R.id.startButton);
        startButton.setVisibility(View.VISIBLE);

        this.timerTV = findViewById(R.id.timerTV);
        this.timerTV.setText("00:00:00");

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!MainActivity.running && !MainActivity.paused) {
                    MainActivity.running = true;
                    MainActivity.paused = false;
                    MainActivity.startTime = System.currentTimeMillis();
                    startTimer();
                    startButton.setText("PAUSE");
                }
                else if (MainActivity.running && !MainActivity.paused) {
                    MainActivity.running = false;
                    stopTimer();
                    MainActivity.paused = true;
                    MainActivity.pauseTime = System.currentTimeMillis();
                    MainActivity.pauseTimerText = timerText();
                    startButton.setText("RESUME");
                }
                else if(MainActivity.paused) {
                    MainActivity.running = true;
                    MainActivity.paused = false;
                    startTimer();
                    startButton.setText("PAUSE");
                }

                resetButton.setVisibility(View.VISIBLE);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.running = false;
                MainActivity.paused = false;
                MainActivity.pauseTime = 0;
                MainActivity.timerTV.setText("00:00:00");
                resetButton.setVisibility(View.GONE);
                startButton.setText("START");
            }
        });
    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!MainActivity.running) {
                return;
            }
            if (pauseTimerText != null)
            {
                MainActivity.timerTV.setText(pauseTimerText);
                pauseTimerText = null;
            }
            else
            {
                MainActivity.timerTV.setText(timerText());
            }
            timerHandler.postDelayed(timerRunnable, 100);
        }
    };

    void startTimer() {
        MainActivity.running = true;
        timerRunnable.run();
    }

    void stopTimer() {
        MainActivity.running = false;
        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.removeCallbacksAndMessages(null);
    }

    public long getElapsedTimeSecs() {
        if (MainActivity.running) {
            return ((System.currentTimeMillis() - MainActivity.startTime) / 1000) % 60;
        }
        else if (MainActivity.pauseTime > 0) {
            return ((MainActivity.pauseTime - MainActivity.startTime) / 1000) % 60;
        }
        return 0;
    }

    public long getElapsedTimeMin() {
        if (MainActivity.running) {
           return (((System.currentTimeMillis() - MainActivity.startTime) / 1000) / 60 ) % 60;
        }
        else if (MainActivity.pauseTime > 0) {
            return (((MainActivity.pauseTime - MainActivity.startTime) / 1000) / 60 ) % 60;
        }
        return 0;
    }

    public long getElapsedTimeHour() {
        if (MainActivity.running) {
            return ((((System.currentTimeMillis() - MainActivity.startTime) / 1000) / 60 ) / 60);
        }
        else if (MainActivity.pauseTime > 0) {
            return ((((MainActivity.pauseTime - MainActivity.startTime) / 1000) / 60 ) / 60);
        }
        return 0;
    }

    public String timerText() {

        String hourText = getElapsedTimeHour() == 0 ? "00" : Long.toString(getElapsedTimeHour());
        String minText = getElapsedTimeMin() == 0 ? "00" : Long.toString(getElapsedTimeMin());
        String secText = getElapsedTimeSecs() == 0 ? "00" : Long.toString(getElapsedTimeSecs());

        return hourText + ":" + minText + ":" + secText;
    }
}
