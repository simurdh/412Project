package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;


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

    /* set alarm time preferences */
    public void setPreferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(HOUR, hour);
        editor.putInt(MINUTES, minutes);
        editor.putString(AM_PM, am_pm);
        editor.putString(DAYS, days);
        editor.putInt(CHALLENGES, challenges);
        editor.putBoolean(ACTIVE,active);
        editor.apply();
    }

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

    public LinearLayout makeView(Context ctx){
        /*
        *
        * */

        LinearLayout AlarmFull = new LinearLayout(ctx);

        LinearLayout.LayoutParams alarmFullParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        AlarmFull.setBackgroundColor(ContextCompat.getColor(ctx, R.color.alarmBackground));
        AlarmFull.setId(this.id);
        AlarmFull.setLayoutParams(alarmFullParams);

        ImageView alarmIcon = new ImageView(ctx);
        AlarmFull.addView(alarmIcon);
        ViewGroup.LayoutParams params = alarmIcon.getLayoutParams();

        Log.i(TAG, params.toString());

        params.height = ctx.getResources().getDimensionPixelSize(R.dimen.alarmIconHeight);
        params.width = ctx.getResources().getDimensionPixelSize(R.dimen.alarmIconWidth);
        alarmIcon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.alarm_clock));

        return AlarmFull;
    }
}
