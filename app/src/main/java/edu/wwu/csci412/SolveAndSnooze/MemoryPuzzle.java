/**
 * Memory puzzle controller class
 */
package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MemoryPuzzle extends AppCompatActivity {

    private Button firstClicked;
    private Button secondClicked;
    private Drawable firstImage;
    private Drawable secondImage;
    private ButtonGridView view;

    private boolean match; //keeps track of if last pair was a match
    private MediaPlayer sound;
    private MemoryPuzzleModel memoryPuzzleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels / 3;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels / 5 - getStatusBarHeight();
        int offset = 0; //used to center gridview

        // Choose the smaller of values for the button size, so they will always fit on the screen
        if (height < width) {
            offset = (width*3 - height*3)/2;
            width = height;
        }

        SoundManager soundSelection = SoundManager.getInstance(this);

        memoryPuzzleModel = new MemoryPuzzleModel(this);
        sound = MediaPlayer.create(this,soundSelection.currSound);
        sound.setLooping(true);
        sound.start();
        view = new ButtonGridView(this, width, offset, new gridButtonClicked(), memoryPuzzleModel);
        setContentView(view);

    }

    private class solveButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MathPuzzle.class);
            startActivityForResult(intent, 0);
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
                        Intent intent = new Intent(v.getContext(), MathPuzzle.class);
                        startActivityForResult(intent, 0);
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
