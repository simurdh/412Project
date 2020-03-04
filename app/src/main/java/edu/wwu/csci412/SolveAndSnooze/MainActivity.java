package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
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

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* main activity screen controller */
public class MainActivity extends AppCompatActivity {
    public static AlarmData alarmData;
    private GeofencingClient gfClient;
    private List<Geofence> gfList;
    private PendingIntent geofencePendingIntent;

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

        //Get alarm checkbox status.
        CheckBox alarmCheck = findViewById(R.id.alarmCheckBox);
        alarmCheck.setChecked(alarmData.getActive());
        if(alarmCheck.isChecked())
        {
            alarmSetup();
        }

        // Set up geofence client
        if (gfClient == null) {
            gfClient = LocationServices.getGeofencingClient(this);
        }

        if (gfList == null) {
            gfList = new ArrayList<>();
        }

//        // Required if for Android 10 or higher.
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (permissionRationaleAlreadyShown) {
//                ActivityCompat.requestPermissions(this,
//                        new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION },
//                        background-location-permission-request-code);
//            } else {
//                // Show an explanation to the user as to why your app needs the
//                // permission. Display the explanation *asynchronously* -- don't block
//                // this thread waiting for the user's response!
//            }
//        } else {
//            // Background location runtime permission already granted.
//            // You can now call geofencingClient.addGeofences().
//        }

        gfClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
    }

    public void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView() {
        //widgets used on screen
        Button editButton = findViewById(R.id.EditButton);
        Button challengeButton = findViewById(R.id.challengeButton);
        Button sensorButton = findViewById(R.id.SensorChallenge);
        ImageButton addAlarm = findViewById(R.id.addAlarmButton);
        final CheckBox alarmCheckBox = findViewById(R.id.alarmCheckBox);
        MediaPlayer sound = MediaPlayer.create(this, R.raw.alarm);
        sound.stop();

        // listeners for widgets
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

        sensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SensorData.class);
                startActivityForResult(intent, 0);
            }
        });

        alarmCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSetup();
            }
        });

    }

    public void setAlarms(int dayOfWeek)
    {
        final CheckBox alarmCheckBox = findViewById(R.id.alarmCheckBox);
        Intent intent = new Intent(MainActivity.this, MemoryPuzzle.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // enable alarm
        if(alarmCheckBox.isChecked())
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            if(alarmData.getAM_PM().equals("PM"))
            {
                cal.set(Calendar.HOUR_OF_DAY,alarmData.getHour()+12);
            }
            else
            {
                cal.set(Calendar.HOUR_OF_DAY,alarmData.getHour());
            }
            cal.set(Calendar.MINUTE,alarmData.getMinutes());
            cal.set(Calendar.SECOND, 0);

            //Check that day is not in the past. If so set for next same day of week.
            if(cal.getTimeInMillis() < System.currentTimeMillis())
            {
                cal.add(Calendar.DAY_OF_YEAR,7);
            }

            alarmData.setActive(true);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
        else
        {
            //disable alarm
            alarmData.setActive(false);
            am.cancel(pendingIntent);
        }
    }

    public void alarmSetup()
    {
        String daysString = alarmData.getDays();
        String[] days = daysString.split(" ");

        for(int i = 0; i < days.length; i++)
        {
            if(days[i].equals("M"))
            {
                setAlarms(Calendar.MONDAY);
            }
            else if (days[i].equals("T"))
            {
                setAlarms(Calendar.TUESDAY);
            }
            else if (days[i].equals("W"))
            {
                setAlarms(Calendar.WEDNESDAY);
            }
            else if (days[i].equals("Th"))
            {
                setAlarms(Calendar.THURSDAY);
            }
            else if (days[i].equals("F"))
            {
                setAlarms(Calendar.FRIDAY);
            }
            else if (days[i].equals("Sa"))
            {
                setAlarms(Calendar.SATURDAY);
            }
            else if (days[i].equals("Su"))
            {
                setAlarms(Calendar.SUNDAY);
            }
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(gfList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    // create a geofence with given id (Should match the id of corresponding alarm)
    public Geofence createGeofence(String id) {
        Geofence gf = new Geofence.Builder()
                .setRequestId(id)

                // Get the users location and set the region from it
                .setCircularRegion()

                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();


        gfList.add(gf);
        return gf;
    }
}
