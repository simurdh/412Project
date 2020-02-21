package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MemoryPuzzle extends AppCompatActivity {

    private Button firstClicked;
    private Button secondClicked;
    private Drawable firstImage;
    private Drawable secondImage;
    private ButtonGridView view;

    private boolean match; //keeps track of if last pair was a match

    private MemoryPuzzleModel memoryPuzzleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x/3;
        int height = size.y/5;
        int offset = 0; //used to center gridview

        // Choose the smaller of values for the button size, so they will always fit on the screen
        if (height < width) {
            offset = (width*3 - height*3)/2;
            width = height;
        }

        memoryPuzzleModel = new MemoryPuzzleModel(this);
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
}
