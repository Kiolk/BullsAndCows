package com.example.notepad.bullsandcows;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    public static int DIG = 4;
    public static int[] randomNumber = new int[10];
    String codedNumber ="";
    int enteredNumber = 0;
    int cntMoves = 1;
    boolean start = false;


    public static int[] enteredArray = new int[10];
    int chekRestart = DIG;

    int cnt0 = 0;
    int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0, cnt6 = 0, cnt7 = 0, cnt8 = 0, cnt9 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
    }

   public void onRestart(){
       super.onRestart();

   }

    public static void main(String[] argc){

    }

    public boolean chekNumberForCorrect(){
        EditText edittext = (EditText) findViewById(R.id.editText);
        String number = edittext.getText().toString();
        if(number.length() == DIG){
            for(int i = 0; i < DIG; ++i){
                if(number.charAt(i) < '0' || number.charAt(i) > '9')
                    return false;
            }
            int numberForCheking = Integer.parseInt(number);
            int k = 1;
            for (int i = 0; i < DIG - 1; ++i){
                k = k * 10;
            }
            if(numberForCheking >= k){
                int [] numberArray;
                numberArray = new int [DIG];
                for(int i = DIG - 1; i >= 0; --i){
                    numberArray[i] = numberForCheking % 10;
                    numberForCheking = numberForCheking / 10;
                }
                for(int i = 0, cnt = 0; i < DIG; ++i){
                    for(int j = 0; j < DIG; ++j){
                        if(numberArray[i] == numberArray[j])
                            ++cnt;
                        if(cnt > DIG)
                            return false;
                    }
                }
                return true;
            }
            else if(number.charAt(0) == '0' && numberForCheking >= k / 10){
                int [] numberArray;
                numberArray = new int [DIG];
                for(int i = DIG - 1; i >= 1; --i){
                    numberArray[i] = numberForCheking % 10;
                    numberForCheking = numberForCheking / 10;
                }
                for(int i = 0, cnt = 0; i < DIG; ++i){
                    for(int j = 0; j < DIG; ++j){
                        if(numberArray[i] == numberArray[j])
                            ++cnt;
                        if(cnt > DIG)
                            return false;
                    }
                }

                return true;
            }
        }
        return false;
    }

    public void submitEnter(View view){
        if (start) {
            if (chekNumberForCorrect()) {
                getNumber();
            } else {
                Context context = getApplicationContext();
                String message = "Enter " + DIG + "-digits number, without repeating digits!";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, message, duration);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
    }

    public void getNumber(){
        EditText edittext = (EditText) findViewById(R.id.editText);
        String number = edittext.getText().toString();
        enteredNumber = Integer.parseInt(number);
        for(int i = DIG - 1; i >= 0; --i){
            enteredArray[i] = enteredNumber % 10;
            enteredNumber = enteredNumber / 10;
        }
        shiftData();
        TextView test3 = (TextView) findViewById(R.id.number1);
        test3.setText(number);
        if(chekingBulls() == DIG) {
            TextView test5 = (TextView) findViewById(R.id.number1);
            test5.setText("Won!" );
        }
        TextView test1 = (TextView) findViewById(R.id.number2);
        test1.setText("" + chekingBulls());
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        image.setImageResource(R.drawable.bullgood);
        TextView test2 = (TextView) findViewById(R.id.number3);
        ImageView image2 = (ImageView) findViewById(R.id.imageView);
        image2.setImageResource(R.drawable.cowgood);
        test2.setText("" + (chekingCows() - chekingBulls()));
        TextView test4 = (TextView) findViewById(R.id.cntMove);
        test4.setText("" + cntMoves);
        ++cntMoves;
    }

    public void shiftData(){
        TextView copyE1 = (TextView) findViewById(R.id.cntMoveE);
        TextView fromD1 = (TextView) findViewById(R.id.cntMoveD);
        copyE1.setText("" + fromD1.getText());
        TextView fromC1 = (TextView) findViewById(R.id.cntMoveC);
        fromD1.setText("" + fromC1.getText());
        TextView fromB1 = (TextView) findViewById(R.id.cntMoveB);
        fromC1.setText("" + fromB1.getText());
        TextView fromA1 = (TextView) findViewById(R.id.cntMoveA);
        fromB1.setText("" + fromA1.getText());
        TextView from = (TextView) findViewById(R.id.cntMove);
        fromA1.setText("" + from.getText());


        TextView copyE2 = (TextView) findViewById(R.id.number1E);
        TextView fromD2 = (TextView) findViewById(R.id.number1D);
        copyE2.setText("" + fromD2.getText());
        TextView fromC2 = (TextView) findViewById(R.id.number1C);
        fromD2.setText("" + fromC2.getText());
        TextView fromB2 = (TextView) findViewById(R.id.number1B);
        fromC2.setText("" + fromB2.getText());
        TextView fromA2 = (TextView) findViewById(R.id.number1A);
        fromB2.setText("" + fromA2.getText());
        TextView from2 = (TextView) findViewById(R.id.number1);
        fromA2.setText("" + from2.getText());

        TextView copyE3 = (TextView) findViewById(R.id.number2E);
        TextView fromD3 = (TextView) findViewById(R.id.number2D);
        copyE3.setText("" + fromD3.getText());
        TextView fromC3 = (TextView) findViewById(R.id.number2C);
        fromD3.setText("" + fromC3.getText());
        TextView fromB3 = (TextView) findViewById(R.id.number2B);
        fromC3.setText("" + fromB3.getText());
        TextView fromA3 = (TextView) findViewById(R.id.number2A);
        fromB3.setText("" + fromA3.getText());
        TextView from3 = (TextView) findViewById(R.id.number2);
        fromA3.setText("" + from3.getText());

        TextView copyE4 = (TextView) findViewById(R.id.number3E);
        TextView fromD4 = (TextView) findViewById(R.id.number3D);
        copyE4.setText("" + fromD4.getText());
        TextView fromC4 = (TextView) findViewById(R.id.number3C);
        fromD4.setText("" + fromC4.getText());
        TextView fromB4 = (TextView) findViewById(R.id.number3B);
        fromC4.setText("" + fromB4.getText());
        TextView fromA4 = (TextView) findViewById(R.id.number3A);
        fromB4.setText("" + fromA4.getText());
        TextView from4 = (TextView) findViewById(R.id.number3);
        fromA4.setText("" + from4.getText());



        if( cntMoves == 2){
            ImageView image = (ImageView) findViewById(R.id.imageView1A);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewA);
            image2.setImageResource(R.drawable.cowgood);
        }
        else if( cntMoves == 3){
            ImageView image = (ImageView) findViewById(R.id.imageView1B);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewB);
            image2.setImageResource(R.drawable.cowgood);
        }
        else if( cntMoves == 4){
            ImageView image = (ImageView) findViewById(R.id.imageView1C);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewC);
            image2.setImageResource(R.drawable.cowgood);
        }
        else if( cntMoves == 5){
            ImageView image = (ImageView) findViewById(R.id.imageView1D);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewD);
            image2.setImageResource(R.drawable.cowgood);
        }

        else if( cntMoves == 6){
            ImageView image = (ImageView) findViewById(R.id.imageView1E);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewE);
            image2.setImageResource(R.drawable.cowgood);


        }

    }

   public int chekingBulls(){
        int bulls = 0;
        for(int i = 0; i < DIG; ++i){
            if (randomNumber[i] == enteredArray[i])
                ++bulls;
        }
        return bulls;
    }

    public int chekingCows(){
        int cows = 0;
        int i = 0;
        int j = 0;
        for(i = 0; i < DIG; ++i) {
            for (j = 0; j < DIG; ++j) {
            if (enteredArray[i] == randomNumber[j])
                ++cows;
            }
        }
        return cows;
    }

    public void submitSetting(View view){

        Intent intent2 = new Intent(this, Setting.class);
        startActivityForResult(intent2, 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String buf = data.getStringExtra("numberofdigits");
        DIG = Integer.parseInt(buf);
        //submitStart();
        if(chekRestart != DIG && start){
            chekRestart = DIG;
            TextView start7 = (TextView) findViewById(R.id.start);
            start7.setText("Start game");
            EditText edittext8 = (EditText) findViewById(R.id.editText);
            edittext8.setText("" + codedNumber);

            codedNumber ="";
            start = false;
        }
    }

    public void  submitRules(View view){
        Intent intent = new Intent(this, Rulespage.class);
        startActivity(intent);
    }

    public void submitStart(View view){
        submitStart();

    }

    public void submitStart(){
        if (!start) {
            crateRandomNumber();
            TextView start1 = (TextView) findViewById(R.id.start);
            start1.setText("Show number");

            start = true;
            enteredNumber = 0;
            cntMoves = 1;
            cnt0 = 3;
            cnt1 = 3; cnt2 = 3; cnt3 = 3; cnt4 = 3; cnt5 = 3; cnt6 = 3; cnt7 = 3; cnt8 = 3; cnt9 = 3;
            submit0();
            submit1();
            submit2();
            submit3();
            submit4();
            submit5();
            submit6();
            submit7();
            submit8();
            submit9();

            Context context = getApplicationContext();
            String message = "Enter " + DIG + "-digits number, without repeating digits!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

            ImageView image = (ImageView) findViewById(R.id.imageView1D);
            image.setImageResource(R.drawable.zero);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewD);
            image2.setImageResource(R.drawable.zero);
            ImageView image3 = (ImageView) findViewById(R.id.imageView1C);
            image3.setImageResource(R.drawable.zero);
            ImageView image4 = (ImageView) findViewById(R.id.imageViewC);
            image4.setImageResource(R.drawable.zero);
            ImageView image5 = (ImageView) findViewById(R.id.imageView1B);
            image5.setImageResource(R.drawable.zero);
            ImageView image6 = (ImageView) findViewById(R.id.imageViewB);
            image6.setImageResource(R.drawable.zero);
            ImageView image7 = (ImageView) findViewById(R.id.imageView1A);
            image7.setImageResource(R.drawable.zero);
            ImageView image8 = (ImageView) findViewById(R.id.imageViewA);
            image8.setImageResource(R.drawable.zero);
            ImageView image9 = (ImageView) findViewById(R.id.imageView1);
            image9.setImageResource(R.drawable.zero);
            ImageView image10 = (ImageView) findViewById(R.id.imageView);
            image10.setImageResource(R.drawable.zero);
            ImageView image11 = (ImageView) findViewById(R.id.imageView1E);
            image11.setImageResource(R.drawable.zero);
            ImageView image12 = (ImageView) findViewById(R.id.imageViewE);
            image12.setImageResource(R.drawable.zero);


            EditText edittext = (EditText) findViewById(R.id.editText);
            edittext.setText("");
            TextView test3 = (TextView) findViewById(R.id.number1);
            test3.setText("");
            TextView test1 = (TextView) findViewById(R.id.number2);
            test1.setText("");
            TextView test2 = (TextView) findViewById(R.id.number3);
            test2.setText("");
            TextView test4 = (TextView) findViewById(R.id.cntMove);
            test4.setText("");
            enteredNumber = 0;
            cntMoves = 1;
            for(int i = 0; i < 4; ++i)
            shiftData();

        }
        else{
            TextView start2 = (TextView) findViewById(R.id.start);
            start2.setText("Start game");

            EditText edittext = (EditText) findViewById(R.id.editText);
            edittext.setText("" + codedNumber);

            codedNumber ="";
            start = false;
        }

    }

    public void crateRandomNumber(){
        for(int i = 0, j = 0; i < DIG; ++i){
            Random r = new Random();
            int k = r.nextInt(9);
            if (i == 0)
                randomNumber[i] = k;
            else {
                for (j = 0; j < i; ++j) {
                    if (randomNumber[j] == k) {
                        --i;
                        break;
                    }
                    else randomNumber[i] = k;
                }
            }

        }
        for(int i = 0; i < DIG; ++i){
            codedNumber = codedNumber + randomNumber[i];
        }

    }

    public void submit0(View view){
        submit0();
    }

    public void submit0() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help0);
            if (cnt0 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt0 = 1;
            } else if (cnt0 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt0 = 2;
            } else if (cnt0 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt0 = 3;
            } else if (cnt0 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt0 = 0;
            }
        }
    }

    public void submit1(View view){
        submit1();
    }

    public void submit1() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help1);
            if (cnt1 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt1 = 1;
            } else if (cnt1 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt1 = 2;
            } else if (cnt1 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt1 = 3;
            } else if (cnt1 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt1 = 0;
            }
        }
    }

    public void submit2(View view){
        submit2();
    }

    public void submit2() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help2);
            if (cnt2 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt2 = 1;
            } else if (cnt2 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt2 = 2;
            } else if (cnt2 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt2 = 3;
            } else if (cnt2 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt2 = 0;
            }
        }
    }

    public void submit3(View view){
        submit3();
    }

    public void submit3() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help3);
            if (cnt3 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt3 = 1;
            } else if (cnt3 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt3 = 2;
            } else if (cnt3 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt3 = 3;
            } else if (cnt3 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt3 = 0;
            }
        }
    }

    public void submit4(View view){
        submit4();
    }

    public void submit4() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help4);
            if (cnt4 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt4 = 1;
            } else if (cnt4 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt4 = 2;
            } else if (cnt4 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt4 = 3;
            } else if (cnt4 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt4 = 0;
            }
        }
    }

    public void submit5(View view){
        submit5();
    }

    public void submit5() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help5);
            if (cnt5 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt5 = 1;
            } else if (cnt5 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt5 = 2;
            } else if (cnt5 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt5 = 3;
            } else if (cnt5 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt5 = 0;
            }
        }
    }

    public void submit6(View view){
        submit6();
    }

    public void submit6() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help6);
            if (cnt6 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt6 = 1;
            } else if (cnt6 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt6 = 2;
            } else if (cnt6 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt6 = 3;
            } else if (cnt6 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt6 = 0;
            }
        }
    }

    public void submit7(View view){
        submit7();
    }

    public void submit7() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help7);
            if (cnt7 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt7 = 1;
            } else if (cnt7 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt7 = 2;
            } else if (cnt7 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt7 = 3;
            } else if (cnt7 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt7 = 0;
            }
        }
    }

    public void submit8(View view){
        submit8();
    }

    public void submit8() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help8);
            if (cnt8 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt8 = 1;
            } else if (cnt8 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt8 = 2;
            } else if (cnt8 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt8 = 3;
            } else if (cnt8 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt8 = 0;
            }
        }
    }

    public void submit9(View view){
        submit9();
    }

    public void submit9() {
        if (start) {
            TextView help0 = (TextView) findViewById(R.id.help9);
            if (cnt9 == 0) {
                help0.setBackgroundColor(0xFFFFFF00);
                cnt9 = 1;
            } else if (cnt9 == 1) {
                help0.setBackgroundColor(0xFF76FF03);
                cnt9 = 2;
            } else if (cnt9 == 2) {
                help0.setBackgroundColor(0xFFFF3D00);
                cnt9 = 3;
            } else if (cnt9 == 3) {
                help0.setBackgroundColor(0xFFEEEEEE);
                cnt9 = 0;
            }
        }
    }

}
