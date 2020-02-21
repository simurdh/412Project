package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class ButtonGridView extends GridLayout {
    private int width;
    private MemoryButton[][] buttons;

    public ButtonGridView(Context context, int width, int offset, View.OnClickListener listener, MemoryPuzzleModel puzzle) {
        super(context);
        this.width = width;

        setColumnCount(3);
        setRowCount(5);

        setPadding(offset, 0, 0, 0);

        // Set up layout parameters of 1st row of gridLayout
        TextView title = new TextView(context);
        GridLayout.Spec rowSpec = GridLayout.spec(0, 1);
        GridLayout.Spec colSpec = GridLayout.spec(0, 3);

        GridLayout.LayoutParams lpStatus = new LayoutParams(rowSpec, colSpec);
        title.setLayoutParams(lpStatus);
        title.setWidth(3*width);
        title.setHeight(width);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(getResources().getDimension(R.dimen.textsize));
        title.setText(getResources().getString(R.string.title));
        addView(title);

        // Create buttons
        buttons = new MemoryButton[4][3];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new MemoryButton(context);
                buttons[row][col].setOnClickListener(listener);
                buttons[row][col].setBackgroundColor(getResources().getColor(R.color.teal));
                buttons[row][col].setEnabled(true);
                puzzle.setButtonImage(buttons[row][col]);
                addView(buttons[row][col], width, width);
            }
        }
    }

    public ButtonGridView(Context context) {
        super(context);
    }

}
