package com.example.fitnesscounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String INTENT_PARAM_WORKOUT = "INTENT_PARAM_WORKOUT";
    Button jumpingJack, bikeCrunch, burpees, absPlanks, sidePlanks, mountainClimbers, vUps;
    int duration;
    String name;
    ListView historyRecord;
    SharedPreferences sharedPreferences;
    public static final String DEFAULT = "N/A";
    List<Workout> workoutData;
    List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
    String[] from = {"woName", "woTime", "woDate"};
    int[] views = {R.id.woName, R.id.woTime, R.id.woDate};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Fitness Counter");

        sharedPreferences = getSharedPreferences("WorkOut", MODE_PRIVATE);

        jumpingJack = findViewById(R.id.jumpingJackbtn);
        jumpingJack.setOnClickListener(this);
        bikeCrunch = findViewById(R.id.bicycleCrunchbtn);
        bikeCrunch.setOnClickListener(this);
        burpees = findViewById(R.id.burpeesbtn);
        burpees.setOnClickListener(this);
        absPlanks = findViewById(R.id.absPlanksbtn);
        absPlanks.setOnClickListener(this);
        sidePlanks = findViewById(R.id.sidePlanksbtn);
        sidePlanks.setOnClickListener(this);
        mountainClimbers = findViewById(R.id.mountainClimbersbtn);
        mountainClimbers.setOnClickListener(this);
        vUps = findViewById(R.id.vUpsbtn);
        vUps.setOnClickListener(this);

        historyRecord = findViewById(R.id.historyRecord);
        readHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readHistory();
    }

    public void readHistory(){

        Gson gson = new Gson();
        String json = sharedPreferences.getString("WO", DEFAULT);

        if(!json.isEmpty()){
            Type type = new TypeToken<List<Workout>>(){}.getType();

            workoutData = gson.fromJson(json, type);

            for (int i = 0; i < workoutData.size(); i++){
                Map<String, String> datanum = new HashMap<String, String>();
                datanum.put("woName", workoutData.get(i).getName());
                datanum.put("woTime", workoutData.get(i).getTime()/1000 + "s");
                datanum.put("woDate", String.valueOf(workoutData.get(i).getDate()));
                prolist.add(datanum);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, prolist, R.layout.list_items, from, views);
            historyRecord.setAdapter(simpleAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jumpingJackbtn:
                duration = 60000;
                name = "Jumping Jack";
            case R.id.bicycleCrunchbtn:
                duration = 45000;
                name = "Bicycle Crunch";
                break;
            case R.id.burpeesbtn:
                duration = 20000;
                name = "Burpees";
                break;
            case R.id.absPlanksbtn:
                duration = 60000;
                name = "Abs Planks";
                break;
            case R.id.sidePlanksbtn:
                duration = 20000;
                name = "Side Planks";
                break;
            case R.id.mountainClimbersbtn:
                duration = 30000;
                name = "Mountain Climbers";
                break;
            case R.id.vUpsbtn:
                duration = 15000;
                name = "Abs V-Ups";
                break;

        }
        Workout workout = new Workout(duration, name);
        Intent change = new Intent(this, MainActivity2.class);
        change.putExtra(INTENT_PARAM_WORKOUT, workout);
        startActivity(change);
    }
}