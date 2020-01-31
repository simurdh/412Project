package edu.wwu.csci412.SolveAndSnooze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MemoryPuzzle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_puzzle);
    }

    public void onClickSwitch(View v) {
       finish();
    }

}
