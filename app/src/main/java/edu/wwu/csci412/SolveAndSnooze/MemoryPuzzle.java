package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MemoryPuzzle extends AppCompatActivity {

    private Drawable img1;
    private Drawable img2;
    private Drawable img3;
    private Drawable img4;
    private Drawable img5;
    private Drawable img6;

    private HashMap<Drawable, Integer> imgMap; //map of images not turned over and how many have been turned over


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_puzzle);

        Button solveButton = findViewById(R.id.solveButton);
        solveButton.setOnClickListener(new solveButtonClicked());

        getImages();
        initButtons();
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
            Random r = new Random(System.currentTimeMillis());
            List<Drawable> keys = new ArrayList<>(imgMap.keySet());
            Drawable img = keys.get(r.nextInt(keys.size()));
            int uncovered = imgMap.get(img) + 1;


            if (uncovered > 1) {
                // Both images have been turned over!
                imgMap.remove(img);

            } else {
                imgMap.put(img, uncovered);
            }

            v.setBackgroundDrawable(img);
            v.setEnabled(false);
        }
    }

    private void getImages() {
        // TODO:

        Resources r  = getApplicationContext().getResources();
        ArrayList<Drawable> images = new ArrayList<>();
        images.add(r.getDrawable(R.drawable.dinosaur_128px));
        images.add(r.getDrawable(R.drawable.bat_128px));
        images.add(r.getDrawable(R.drawable.bear_128px));
        images.add(r.getDrawable(R.drawable.bee_128px));
        images.add(r.getDrawable(R.drawable.bird_128px));
        images.add(r.getDrawable(R.drawable.bug_128px));
        images.add(r.getDrawable(R.drawable.butterfly_128px));
        images.add(r.getDrawable(R.drawable.camel_128px));
        images.add(r.getDrawable(R.drawable.cat_128px));
        images.add(r.getDrawable(R.drawable.cheetah_128px));
        images.add(r.getDrawable(R.drawable.chicken_128px));
        images.add(r.getDrawable(R.drawable.coala_128px));


        Random rand = new Random(System.currentTimeMillis());
        int index = rand.nextInt() % images.size();
        img1 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        img2 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        img3 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        img4 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        img5 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        img6 = images.get(index);
        images.remove(index);


        imgMap = new HashMap<>();
        imgMap.put(img1, 0);
        imgMap.put(img2, 0);
        imgMap.put(img3, 0);
        imgMap.put(img4, 0);
        imgMap.put(img5, 0);
        imgMap.put(img6, 0);
    }

    private void initButtons() {
        Button b00 = findViewById(R.id.c0r0);
        b00.setOnClickListener(new gridButtonClicked());

        Button b10 = findViewById(R.id.c1r0);
        b10.setOnClickListener(new gridButtonClicked());

        Button b20 = findViewById(R.id.c2r0);
        b20.setOnClickListener(new gridButtonClicked());

        Button b01 = findViewById(R.id.c0r1);
        b01.setOnClickListener(new gridButtonClicked());

        Button b11 = findViewById(R.id.c1r1);
        b11.setOnClickListener(new gridButtonClicked());

        Button b21 = findViewById(R.id.c2r1);
        b21.setOnClickListener(new gridButtonClicked());

        Button b02 = findViewById(R.id.c0r2);
        b02.setOnClickListener(new gridButtonClicked());

        Button b12 = findViewById(R.id.c1r2);
        b12.setOnClickListener(new gridButtonClicked());

        Button b22 = findViewById(R.id.c2r2);
        b22.setOnClickListener(new gridButtonClicked());

        Button b03 = findViewById(R.id.c0r3);
        b03.setOnClickListener(new gridButtonClicked());

        Button b13 = findViewById(R.id.c1r3);
        b13.setOnClickListener(new gridButtonClicked());

        Button b23 = findViewById(R.id.c2r3);
        b23.setOnClickListener(new gridButtonClicked());
    }
}
