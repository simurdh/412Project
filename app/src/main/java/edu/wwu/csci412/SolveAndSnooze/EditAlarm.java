/**
 * Edit alarm info logic
 */
package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.Voice;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* edit alarm screen controller */
public class EditAlarm extends AppCompatActivity {
    private static final int COMMAND_REQUEST = 1;

    public boolean isNew;

    private static final String TAG = "EditAlarmActivity";

    private AlarmData currInstance;
    private AlarmLocation alarmLocation;
    private boolean createGf; // flag to tell whether to make a geofence
    private boolean removeGf; // flag to tell whether to remove a geofence

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alarmLocation = AlarmLocation.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        //Test if device supports speech recognition
        PackageManager manager = getPackageManager();
        List<ResolveInfo> listOfMatches = manager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (listOfMatches.size() > 0) {
            listen();
        }
        else {
            //Speech recognition not supported
            Toast.makeText(this, "Device does not support speech recognition", Toast.LENGTH_LONG).show();
        }
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
        Button speakButton = findViewById(R.id.speakButton);

        currInstance = null;

        SoundManager sound = SoundManager.getInstance(this);

        DatabaseManager db = new DatabaseManager(this);

        RadioButton classicRadio = findViewById(R.id.classic_radio);
        RadioButton airRadio = findViewById(R.id.air_radio);
        RadioButton enterpriseRadio = findViewById(R.id.enterprise_radio);
        RadioButton williamRadio = findViewById(R.id.william_radio);

        switch(sound.currSound){
            case R.raw.alarm:
                classicRadio.setChecked(true);
                break;
            case R.raw.air_raid:
                airRadio.setChecked(true);
                break;
            case R.raw.star_trek_klaxon:
                enterpriseRadio.setChecked(true);
                break;
            case R.raw.william_tell:
                williamRadio.setChecked(true);
                break;
        }

        classicRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundManager sound = SoundManager.getInstance(EditAlarm.this);
                sound.setAlarmSound(0, EditAlarm.this);
            }
        });

        airRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundManager sound = SoundManager.getInstance(EditAlarm.this);
                sound.setAlarmSound(1, EditAlarm.this);
            }
        });

        enterpriseRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundManager sound = SoundManager.getInstance(EditAlarm.this);
                sound.setAlarmSound(2, EditAlarm.this);
            }
        });

        williamRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundManager sound = SoundManager.getInstance(EditAlarm.this);
                sound.setAlarmSound(3, EditAlarm.this);
            }
        });


        if(this.isNew){
            currInstance = new AlarmData();

            deleteButton.setText("cancel");
            createGf = false;
            removeGf = false;
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

        /* location */
        final Switch locationEnabled = (Switch) findViewById(R.id.locationSwitch);
        locationEnabled.setOnCheckedChangeListener(new LocationEnabledListener());

        // if location is enabled already, update switch
        if (!EditAlarm.this.isNew) {
            if (EditAlarm.this.currInstance.getHasGf().equals("true")) {
                locationEnabled.setChecked(true);
            }
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

                //Get challenge options.
                CheckBox memBox = findViewById(R.id.memoryCheckbox);
                CheckBox mathBox = findViewById(R.id.mathCheckbox);
                CheckBox tiltBox = findViewById(R.id.tiltCheckbox);
                if(memBox.isChecked())
                    EditAlarm.this.currInstance.setMemEnabled(true);
                else
                    EditAlarm.this.currInstance.setMemEnabled(false);
                if(mathBox.isChecked())
                    EditAlarm.this.currInstance.setMathEnabled(true);
                else
                    EditAlarm.this.currInstance.setMathEnabled(false);
                if(tiltBox.isChecked())
                    EditAlarm.this.currInstance.setTiltEnabled(true);
                else
                    EditAlarm.this.currInstance.setTiltEnabled(false);

                //Make sure challenges completed is 0.
                EditAlarm.this.currInstance.setChallengesCompleted(0);

                //Get gps setting.
                if(locationEnabled.isChecked())
                {
                    EditAlarm.this.currInstance.setInRange(true);
                }
                else
                {
                    EditAlarm.this.currInstance.setInRange(true);
                }

                if(EditAlarm.this.isNew){
                    EditAlarm.this.currInstance.setid(db.insert(EditAlarm.this.currInstance));
                } else {
                    db.updateById(EditAlarm.this.currInstance.getid(),
                            EditAlarm.this.currInstance.getHour(),
                            EditAlarm.this.currInstance.getMinutes(),
                            EditAlarm.this.currInstance.getAM_PM(),
                            EditAlarm.this.currInstance.getDays(),
                            EditAlarm.this.currInstance.getChallenges(),
                            Boolean.toString(EditAlarm.this.currInstance.getActive()),
                            EditAlarm.this.currInstance.isInRange(),
                            EditAlarm.this.currInstance.getHasGf(),
                            EditAlarm.this.currInstance.getMemEnabled(),
                            EditAlarm.this.currInstance.getMathEnabled(),
                            EditAlarm.this.currInstance.getTiltEnabled(),
                            EditAlarm.this.currInstance.getChallengesCompleted()
                            );
                }

                if (createGf) {
                    alarmLocation.createGeofence(String.valueOf(currInstance.getid()));
                } else if (removeGf) {
                    alarmLocation.deleteGeofence(String.valueOf(currInstance.getid()));

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

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Test if device supports speech recognition
                PackageManager manager = getPackageManager();
                List<ResolveInfo> listOfMatches = manager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
                if (listOfMatches.size() > 0) {
                    listen();
                }
                else {
                    //Speech recognition not supported
                    //Toast.makeText(this, "Device does not support speech recognition", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /* voice recognition method to listen for input */
    private void listen() {
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Add to alarm settings");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        startActivityForResult(listenIntent, COMMAND_REQUEST);
    }

    /* voice recognition method to handle voice input into array */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMAND_REQUEST && resultCode == RESULT_OK) {
            //get list of possible words
            ArrayList<String> returnedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //get scores for possible words
            float[] scores = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

            //display results
            int i = 0;
            for (String word : returnedWords) {
                if (scores != null && i < scores.length) {
                    Log.w("EditAlarm", word + ": " + scores[i]);
                }
                i++;
            }
            //get individual words from sentence said and set appropriately
            List<String> individualReturnedWords = Arrays.asList(returnedWords.get(0).split(" "));
            VoiceRecognition.voiceRec(this, individualReturnedWords);


        }
    }

    private class LocationEnabledListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // if location is enabled, create a new geofence
            if (isChecked) {
                createGf = true;
                removeGf = false;

            // if location is not enabled, destroy previous geofence if exists
            } else {
                createGf = false;
                if (currInstance.getHasGf().equals("true")) {
                    removeGf = true;
                }
            }
        }
    }
}
