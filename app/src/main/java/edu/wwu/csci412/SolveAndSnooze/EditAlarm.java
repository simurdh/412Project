package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
/* edit alarm screen controller */
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
        /* widgets on screen */
        Button saveButton = findViewById(R.id.saveButton);
        final AlarmData alarmData = MainActivity.alarmData;

        /* set alarm data preferences and switch to main activity */
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TimePicker pickTime = findViewById(R.id.pickTime);
                CheckBox monday = findViewById(R.id.mondayCheckbox);
                CheckBox tuesday = findViewById(R.id.tuesdayCheckbox);
                CheckBox wednesday = findViewById(R.id.wednesdayCheckbox);
                CheckBox thursday = findViewById(R.id.thursdayCheckbox);
                CheckBox friday = findViewById(R.id.fridayCheckbox);
                CheckBox saturday = findViewById(R.id.saturdayCheckbox);
                CheckBox sunday = findViewById(R.id.sundayCheckbox);
                StringBuilder days = new StringBuilder();

                //Get time from TimePicker
                int hour = pickTime.getCurrentHour();

                if(hour > 12)
                {
                    alarmData.setHour(hour-12);
                    alarmData.setAM_PM("PM");
                }
                else
                {
                    alarmData.setHour(hour);
                    alarmData.setAM_PM("AM");
                }
                alarmData.setMinutes(pickTime.getCurrentMinute());

                //Get days from CheckBoxes.
                if (monday.isChecked())
                    days.append("M ");
                if (tuesday.isChecked())
                    days.append("T ");
                if (wednesday.isChecked())
                    days.append("W ");
                if (thursday.isChecked())
                    days.append("Th ");
                if (friday.isChecked())
                    days.append("F ");
                if (saturday.isChecked())
                    days.append("Sa ");
                if (sunday.isChecked())
                    days.append("Su");
                alarmData.setDays(days.toString());

                //Get challenge count.
                SeekBar challenges = findViewById(R.id.seekBar);
                alarmData.setChallenges(challenges.getProgress());

                alarmData.setPreferences(EditAlarm.this);
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

}
