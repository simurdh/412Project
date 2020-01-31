package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MemoryPuzzle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_puzzle);

        Button solveButton = findViewById(R.id.solveButton);

        solveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MathPuzzle.class);
                startActivityForResult(intent, 0);
            }
        });

    }


}
