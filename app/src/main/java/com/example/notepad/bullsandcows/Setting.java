package com.example.notepad.bullsandcows;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class Setting extends AppCompatActivity {

    public static int numberOfDigits = 4;
    Button modButton;
    static boolean mode;
    //comm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_setting);
        modButton = (Button) findViewById(R.id.modeButton);
        Intent intent = getIntent();
        mode = intent.getBooleanExtra("modeState", mode);
                if (!mode){
            modButton.setText("Night mode");
        } else {
            modButton.setText("Daily mode");
        }
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("" + numberOfDigits);




        modButton.setOnClickListener(clickButton);
    }

    View.OnClickListener clickButton = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.modeButton:
                    if (!mode) {
                        modButton.setText("Daily mode");
                        mode = true;
                    } else {
                        modButton.setText("Night mode");
                        mode = false;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void submitMinus(View view) {
        numberOfDigits -= 1;
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("" + numberOfDigits);
    }

    public void submitPlus(View view) {
        numberOfDigits += 1;
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("" + numberOfDigits);
    }

    public void submitApply(View view) {
        TextView text = (TextView) findViewById(R.id.text);
        String number = text.getText().toString();
        Intent intent3 = new Intent();
        intent3.putExtra("numberofdigits", number);
        intent3.putExtra("modeState", mode);
        setResult(RESULT_OK, intent3);
        finish();
    }

}
