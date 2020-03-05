package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
/* edit alarm screen controller */
public class EditAlarm extends AppCompatActivity {

    public boolean isNew;

    private static final String TAG = "EditAlarmActivity";

    private AlarmData currInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

    }
    public void onStart() {
        this.isNew = MainActivity.isNew;

        super.onStart();
        updateView();
    }

    public void updateView() {
        /* widgets on screen */
        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        currInstance = null;

        DatabaseManager db = new DatabaseManager(this);

        if(this.isNew){
            currInstance = new AlarmData();

            deleteButton.setText("cancel");
        } else {
            currInstance = db.selectById(MainActivity.selectedID);
            String infoString = String.format("isActive: %s", Boolean.toString(currInstance.getActive()));

            Log.i(TAG, infoString);

            TimePicker pickTime = findViewById(R.id.pickTime);
            CheckBox monday = findViewById(R.id.mondayCheckbox);
            CheckBox tuesday = findViewById(R.id.tuesdayCheckbox);
            CheckBox wednesday = findViewById(R.id.wednesdayCheckbox);
            CheckBox thursday = findViewById(R.id.thursdayCheckbox);
            CheckBox friday = findViewById(R.id.fridayCheckbox);
            CheckBox saturday = findViewById(R.id.saturdayCheckbox);
            CheckBox sunday = findViewById(R.id.sundayCheckbox);
            SeekBar challenges = findViewById(R.id.seekBar);

            pickTime.setCurrentHour(currInstance.getHour());
            pickTime.setCurrentMinute(currInstance.getMinutes());

            String daysString = currInstance.getDays();
            String[] days = daysString.split(" ");

            for(int i = 0; i < days.length; i++)
            {
                if(days[i].equals("M"))
                {
                    monday.setChecked(true);
                }
                else if (days[i].equals("T"))
                {
                    tuesday.setChecked(true);
                }
                else if (days[i].equals("W"))
                {
                    wednesday.setChecked(true);
                }
                else if (days[i].equals("Th"))
                {
                    thursday.setChecked(true);
                }
                else if (days[i].equals("F"))
                {
                    friday.setChecked(true);
                }
                else if (days[i].equals("Sa"))
                {
                    saturday.setChecked(true);
                }
                else if (days[i].equals("Su"))
                {
                    sunday.setChecked(true);
                }
            }

            challenges.setProgress(currInstance.getChallenges());
        }




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

                DatabaseManager db = new DatabaseManager(EditAlarm.this);

                //Get time from TimePicker
                int hour = pickTime.getCurrentHour();

                if(hour > 12)
                {
                    EditAlarm.this.currInstance.setHour(hour-12);
                    EditAlarm.this.currInstance.setAM_PM("PM");
                }
                else if (hour == 12)
                {
                    EditAlarm.this.currInstance.setHour(hour);
                    EditAlarm.this.currInstance.setAM_PM("PM");
                }
                else
                {
                    EditAlarm.this.currInstance.setHour(hour);
                    EditAlarm.this.currInstance.setAM_PM("AM");
                }
                EditAlarm.this.currInstance.setMinutes(pickTime.getCurrentMinute());

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
                EditAlarm.this.currInstance.setDays(days.toString());

                //Get challenge count.
                SeekBar challenges = findViewById(R.id.seekBar);
                EditAlarm.this.currInstance.setChallenges(challenges.getProgress());

                if(EditAlarm.this.isNew){
                    db.insert(EditAlarm.this.currInstance);
                } else {
                    db.updateById(EditAlarm.this.currInstance.getid(),
                            EditAlarm.this.currInstance.getHour(),
                            EditAlarm.this.currInstance.getMinutes(),
                            EditAlarm.this.currInstance.getAM_PM(),
                            EditAlarm.this.currInstance.getDays(),
                            EditAlarm.this.currInstance.getChallenges(),
                            Boolean.toString(EditAlarm.this.currInstance.getActive())
                            );
                }

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EditAlarm.this.isNew){
                    DatabaseManager db = new DatabaseManager(EditAlarm.this);
                    db.deleteByID(EditAlarm.this.currInstance.getid());
                }
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }



}
