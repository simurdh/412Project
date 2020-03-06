package edu.wwu.csci412.SolveAndSnooze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;

import java.util.Calendar;


public class AlarmData {

    private static final String TAG = "Alarm Data";

    /* preferences key value pairs */
    public static final String HOUR = "hour";
    public static final String MINUTES = "minutes";
    public static final String AM_PM = "am_pm";
    public static final String DAYS = "days";
    public static final String CHALLENGES = "challenges";
    public static final String ACTIVE = "active";

    /* alarm variables */
    private int id;
    private int hour;
    private int minutes;
    private String am_pm;
    private String days;
    private int challenges;
    private boolean active;
    private Context ctx;
    private CheckBox armAlarm;

    /* instantiate alarm data */

    public AlarmData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        setHour(pref.getInt(HOUR, 0));
        setMinutes(pref.getInt(MINUTES, 00));
        setAM_PM(pref.getString(AM_PM, "AM"));
        setDays(pref.getString(DAYS,"M T W Th F"));
        setChallenges(pref.getInt(CHALLENGES,1));
        setActive(pref.getBoolean(ACTIVE,false));
    }

    /* Secondary constructor for DB */

    public AlarmData(int id, int hour, int minutes, String am_pm, String days, int challenges,
                     boolean active){
        this.id = id;
        this.hour = hour;
        this.minutes = minutes;
        this.am_pm = am_pm;
        this.days = days;
        this.challenges = challenges;
        this.active = active;
        this.ctx = null;
    }

    /* Tertiary constructor for new Alarms */

    public AlarmData(){}

    /* getters and setters for alarm data */
    public void setHour(int hour) { this.hour = hour; }
    public int getHour() { return this.hour; }

    public void setMinutes(int minutes) { this.minutes = minutes; }
    public int getMinutes() { return this.minutes; }

    public void setAM_PM(String am_pm) { this.am_pm = am_pm; }
    public String getAM_PM() { return this.am_pm; }

    public void setDays(String days) {this.days = days;}
    public String getDays() {return this.days;}

    public void setChallenges(int challenges) {this.challenges = challenges;}
    public int getChallenges() {return this.challenges;}

    public void setActive(boolean active) {this.active = active;}
    public boolean getActive() {return this.active;}

    public void setid(int id) {this.id = id;}
    public int getid() {return this.id;}



    public String getTimeString(){
        String timeString = null;

        if(this.minutes < 10){
            timeString = String.format("%d:0%d", this.hour, this.minutes);
        } else {
            timeString = String.format("%d:%d", this.hour, this.minutes);
        }

        timeString += " " + am_pm;

        return timeString;
    }

    public String getNumChallengesString(){
        return String.format("%d challenges", this.challenges);
    }

    public void setAlarm(int dayOfWeek){
        MainActivity activity = (MainActivity) this.ctx;

        Intent intent = new Intent(activity, MemoryPuzzle.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        AlarmManager am = (AlarmManager) this.ctx.getSystemService(Context.ALARM_SERVICE);

        if(this.active) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            if(this.getAM_PM().equals("PM"))
            {
                cal.set(Calendar.HOUR_OF_DAY,this.getHour()+12);
            }
            else
            {
                cal.set(Calendar.HOUR_OF_DAY,this.getHour());
            }
            cal.set(Calendar.MINUTE,this.getMinutes());
            cal.set(Calendar.SECOND, 0);

            //Check that day is not in the past. If so set for next same day of week.
            if(cal.getTimeInMillis() < System.currentTimeMillis())
            {
                cal.add(Calendar.DAY_OF_YEAR,7);
            }

            this.setActive(true);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
        else
        {
            //disable alarm
            this.setActive(false);
            am.cancel(pendingIntent);
        }
    }
}
