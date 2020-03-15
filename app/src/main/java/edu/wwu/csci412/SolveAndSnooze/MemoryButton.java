/**
 * Button for the memory puzzle
 */
package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatButton;

public class MemoryButton extends AppCompatButton {
    private Drawable img;

    public MemoryButton(Context context) {
        super(context);
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
