package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    private boolean match; //keeps track of if last pair was a match
    private MediaPlayer sound = MediaPlayer.create(this,R.raw.alarm);

    private MemoryPuzzleModel memoryPuzzleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sound.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_puzzle);

        Button solveButton = findViewById(R.id.solveButton);
        solveButton.setOnClickListener(new solveButtonClicked());

        memoryPuzzleModel = new MemoryPuzzleModel(this);
        initButtons();
    }

    private class solveButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            sound.stop();
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

    // Initialize button listeners and images
    private void initButtons() {
        Button b00 = findViewById(R.id.c0r0);
        b00.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b00);


        Button b10 = findViewById(R.id.c1r0);
        b10.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b10);

        Button b20 = findViewById(R.id.c2r0);
        b20.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b20);

        Button b01 = findViewById(R.id.c0r1);
        b01.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b01);

        Button b11 = findViewById(R.id.c1r1);
        b11.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b11);

        Button b21 = findViewById(R.id.c2r1);
        b21.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b21);

        Button b02 = findViewById(R.id.c0r2);
        b02.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b02);

        Button b12 = findViewById(R.id.c1r2);
        b12.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b12);

        Button b22 = findViewById(R.id.c2r2);
        b22.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b22);

        Button b03 = findViewById(R.id.c0r3);
        b03.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b03);

        Button b13 = findViewById(R.id.c1r3);
        b13.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b13);

        Button b23 = findViewById(R.id.c2r3);
        b23.setOnClickListener(new gridButtonClicked());
        memoryPuzzleModel.setButtonImage(b23);
    }
}
