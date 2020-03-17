package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class BasicPuzzle extends AppCompatActivity {
    private MediaPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_puzzle);
        SoundManager soundSelection = SoundManager.getInstance(this);
        setupAudio();
        sound = MediaPlayer.create(this,soundSelection.currSound);
        sound.setLooping(true);
        sound.setVolume(100,100);
        sound.start();
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

    public void onStopClick(View view)
    {
        sound.pause();
        sound.stop();
        sound.release();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }


}
