/**
 * Math puzzle
 */

package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.media.AudioManager;

import java.util.ArrayList;
import java.util.Random;

public class MathPuzzle extends AppCompatActivity {

    private static final String tag = "MATH_PUZZLE";

    // THESE FIELDS SHOULD BE MOVED TO A MODEL

    private Random rand;

    private enum Operator {
        mul, sub, add
    }

    private int[] operands = new int[6];
    private int[] answers = new int[3];
    private Operator[] operators = new Operator[3];
    private MediaPlayer sound;
    private DatabaseManager db;
    private int callingAlarmId;
    private int challengesCompleted;

    // \FIELDS

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        rand = new Random();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_solve);

        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAnswers()){
                    onSuccess(v);
                } else {
                    onFailure();
                }
            }
        });

        setupAudio();
        SoundManager soundSelection = SoundManager.getInstance(this);

        sound = MediaPlayer.create(this, soundSelection.currSound);
        sound.setLooping(true);
        sound.setVolume(100,100);
        sound.start();
        generateQuestions();

        setQuesitions();
    }

    @Override
    public void onBackPressed()
    {
        //Do nothing.
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN))
        {
            //Do nothing
        }
        if((keyCode == KeyEvent.KEYCODE_VOLUME_UP))
        {
            //Do nothing
        }
        return true;
    }

    // THESE METHODS SHOULD OUGHT TO BE IN A MODEL

    private Operator generateOp(){
        int opChoice = this.rand.nextInt(100) % 3; //Bounds at 100 to get more randomness

        if(opChoice == 0){
            return Operator.mul;
        } else if(opChoice ==1){
            return Operator.sub;
        } else {
            return Operator.add;
        }
    }

    private void generateQuestions(){
        int j = 0;

        for (int i = 0; i < 6; i ++){

            operands[i] = this.rand.nextInt(11) + 1;

            if((i - 1) % 2 == 0){ // every other index (1,3, etc...)
                Operator op = generateOp();
                operators[j] = op;
                answers[j] = getAnswer(i, operands, op);

                j += 1;
            }
        }
    }

    private int getAnswer(int i, int[] operands, Operator op){
        int answer;

        if(op == Operator.add){
            answer = operands[i - 1] + operands[i];
        } else if(op == Operator.sub){
            answer = operands[i - 1] - operands[i];
        } else {
            answer = operands[i - 1] * operands[i];
        }

        return answer;
    }

    private String getOpChar(Operator op){
        String opChar;

        if(op == Operator.add){
            opChar = "+";
        } else if(op == Operator.sub){
            opChar = "-";
        } else {
            opChar = "x";
        }

        return opChar;
    }

    // \Model methods

    private void setQuesitions(){
        TextView q1 = findViewById(R.id.question1_text);
        TextView q2 = findViewById(R.id.question2_text);
        TextView q3 = findViewById(R.id.question3_text);

        q1.setText(String.format("%d %s %d",
                operands[0],
                getOpChar(operators[0]),
                operands[1]
        ));

        q2.setText(String.format("%d %s %d",
                operands[2],
                getOpChar(operators[1]),
                operands[3]
        ));

        q3.setText(String.format("%d %s %d",
                operands[4],
                getOpChar(operators[2]),
                operands[5]
        ));
    }


    private boolean checkAnswers(){
        boolean allCorrect = true;

        EditText a1 = findViewById(R.id.answer1);
        EditText a2 = findViewById(R.id.answer2);
        EditText a3 = findViewById(R.id.answer3);

        if (a1.getText().toString().isEmpty()){
            return false;
        } else if(a2.getText().toString().isEmpty()){
            return false;
        } else if(a3.getText().toString().isEmpty()){
            return false;
        }

        int a1Int = Integer.parseInt(a1.getText().toString());
        int a2Int = Integer.parseInt(a2.getText().toString());
        int a3Int = Integer.parseInt(a3.getText().toString());

        if(a1Int != answers[0]){
            allCorrect = false;
        }

        if(a2Int != answers[1]){
            allCorrect = false;
        }

        if(a3Int != answers[2]){
            allCorrect = false;
        }

        return allCorrect;
    }

    private void onSuccess(View v){
        sound.pause();
        sound.stop();
        sound.release();

        //Get the database.
        db = new DatabaseManager(this);

        //Get the id of the alarm that was triggered.
        callingAlarmId = this.getIntent().getIntExtra("alarmID", 0);

        //Get the current number of challenges completed.
        challengesCompleted = this.getIntent().getIntExtra("challengesCompleted",0);

        ArrayList<Intent> validIntents = new ArrayList<Intent>();
        Intent memIntent = new Intent(v.getContext(),MemoryPuzzle.class);
        Intent mathIntent = new Intent(v.getContext(), MathPuzzle.class);
        Intent tiltIntent = new Intent(v.getContext(), SensorData.class);

        System.out.println("CALLING ALARM ID: "+callingAlarmId);

        if(Boolean.parseBoolean(db.selectById(callingAlarmId).getMemEnabled()))
            validIntents.add(memIntent);
        if(Boolean.parseBoolean(db.selectById(callingAlarmId).getMathEnabled()))
            validIntents.add(mathIntent);
        if(Boolean.parseBoolean(db.selectById(callingAlarmId).getTiltEnabled()))
            validIntents.add(tiltIntent);

        challengesCompleted++;

        if(db.selectById(callingAlarmId).getChallenges() == challengesCompleted)
        {
            Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
            startActivityForResult(mainIntent,0);
        }
        else
        {
            Random random = new Random();
            Intent currentIntent = validIntents.get(random.nextInt(validIntents.size()));
            currentIntent.putExtra("alarmID",callingAlarmId);
            currentIntent.putExtra("challengesCompleted", challengesCompleted);
            startActivityForResult(currentIntent,0);
        }
    }

    private void onFailure(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Incorrect answer(s) try again!");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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
}