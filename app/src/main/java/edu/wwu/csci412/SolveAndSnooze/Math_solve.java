package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Math_solve extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_solve);
    }

    public void onClickSwitch(View v) {
        Intent intent = new Intent(this, MemoryPuzzle.class);
        startActivity(intent);
    }
}
