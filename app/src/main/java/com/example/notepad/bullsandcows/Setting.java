package com.example.notepad.bullsandcows;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Setting extends AppCompatActivity {
    public static int numberOfDigits = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_setting);
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("" + numberOfDigits);

        Intent intent3 = new Intent();
        intent3.putExtra("numberofdigits", numberOfDigits);
        setResult(RESULT_OK, intent3);
    }


    public void submitMinus(View view){
        numberOfDigits -= 1;
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("" + numberOfDigits);
     }

     public void submitPlus(View view){
         numberOfDigits += 1;
         TextView text = (TextView) findViewById(R.id.text);
         text.setText("" + numberOfDigits);
     }

     public void submitApply(View view){
         TextView text = (TextView) findViewById(R.id.text);
         String number = text.getText().toString();
         Intent intent3 = new Intent();
         intent3.putExtra("numberofdigits", number);
         setResult(RESULT_OK, intent3);
         finish();
     }
}
