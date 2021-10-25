package com.example.fitnesscounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    TextView countDownText;
    Button startpauseBtn, resetBtn;
    CountDownTimer countDownTimer;
    Workout workout;
    boolean timerRunning;
    long durationLeft;
    ArrayList<Workout> woHistory = new ArrayList<>();
    SharedPreferences sharedPreferences;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Fitness Counter");

        sharedPreferences = getSharedPreferences("WorkOut", MODE_PRIVATE);

        countDownText = findViewById(R.id.textViewCountDown);
        startpauseBtn = findViewById(R.id.startPauseBtn);
        resetBtn = findViewById(R.id.resetBtn);

        Intent intent = getIntent();
        workout = (Workout) intent.getSerializableExtra(MainActivity.INTENT_PARAM_WORKOUT);
        durationLeft = workout.getTime();

        startpauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning){
                    pauseTimer();
                }
                else{
                    startTimer();
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDown();
    }

    public void startTimer(){
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sixty);
        }

        countDownTimer = new CountDownTimer(durationLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                durationLeft = millisUntilFinished;
                updateCountDown();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                durationLeft = workout.getTime();
                workout.setDate();
                stopPlayer();
                updateCountDown();
                startpauseBtn.setText("START");

                saveProgress();
            }
        }.start();
        mediaPlayer.start();

        timerRunning = true;
        startpauseBtn.setText("PAUSE");
        resetBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    public void updateCountDown(){
        int seconds = (workout.getTime() / 1000) - (int) durationLeft / 1000;
        String outputText = String.format(Locale.getDefault(), "%02d", seconds);
        countDownText.setText(outputText);
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
        startpauseBtn.setText("RESUME");
        resetBtn.setVisibility(View.VISIBLE);
    }

    public void resetTimer(){
        durationLeft = workout.getTime();
        updateCountDown();
        resetBtn.setVisibility(View.INVISIBLE);
        stopPlayer();
        startpauseBtn.setText("START");
        startpauseBtn.setVisibility(View.VISIBLE);
    }

    private void stopPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void saveProgress(){
        woHistory.add(workout);
        Gson gson = new Gson();
        String json = gson.toJson(woHistory);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("WO", json);
        editor.commit();
    }
}