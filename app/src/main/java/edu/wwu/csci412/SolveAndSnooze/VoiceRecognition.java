package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.SeekBar;
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
                    Log.w("VoiceRecognition", "Caught number format exception");
                }
                Log.w("VoiceRecognition", "hour is " + hourOfAlarm);
                Log.w("VoiceRecognition", "minute is " + minuteOfAlarm);
            }
            else {
                try {
                    hourOfAlarm += Integer.parseInt(individualReturnedWords.get(timeForAlarmIndex));
                }catch (NumberFormatException e) {
                    Log.w("VoiceRecognition", "Caught number format exception");
                }
                Log.w("VoiceRecognition", "on the hour of " + hourOfAlarm);
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
            Log.w("VoiceRecognition", "You said Monday");
            CheckBox mon = context.findViewById(R.id.mondayCheckbox);
            mon.setChecked(true);
        }
        if (individualReturnedWords.contains("Tuesday")) {
            Log.w("VoiceRecognition", "You said Tuesday");
            CheckBox tues = context.findViewById(R.id.tuesdayCheckbox);
            tues.setChecked(true);
        }
        if (individualReturnedWords.contains("Wednesday")) {
            Log.w("VoiceRecognition", "You said Wednesday");
            CheckBox wed = context.findViewById(R.id.wednesdayCheckbox);
            wed.setChecked(true);
        }
        if (individualReturnedWords.contains("Thursday")) {
            Log.w("VoiceRecognition", "You said Thursday");
            CheckBox thurs = context.findViewById(R.id.thursdayCheckbox);
            thurs.setChecked(true);
        }
        if (individualReturnedWords.contains("Friday")) {
            Log.w("VoiceRecognition", "You said Friday");
            CheckBox fri = context.findViewById(R.id.fridayCheckbox);
            fri.setChecked(true);
        }
        if (individualReturnedWords.contains("Saturday")) {
            Log.w("VoiceRecognition", "You said Saturday");
            CheckBox sat = context.findViewById(R.id.saturdayCheckbox);
            sat.setChecked(true);
        }
        if (individualReturnedWords.contains("Sunday")) {
            Log.w("VoiceRecognition", "You said Sunday");
            CheckBox sun = context.findViewById(R.id.sundayCheckbox);
            sun.setChecked(true);
        }

        /* set challenges based on number said */
        //catch challenges or puzzles or puzzle or challenge or game or games
        int challengeWordIndex = -1;
        if (individualReturnedWords.contains("challenge")) {
            Log.w("VoiceRecognition", "You indicated number of challenge");
            challengeWordIndex = individualReturnedWords.indexOf("challenge");
        }
        else if (individualReturnedWords.contains("challenges")) {
            Log.w("VoiceRecognition", "You indicated number of challenges");
            challengeWordIndex = individualReturnedWords.indexOf("challenges");
        }
        else if (individualReturnedWords.contains("puzzle")) {
            Log.w("VoiceRecognition", "You indicated number of puzzle");
            challengeWordIndex = individualReturnedWords.indexOf("puzzle");
        }
        else if (individualReturnedWords.contains("puzzles")) {
            Log.w("VoiceRecognition", "You indicated number of puzzles");
            challengeWordIndex = individualReturnedWords.indexOf("puzzles");
        }
        else if (individualReturnedWords.contains("game")) {
            Log.w("VoiceRecognition", "You indicated number of game");
            challengeWordIndex = individualReturnedWords.indexOf("game");
        }
        else if (individualReturnedWords.contains("games")) {
            Log.w("VoiceRecognition", "You indicated number of games");
            challengeWordIndex = individualReturnedWords.indexOf("games");
        }
        int numberOfChallenges = -1;
        if (challengeWordIndex != -1) {
            if (challengeWordIndex-1 >= 0) {
                try {
                    numberOfChallenges = Integer.parseInt(individualReturnedWords.get(challengeWordIndex-1));
                }catch (NumberFormatException e) {
                    Log.w("VoiceRecognition", "Caught number format exception");
                }
                Log.w("VoiceRecognition", "This many challenges requested: " + numberOfChallenges);
            }
            timeWheel.setCurrentHour(hourOfAlarm);
            //set number of challenges
            if (numberOfChallenges >= 0 && numberOfChallenges <= 5) {
                SeekBar challengesBar = context.findViewById(R.id.seekBar);
                challengesBar.setProgress(numberOfChallenges);
            }
        }

        /* set alarm challenges and location based on voice command*/
        if (individualReturnedWords.contains("memory")) {
            Log.w("VoiceRecognition", "You said memory");
            CheckBox memory = context.findViewById(R.id.memoryCheckbox);
            memory.setChecked(true);
        }
        if (individualReturnedWords.contains("math")) {
            Log.w("VoiceRecognition", "You said math");
            CheckBox math = context.findViewById(R.id.mathCheckbox);
            math.setChecked(true);
        }
        if (individualReturnedWords.contains("tilt")) {
            Log.w("VoiceRecognition", "You said tilt");
            //CheckBox tlt = context.findViewById(R.id.tiltCheckbox);
            //tilt.setChecked(true);
        }
        if (individualReturnedWords.contains("game"))
            Log.w("VoiceRecognition", "You said game?");

        if (individualReturnedWords.contains("location")) {
            Log.w("VoiceRecognition", "Turn on location");
            Switch location = context.findViewById(R.id.locationSwitch);
            location.setChecked(true);
        }
    }
}
