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
    private static final String IN_RANGE = "inRange";
    private static final String HAS_GF = "hasGeofence";


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

    }

    /* Secondary constructor for DB */

    public AlarmData(int id, int hour, int minutes, String am_pm, String days, int challenges,
                     boolean active, boolean inRange, boolean hasGf){
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
    }

    /* Tertiary constructor for new Alarms */

    public AlarmData(){}

    /* set alarm time preferences */
//    public void setPreferences(Context context) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putInt(HOUR, hour);
//        editor.putInt(MINUTES, minutes);
//        editor.putString(AM_PM, am_pm);
//        editor.putString(DAYS, days);
//        editor.putInt(CHALLENGES, challenges);
//        editor.putBoolean(ACTIVE,active);
//        editor.apply();
//    }

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

    public LinearLayout makeView(Context ctx){
        /*
        * Generates a view for an  alarm bind alarm object to current
        * context.
        * */

        this.ctx = ctx;

        LinearLayout AlarmFull = new LinearLayout(ctx);

        LinearLayout.LayoutParams alarmFullParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        alarmFullParams.topMargin = 30;
        AlarmFull.setBackgroundColor(ContextCompat.getColor(ctx, R.color.alarmBackground));
        AlarmFull.setId(this.id);
        AlarmFull.setOrientation(LinearLayout.HORIZONTAL);
        AlarmFull.setLayoutParams(alarmFullParams);

        AlarmFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectedID = AlarmData.this.id;
                MainActivity.isNew = false;
                Intent intent = new Intent(v.getContext(), EditAlarm.class);
                AlarmData.this.ctx.startActivity(intent);
            }
        });

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

        this.armAlarm = new CheckBox(ctx);
        this.armAlarm.setGravity(Gravity.CENTER);
        AlarmFull.addView(this.armAlarm);
        ViewGroup.LayoutParams armAlarmParams = this.armAlarm.getLayoutParams();

        this.armAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmData.this.active = isChecked;
                AlarmData.this.alarmSetup();
            }
        });


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
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
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

    public void alarmSetup()
    {
        String daysString = this.getDays();
        String[] days = daysString.split(" ");

        for(int i = 0; i < days.length; i++)
        {
            if(days[i].equals("M"))
            {
                setAlarm(Calendar.MONDAY);
            }
            else if (days[i].equals("T"))
            {
                setAlarm(Calendar.TUESDAY);
            }
            else if (days[i].equals("W"))
            {
                setAlarm(Calendar.WEDNESDAY);
            }
            else if (days[i].equals("Th"))
            {
                setAlarm(Calendar.THURSDAY);
            }
            else if (days[i].equals("F"))
            {
                setAlarm(Calendar.FRIDAY);
            }
            else if (days[i].equals("Sa"))
            {
                setAlarm(Calendar.SATURDAY);
            }
            else if (days[i].equals("Su"))
            {
                setAlarm(Calendar.SUNDAY);
            }
        }
    }
}
