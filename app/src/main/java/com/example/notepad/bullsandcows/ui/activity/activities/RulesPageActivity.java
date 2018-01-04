package com.example.notepad.bullsandcows.ui.activity.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;

public class RulesPageActivity extends AppCompatActivity {
    int cnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_rulespage);
    }

    public void submitBull(View view){
        TextView bullText = (TextView) findViewById(R.id.helpFromBull);
        if (cnt == 0){
            bullText.setText("This is simple logical game");
            ++cnt;
        }
        else  if (cnt == 1){
            bullText.setText("You mast be find number what codided your nice phone");
            ++cnt;
        }
        else if (cnt == 2){
            bullText.setText("If number stay in same position it is bull");
            ++cnt;
        }
        else if (cnt == 3){
            bullText.setText("If number stay in other position it is cow");
            ++cnt;
        }
        else if (cnt == 4){
            bullText.setText("Good luck!");
            cnt = 0;
        }


    }
}
