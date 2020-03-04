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
import android.location.LocationProvider;
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
    public static boolean isNew;
    public static int selectedID;
    private GeofencingClient gfClient;
    private List<Geofence> gfList;
    private PendingIntent geofencePendingIntent;
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmData alarmData = new AlarmData(this);
        setContentView(R.layout.activity_main);


        // Set up geofence client
        if (gfClient == null) {
            gfClient = LocationServices.getGeofencingClient(this);
        }

        if (gfList == null) {
            gfList = new ArrayList<>();
        }


        gfClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
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
