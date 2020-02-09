package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class EditAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

    }
    public void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView() {
        Button saveButton = findViewById(R.id.saveButton);
        final AlarmData alarmData = MainActivity.alarmData;

        /* set alarm data preferences and switch to main activity */
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alarmData.setPreferences(EditAlarm.this);
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

}
