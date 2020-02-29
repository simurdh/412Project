package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
        * Generates a view for an  alarm
        * */

        LinearLayout AlarmFull = new LinearLayout(ctx);

        LinearLayout.LayoutParams alarmFullParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        AlarmFull.setBackgroundColor(ContextCompat.getColor(ctx, R.color.alarmBackground));
        AlarmFull.setId(this.id);
        AlarmFull.setOrientation(LinearLayout.HORIZONTAL);
        AlarmFull.setLayoutParams(alarmFullParams);

        ImageView alarmIcon = new ImageView(ctx);
        AlarmFull.addView(alarmIcon);
        ViewGroup.LayoutParams iconParams = alarmIcon.getLayoutParams();

        iconParams.height = ctx.getResources().getDimensionPixelSize(R.dimen.alarmIconHeight);
        iconParams.width = ctx.getResources().getDimensionPixelSize(R.dimen.alarmIconWidth);
        alarmIcon.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.alarm_clock));


        LinearLayout timeDiv = new LinearLayout(ctx);

        LinearLayout.LayoutParams timeDivParams = new LinearLayout.LayoutParams(
                ctx.getResources().getDimensionPixelSize(R.dimen.timeDivWidth),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        AlarmFull.addView(timeDiv);
        timeDiv.setOrientation(LinearLayout.VERTICAL);
        timeDiv.setLayoutParams(timeDivParams);

        TextView timeView = new TextView(ctx);
        timeDiv.addView(timeView);
        ViewGroup.LayoutParams timeViewParams = timeView.getLayoutParams();

        timeViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        timeViewParams.width = ctx.getResources().getDimensionPixelSize(R.dimen.timeTextWidth);
        timeView.setText(this.getTimeString());
        timeView.setPadding(ctx.getResources().getDimensionPixelSize(R.dimen.timeTextPaddingLeft),
                0,0,0);


        TextView daysView = new TextView(ctx);
        timeDiv.addView(daysView);
        ViewGroup.LayoutParams daysViewParams = daysView.getLayoutParams();

        daysViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        daysViewParams.width = ctx.getResources().getDimensionPixelSize(R.dimen.daysTextWidth);
        daysView.setText(this.days);
        daysView.setPadding(ctx.getResources().getDimensionPixelSize(R.dimen.daysTextPaddingLeft),
                0,0,0);

        TextView numChallengesView = new TextView(ctx);
        AlarmFull.addView(numChallengesView);
        ViewGroup.LayoutParams numChallengesViewParams = numChallengesView.getLayoutParams();

        numChallengesViewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        numChallengesViewParams.width = ctx.getResources().getDimensionPixelSize(R.dimen.numChallengesWidth);
        numChallengesView.setText(this.getNumChallengesString());
        numChallengesView.setPadding(ctx.getResources().getDimensionPixelSize(R.dimen.numChallengesPaddingLeft),
                ctx.getResources().getDimensionPixelSize(R.dimen.numChallengesPaddingTop),
                0,0);

        CheckBox armAlarm = new CheckBox(ctx);
        armAlarm.setGravity(Gravity.CENTER);
        AlarmFull.addView(armAlarm);
        ViewGroup.LayoutParams armAlarmParams = armAlarm.getLayoutParams();

        armAlarmParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        armAlarmParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        return AlarmFull;
    }

    private String getTimeString(){
        String timeString = null;

        if(this.minutes < 10){
            timeString = String.format("%d:0%d", this.hour, this.minutes);
        } else {
            timeString = String.format("%d:%d", this.hour, this.minutes);
        }

        timeString += " " + am_pm;

        return timeString;
    }

    private String getNumChallengesString(){
        return String.format("%d challenges", this.challenges);
    }
}
