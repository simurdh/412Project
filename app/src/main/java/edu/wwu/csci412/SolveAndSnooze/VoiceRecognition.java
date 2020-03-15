package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

// using voice recognition words in array scan for what to set on screen
public class VoiceRecognition extends EditAlarm {

    // change status of editalarm based on words said during voice recognition
    public static void voiceRec(FragmentActivity context, List<String> individualReturnedWords) {
        /* set time picker based on voice command */
        int timeOfDayIndex = -1;
        int timeForAlarmIndex = 0;
        int hourOfAlarm = 0;
        int minuteOfAlarm = 0;
        TimePicker timeWheel = context.findViewById(R.id.pickTime);

        // is it am or pm alarm
        if (individualReturnedWords.contains("a.m.")) {
            timeOfDayIndex = individualReturnedWords.indexOf("a.m.");
        }
        else if (individualReturnedWords.contains("p.m.")) {
            timeOfDayIndex = individualReturnedWords.indexOf("p.m.");
            hourOfAlarm = 12;
        }

        // find out time to set alarm for
        if (timeOfDayIndex > 0) {
            timeForAlarmIndex = timeOfDayIndex-1;
            if (individualReturnedWords.get(timeForAlarmIndex).split(":").length > 1) {
                try {
                    hourOfAlarm += Integer.parseInt(individualReturnedWords.get(timeForAlarmIndex).split(":")[0]);
                    minuteOfAlarm = Integer.parseInt(individualReturnedWords.get(timeForAlarmIndex).split(":")[1]);
                }catch (NumberFormatException e) {
                    Log.w("EditAlarm", "Caught number format exception");
                }
                Log.w("EditAlarm", "hour is " + hourOfAlarm);
                Log.w("EditAlarm", "minute is " + minuteOfAlarm);
            }
            else {
                try {
                    hourOfAlarm += Integer.parseInt(individualReturnedWords.get(timeForAlarmIndex));
                }catch (NumberFormatException e) {
                    Log.w("EditAlarm", "Caught number format exception");
                }
                Log.w("EditAlarm", "on the hour of " + hourOfAlarm);
            }
            timeWheel.setCurrentHour(hourOfAlarm);
            timeWheel.setCurrentMinute(minuteOfAlarm);
        }

        /* set day of the week based on voice command */
        if (individualReturnedWords.contains("weekend")) {
            Log.w("VoiceRecognition", "Set for weekend");
            CheckBox sat = context.findViewById(R.id.saturdayCheckbox);
            sat.setChecked(true);
            CheckBox sun = context.findViewById(R.id.sundayCheckbox);
            sun.setChecked(true);
        }
        if (individualReturnedWords.contains("weekdays")) {
            Log.w("VoiceRecognition", "Set for weekdays");
            CheckBox mon = context.findViewById(R.id.mondayCheckbox);
            mon.setChecked(true);
            CheckBox tues = context.findViewById(R.id.tuesdayCheckbox);
            tues.setChecked(true);
            CheckBox wed = context.findViewById(R.id.wednesdayCheckbox);
            wed.setChecked(true);
            CheckBox thurs = context.findViewById(R.id.thursdayCheckbox);
            thurs.setChecked(true);
            CheckBox fri = context.findViewById(R.id.fridayCheckbox);
            fri.setChecked(true);
        }
        if (individualReturnedWords.contains("Monday")) {
            Log.w("EditAlarm", "You said Monday");
            CheckBox mon = context.findViewById(R.id.mondayCheckbox);
            mon.setChecked(true);
        }
        if (individualReturnedWords.contains("Tuesday")) {
            Log.w("EditAlarm", "You said Tuesday");
            CheckBox tues = context.findViewById(R.id.tuesdayCheckbox);
            tues.setChecked(true);
        }
        if (individualReturnedWords.contains("Wednesday")) {
            Log.w("EditAlarm", "You said Wednesday");
            CheckBox wed = context.findViewById(R.id.wednesdayCheckbox);
            wed.setChecked(true);
        }
        if (individualReturnedWords.contains("Thursday")) {
            Log.w("EditAlarm", "You said Thursday");
            CheckBox thurs = context.findViewById(R.id.thursdayCheckbox);
            thurs.setChecked(true);
        }
        if (individualReturnedWords.contains("Friday")) {
            Log.w("EditAlarm", "You said Friday");
            CheckBox fri = context.findViewById(R.id.fridayCheckbox);
            fri.setChecked(true);
        }
        if (individualReturnedWords.contains("Saturday")) {
            Log.w("EditAlarm", "You said Saturday");
            CheckBox sat = context.findViewById(R.id.saturdayCheckbox);
            sat.setChecked(true);
        }
        if (individualReturnedWords.contains("Sunday")) {
            Log.w("EditAlarm", "You said Sunday");
            CheckBox sun = context.findViewById(R.id.sundayCheckbox);
            sun.setChecked(true);
        }

        /* set alarm challenges and location based on voice command*/
        if (individualReturnedWords.contains("memory")) {
            Log.w("EditAlarm", "You said memory");
            CheckBox memory = context.findViewById(R.id.memoryCheckbox);
            memory.setChecked(true);
        }
        if (individualReturnedWords.contains("math")) {
            Log.w("EditAlarm", "You said math");
            CheckBox math = context.findViewById(R.id.mathCheckbox);
            math.setChecked(true);
        }
        if (individualReturnedWords.contains("game"))
            Log.w("EditAlarm", "You said game?");

        if (individualReturnedWords.contains("location")) {
            Log.w("EditAlarm", "Turn on location");
            Switch location = context.findViewById(R.id.locationSwitch);
            location.setChecked(true);
        }
    }
}
