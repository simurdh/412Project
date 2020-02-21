package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private static CharacterSprite characterSprite;
    private static CharacterSprite userSprite;
    private SensorData sensorData;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        //new instance of thread
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dinosaur_128px), 100,100);
        userSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.cow_128px), 500, 500);

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

    public void update() {
        characterSprite.update();
        userSprite.updateUser(0,0);
    }

    public static void updateUser(float xAccel, float yAccel) {
        characterSprite.update();
        userSprite.updateUser(xAccel, yAccel);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(canvas != null) {
            characterSprite.draw(canvas);
            userSprite.draw(canvas);
        }
    }
}
