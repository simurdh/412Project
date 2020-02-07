package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AlarmData {
    public static final String HOUR = "hour";
    public static final String MINUTES = "minutes";
    public static final String AM_PM = "am_pm";

    private int hour;
    private int minutes;
    private String am_pm;

    /* instantiate alarm data */
    public AlarmData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        setHour(pref.getInt(HOUR, 0));
        setMinutes(pref.getInt(MINUTES, 0));
        setAM_PM(pref.getString(AM_PM, ""));

    }

    public void setPreferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(HOUR, hour);
        editor.putInt(MINUTES, minutes);
        editor.putString(AM_PM, am_pm);
        editor.apply();
    }

    public void setHour(int hour) { this.hour = hour; }
    public int getHour() { return this.hour; }

    public void setMinutes(int minutes) { this.minutes = minutes; }
    public int getMinutes() { return this.minutes; }

    public void setAM_PM(String am_pm) { this.am_pm = am_pm; }
    public String getAM_PM() { return this.am_pm; }


}
