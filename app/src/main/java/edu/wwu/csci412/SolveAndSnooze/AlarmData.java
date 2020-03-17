/**
 * Store information about an alarm
 */

package edu.wwu.csci412.SolveAndSnooze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.CheckBox;

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
    private static final String IN_RANGE = "inRange";
    private static final String HAS_GF = "hasGeofence";
    private static final String MEM_PUZZLE = "memoryPuzzle";
    private static final String MATH_PUZZLE = "mathPuzzle";
    private static final String TILT_PUZZLE = "tilePuzzle";
    private static final String CHALLENGES_COMPLETED = "challengesCompleted";


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
    private boolean inRange; // if the user is in range of location enabled alarm. Will always be true if location is disabled
    private boolean hasGf;
    private boolean memPuzzle;
    private boolean mathPuzzle;
    private boolean tiltPuzzle;
    private int challengesCompleted;

    /* instantiate alarm data */

    public AlarmData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        setHour(pref.getInt(HOUR, 0));
        setMinutes(pref.getInt(MINUTES, 00));
        setAM_PM(pref.getString(AM_PM, "AM"));
        setDays(pref.getString(DAYS,"M T W Th F"));
        setChallenges(pref.getInt(CHALLENGES,1));
        setActive(pref.getBoolean(ACTIVE,false));
        setInRange(pref.getBoolean(IN_RANGE, true));
        setHasGf(pref.getBoolean(HAS_GF, false));
        setMemEnabled(pref.getBoolean(MEM_PUZZLE, false));
        setMathEnabled(pref.getBoolean(MATH_PUZZLE, false));
        setTiltEnabled(pref.getBoolean(TILT_PUZZLE, false));
        setChallengesCompleted(pref.getInt(CHALLENGES_COMPLETED, 0));

    }

    /* Secondary constructor for DB */

    public AlarmData(int id, int hour, int minutes, String am_pm, String days, int challenges,
                     boolean active, boolean inRange, boolean hasGf, boolean memPuzzle, boolean mathPuzzle,
                     boolean tiltPuzzle, int challengesCompleted){
        this.id = id;
        this.hour = hour;
        this.minutes = minutes;
        this.am_pm = am_pm;
        this.days = days;
        this.challenges = challenges;
        this.active = active;
        this.inRange = inRange;
        this.hasGf = hasGf;
        this.ctx = null;
        this.memPuzzle = memPuzzle;
        this.mathPuzzle = mathPuzzle;
        this.tiltPuzzle = tiltPuzzle;
        this.challengesCompleted = challengesCompleted;
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

    public String isInRange() {
        return String.valueOf(inRange);
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public String getHasGf() {
        return String.valueOf(hasGf);
    }

    public void setHasGf(boolean hasGf) {
        this.hasGf = hasGf;
    }

    public String getMemEnabled() {return String.valueOf(memPuzzle);}

    public void setMemEnabled(boolean memPuzzle){this.memPuzzle = memPuzzle;}

    public String getMathEnabled() {return String.valueOf(mathPuzzle);}

    public void setMathEnabled(boolean mathPuzzle){this.mathPuzzle = mathPuzzle;}

    public String getTiltEnabled() {return String.valueOf(tiltPuzzle);}

    public void setTiltEnabled(boolean tiltPuzzle){this.tiltPuzzle = tiltPuzzle;}

    public int getChallengesCompleted() {return this.challengesCompleted;}

    public void setChallengesCompleted(int challengesCompleted) {this.challengesCompleted = challengesCompleted;};



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

    /**
     * activate the alarm
     * @param dayOfWeek day the alarm will sound
     */
    public void setAlarm(int dayOfWeek){
        MainActivity activity = (MainActivity) this.ctx;

        Intent intent = new Intent(activity, MemoryPuzzle.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        AlarmManager am = (AlarmManager) this.ctx.getSystemService(Context.ALARM_SERVICE);

        if(this.active && this.inRange) {
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
            System.out.println("ALARM SET!");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        }
        else
        {
            //disable alarm
            System.out.println("ALARM DISABLED");
            System.out.println("Active: "+this.getActive()+" inRange: "+this.inRange);
            this.setActive(false);
            am.cancel(pendingIntent);
        }
    }
}
