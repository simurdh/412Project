package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button editButton = findViewById(R.id.EditButton);
        Button challengeButton = findViewById(R.id.challengeButton);
        ImageButton addAlarm = findViewById(R.id.addAlarmButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditAlarm.class);
                startActivityForResult(intent, 0);
            }
        });

        addAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditAlarm.class);
                startActivityForResult(intent, 0);
            }
        });

        challengeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MemoryPuzzle.class);
                startActivityForResult(intent, 0);
            }
        });


    }


}
