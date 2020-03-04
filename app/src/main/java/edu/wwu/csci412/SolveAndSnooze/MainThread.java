package edu.wwu.csci412.SolveAndSnooze;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//controls the execution of the character sprite moving around
public class MainThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private Boolean running;
    public static Canvas canvas;

    //begin the drawing of the character sprites
    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    //synchronize the drawing of character sprites on screen
    @Override
    public void run() {
        while (running) {
            canvas = null;

            // one thread at a time
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //set running boolean variable
    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

}
