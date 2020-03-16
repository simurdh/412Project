package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

//
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private static CharacterSprite characterSprite;
    private static CharacterSprite characterSprite2;
    private static CharacterSprite characterSprite3;
    private static CharacterSprite characterSprite4;
    private static CharacterSprite userSprite;
    private static boolean showCharacterSprite;
    private static boolean showCharacterSprite2;
    private static boolean showCharacterSprite3;
    private static boolean showCharacterSprite4;
    private static boolean showUserSprite;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private MediaPlayer sound;
    private int callingAlarmId;
    private DatabaseManager db;
    private int challengesCompleted;


    public GameView(Context context, int alarmId, int challengesCompleted) {
        super(context);

        getHolder().addCallback(this);
        callingAlarmId = alarmId;
        db = new DatabaseManager(context);
        this.challengesCompleted = challengesCompleted;

        //new instance of thread
        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //set up screen to show sprites and start thread that updates the view
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //array of 8 integers
        ArrayList<Integer> possiblePosition = new ArrayList<>();
        ArrayList<Integer> position = new ArrayList<>();
        //randomly populate with 2,3,4,5,6,7,8,9
        for (int i = 0; i < 8; i++) {
            possiblePosition.add(i+2);
        }

        Random rand = new Random(System.currentTimeMillis());
        for (int j = 0; j < 8; j++) {
            if (possiblePosition.size() > 0) {
                int randIdx = rand.nextInt(possiblePosition.size());
                int idx = possiblePosition.get(randIdx);
                position.add(idx);
                possiblePosition.remove(randIdx);
            }
            else {
                position.add(0);
            }
        }

        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.mouse_64px), screenWidth/position.get(0),screenHeight/position.get(1));
        characterSprite2 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.mouse_64px), screenWidth/position.get(2),screenHeight/position.get(3));
        characterSprite3 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.mouse_64px), screenWidth/position.get(4),screenHeight/position.get(5));
        characterSprite4 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.mouse_64px), screenWidth/position.get(6),screenHeight/position.get(7));
        userSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.fox_64px), screenWidth/2, screenHeight/2);

        Context context = getContext();
        sound = MediaPlayer.create(context, R.raw.alarm);
        sound.setLooping(true);
        sound.setVolume(100,100);
        sound.start();

        showCharacterSprite = true;
        showCharacterSprite2 = true;
        showCharacterSprite3 = true;
        showCharacterSprite4 = true;
        showUserSprite = true;

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    //if sprites are touching, remove the non-user sprite form the screen and update other sprites
    //exit the screen if there are no more non-user sprites remaining
    public void update() {
        if (areTouching(userSprite, characterSprite))
            showCharacterSprite = false;
        if (areTouching(userSprite, characterSprite2))
            showCharacterSprite2 = false;
        if (areTouching(userSprite, characterSprite3))
            showCharacterSprite3 = false;
        if (areTouching(userSprite, characterSprite4))
            showCharacterSprite4 = false;

        //exit game screen
        if (!showCharacterSprite && !showCharacterSprite2 && !showCharacterSprite3 && !showCharacterSprite4) {

            sound.pause();
            sound.stop();
            sound.release();

            thread.setRunning(false);

            Context context = getContext();

            ArrayList<Intent> validIntents = new ArrayList<Intent>();
            Intent memIntent = new Intent(context,MemoryPuzzle.class);
            Intent mathIntent = new Intent(context, MathPuzzle.class);
            Intent tiltIntent = new Intent(context, SensorData.class);

            if(Boolean.parseBoolean(db.selectById(callingAlarmId).getMemEnabled()))
                validIntents.add(memIntent);
            if(Boolean.parseBoolean(db.selectById(callingAlarmId).getMathEnabled()))
                validIntents.add(mathIntent);
            if(Boolean.parseBoolean(db.selectById(callingAlarmId).getTiltEnabled()))
                validIntents.add(tiltIntent);

            challengesCompleted++;

            if(db.selectById(callingAlarmId).getChallenges() == challengesCompleted)
            {
                Intent mainIntent = new Intent(context, MainActivity.class);
                context.startActivity(mainIntent);
            }
            else
            {
                Random random = new Random();
                Intent currentIntent = validIntents.get(random.nextInt(validIntents.size()));
                currentIntent.putExtra("alarmID",callingAlarmId);
                currentIntent.putExtra("challengesCompleted", challengesCompleted);
                context.startActivity(currentIntent);
            }
        }

        if (showCharacterSprite)
            characterSprite.update();
        if (showCharacterSprite2)
            characterSprite2.update();
        if (showCharacterSprite3)
            characterSprite3.update();
        if (showCharacterSprite4)
            characterSprite4.update();
        if (showUserSprite)
            userSprite.updateUser(0,0);
    }

    //if sprites are touching, remove the non-user sprite form the screen and update other sprites
    public static void updateUser(float xAccel, float yAccel) {
        if (areTouching(userSprite, characterSprite))
            showCharacterSprite = false;
        if (areTouching(userSprite, characterSprite2))
            showCharacterSprite2 = false;
        if (areTouching(userSprite, characterSprite3))
            showCharacterSprite3 = false;
        if (areTouching(userSprite, characterSprite4))
            showCharacterSprite4 = false;

        if (showCharacterSprite)
            characterSprite.update();
        if (showCharacterSprite2)
            characterSprite2.update();
        if (showCharacterSprite3)
            characterSprite3.update();
        if (showCharacterSprite4)
            characterSprite4.update();
        if (showUserSprite)
            userSprite.updateUser(xAccel, yAccel);
    }

    //return true if the two sprites passed in are within 50 units, considered 'touching'
    public static boolean areTouching(CharacterSprite predator, CharacterSprite prey) {
        int distApartX = predator.getX() - prey.getX();
        int distApartY = predator.getY() - prey.getY();
        if (distApartX < 0) {
            distApartX = distApartX*-1;
        }
        if (distApartY < 0) {
            distApartY = distApartY*-1;
        }
        if (distApartX < 50 && distApartY < 50) {
            Log.d(TAG, "crossed it");
            return true;
        }
        else {
            return false;
        }
    }

    //draw sprites if they are supposed to be shown
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(canvas != null) {
            if (showCharacterSprite)
                characterSprite.draw(canvas);
            if (showCharacterSprite2)
                characterSprite2.draw(canvas);
            if (showCharacterSprite3)
                characterSprite3.draw(canvas);
            if (showCharacterSprite4)
                characterSprite4.draw(canvas);
            if (showUserSprite)
                userSprite.draw(canvas);
        }
    }
}
