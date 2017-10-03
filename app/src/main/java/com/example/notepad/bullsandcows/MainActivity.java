package com.example.notepad.bullsandcows;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    String codedNumber = "";
    int enteredNumber = 0;
    int cntMoves = 1;
    boolean start = false;
    boolean mode;

    public static int[] enteredArray = new int[10];
    int chekRestart = DIG;

    int cnt0 = 0;
    int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0, cnt6 = 0, cnt7 = 0, cnt8 = 0, cnt9 = 0;

    TextView numberForScreen;
    Button number1;
    Button number2;
    Button number3;
    Button number4;
    Button number5;
    Button number6;
    Button number7;
    Button number8;
    Button number9;
    Button number0;
    Button enterButton;
    Button startButton;
    Button del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        numberForScreen = (TextView) findViewById(R.id.editText);
        number1 = (Button) findViewById(R.id.buttom1);
        number2 = (Button) findViewById(R.id.buttom2);
        number3 = (Button) findViewById(R.id.buttom3);
        number4 = (Button) findViewById(R.id.buttom4);
        number5 = (Button) findViewById(R.id.buttom5);
        number6 = (Button) findViewById(R.id.buttom6);
        number7 = (Button) findViewById(R.id.buttom7);
        number8 = (Button) findViewById(R.id.buttom8);
        number9 = (Button) findViewById(R.id.buttom9);
        number0 = (Button) findViewById(R.id.buttom0);
        enterButton = (Button) findViewById(R.id.enter);
        startButton = (Button) findViewById(R.id.start);
        del = (Button) findViewById(R.id.buttomDel);

        View.OnClickListener clickButton = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String buf;
                buf = numberForScreen.getText().toString();
                switch (view.getId()) {
                    case R.id.buttom1:
                        numberForScreen.setText(buf + 1);
                        break;
                    case R.id.buttom2:
                        numberForScreen.setText(buf + 2);
                        break;
                    case R.id.buttom3:
                        numberForScreen.setText(buf + 3);
                        break;
                    case R.id.buttom4:
                        numberForScreen.setText(buf + 4);
                        break;
                    case R.id.buttom5:
                        numberForScreen.setText(buf + 5);
                        break;
                    case R.id.buttom6:
                        numberForScreen.setText(buf + 6);
                        break;
                    case R.id.buttom7:
                        numberForScreen.setText(buf + 7);
                        break;
                    case R.id.buttom8:
                        numberForScreen.setText(buf + 8);
                        break;
                    case R.id.buttom9:
                        numberForScreen.setText(buf + 9);
                        break;
                    case R.id.buttom0:
                        numberForScreen.setText(buf + 0);
                        break;
                    case R.id.buttomDel:
                        if (buf.length() > 0) {
                            buf = buf.substring(0, buf.length() - 1);
                            numberForScreen.setText(buf);
                        }
                        break;
                    case R.id.enter:
                        if (start) {
                            if (chekNumberForCorrect()) {
                                getNumber();
                                numberForScreen.setText("");
                            } else {
                                Context context = getApplicationContext();
                                String message = "Enter " + DIG + "-digits number, without repeating digits!";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, message, duration);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                            }
                        }
                        break;
                    case R.id.start:
                        submitStart();
                        break;
                    default:
                        break;

                }
            }
        };
        number1.setOnClickListener(clickButton);
        number2.setOnClickListener(clickButton);
        number3.setOnClickListener(clickButton);
        number4.setOnClickListener(clickButton);
        number5.setOnClickListener(clickButton);
        number6.setOnClickListener(clickButton);
        number7.setOnClickListener(clickButton);
        number8.setOnClickListener(clickButton);
        number9.setOnClickListener(clickButton);
        number0.setOnClickListener(clickButton);
        enterButton.setOnClickListener(clickButton);
        startButton.setOnClickListener(clickButton);
        del.setOnClickListener(clickButton);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Rules");
        menu.add(0, 2, 1, "Setting");
        menu.add(0, 3, 2, "About app");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(this, Rulespage.class);
                startActivity(intent);
                break;
            case 2:
                Intent intent2 = new Intent(this, Setting.class);
                startActivityForResult(intent2, 1);
                break;
            case 3:
                Intent intent3 = new Intent(this, About.class);
                startActivity(intent3);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRestart() {
        super.onRestart();

    }

    public static void main(String[] argc) {

    }

    public boolean chekNumberForCorrect() {
        TextView edittext = (TextView) findViewById(R.id.editText);
        String number = edittext.getText().toString();
        if (number.length() == DIG) {
            for (int i = 0; i < DIG; ++i) {
                if (number.charAt(i) < '0' || number.charAt(i) > '9') {
                    return false;
                }
            }
            int numberForCheking = Integer.parseInt(number);
            int k = 1;
            for (int i = 0; i < DIG - 1; ++i) {
                k = k * 10;
            }
            if (numberForCheking >= k) {
                int[] numberArray;
                numberArray = new int[DIG];
                for (int i = DIG - 1; i >= 0; --i) {
                    numberArray[i] = numberForCheking % 10;
                    numberForCheking = numberForCheking / 10;
                }
                for (int i = 0, cnt = 0; i < DIG; ++i) {
                    for (int j = 0; j < DIG; ++j) {
                        if (numberArray[i] == numberArray[j]) {
                            ++cnt;
                        }
                        if (cnt > DIG) {
                            return false;
                        }
                    }
                }
                return true;
            } else if (number.charAt(0) == '0' && numberForCheking >= k / 10) {
                int[] numberArray;
                numberArray = new int[DIG];
                for (int i = DIG - 1; i >= 1; --i) {
                    numberArray[i] = numberForCheking % 10;
                    numberForCheking = numberForCheking / 10;
                }
                for (int i = 0, cnt = 0; i < DIG; ++i) {
                    for (int j = 0; j < DIG; ++j) {
                        if (numberArray[i] == numberArray[j]) {
                            ++cnt;
                        }
                        if (cnt > DIG) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
        return false;
    }

//    public void submitEnter(View view){
//        if (start) {
//            if (chekNumberForCorrect()) {
//                getNumber();
//            } else {
//                Context context = getApplicationContext();
//                String message = "Enter " + DIG + "-digits number, without repeating digits!";
//                int duration = Toast.LENGTH_LONG;
//                Toast toast = Toast.makeText(context, message, duration);
//                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                toast.show();
//            }
//        }
//    }

    public void getNumber() {
        TextView edittext = (TextView) findViewById(R.id.editText);
        String number = edittext.getText().toString();
        enteredNumber = Integer.parseInt(number);
        for (int i = DIG - 1; i >= 0; --i) {
            enteredArray[i] = enteredNumber % 10;
            enteredNumber = enteredNumber / 10;
        }
        shiftData();
        TextView test3 = (TextView) findViewById(R.id.number1);
        test3.setText(number);
        if (chekingBulls() == DIG) {
            TextView test5 = (TextView) findViewById(R.id.number1);
            test5.setText("Won!");
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

    public void shiftData() {
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

        if (cntMoves == 2) {
            ImageView image = (ImageView) findViewById(R.id.imageView1A);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewA);
            image2.setImageResource(R.drawable.cowgood);
        } else if (cntMoves == 3) {
            ImageView image = (ImageView) findViewById(R.id.imageView1B);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewB);
            image2.setImageResource(R.drawable.cowgood);
        } else if (cntMoves == 4) {
            ImageView image = (ImageView) findViewById(R.id.imageView1C);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewC);
            image2.setImageResource(R.drawable.cowgood);
        } else if (cntMoves == 5) {
            ImageView image = (ImageView) findViewById(R.id.imageView1D);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewD);
            image2.setImageResource(R.drawable.cowgood);
        } else if (cntMoves == 6) {
            ImageView image = (ImageView) findViewById(R.id.imageView1E);
            image.setImageResource(R.drawable.bullgood);
            ImageView image2 = (ImageView) findViewById(R.id.imageViewE);
            image2.setImageResource(R.drawable.cowgood);

        }

    }

    public int chekingBulls() {
        int bulls = 0;
        for (int i = 0; i < DIG; ++i) {
            if (randomNumber[i] == enteredArray[i]) {
                ++bulls;
            }
        }
        return bulls;
    }

    public int chekingCows() {
        int cows = 0;
        int i = 0;
        int j = 0;
        for (i = 0; i < DIG; ++i) {
            for (j = 0; j < DIG; ++j) {
                if (enteredArray[i] == randomNumber[j]) {
                    ++cows;
                }
            }
        }
        return cows;
    }

//    public void submitSetting(View view){
//
//        Intent intent2 = new Intent(this, Setting.class);
//        startActivityForResult(intent2, 1);
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String buf = data.getStringExtra("numberofdigits");
        mode = data.getBooleanExtra("modeState", mode);
        if (mode) {
            LinearLayout lyaoutmain = (LinearLayout) findViewById(R.id.mainLyaout);
            lyaoutmain.setBackgroundColor(Color.BLACK);
            TextView t1 = (TextView) findViewById(R.id.number3E);
            t1.setTextColor(Color.WHITE);
            TextView t2 = (TextView) findViewById(R.id.number2E);
            t2.setTextColor(Color.WHITE);
            TextView t3 = (TextView) findViewById(R.id.number1E);
            t3.setTextColor(Color.WHITE);
            TextView t4 = (TextView) findViewById(R.id.cntMove);
            t4.setTextColor(Color.WHITE);
            TextView t5 = (TextView) findViewById(R.id.number1);
            t5.setTextColor(Color.WHITE);
            TextView t6 = (TextView) findViewById(R.id.cntMoveA);
            t6.setTextColor(Color.WHITE);
            TextView t7 = (TextView) findViewById(R.id.cntMoveB);
            t7.setTextColor(Color.WHITE);
            TextView t8 = (TextView) findViewById(R.id.cntMoveC);
            t8.setTextColor(Color.WHITE);
            TextView t9 = (TextView) findViewById(R.id.cntMoveD);
            t9.setTextColor(Color.WHITE);
            TextView t10 = (TextView) findViewById(R.id.cntMoveE);
            t10.setTextColor(Color.WHITE);
            TextView t11 = (TextView) findViewById(R.id.number1A);
            t11.setTextColor(Color.WHITE);
            TextView t12 = (TextView) findViewById(R.id.number1B);
            t12.setTextColor(Color.WHITE);
            TextView t13 = (TextView) findViewById(R.id.number1C);
            t13.setTextColor(Color.WHITE);
            TextView t14 = (TextView) findViewById(R.id.number1D);
            t14.setTextColor(Color.WHITE);
            TextView t15 = (TextView) findViewById(R.id.number2);
            t15.setTextColor(Color.WHITE);
            TextView t16 = (TextView) findViewById(R.id.number2A);
            t16.setTextColor(Color.WHITE);
            TextView t17 = (TextView) findViewById(R.id.number2B);
            t17.setTextColor(Color.WHITE);
            TextView t18 = (TextView) findViewById(R.id.number2C);
            t18.setTextColor(Color.WHITE);
            TextView t19 = (TextView) findViewById(R.id.number2D);
            t19.setTextColor(Color.WHITE);
            TextView t20 = (TextView) findViewById(R.id.number3);
            t20.setTextColor(Color.WHITE);
            TextView t21 = (TextView) findViewById(R.id.number3A);
            t21.setTextColor(Color.WHITE);
            TextView t22 = (TextView) findViewById(R.id.number3B);
            t22.setTextColor(Color.WHITE);
            TextView t23 = (TextView) findViewById(R.id.number3D);
            t23.setTextColor(Color.WHITE);
            TextView t24 = (TextView) findViewById(R.id.number3C);
            t24.setTextColor(Color.WHITE);
            TextView t25 = (TextView) findViewById(R.id.editText);
            t25.setTextColor(Color.WHITE);
        } else {
            LinearLayout lyaoutmain = (LinearLayout) findViewById(R.id.mainLyaout);
            lyaoutmain.setBackgroundColor(Color.WHITE);
            TextView t1 = (TextView) findViewById(R.id.number3E);
            t1.setTextColor(Color.BLACK);
            TextView t2 = (TextView) findViewById(R.id.number2E);
            t2.setTextColor(Color.BLACK);
            TextView t3 = (TextView) findViewById(R.id.number1E);
            t3.setTextColor(Color.BLACK);
            TextView t4 = (TextView) findViewById(R.id.cntMove);
            t4.setTextColor(Color.BLACK);
            TextView t5 = (TextView) findViewById(R.id.number1);
            t5.setTextColor(Color.BLACK);
            TextView t6 = (TextView) findViewById(R.id.cntMoveA);
            t6.setTextColor(Color.BLACK);
            TextView t7 = (TextView) findViewById(R.id.cntMoveB);
            t7.setTextColor(Color.BLACK);
            TextView t8 = (TextView) findViewById(R.id.cntMoveC);
            t8.setTextColor(Color.BLACK);
            TextView t9 = (TextView) findViewById(R.id.cntMoveD);
            t9.setTextColor(Color.BLACK);
            TextView t10 = (TextView) findViewById(R.id.cntMoveE);
            t10.setTextColor(Color.BLACK);
            TextView t11 = (TextView) findViewById(R.id.number1A);
            t11.setTextColor(Color.BLACK);
            TextView t12 = (TextView) findViewById(R.id.number1B);
            t12.setTextColor(Color.BLACK);
            TextView t13 = (TextView) findViewById(R.id.number1C);
            t13.setTextColor(Color.BLACK);
            TextView t14 = (TextView) findViewById(R.id.number1D);
            t14.setTextColor(Color.BLACK);
            TextView t15 = (TextView) findViewById(R.id.number2);
            t15.setTextColor(Color.BLACK);
            TextView t16 = (TextView) findViewById(R.id.number2A);
            t16.setTextColor(Color.BLACK);
            TextView t17 = (TextView) findViewById(R.id.number2B);
            t17.setTextColor(Color.BLACK);
            TextView t18 = (TextView) findViewById(R.id.number2C);
            t18.setTextColor(Color.BLACK);
            TextView t19 = (TextView) findViewById(R.id.number2D);
            t19.setTextColor(Color.BLACK);
            TextView t20 = (TextView) findViewById(R.id.number3);
            t20.setTextColor(Color.BLACK);
            TextView t21 = (TextView) findViewById(R.id.number3A);
            t21.setTextColor(Color.BLACK);
            TextView t22 = (TextView) findViewById(R.id.number3B);
            t22.setTextColor(Color.BLACK);
            TextView t23 = (TextView) findViewById(R.id.number3D);
            t23.setTextColor(Color.BLACK);
            TextView t24 = (TextView) findViewById(R.id.number3C);
            t24.setTextColor(Color.BLACK);
            TextView t25 = (TextView) findViewById(R.id.editText);
            t25.setTextColor(Color.BLACK);
        }
        DIG = Integer.parseInt(buf);
        //submitStart();
        if (chekRestart != DIG && start) {
            chekRestart = DIG;
            TextView start7 = (TextView) findViewById(R.id.start);
            start7.setText("Start game");
            TextView edittext8 = (TextView) findViewById(R.id.editText);
            edittext8.setText("" + codedNumber);

            codedNumber = "";
            start = false;
        }
    }

//    public void  submitRules(View view){
//        Intent intent = new Intent(this, Rulespage.class);
//        startActivity(intent);
//    }

//    public void submitStart(View view){
//        submitStart();
//
//    }

    public void submitStart() {
        if (!start) {
            crateRandomNumber();
            TextView start1 = (TextView) findViewById(R.id.start);
            start1.setText("Show number");

            start = true;
            enteredNumber = 0;
            cntMoves = 1;
            cnt0 = 3;
            cnt1 = 3;
            cnt2 = 3;
            cnt3 = 3;
            cnt4 = 3;
            cnt5 = 3;
            cnt6 = 3;
            cnt7 = 3;
            cnt8 = 3;
            cnt9 = 3;
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

            TextView edittext = (TextView) findViewById(R.id.editText);
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
            for (int i = 0; i < 5; ++i) {
                shiftData();
            }

        } else {
            TextView start2 = (TextView) findViewById(R.id.start);
            start2.setText("Start game");

            TextView edittext = (TextView) findViewById(R.id.editText);
            edittext.setText("" + codedNumber);

            codedNumber = "";
            start = false;
        }

    }

    public void crateRandomNumber() {
        for (int i = 0, j = 0; i < DIG; ++i) {
            Random r = new Random();
            int k = r.nextInt(9);
            if (i == 0) {
                randomNumber[i] = k;
            } else {
                for (j = 0; j < i; ++j) {
                    if (randomNumber[j] == k) {
                        --i;
                        break;
                    } else {
                        randomNumber[i] = k;
                    }
                }
            }

        }
        for (int i = 0; i < DIG; ++i) {
            codedNumber = codedNumber + randomNumber[i];
        }

    }

    public void submit0(View view) {
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

    public void submit1(View view) {
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

    public void submit2(View view) {
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

    public void submit3(View view) {
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

    public void submit4(View view) {
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

    public void submit5(View view) {
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

    public void submit6(View view) {
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

    public void submit7(View view) {
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

    public void submit8(View view) {
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

    public void submit9(View view) {
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
