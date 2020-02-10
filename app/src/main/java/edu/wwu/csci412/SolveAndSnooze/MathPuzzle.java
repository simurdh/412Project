package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MathPuzzle extends AppCompatActivity {

    // THESE FIELDS SHOULD BE MOVED TO A MODEL

    private enum Operator {
        mul, div, sub, add
    }

    private int[] lOperands = new int[3];
    private int[] rOperands = new int[3];
    private Operator[] operators = new Operator[3];

    // \FIELDS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_solve);

        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void setQuesitions(){


    }

}