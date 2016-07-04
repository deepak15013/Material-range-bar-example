package com.iiitd.timeline;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appyvet.rangebar.RangeBar;

public class MainActivity extends AppCompatActivity {

    private RangeBar rbView;
    private ToggleButton btnStartThread;
    private Chronometer chronoTimer;
    private Button btnTrim;
    private TextView tvMinutes;
    private TextView tvValue;

    private volatile boolean threadRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbView = (RangeBar) findViewById(R.id.rb_view);
        btnStartThread = (ToggleButton) findViewById(R.id.btn_start_thread);
        chronoTimer = (Chronometer) findViewById(R.id.chronoTimer);
        btnTrim = (Button) findViewById(R.id.btn_trim);
        tvMinutes = (TextView) findViewById(R.id.tv_minutes);
        tvValue = (TextView) findViewById(R.id.tv_value);

        btnStartThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnStartThread.getText().toString().equalsIgnoreCase("on")) {
                    Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
                    chronoTimer.setBase(SystemClock.elapsedRealtime());
                    chronoTimer.start();
                    threadRunning = true;
                    startThread();
                }
                else if(btnStartThread.getText().toString().equalsIgnoreCase("OFF")) {
                    Toast.makeText(MainActivity.this, "stop", Toast.LENGTH_SHORT).show();
                    threadRunning = false;
                    chronoTimer.stop();
                }
            }
        });

        btnTrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("rangebar","leftIndex: "+rbView.getLeftIndex());
                Log.v("rangebar","leftPinValue: "+rbView.getLeftPinValue());
                Log.v("rangebar","rightIndex: "+rbView.getRightIndex());
                Log.v("rangebar","rightPinValue: "+rbView.getRightPinValue());
                Log.v("rangebar","left: "+rbView.getLeft());
                Log.v("rangebar","right: "+rbView.getRight());
                Log.v("rangebar","tickStart: "+rbView.getTickStart());
                Log.v("rangebar","tickEnd: "+rbView.getTickEnd());
                tvValue.setText(getString(R.string.tv_value, rbView.getLeftPinValue(), rbView.getRightPinValue()));
            }
        });

        rbView.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

            }
        });

        chronoTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Log.v("chrono","Time: "+chronometer.getText());
            }
        });

    }

    public void startThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int tickCount = 0;

                while(threadRunning) {
                    try {
                        Thread.sleep(5000);
                        tickCount++;
                        setTickData(tickCount);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    public void setTickData(final int tickCount) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v("rangebar","tickCount: "+tickCount);
                rbView.setTickEnd(tickCount);
                tvMinutes.setText(String.valueOf(tickCount));
            }
        });
    }
}
