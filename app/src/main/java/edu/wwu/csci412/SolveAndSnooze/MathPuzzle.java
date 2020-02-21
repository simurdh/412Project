package edu.wwu.csci412.SolveAndSnooze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.channels.AlreadyBoundException;
import java.util.Random;

public class MathPuzzle extends AppCompatActivity {

    private static final String tag = "MATH_PUZZLE";

    // THESE FIELDS SHOULD BE MOVED TO A MODEL

    private Random rand;

    private enum Operator {
        mul, sub, add
    }

    private int[] operands = new int[6];
    private int[] answers = new int[3];
    private Operator[] operators = new Operator[3];
    private MediaPlayer sound;

    // \FIELDS

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        rand = new Random();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_solve);

        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAnswers()){
                    onSuccess(v);
                } else {
                    onFailure();
                }
            }
        });

        sound = MediaPlayer.create(this, R.raw.alarm);
        sound.setLooping(true);
        sound.start();
        generateQuestions();
        setQuesitions();
    }

    // THESE METHODS SHOULD OUGHT TO BE IN A MODEL

    private Operator generateOp(){
        int opChoice = this.rand.nextInt(100) % 3; //Bounds at 100 to get more randomness

        if(opChoice == 0){
            return Operator.mul;
        } else if(opChoice ==1){
            return Operator.sub;
        } else {
            return Operator.add;
        }
    }

    private void generateQuestions(){
        int j = 0;

        for (int i = 0; i < 6; i ++){

            operands[i] = this.rand.nextInt(11) + 1;

            if((i - 1) % 2 == 0){ // every other index (1,3, etc...)
                Operator op = generateOp();
                operators[j] = op;
                answers[j] = getAnswer(i, operands, op);

                j += 1;
            }
        }
    }

    private int getAnswer(int i, int[] operands, Operator op){
        int answer;

        if(op == Operator.add){
            answer = operands[i - 1] + operands[i];
        } else if(op == Operator.sub){
            answer = operands[i - 1] - operands[i];
        } else {
            answer = operands[i - 1] * operands[i];
        }

        return answer;
    }

    private String getOpChar(Operator op){
        String opChar;

        if(op == Operator.add){
            opChar = "+";
        } else if(op == Operator.sub){
            opChar = "-";
        } else {
            opChar = "x";
        }

        return opChar;
    }

    // \Model methods

    private void setQuesitions(){
        TextView q1 = findViewById(R.id.question1_text);
        TextView q2 = findViewById(R.id.question2_text);
        TextView q3 = findViewById(R.id.question3_text);

        q1.setText(String.format("%d %s %d",
                operands[0],
                getOpChar(operators[0]),
                operands[1]
        ));

        q2.setText(String.format("%d %s %d",
                operands[2],
                getOpChar(operators[1]),
                operands[3]
        ));

        q3.setText(String.format("%d %s %d",
                operands[4],
                getOpChar(operators[2]),
                operands[5]
        ));
    }


    private boolean checkAnswers(){
        boolean allCorrect = true;

        EditText a1 = findViewById(R.id.answer1);
        EditText a2 = findViewById(R.id.answer2);
        EditText a3 = findViewById(R.id.answer3);

        if (a1.getText().toString().isEmpty()){
            return false;
        } else if(a2.getText().toString().isEmpty()){
            return false;
        } else if(a3.getText().toString().isEmpty()){
            return false;
        }

        int a1Int = Integer.parseInt(a1.getText().toString());
        int a2Int = Integer.parseInt(a2.getText().toString());
        int a3Int = Integer.parseInt(a3.getText().toString());

        if(a1Int != answers[0]){
            allCorrect = false;
        }

        if(a2Int != answers[1]){
            allCorrect = false;
        }

        if(a3Int != answers[2]){
            allCorrect = false;
        }

        return allCorrect;
    }

    private void onSuccess(View v){
        sound.pause();
        sound.stop();
        sound.release();
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }

    private void onFailure(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Incorrect answer(s) try again!");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}