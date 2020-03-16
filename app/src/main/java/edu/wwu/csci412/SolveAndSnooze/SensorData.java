package edu.wwu.csci412.SolveAndSnooze;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

//detect sensor changes of accelerometer, update sprite on change
public class SensorData extends Activity implements SensorEventListener {

    private SensorManager sensorManager;

    private static final String TAG = "SensorData";
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this,this.getIntent().getIntExtra("alarmID", 0),this.getIntent().getIntExtra("challengesCompleted",0)));
        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(SensorData.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered accelerometer listener");
        setupAudio();
    }

    @Override
    public void onBackPressed()
    {
        //Do nothing.
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN))
        {
            //Do nothing
        }
        if((keyCode == KeyEvent.KEYCODE_VOLUME_UP))
        {
            //Do nothing
        }
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //updateUser sprite location when screen tilts
        int xAccelInitial = 0;
        int yAccelInitial = 0;
        GameView.updateUser(xAccelInitial-event.values[0], yAccelInitial-event.values[1]);
    }

    public void setupAudio()
    {
        AudioManager sysAudio;
        sysAudio=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        sysAudio.setStreamVolume(AudioManager.STREAM_MUSIC, 80, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        sysAudio.setStreamVolume(AudioManager.STREAM_RING, 80, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        sysAudio.setStreamVolume(AudioManager.STREAM_ALARM, 80, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        sysAudio.setStreamVolume(AudioManager.STREAM_SYSTEM, 80, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        sysAudio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 80, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
}
