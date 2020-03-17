/**
 * Model Class to hold images for the memory puzzle
 */
package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MemoryPuzzleModel {

    private List<Drawable> images; // all 40 images
    private List<Drawable> selectedImages; // selected images of those 40
    private HashMap<Button, Drawable> buttonImages;
    private Resources r;
    private int pairsFound;

    public MemoryPuzzleModel(Context context) {
        r = context.getApplicationContext().getResources();

        getImages();
        pairsFound = 0;
        buttonImages = new HashMap<>();
    }

    private void getImages() {
        // All possible images for the memory puzzle
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
        images.add(r.getDrawable(R.drawable.cow_128px));
        images.add(r.getDrawable(R.drawable.crocodile_128px));
        images.add(r.getDrawable(R.drawable.dinosaur_128px));
        images.add(r.getDrawable(R.drawable.dog_128px));
        images.add(r.getDrawable(R.drawable.dolphin_128px));
        images.add(r.getDrawable(R.drawable.dove_128px));
        images.add(r.getDrawable(R.drawable.duck_128px));
        images.add(r.getDrawable(R.drawable.eagle_128px));
        images.add(r.getDrawable(R.drawable.elephant_128px));
        images.add(r.getDrawable(R.drawable.fish_128px));
        images.add(r.getDrawable(R.drawable.flamingo_128px));
        images.add(r.getDrawable(R.drawable.fox_128px));
        images.add(r.getDrawable(R.drawable.frog_128px));
        images.add(r.getDrawable(R.drawable.giraffe_128px));
        images.add(r.getDrawable(R.drawable.gorilla_128px));
        images.add(r.getDrawable(R.drawable.horse_128px));
        images.add(r.getDrawable(R.drawable.kangoroo_128px));
        images.add(r.getDrawable(R.drawable.leopard_128px));
        images.add(r.getDrawable(R.drawable.lion_128px));
        images.add(r.getDrawable(R.drawable.monkey_128px));
        images.add(r.getDrawable(R.drawable.mouse_128px));
        images.add(r.getDrawable(R.drawable.panda_128px));
        images.add(r.getDrawable(R.drawable.parrot_128px));
        images.add(r.getDrawable(R.drawable.penguin_128px));
        images.add(r.getDrawable(R.drawable.shark_128px));
        images.add(r.getDrawable(R.drawable.sheep_128px));
        images.add(r.getDrawable(R.drawable.snake_128px));
        images.add(r.getDrawable(R.drawable.spider_128px));
        images.add(r.getDrawable(R.drawable.squirrel_128px));
        images.add(r.getDrawable(R.drawable.starfish_128px));
        images.add(r.getDrawable(R.drawable.tiger_128px));
        images.add(r.getDrawable(R.drawable.turtle_128px));
        images.add(r.getDrawable(R.drawable.wolf_128px));
        images.add(r.getDrawable(R.drawable.zebra_128px));


        // Select 6 random images
        selectedImages = new ArrayList<>();

        Random rand = new Random(System.currentTimeMillis());
        int index = rand.nextInt(images.size());
        Drawable img1 = images.get(index);
        selectedImages.add(img1);
        selectedImages.add(img1);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img2 = images.get(index);
        selectedImages.add(img2);
        selectedImages.add(img2);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img3 = images.get(index);
        selectedImages.add(img3);
        selectedImages.add(img3);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img4 = images.get(index);
        selectedImages.add(img4);
        selectedImages.add(img4);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img5 = images.get(index);
        selectedImages.add(img5);
        selectedImages.add(img5);
        images.remove(index);

        index = rand.nextInt(images.size());
        Drawable img6 = images.get(index);
        selectedImages.add(img6);
        selectedImages.add(img6);
        images.remove(index);

    }


    public void setButtonImage(Button button) {
        Random rand = new Random(System.currentTimeMillis());
        int index = rand.nextInt(selectedImages.size());
        buttonImages.put(button, selectedImages.get(index));
        selectedImages.remove(index);
    }


    public List<Drawable> getSelectedImages() {
        return selectedImages;
    }


    public int getPairsFound() {
        return pairsFound;
    }

    public void incrementPairsFound() {
        this.pairsFound++;
    }

    public Drawable getButtonImg(Button button) {
        return buttonImages.get(button);
    }

}
