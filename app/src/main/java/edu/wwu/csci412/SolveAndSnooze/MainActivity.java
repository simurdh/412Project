package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/* main activity screen controller */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_LOCATION = 0;

    public static boolean isNew;
    public static int selectedID;
    private AlarmLocation alarmLocation;
    private DatabaseManager db;

    private static final String TAG = "MAIN_ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},1);
        AlarmData alarmData = new AlarmData(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        alarmLocation = AlarmLocation.getInstance(this);
        db = new DatabaseManager(this);
    }

    public void onStart() {
        super.onStart();
        updateView();

        // verify location is enabled
        boolean gpsEnabled = alarmLocation.locationServicesEnabled();
        if (gpsEnabled == false) {
            enableLocationSettings();
        }

        boolean permissionAccessCoarseLocationApproved =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        if (permissionAccessCoarseLocationApproved) {
            boolean permissionAccessFineLocationApproved =
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED;

            if (permissionAccessFineLocationApproved) {

            } else {
                // App can only access location in the foreground. Display a dialog
                // warning the user that your app must have all-the-time access to
                // location in order to function properly. Then, request background
                // location.
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_LOCATION);
            }
        } else {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    },
                    PERMISSION_REQUEST_LOCATION);
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    public void updateView() {
        //widgets used on screen
        //Button editButton = findViewById(R.id.EditButton);
//        ImageButton addAlarm = findViewById(R.id.addAlarmButton);
        //final CheckBox alarmCheckBox = findViewById(R.id.alarmCheckBox);
        MediaPlayer sound = MediaPlayer.create(this, R.raw.alarm);
        sound.stop();

        LinearLayout alarmList = findViewById(R.id.AlarmList);

        if(alarmList.getChildCount() > 0){
            alarmList.removeAllViews();
        }

        DatabaseManager db = new DatabaseManager(this);

        ArrayList<AlarmData> dataList = db.selectAll();


        for(AlarmData alarm : dataList){
            alarmList.addView(makeView(alarm));
            alarmSetup(alarm);
        }
    }

    public void setAlarms(int dayOfWeek, boolean active, AlarmData alarmData)
    {

        Intent selectedIntent;

        ArrayList<Intent> validIntents = new ArrayList<Intent>();
        Intent memIntent = new Intent(MainActivity.this,MemoryPuzzle.class);
        Intent mathIntent = new Intent(MainActivity.this, MathPuzzle.class);
        Intent tiltIntent = new Intent(MainActivity.this, SensorData.class);

        if(Boolean.parseBoolean(alarmData.getMemEnabled()))
            validIntents.add(memIntent);
        if(Boolean.parseBoolean(alarmData.getMathEnabled()))
            validIntents.add(mathIntent);
        if(Boolean.parseBoolean(alarmData.getTiltEnabled()))
            validIntents.add(tiltIntent);

        if(alarmData.getChallenges() == 0 || validIntents.size() == 0)
        {
            selectedIntent = new Intent(MainActivity.this, BasicPuzzle.class);
        }
        else
        {
            Random random = new Random();
            selectedIntent = validIntents.get(random.nextInt(validIntents.size()));
            selectedIntent.putExtra("alarmID",alarmData.getid());
            selectedIntent.putExtra("challengesCompleted", 0);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, alarmData.getid(), selectedIntent, 0);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // enable alarm
        if(active)
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

    public LinearLayout makeView(final AlarmData alarmData){
        /*
         * Generates a view for an  alarm bind alarm object to current
         * context.
         * */

        LinearLayout AlarmFull = new LinearLayout(this);

        LinearLayout.LayoutParams alarmFullParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        alarmFullParams.topMargin = 30;
        AlarmFull.setBackgroundColor(ContextCompat.getColor(this, R.color.alarmBackground));
        AlarmFull.setId(alarmData.getid());
        AlarmFull.setOrientation(LinearLayout.HORIZONTAL);
        AlarmFull.setLayoutParams(alarmFullParams);

        AlarmFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectedID = alarmData.getid();
                MainActivity.isNew = false;
                Intent intent = new Intent(v.getContext(), EditAlarm.class);
                startActivity(intent);
            }
        });

        ImageView alarmIcon = new ImageView(this);
        AlarmFull.addView(alarmIcon);
        ViewGroup.LayoutParams iconParams = alarmIcon.getLayoutParams();

        iconParams.height = this.getResources().getDimensionPixelSize(R.dimen.alarmIconHeight);
        iconParams.width = this.getResources().getDimensionPixelSize(R.dimen.alarmIconWidth);
        alarmIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.alarm_clock));


        LinearLayout timeDiv = new LinearLayout(this);

        LinearLayout.LayoutParams timeDivParams = new LinearLayout.LayoutParams(
                this.getResources().getDimensionPixelSize(R.dimen.timeDivWidth),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        AlarmFull.addView(timeDiv);
        timeDiv.setOrientation(LinearLayout.VERTICAL);
        timeDiv.setLayoutParams(timeDivParams);

        TextView timeView = new TextView(this);
        timeDiv.addView(timeView);
        ViewGroup.LayoutParams timeViewParams = timeView.getLayoutParams();

        timeViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        timeViewParams.width = this.getResources().getDimensionPixelSize(R.dimen.timeTextWidth);
        timeView.setText(alarmData.getTimeString());
        timeView.setPadding(this.getResources().getDimensionPixelSize(R.dimen.timeTextPaddingLeft),
                0,0,0);


        TextView daysView = new TextView(this);
        timeDiv.addView(daysView);
        ViewGroup.LayoutParams daysViewParams = daysView.getLayoutParams();

        daysViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        daysViewParams.width = this.getResources().getDimensionPixelSize(R.dimen.daysTextWidth);
        daysView.setText(alarmData.getDays());
        daysView.setPadding(this.getResources().getDimensionPixelSize(R.dimen.daysTextPaddingLeft),
                0,0,0);

        TextView numChallengesView = new TextView(this);
        AlarmFull.addView(numChallengesView);
        ViewGroup.LayoutParams numChallengesViewParams = numChallengesView.getLayoutParams();

        numChallengesViewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        numChallengesViewParams.width = this.getResources().getDimensionPixelSize(R.dimen.numChallengesWidth);
        numChallengesView.setText(alarmData.getNumChallengesString());
        numChallengesView.setPadding(this.getResources().getDimensionPixelSize(R.dimen.numChallengesPaddingLeft),
                this.getResources().getDimensionPixelSize(R.dimen.numChallengesPaddingTop),
                0,0);

        CheckBox armAlarm = new CheckBox(this);
        armAlarm.setGravity(Gravity.CENTER);
        AlarmFull.addView(armAlarm);
        ViewGroup.LayoutParams armAlarmParams = armAlarm.getLayoutParams();
        armAlarm.setChecked(alarmData.getActive());

        armAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarmData.setActive(isChecked);
                alarmSetup(alarmData);
            }
        });


        armAlarmParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        armAlarmParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        return AlarmFull;

    }

    public void alarmSetup(AlarmData alarmData)
    {
        String daysString = alarmData.getDays();
        String[] days = daysString.split(" ");

        DatabaseManager db = new DatabaseManager(this);

        for(int i = 0; i < days.length; i++)
        {
            if(days[i].equals("M"))
            {
                setAlarms(Calendar.MONDAY, true, alarmData);
            }
            else if (days[i].equals("T"))
            {
                setAlarms(Calendar.TUESDAY, true, alarmData);
            }
            else if (days[i].equals("W"))
            {
                setAlarms(Calendar.WEDNESDAY, true, alarmData);
            }
            else if (days[i].equals("Th"))
            {
                setAlarms(Calendar.THURSDAY, true, alarmData);
            }
            else if (days[i].equals("F"))
            {
                setAlarms(Calendar.FRIDAY, true, alarmData);
            }
            else if (days[i].equals("Sa"))
            {
                setAlarms(Calendar.SATURDAY, true, alarmData);
            }
            else if (days[i].equals("Su"))
            {
                setAlarms(Calendar.SUNDAY, true, alarmData);
            }
        }

        db.updateById(alarmData.getid(),
                alarmData.getHour(),
                alarmData.getMinutes(),
                alarmData.getAM_PM(),
                alarmData.getDays(),
                alarmData.getChallenges(),
                Boolean.toString(alarmData.getActive()),
                alarmData.isInRange(),
                alarmData.getHasGf(),
                alarmData.getMemEnabled(),
                alarmData.getMathEnabled(),
                alarmData.getTiltEnabled(),
                alarmData.getChallengesCompleted()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // user wants to create alarm-- navigate to edit alarm screen
        if (id == R.id.addAlarmButton) {
            MainActivity.isNew = true;
            Intent intent = new Intent(this, EditAlarm.class);
            startActivityForResult(intent, 0);

        // show help page
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivityForResult(intent, 0);
        }

        return super.onOptionsItemSelected(item);
    }
}
