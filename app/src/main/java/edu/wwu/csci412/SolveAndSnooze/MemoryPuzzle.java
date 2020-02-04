package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MemoryPuzzle extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_puzzle);

        Button solveButton = findViewById(R.id.solveButton);
        solveButton.setOnClickListener(new solveButtonClicked());

        Button b00 = findViewById(R.id.c0r0);
        Button b10 = findViewById(R.id.c1r0);
        b00.setOnClickListener(new gridButtonClicked());
        b10.setOnClickListener(new gridButtonClicked());

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
            Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.dinosaur_128px);
            v.setBackgroundDrawable(img);
        }
    }
}
