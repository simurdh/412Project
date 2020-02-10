package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static AlarmData alarmData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmData = new AlarmData(this);
        setContentView(R.layout.activity_main);

        //Get hours & minutes from alarmData.
        StringBuilder timeString = new StringBuilder();
        TextView alarmTime = findViewById(R.id.alarmTime);
        timeString.append(alarmData.getHour() + ":");
        if (alarmData.getMinutes() < 10)
            timeString.append("0"+alarmData.getMinutes()+" "); //Adds extra 0. Fixes times like 9:0 AM.
        else
            timeString.append(alarmData.getMinutes()+" ");
        timeString.append(alarmData.getAM_PM());
        alarmTime.setText(timeString);

        //Get days from alarmData
        TextView alarmDays = findViewById(R.id.alarmDays);
        alarmDays.setText(alarmData.getDays());

        //Get challenges from alarmData.
        String challengeString;
        TextView challenges = findViewById(R.id.alarmChallenges);
        challengeString = alarmData.getChallenges() + " Challenges";
        challenges.setText(challengeString);
    }

    public void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView() {
        Button editButton = findViewById(R.id.EditButton);
        Button challengeButton = findViewById(R.id.challengeButton);
        ImageButton addAlarm = findViewById(R.id.addAlarmButton);
        final CheckBox alarmCheckBox = findViewById(R.id.alarmCheckBox);

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

        alarmCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemoryPuzzle.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if(alarmCheckBox.isChecked())
                {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY,alarmData.getHour());
                    cal.set(Calendar.MINUTE,alarmData.getMinutes());
                    cal.set(Calendar.SECOND, 0);

                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                else
                {
                    am.cancel(pendingIntent);
                }
            }
        });

    }
}
