/**
 * Controller class for setting alarm sounds
 */

package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SoundManager {

    private static final String PREFERENCE_SOUND = "sound";
    private static SoundManager instance = null;
    public int currSound;
    private int[] validSounds = {
            R.raw.alarm,
            R.raw.air_raid,
            R.raw.star_trek_klaxon,
            R.raw.william_tell
    };

    private SoundManager(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        currSound = pref.getInt(PREFERENCE_SOUND, R.raw.alarm);
    }

    public static SoundManager getInstance(Context context){
        if(instance == null){
            instance = new SoundManager(context);
        }

        return instance;
    }

    /**
     * set alarm sound
     * @param choice the user selected sound from Edit Alarm
     */
    public void setAlarmSound(int choice, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();

        currSound = validSounds[choice];

        edit.putInt(PREFERENCE_SOUND, currSound);
        edit.commit();
    }
}
