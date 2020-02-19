package edu.wwu.csci412.SolveAndSnooze;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {

    private Bitmap image;
    private int x;
    private int y;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public CharacterSprite(Bitmap bmp, int xStart, int yStart) {
        image = bmp;
        x = xStart;
        y = yStart;

    }


    public void draw (Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {

        if (x<0 && y<0) {
            x = screenWidth/2;
            y = screenHeight/2;
        } else {
            x += xVelocity;
            y += yVelocity;
            // change velocity direction if at screen edge
            if ((x > screenWidth - image.getWidth()) || (x < 0)) {
                xVelocity = xVelocity*-1;
            }
            if ((y > screenHeight - image.getHeight() || y < 0)) {
                yVelocity = yVelocity*-1;
            }
        }

    }

    public void updateUser(float xAccel, float yAccel) {
        //move left
        if (xAccel < 0) {
            if (xVelocity > 0) {
                xVelocity = xVelocity*-1;
            }
            // don't move left off screen
            if (x > 0) {
                x += xVelocity;
            }

        }
        //move right
        else if (xAccel > 0) {
            if (xVelocity < 0) {
                xVelocity = xVelocity*-1;
            }
            // don't move right off screen
            if (x < screenWidth-image.getWidth()) {
                x += xVelocity;
            }
        }
        else {
            //do nothing
        }



    }

}
