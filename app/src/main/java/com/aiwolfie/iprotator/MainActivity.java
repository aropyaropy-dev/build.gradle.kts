package com.aiwolfie.iprotator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import android.graphics.Color;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {

    TextView status, ip, timer;
    Button start, stop;
    Handler handler = new Handler();
    boolean running = false;
    int seconds = 5;

    Runnable rotation = new Runnable() {
        @Override
        public void run() {
            if (!running) return;

            seconds--;

            if (seconds <= 0) {
                seconds = 5;
                checkIP();
            }

            timer.setText("Rotasi berikutnya: " + seconds + " detik");
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        layout.setPadding(30, 60, 30, 30);
        layout.setBackgroundColor(Color.rgb(15, 20, 32));

        TextView title = new TextView(this);
        title.setText("IP ROTATOR");
        title.setTextSize(30);
        title.setTextColor(Color.WHITE);
        layout.addView(title);

        status = new TextView(this);
        status.setText("STATUS: DISCONNECTED");
        status.setTextColor(Color.YELLOW);
        layout.addView(status);

        ip = new TextView(this);
        ip.setText("PUBLIC IP: Checking...");
        ip.setTextColor(Color.WHITE);
        layout.addView(ip);

        timer = new TextView(this);
        timer.setText("INTERVAL: 5 SECONDS");
        timer.setTextColor(Color.WHITE);
        layout.addView(timer);

        start = new Button(this);
        start.setText("START");
        layout.addView(start);

        stop = new Button(this);
        stop.setText("STOP");
        layout.addView(stop);

        start.setOnClickListener(v -> {
            if (running) return;
            running = true;
            seconds = 5;
            status.setText("STATUS: RUNNING");
            checkIP();
            handler.post(rotation);
        });

        stop.setOnClickListener(v -> {
            running = false;
            handler.removeCallbacks(rotation);
            status.setText("STATUS: STOPPED");
            timer.setText("ROTATION STOPPED");
        });

        setContentView(layout);
        checkIP();
    }

    void checkIP() {
        new Thread(() -> {
            String result = "Unavailable";
            try {
                URL url = new URL("https://api.ipify.org");
                HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                InputStream in = conn.getInputStream();
                byte[] data = new byte[256];
                int n = in.read(data);
                result = new String(data, 0, n,
                    StandardCharsets.UTF_8);

                in.close();
                conn.disconnect();
            } catch (Exception e) {
                result = "Connection error";
            }

            final String finalResult = result;

            runOnUiThread(() ->
                ip.setText("PUBLIC IP: " + finalResult)
            );
        }).start();
    }
          }
