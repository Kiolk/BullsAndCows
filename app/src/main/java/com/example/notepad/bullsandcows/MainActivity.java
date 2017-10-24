package com.example.notepad.bullsandcows;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.fragments.WinFragment;
import com.example.notepad.bullsandcows.services.WinSoundService;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String SAVED_TEXT = "saved-text";
    ArrayList<String> mMoves = new ArrayList<>();
    ArrayList<String> mNumbers = new ArrayList<>();
    ArrayList<String> mCows = new ArrayList<>();
    ArrayList<String> mBuls = new ArrayList<>();

    public static int DIG = 4;
    public static int[] randomNumber = new int[10];
    String codedNumber = "";
    int enteredNumber = 0;
    int cntMoves = 1;
    boolean start = false;
    boolean mode;
    SharedPreferences mSaveNikName;

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
    TextView mTimer;
    TextView mNikOfUser;
    private long mTimerCount = 0;
    Timer mTimerTimer;
    WriteReadFile mWriteReadFile = new WriteReadFile();
    Boolean mCurrentVersionOfApp = true;
    WinFragment mWinFragment;
    FragmentTransaction mTransaction;
    FrameLayout mFrameLayout;

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
        mTimer = (TextView) findViewById(R.id.timer_text_view);
        mNikOfUser = (TextView) findViewById(R.id.user_name_text_view);
        mWinFragment = new WinFragment();
        mFrameLayout = (FrameLayout) findViewById(R.id.win_container);
        loadNikName();

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
                            if (checkNumberForCorrect()) {
                                getNumber();
                                numberForScreen.setText("");
                                checkNumberForWin();

                            } else {
                                Context context = getApplicationContext();
                                String message = "Enter " + DIG + "-digits number, without repeating digits!";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, message, duration);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                            }
                            createListViewWithMoves();
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
        startWelcomePage();
//        getDrawerToggleDelegate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveNikName();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Rules");
        menu.add(0, 2, 1, "Setting");
        menu.add(0, 3, 2, "About app");
        menu.add(0, 4, 3, "Records");
        menu.add(0, 5, 4, "Online records");
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
                intent2.putExtra("modeState", mode);
                intent2.putExtra("nikOfUser", mNikOfUser.getText());
                startActivityForResult(intent2, 1);
                break;
            case 3:
                Intent intent3 = new Intent(this, About.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, Records.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(this, OnlineRecords.class);
                startActivity(intent5);
                break;
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

    public boolean checkNumberForCorrect() {
        TextView editText = (TextView) findViewById(R.id.editText);
        String number = editText.getText().toString();
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

    public void getNumber() {
        TextView editText = (TextView) findViewById(R.id.editText);
        String number = editText.getText().toString();
        mNumbers.add(number);
        mMoves.add("" + cntMoves);
        enteredNumber = Integer.parseInt(number);
        for (int i = DIG - 1; i >= 0; --i) {
            enteredArray[i] = enteredNumber % 10;
            enteredNumber = enteredNumber / 10;
        }
        //shiftData();
        TextView test3 = (TextView) findViewById(R.id.number1);
        test3.setText(number);
        mBuls.add("" + chekingBulls());
        //checkNumberForWin();
        TextView test1 = (TextView) findViewById(R.id.number2);
        test1.setText("" + chekingBulls());
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        image.setImageResource(R.drawable.bullgood);
        TextView test2 = (TextView) findViewById(R.id.number3);
        ImageView image2 = (ImageView) findViewById(R.id.imageView);
        image2.setImageResource(R.drawable.cowgood);
        test2.setText("" + (chekingCows() - chekingBulls()));
        mCows.add("" + (chekingCows() - chekingBulls()));
        TextView test4 = (TextView) findViewById(R.id.cntMove);
        test4.setText("" + cntMoves);
        ++cntMoves;
    }

    public void checkNumberForWin() {
        if (chekingBulls() == DIG) {
            TextView test5 = (TextView) findViewById(R.id.number1);
            test5.setText("Won!");
            TextView wonText = (TextView) findViewById(R.id.editText);
            wonText.setText("WON!");
            mTimerTimer.cancel();
            String numberOfMoves = "" + (cntMoves - 1);
            String numberOfCodedDigits = "" + DIG;
            RecordsToNet note = new RecordsToNet();
            note.setDate(System.currentTimeMillis());
            note.setTime(mTimer.getText().toString());
            note.setNikName(mNikOfUser.getText().toString());
            note.setMoves(numberOfMoves);
            note.setCodes(numberOfCodedDigits);
            new RecordAsyncTaskPost().execute(note);
            mWriteReadFile.writeInFile(numberOfMoves, mNikOfUser.getText().toString(), mTimer.getText().toString(), numberOfCodedDigits, this);
            mWriteReadFile.readFile(this);
            showWinFragment();
            setWinText();
        }
    }

//    public void shiftData() {
//        TextView copyE1 = (TextView) findViewById(R.id.cntMoveE);
//        TextView fromD1 = (TextView) findViewById(R.id.cntMoveD);
//        copyE1.setText("" + fromD1.getText());
//        TextView fromC1 = (TextView) findViewById(R.id.cntMoveC);
//        fromD1.setText("" + fromC1.getText());
//        TextView fromB1 = (TextView) findViewById(R.id.cntMoveB);
//        fromC1.setText("" + fromB1.getText());
//        TextView fromA1 = (TextView) findViewById(R.id.cntMoveA);
//        fromB1.setText("" + fromA1.getText());
//        TextView from = (TextView) findViewById(R.id.cntMove);
//        fromA1.setText("" + from.getText());
//
//        TextView copyE2 = (TextView) findViewById(R.id.number1E);
//        TextView fromD2 = (TextView) findViewById(R.id.number1D);
//        copyE2.setText("" + fromD2.getText());
//        TextView fromC2 = (TextView) findViewById(R.id.number1C);
//        fromD2.setText("" + fromC2.getText());
//        TextView fromB2 = (TextView) findViewById(R.id.number1B);
//        fromC2.setText("" + fromB2.getText());
//        TextView fromA2 = (TextView) findViewById(R.id.number1A);
//        fromB2.setText("" + fromA2.getText());
//        TextView from2 = (TextView) findViewById(R.id.number1);
//        fromA2.setText("" + from2.getText());
//
//        TextView copyE3 = (TextView) findViewById(R.id.number2E);
//        TextView fromD3 = (TextView) findViewById(R.id.number2D);
//        copyE3.setText("" + fromD3.getText());
//        TextView fromC3 = (TextView) findViewById(R.id.number2C);
//        fromD3.setText("" + fromC3.getText());
//        TextView fromB3 = (TextView) findViewById(R.id.number2B);
//        fromC3.setText("" + fromB3.getText());
//        TextView fromA3 = (TextView) findViewById(R.id.number2A);
//        fromB3.setText("" + fromA3.getText());
//        TextView from3 = (TextView) findViewById(R.id.number2);
//        fromA3.setText("" + from3.getText());
//
//        TextView copyE4 = (TextView) findViewById(R.id.number3E);
//        TextView fromD4 = (TextView) findViewById(R.id.number3D);
//        copyE4.setText("" + fromD4.getText());
//        TextView fromC4 = (TextView) findViewById(R.id.number3C);
//        fromD4.setText("" + fromC4.getText());
//        TextView fromB4 = (TextView) findViewById(R.id.number3B);
//        fromC4.setText("" + fromB4.getText());
//        TextView fromA4 = (TextView) findViewById(R.id.number3A);
//        fromB4.setText("" + fromA4.getText());
//        TextView from4 = (TextView) findViewById(R.id.number3);
//        fromA4.setText("" + from4.getText());
//
//        if (cntMoves == 2) {
//            ImageView image = (ImageView) findViewById(R.id.imageView1A);
//            image.setImageResource(R.drawable.bullgood);
//            ImageView image2 = (ImageView) findViewById(R.id.imageViewA);
//            image2.setImageResource(R.drawable.cowgood);
//        } else if (cntMoves == 3) {
//            ImageView image = (ImageView) findViewById(R.id.imageView1B);
//            image.setImageResource(R.drawable.bullgood);
//            ImageView image2 = (ImageView) findViewById(R.id.imageViewB);
//            image2.setImageResource(R.drawable.cowgood);
//        } else if (cntMoves == 4) {
//            ImageView image = (ImageView) findViewById(R.id.imageView1C);
//            image.setImageResource(R.drawable.bullgood);
//            ImageView image2 = (ImageView) findViewById(R.id.imageViewC);
//            image2.setImageResource(R.drawable.cowgood);
//        } else if (cntMoves == 5) {
//            ImageView image = (ImageView) findViewById(R.id.imageView1D);
//            image.setImageResource(R.drawable.bullgood);
//            ImageView image2 = (ImageView) findViewById(R.id.imageViewD);
//            image2.setImageResource(R.drawable.cowgood);
//        } else if (cntMoves == 6) {
//            ImageView image = (ImageView) findViewById(R.id.imageView1E);
//            image.setImageResource(R.drawable.bullgood);
//            ImageView image2 = (ImageView) findViewById(R.id.imageViewE);
//            image2.setImageResource(R.drawable.cowgood);
//
//        }
//
//    }

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String buf = data.getStringExtra("numberofdigits");
                    mode = data.getBooleanExtra("modeState", mode);
                    mNikOfUser.setText(data.getStringExtra("nikOfUser"));
                    createListViewWithMoves();
                    if (mode) {
                        LinearLayout lyaoutmain = (LinearLayout) findViewById(R.id.mainLyaout);
                        lyaoutmain.setBackgroundColor(Color.BLACK);
//                TextView t1 = (TextView) findViewById(R.id.number3E);
//                t1.setTextColor(Color.WHITE);
//                TextView t2 = (TextView) findViewById(R.id.number2E);
//                t2.setTextColor(Color.WHITE);
//                TextView t3 = (TextView) findViewById(R.id.number1E);
//                t3.setTextColor(Color.WHITE);
//                TextView t4 = (TextView) findViewById(R.id.cntMove);
//                t4.setTextColor(Color.WHITE);
//                TextView t5 = (TextView) findViewById(R.id.number1);
//                t5.setTextColor(Color.WHITE);
//                TextView t6 = (TextView) findViewById(R.id.cntMoveA);
//                t6.setTextColor(Color.WHITE);
//                TextView t7 = (TextView) findViewById(R.id.cntMoveB);
//                t7.setTextColor(Color.WHITE);
//                TextView t8 = (TextView) findViewById(R.id.cntMoveC);
//                t8.setTextColor(Color.WHITE);
//                TextView t9 = (TextView) findViewById(R.id.cntMoveD);
//                t9.setTextColor(Color.WHITE);
//                TextView t10 = (TextView) findViewById(R.id.cntMoveE);
//                t10.setTextColor(Color.WHITE);
//                TextView t11 = (TextView) findViewById(R.id.number1A);
//                t11.setTextColor(Color.WHITE);
//                TextView t12 = (TextView) findViewById(R.id.number1B);
//                t12.setTextColor(Color.WHITE);
//                TextView t13 = (TextView) findViewById(R.id.number1C);
//                t13.setTextColor(Color.WHITE);
//                TextView t14 = (TextView) findViewById(R.id.number1D);
//                t14.setTextColor(Color.WHITE);
//                TextView t15 = (TextView) findViewById(R.id.number2);
//                t15.setTextColor(Color.WHITE);
//                TextView t16 = (TextView) findViewById(R.id.number2A);
//                t16.setTextColor(Color.WHITE);
//                TextView t17 = (TextView) findViewById(R.id.number2B);
//                t17.setTextColor(Color.WHITE);
//                TextView t18 = (TextView) findViewById(R.id.number2C);
//                t18.setTextColor(Color.WHITE);
//                TextView t19 = (TextView) findViewById(R.id.number2D);
//                t19.setTextColor(Color.WHITE);
//                TextView t20 = (TextView) findViewById(R.id.number3);
//                t20.setTextColor(Color.WHITE);
//                TextView t21 = (TextView) findViewById(R.id.number3A);
//                t21.setTextColor(Color.WHITE);
//                TextView t22 = (TextView) findViewById(R.id.number3B);
//                t22.setTextColor(Color.WHITE);
//                TextView t23 = (TextView) findViewById(R.id.number3D);
//                t23.setTextColor(Color.WHITE);
//                TextView t24 = (TextView) findViewById(R.id.number3C);
//                t24.setTextColor(Color.WHITE);
                        TextView t25 = (TextView) findViewById(R.id.editText);
                        t25.setTextColor(Color.WHITE);

                    } else {

                        LinearLayout lyaoutmain = (LinearLayout) findViewById(R.id.mainLyaout);
                        lyaoutmain.setBackgroundColor(Color.WHITE);
//                TextView t1 = (TextView) findViewById(R.id.number3E);
//                t1.setTextColor(Color.BLACK);
//                TextView t2 = (TextView) findViewById(R.id.number2E);
//                t2.setTextColor(Color.BLACK);
//                TextView t3 = (TextView) findViewById(R.id.number1E);
//                t3.setTextColor(Color.BLACK);
//                TextView t4 = (TextView) findViewById(R.id.cntMove);
//                t4.setTextColor(Color.BLACK);
//                TextView t5 = (TextView) findViewById(R.id.number1);
//                t5.setTextColor(Color.BLACK);
//                TextView t6 = (TextView) findViewById(R.id.cntMoveA);
//                t6.setTextColor(Color.BLACK);
//                TextView t7 = (TextView) findViewById(R.id.cntMoveB);
//                t7.setTextColor(Color.BLACK);
//                TextView t8 = (TextView) findViewById(R.id.cntMoveC);
//                t8.setTextColor(Color.BLACK);
//                TextView t9 = (TextView) findViewById(R.id.cntMoveD);
//                t9.setTextColor(Color.BLACK);
//                TextView t10 = (TextView) findViewById(R.id.cntMoveE);
//                t10.setTextColor(Color.BLACK);
//                TextView t11 = (TextView) findViewById(R.id.number1A);
//                t11.setTextColor(Color.BLACK);
//                TextView t12 = (TextView) findViewById(R.id.number1B);
//                t12.setTextColor(Color.BLACK);
//                TextView t13 = (TextView) findViewById(R.id.number1C);
//                t13.setTextColor(Color.BLACK);
//                TextView t14 = (TextView) findViewById(R.id.number1D);
//                t14.setTextColor(Color.BLACK);
//                TextView t15 = (TextView) findViewById(R.id.number2);
//                t15.setTextColor(Color.BLACK);
//                TextView t16 = (TextView) findViewById(R.id.number2A);
//                t16.setTextColor(Color.BLACK);
//                TextView t17 = (TextView) findViewById(R.id.number2B);
//                t17.setTextColor(Color.BLACK);
//                TextView t18 = (TextView) findViewById(R.id.number2C);
//                t18.setTextColor(Color.BLACK);
//                TextView t19 = (TextView) findViewById(R.id.number2D);
//                t19.setTextColor(Color.BLACK);
//                TextView t20 = (TextView) findViewById(R.id.number3);
//                t20.setTextColor(Color.BLACK);
//                TextView t21 = (TextView) findViewById(R.id.number3A);
//                t21.setTextColor(Color.BLACK);
//                TextView t22 = (TextView) findViewById(R.id.number3B);
//                t22.setTextColor(Color.BLACK);
//                TextView t23 = (TextView) findViewById(R.id.number3D);
//                t23.setTextColor(Color.BLACK);
//                TextView t24 = (TextView) findViewById(R.id.number3C);
//                t24.setTextColor(Color.BLACK);
                        TextView t25 = (TextView) findViewById(R.id.editText);
                        t25.setTextColor(Color.BLACK);
                    }
                    DIG = Integer.parseInt(buf);
                    if (chekRestart != DIG && start) {
                        chekRestart = DIG;
                        TextView start7 = (TextView) findViewById(R.id.start);
                        start7.setText("Start game");
                        TextView edittext8 = (TextView) findViewById(R.id.editText);
                        edittext8.setText("" + codedNumber);

                        codedNumber = "";
                        start = false;
                    }
                } else {
                    Toast.makeText(this, "We  back without change", Toast.LENGTH_LONG).show();
                }
            case 2:
                if (resultCode == RESULT_OK) {
                    mNikOfUser.setText(data.getStringExtra("nikOfUser"));
                    mCurrentVersionOfApp = data.getBooleanExtra("version", mCurrentVersionOfApp);
                    if (!mCurrentVersionOfApp) {
                        Toast.makeText(this, "Update your app", Toast.LENGTH_LONG).show();
                        finish();
//                        startWelcomePage();
                    }
                } else {
                    Toast.makeText(this, "We back without confirm name", Toast.LENGTH_LONG).show();
//                    finish();
                    startWelcomePage();
                }
                break;
            default:
                break;
        }
    }

    public void submitStart() {
        if (!start) {
            crateRandomNumber();
            TextView start1 = (TextView) findViewById(R.id.start);
            start1.setText("Show number");
            startTimer();
            start = true;
            enteredNumber = 0;
            cntMoves = 1;
//            cnt0 = 3;
//            cnt1 = 3;
//            cnt2 = 3;
//            cnt3 = 3;
//            cnt4 = 3;
//            cnt5 = 3;
//            cnt6 = 3;
//            cnt7 = 3;
//            cnt8 = 3;
//            cnt9 = 3;
//            submit0();
//            submit1();
//            submit2();
//            submit3();
//            submit4();
//            submit5();
//            submit6();
//            submit7();
//            submit8();
//            submit9();

            Context context = getApplicationContext();
            String message = "Enter " + DIG + "-digits number, without repeating digits!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

            cleanListView();
            createListViewWithMoves();

//            ImageView image = (ImageView) findViewById(R.id.imageView1D);
//            image.setImageResource(R.drawable.zero);
//            ImageView image2 = (ImageView) findViewById(R.id.imageViewD);
//            image2.setImageResource(R.drawable.zero);
//            ImageView image3 = (ImageView) findViewById(R.id.imageView1C);
//            image3.setImageResource(R.drawable.zero);
//            ImageView image4 = (ImageView) findViewById(R.id.imageViewC);
//            image4.setImageResource(R.drawable.zero);
//            ImageView image5 = (ImageView) findViewById(R.id.imageView1B);
//            image5.setImageResource(R.drawable.zero);
//            ImageView image6 = (ImageView) findViewById(R.id.imageViewB);
//            image6.setImageResource(R.drawable.zero);
//            ImageView image7 = (ImageView) findViewById(R.id.imageView1A);
//            image7.setImageResource(R.drawable.zero);
//            ImageView image8 = (ImageView) findViewById(R.id.imageViewA);
//            image8.setImageResource(R.drawable.zero);
//            ImageView image9 = (ImageView) findViewById(R.id.imageView1);
//            image9.setImageResource(R.drawable.zero);
//            ImageView image10 = (ImageView) findViewById(R.id.imageView);
//            image10.setImageResource(R.drawable.zero);
//            ImageView image11 = (ImageView) findViewById(R.id.imageView1E);
//            image11.setImageResource(R.drawable.zero);
//            ImageView image12 = (ImageView) findViewById(R.id.imageViewE);
//            image12.setImageResource(R.drawable.zero);

            TextView edittext = (TextView) findViewById(R.id.editText);
            edittext.setText("");
//            TextView test3 = (TextView) findViewById(R.id.number1);
//            test3.setText("");
//            TextView test1 = (TextView) findViewById(R.id.number2);
//            test1.setText("");
//            TextView test2 = (TextView) findViewById(R.id.number3);
//            test2.setText("");
//            TextView test4 = (TextView) findViewById(R.id.cntMove);
//            test4.setText("");
            enteredNumber = 0;
//            cntMoves = 1;
//            for (int i = 0; i < 5; ++i) {
//                //shiftData();
//            }

        } else {
            TextView start2 = (TextView) findViewById(R.id.start);
            start2.setText("Start game");

            TextView edittext = (TextView) findViewById(R.id.editText);
            edittext.setText("" + codedNumber);
            cleanListView();

            codedNumber = "";
            start = false;
            mTimerTimer.cancel();
        }

    }

    public void startTimer() {
        mTimerCount = 0;
        mTimer.setText("");
        mTimerTimer = new Timer();
        mTimerTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Date pDate = new Date(mTimerCount * 1000);
                        DateFormat format = new SimpleDateFormat("mm:ss");
                        String formatedTime = format.format(pDate);
                        mTimer.setText("" + formatedTime);
                        ++mTimerCount;
                    }
                });
            }
        }, 1000, 1000);

    }

    public void cleanListView() {
        mMoves.clear();
        mCows.clear();
        mBuls.clear();
        mNumbers.clear();
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

//    public void submit0(View view) {
//        submit0();
//    }
//
//    public void submit0() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help0);
//            if (cnt0 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt0 = 1;
//            } else if (cnt0 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt0 = 2;
//            } else if (cnt0 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt0 = 3;
//            } else if (cnt0 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt0 = 0;
//            }
//        }
//    }
//
//    public void submit1(View view) {
//        submit1();
//    }
//
//    public void submit1() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help1);
//            if (cnt1 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt1 = 1;
//            } else if (cnt1 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt1 = 2;
//            } else if (cnt1 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt1 = 3;
//            } else if (cnt1 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt1 = 0;
//            }
//        }
//    }
//
//    public void submit2(View view) {
//        submit2();
//    }
//
//    public void submit2() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help2);
//            if (cnt2 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt2 = 1;
//            } else if (cnt2 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt2 = 2;
//            } else if (cnt2 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt2 = 3;
//            } else if (cnt2 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt2 = 0;
//            }
//        }
//    }
//
//    public void submit3(View view) {
//        submit3();
//    }
//
//    public void submit3() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help3);
//            if (cnt3 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt3 = 1;
//            } else if (cnt3 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt3 = 2;
//            } else if (cnt3 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt3 = 3;
//            } else if (cnt3 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt3 = 0;
//            }
//        }
//    }
//
//    public void submit4(View view) {
//        submit4();
//    }
//
//    public void submit4() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help4);
//            if (cnt4 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt4 = 1;
//            } else if (cnt4 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt4 = 2;
//            } else if (cnt4 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt4 = 3;
//            } else if (cnt4 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt4 = 0;
//            }
//        }
//    }
//
//    public void submit5(View view) {
//        submit5();
//    }
//
//    public void submit5() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help5);
//            if (cnt5 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt5 = 1;
//            } else if (cnt5 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt5 = 2;
//            } else if (cnt5 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt5 = 3;
//            } else if (cnt5 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt5 = 0;
//            }
//        }
//    }
//
//    public void submit6(View view) {
//        submit6();
//    }
//
//    public void submit6() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help6);
//            if (cnt6 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt6 = 1;
//            } else if (cnt6 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt6 = 2;
//            } else if (cnt6 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt6 = 3;
//            } else if (cnt6 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt6 = 0;
//            }
//        }
//    }
//
//    public void submit7(View view) {
//        submit7();
//    }
//
//    public void submit7() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help7);
//            if (cnt7 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt7 = 1;
//            } else if (cnt7 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt7 = 2;
//            } else if (cnt7 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt7 = 3;
//            } else if (cnt7 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt7 = 0;
//            }
//        }
//    }
//
//    public void submit8(View view) {
//        submit8();
//    }
//
//    public void submit8() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help8);
//            if (cnt8 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt8 = 1;
//            } else if (cnt8 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt8 = 2;
//            } else if (cnt8 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt8 = 3;
//            } else if (cnt8 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt8 = 0;
//            }
//        }
//    }
//
//    public void submit9(View view) {
//        submit9();
//    }
//
//    public void submit9() {
//        if (start) {
//            TextView help0 = (TextView) findViewById(R.id.help9);
//            if (cnt9 == 0) {
//                help0.setBackgroundColor(0xFFFFFF00);
//                cnt9 = 1;
//            } else if (cnt9 == 1) {
//                help0.setBackgroundColor(0xFF76FF03);
//                cnt9 = 2;
//            } else if (cnt9 == 2) {
//                help0.setBackgroundColor(0xFFFF3D00);
//                cnt9 = 3;
//            } else if (cnt9 == 3) {
//                help0.setBackgroundColor(0xFFEEEEEE);
//                cnt9 = 0;
//            }
//        }
//    }

    public void createListViewWithMoves() {
        ListView listOfMoves = (ListView) findViewById(R.id.list_of_moves_list_view);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), mMoves, mNumbers, mBuls, mCows, mode);
        listOfMoves.setAdapter(customAdapter);
        //checkColorMod();
    }

//    public void checkColorMod() {
//        if (cntMoves > 1) {
//            if (mode) {
//                TextView pBull = (TextView) findViewById(R.id.bulls_list_text_view);
//                pBull.setTextColor(Color.WHITE);
//                TextView pCow = (TextView) findViewById(R.id.cows_list_text_view);
//                pCow.setTextColor(Color.WHITE);
//                TextView pNumber = (TextView) findViewById(R.id.number_list_text_view);
//                pNumber.setTextColor(Color.WHITE);
//                TextView pCnt = (TextView) findViewById(R.id.cnt_move_list_text_view);
//                pCnt.setTextColor(Color.WHITE);
//            } else {
//                TextView pBull = (TextView) findViewById(R.id.bulls_list_text_view);
//                pBull.setTextColor(Color.BLACK);
//                TextView pCow = (TextView) findViewById(R.id.cows_list_text_view);
//                pCow.setTextColor(Color.BLACK);
//                TextView pNumber = (TextView) findViewById(R.id.number_list_text_view);
//                pNumber.setTextColor(Color.BLACK);
//                TextView pCnt = (TextView) findViewById(R.id.cnt_move_list_text_view);
//                pCnt.setTextColor(Color.BLACK);
//            }
//        }
//    }

    public void saveNikName() {
        mSaveNikName = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSaveNikName.edit();
        editor.putString(SAVED_TEXT, mNikOfUser.getText().toString());
        editor.commit();
    }

    public void loadNikName() {
        mSaveNikName = getPreferences(MODE_PRIVATE);
        String savedText = mSaveNikName.getString(SAVED_TEXT, "");
        mNikOfUser.setText(savedText);
    }

    public void startWelcomePage() {
//        ConnectivityManager check = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        CheckConnection dff = new CheckConnectnion();
        if (new CheckConnection().checkConnection(this)) {
            Intent welcomeIntent = new Intent(this, Welcome.class);
            welcomeIntent.putExtra("nikOfUser", mNikOfUser.getText());
            welcomeIntent.putExtra("version", mCurrentVersionOfApp);
            startActivityForResult(welcomeIntent, 2);
        } else {
            Toast.makeText(this, Constants.DISCONNECT_SERVER, Toast.LENGTH_LONG).show();
        }

    }

    public void showWinFragment() {
        mFrameLayout.setVisibility(View.VISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(R.id.win_container, mWinFragment);
        mTransaction.commit();
        FragmentManager fM = getFragmentManager();
        fM.executePendingTransactions();
        startService(new Intent (this, WinSoundService.class));
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    public void closeWinFragment(View view) {
        mFrameLayout.setVisibility(View.INVISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.remove(mWinFragment);
        mTransaction.commit();
        stopService(new Intent(this, WinSoundService.class));
    }

    public void setWinText(){
        Fragment winFragment = getFragmentManager().findFragmentById(R.id.win_container);
        ((TextView)winFragment.getView().findViewById(R.id.win_text_view)).setText("Congratulation, " + mNikOfUser.getText().toString() + "! You are win!");
        ((TextView)winFragment.getView().findViewById(R.id.win_result_text_view)).setText("You result: " + DIG + "-digits number, your find on " + (cntMoves - 1) + " moves, by " + mTimer.getText());
    }
}
