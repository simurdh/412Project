package edu.wwu.csci412.SolveAndSnooze;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

//character sprite traits, draw within bounds of screen
public class CharacterSprite {

    //values of a CharacterSprite object
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

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    //draw the bitmap image on the screen
    public void draw (Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    //called repetitively to move sprite around screen constantly
    public void update() {
/*
        if (x<-10) {
            x = screenWidth/2;
        } else {
            x += xVelocity;

            // change velocity direction if at screen edge
            if ((x > screenWidth - image.getWidth()) || (x < 0)) {
                xVelocity = xVelocity*-1;
            }
        }*/
    }

    //called to update location based on sensor data change
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
        //make it go up and down constantly
        if (y<-10) {
            y = screenHeight/2;
        } else {
            y += yVelocity;
            // change velocity direction if at screen edge
            if ((y > screenHeight - image.getHeight() || y < 0)) {
                yVelocity = yVelocity*-1;
            }
        }
    }

}
