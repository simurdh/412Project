package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
/* main activity screen controller */
public class MainActivity extends AppCompatActivity {
    public static AlarmData alarmData;
    public static boolean isNew;
    public static int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmData alarmData = new AlarmData(this);
        setContentView(R.layout.activity_main);

        //Get hours & minutes from alarmData.
//        StringBuilder timeString = new StringBuilder();
//        TextView alarmTime = findViewById(R.id.alarmTime);
//        timeString.append(alarmData.getHour() + ":");
//        if (alarmData.getMinutes() < 10)
//            timeString.append("0"+alarmData.getMinutes()+" "); //Adds extra 0. Fixes times like 9:0 AM.
//        else
//            timeString.append(alarmData.getMinutes()+" ");
//        timeString.append(alarmData.getAM_PM());
//        alarmTime.setText(timeString);
//
//        //Get days from alarmData
//        TextView alarmDays = findViewById(R.id.alarmDays);
//        alarmDays.setText(alarmData.getDays());
//
//        //Get challenges from alarmData.
//        String challengeString;
//        TextView challenges = findViewById(R.id.alarmChallenges);
//        challengeString = alarmData.getChallenges() + " Challenges";
//        challenges.setText(challengeString);
//
//        //Get alarm checkbox status.
//        CheckBox alarmCheck = findViewById(R.id.alarmCheckBox);
//        alarmCheck.setChecked(alarmData.getActive());
//        if(alarmCheck.isChecked())
//        {
//            alarmSetup();
//        }
    }

    public void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView() {
        //widgets used on screen
        //Button editButton = findViewById(R.id.EditButton);
        Button challengeButton = findViewById(R.id.challengeButton);
        Button sensorButton = findViewById(R.id.SensorChallenge);
        ImageButton addAlarm = findViewById(R.id.addAlarmButton);
        //final CheckBox alarmCheckBox = findViewById(R.id.alarmCheckBox);
        MediaPlayer sound = MediaPlayer.create(this, R.raw.alarm);
        sound.stop();

        LinearLayout alarmList = findViewById(R.id.AlarmList);

        DatabaseManager db = new DatabaseManager(this);

        ArrayList<AlarmData> dataList = db.selectAll();

        //alarmList.addView(alarmData.makeView(this));

        for(AlarmData alarm : dataList){
            alarmList.addView(alarm.makeView(this));
        }

        // listeners for widgets
//        editButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), EditAlarm.class);
//                startActivityForResult(intent, 0);
//            }
//        });

        addAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.isNew = true;
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

        sensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SensorData.class);
                startActivityForResult(intent, 0);
            }
        });

//        alarmCheckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alarmSetup();
//            }
//        });

    }

//    public void setAlarms(int dayOfWeek)
//    {
//        final CheckBox alarmCheckBox = findViewById(R.id.alarmCheckBox);
//        Intent intent = new Intent(MainActivity.this, MemoryPuzzle.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        // enable alarm
//        if(alarmCheckBox.isChecked())
//        {
//            Calendar cal = Calendar.getInstance();
//            cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
//            if(alarmData.getAM_PM().equals("PM"))
//            {
//                cal.set(Calendar.HOUR_OF_DAY,alarmData.getHour()+12);
//            }
//            else
//            {
//                cal.set(Calendar.HOUR_OF_DAY,alarmData.getHour());
//            }
//            cal.set(Calendar.MINUTE,alarmData.getMinutes());
//            cal.set(Calendar.SECOND, 0);
//
//            //Check that day is not in the past. If so set for next same day of week.
//            if(cal.getTimeInMillis() < System.currentTimeMillis())
//            {
//                cal.add(Calendar.DAY_OF_YEAR,7);
//            }
//
//            alarmData.setActive(true);
//            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//        }
//        else
//        {
//            //disable alarm
//            alarmData.setActive(false);
//            am.cancel(pendingIntent);
//        }
//    }

//    public void alarmSetup()
//    {
//        String daysString = alarmData.getDays();
//        String[] days = daysString.split(" ");
//
//        for(int i = 0; i < days.length; i++)
//        {
//            if(days[i].equals("M"))
//            {
//                setAlarms(Calendar.MONDAY);
//            }
//            else if (days[i].equals("T"))
//            {
//                setAlarms(Calendar.TUESDAY);
//            }
//            else if (days[i].equals("W"))
//            {
//                setAlarms(Calendar.WEDNESDAY);
//            }
//            else if (days[i].equals("Th"))
//            {
//                setAlarms(Calendar.THURSDAY);
//            }
//            else if (days[i].equals("F"))
//            {
//                setAlarms(Calendar.FRIDAY);
//            }
//            else if (days[i].equals("Sa"))
//            {
//                setAlarms(Calendar.SATURDAY);
//            }
//            else if (days[i].equals("Su"))
//            {
//                setAlarms(Calendar.SUNDAY);
//            }
//        }
//    }
}
