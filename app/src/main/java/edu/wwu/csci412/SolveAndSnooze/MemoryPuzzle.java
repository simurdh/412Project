package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MemoryPuzzle extends AppCompatActivity {

    private Button firstClicked;
    private Button secondClicked;
    private Drawable firstImage;
    private Drawable secondImage;
    private ButtonGridView view;
    private int callingAlarmId;
    private boolean match; //keeps track of if last pair was a match
    private MediaPlayer sound;
    private MemoryPuzzleModel memoryPuzzleModel;
    private DatabaseManager db;
    private int challengesCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels / 5 - getStatusBarHeight();
        int offset = 0; //used to center gridview

        //Get the id of the alarm that was triggered.
        callingAlarmId = this.getIntent().getIntExtra("alarmID", 0);

        //Get the current challenge completed count.
        challengesCompleted = this.getIntent().getIntExtra("challengesCompleted",0);

        //Get the database.
        db = new DatabaseManager(this);

        // Choose the smaller of values for the button size, so they will always fit on the screen
        if (height < width) {
            offset = (width*3 - height*3)/2;
            width = height;
        }

        memoryPuzzleModel = new MemoryPuzzleModel(this);
        sound = MediaPlayer.create(this,R.raw.alarm);
        sound.setLooping(true);
        sound.start();
        view = new ButtonGridView(this, width, offset, new gridButtonClicked(), memoryPuzzleModel);
        setContentView(view);

    }

    private class solveButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private class gridButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Resources r  = getApplicationContext().getResources();

            Drawable img = memoryPuzzleModel.getButtonImg((Button) v);

            v.setBackgroundDrawable(img);

            // first image clicked
            if (firstImage == null) {
                // Check if we need to reset previous images
                if (!match) {
                    if (firstClicked != null && secondClicked != null) {
                        firstClicked.setBackgroundColor(r.getColor(R.color.teal));
                        secondClicked.setBackgroundColor(r.getColor(R.color.teal));
                        firstClicked.setEnabled(true);
                        secondClicked.setEnabled(true);
                    }
                }

                firstImage = img;
                firstClicked = (Button) v;
                v.setEnabled(false);

            } else {
                //second image clicked
                secondImage = img;
                secondClicked = (Button) v;

                if (firstImage == secondImage) {
                    // match!
                    v.setEnabled(false);
                    match = true;
                    memoryPuzzleModel.incrementPairsFound();
                    if (memoryPuzzleModel.getPairsFound() == 6) {
                        sound.pause();
                        sound.stop();
                        sound.release();
                        ArrayList<Intent> validIntents = new ArrayList<Intent>();
                        Intent memIntent = new Intent(v.getContext(),MemoryPuzzle.class);
                        Intent mathIntent = new Intent(v.getContext(), MathPuzzle.class);
                        Intent tiltIntent = new Intent(v.getContext(), SensorData.class);

                        System.out.println("CALLING ALARM ID IS: "+callingAlarmId);

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
                            System.out.println("PASSING ALARM ID: "+callingAlarmId);
                            startActivityForResult(currentIntent,0);
                        }
                    }
                } else {
                    // not a match!
                    match = false;
                    v.setEnabled(true);
                }

                firstImage = null;
                secondImage = null;
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
