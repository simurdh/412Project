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

    List<Drawable> images;
    List<Drawable> selectedImages;

    private Button firstClicked;
    private Drawable firstImage;
    private int pairsFound;

    private Button secondClicked;
    private Drawable secondImage;

    private HashMap<Button, Drawable> buttonImages;

    private boolean match; //keeps track of if last pair was a match

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
            Resources r  = getApplicationContext().getResources();

            Random rand = new Random(System.currentTimeMillis());
            int index = rand.nextInt(images.size());
            Drawable img = buttonImages.get(v);

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
                    pairsFound++;
                    if (pairsFound == 6) {
                        Intent intent = new Intent(v.getContext(), MathPuzzle.class);
                        startActivityForResult(intent, 0);
                    }
                } else {
                    // unmatch!
                    match = false;
                    v.setEnabled(true);
                }

                firstImage = null;
                secondImage = null;
            }
        }
    }

    private void getImages() {
        Resources r  = getApplicationContext().getResources();
        images = new ArrayList<>();
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
        int index = rand.nextInt(images.size());
        Drawable img1 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img2 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img3 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img4 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img5 = images.get(index);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img6 = images.get(index);
        images.remove(index);


        selectedImages = new ArrayList<>();
        selectedImages.add(img1);
        selectedImages.add(img2);
        selectedImages.add(img3);
        selectedImages.add(img4);
        selectedImages.add(img5);
        selectedImages.add(img6);
        selectedImages.add(img1);
        selectedImages.add(img2);
        selectedImages.add(img3);
        selectedImages.add(img4);
        selectedImages.add(img5);
        selectedImages.add(img6);
    }

    private void initButtons() {
        Random rand = new Random(System.currentTimeMillis());
        pairsFound = 0;

        // Associate buttons with images
        buttonImages = new HashMap<>();

        Button b00 = findViewById(R.id.c0r0);
        b00.setOnClickListener(new gridButtonClicked());
        int index = rand.nextInt(selectedImages.size());
        buttonImages.put(b00, selectedImages.get(index));
        selectedImages.remove(index);


        Button b10 = findViewById(R.id.c1r0);
        b10.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b10, selectedImages.get(index));
        selectedImages.remove(index);

        Button b20 = findViewById(R.id.c2r0);
        b20.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b20, selectedImages.get(index));
        selectedImages.remove(index);

        Button b01 = findViewById(R.id.c0r1);
        b01.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b01, selectedImages.get(index));
        selectedImages.remove(index);

        Button b11 = findViewById(R.id.c1r1);
        b11.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b11, selectedImages.get(index));
        selectedImages.remove(index);

        Button b21 = findViewById(R.id.c2r1);
        b21.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b21, selectedImages.get(index));
        selectedImages.remove(index);

        Button b02 = findViewById(R.id.c0r2);
        b02.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b02, selectedImages.get(index));
        selectedImages.remove(index);

        Button b12 = findViewById(R.id.c1r2);
        b12.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b12, selectedImages.get(index));
        selectedImages.remove(index);

        Button b22 = findViewById(R.id.c2r2);
        b22.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b22, selectedImages.get(index));
        selectedImages.remove(index);

        Button b03 = findViewById(R.id.c0r3);
        b03.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b03, selectedImages.get(index));
        selectedImages.remove(index);

        Button b13 = findViewById(R.id.c1r3);
        b13.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b13, selectedImages.get(index));
        selectedImages.remove(index);

        Button b23 = findViewById(R.id.c2r3);
        b23.setOnClickListener(new gridButtonClicked());
        index = rand.nextInt(selectedImages.size());
        buttonImages.put(b23, selectedImages.get(index));
        selectedImages.remove(index);
    }
}
