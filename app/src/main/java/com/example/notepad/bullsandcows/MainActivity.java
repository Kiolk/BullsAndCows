package com.example.notepad.bullsandcows;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.fragments.WinFragment;
import com.example.notepad.bullsandcows.services.WinSoundService;
import com.example.notepad.bullsandcows.utils.AnimationOfView;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.RandomNumberGenerator;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String SAVED_TEXT = "saved-text";
    public static final String SAVED_PASSWORD = "Saved_password";
    public static final String SAVED_KEEP_PASSWORD = "Saved_keep_password";
    public static final String INPUT_NUMBER = "InputNumber";
    public static final String DEFAULT_VLUE_FOR_STRING = "Error";
    public static final String CODED_NUMBER = "codedNumber";
    public static final String USER_NAME = "userName";
    public static final String START_STATE = "startState";
    public static final String MOVES_ARRAY_LIST = "movesArrayList";
    public static final String INPUTTED_NUMBER_ARRAY_LIST = "inputtedNumberArrayList";
    public static final String NUMBER_OF_BULLS_ARRAY_LIST = "numberOfBullsArrayList";
    public static final String NUMBER_OF_COWS_ARRAY_LIST = "numberOfCowsArrayList";
    public static final String COUNT_OF_MOVES = "countOfMoves";
    public static final String TIMER_OF_MOVES = "timerOfMoves";
    public static final String FIRST_ENTERED_TO_APP = "firstEnteredToApp";
    ArrayList<String> mMoves = new ArrayList<>();
    ArrayList<String> mNumbers = new ArrayList<>();
    ArrayList<String> mCows = new ArrayList<>();
    ArrayList<String> mBulls = new ArrayList<>();

    public static int DIG = 4;
    //    public static int[] randomNumber = new int[10];
    String mCodedNumber = "";
    //    int enteredNumber = 0;
    int cntMoves = 1;
    boolean start = false;
    boolean mode;
    SharedPreferences mSaveNikName;
    //    public static int[] enteredArray = new int[10];
    int chekRestart = DIG;
    TextView mInputNumberView;
    TextView number1;
    TextView number2;
    TextView number3;
    TextView number4;
    TextView number5;
    TextView number6;
    TextView number7;
    TextView number8;
    TextView number9;
    TextView number0;
    TextView enterButton;
    TextView startButton;
    TextView del;
    TextView mTimer;
    TextView mNikOfUser;
    ImageView mOptionMenu;
    private long mTimerCount = 0;
    Timer mTimerTimer = null;
    WriteReadFile mWriteReadFile = new WriteReadFile();
    Boolean mCurrentVersionOfApp = true;
    Boolean mFirstEnteredToApp = true;
    String passwordOfUser;
    Boolean mKeepPassword;
    WinFragment mWinFragment;
    FragmentTransaction mTransaction;
    FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        initializationOfView();
        loadNikName();
//        if(mFirstEnteredToApp) {
//            mFirstEnteredToApp = false;
//            startWelcomePage();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        try {
            outState.putString(INPUT_NUMBER, mInputNumberView.getText().toString());
            outState.putString(CODED_NUMBER, mCodedNumber);
            outState.putString(USER_NAME, mNikOfUser.getText().toString());
            outState.putBoolean(START_STATE, start);
            outState.putBoolean(FIRST_ENTERED_TO_APP, mFirstEnteredToApp);
            outState.putStringArrayList(MOVES_ARRAY_LIST, mMoves);
            outState.putStringArrayList(INPUTTED_NUMBER_ARRAY_LIST, mNumbers);
            outState.putStringArrayList(NUMBER_OF_BULLS_ARRAY_LIST, mBulls);
            outState.putStringArrayList(NUMBER_OF_COWS_ARRAY_LIST, mCows);
            outState.putInt(COUNT_OF_MOVES, cntMoves);
            outState.putLong(TIMER_OF_MOVES, mTimerCount);
//        }catch (Exception pE){
//            pE.printStackTrace();
//        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInputNumberView.setText(savedInstanceState.getString(INPUT_NUMBER, DEFAULT_VLUE_FOR_STRING));
        mCodedNumber = savedInstanceState.getString(CODED_NUMBER, DEFAULT_VLUE_FOR_STRING);
        mNikOfUser.setText(savedInstanceState.getString(USER_NAME, DEFAULT_VLUE_FOR_STRING));
        start = savedInstanceState.getBoolean(START_STATE, false);
        mFirstEnteredToApp = savedInstanceState.getBoolean(FIRST_ENTERED_TO_APP, false);
        cntMoves = savedInstanceState.getInt(COUNT_OF_MOVES, 1);
        if(start){
            startButton.setText(getResources().getString(R.string.SHOW_NAMBER));
            startTimer();
        }
        mMoves = savedInstanceState.getStringArrayList(MOVES_ARRAY_LIST);
        mNumbers = savedInstanceState.getStringArrayList(INPUTTED_NUMBER_ARRAY_LIST);
        mBulls = savedInstanceState.getStringArrayList(NUMBER_OF_BULLS_ARRAY_LIST);
        mCows = savedInstanceState.getStringArrayList(NUMBER_OF_COWS_ARRAY_LIST);
        mTimerCount = savedInstanceState.getLong(TIMER_OF_MOVES, 0);
//        startTimer();
        createListViewWithMoves();
    }

    public void initializationOfView() {

        mInputNumberView = (TextView) findViewById(R.id.editText);
        mInputNumberView.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.BLACKGROTESKC));
        number1 = (TextView) findViewById(R.id.buttom1);
        number2 = (TextView) findViewById(R.id.buttom2);
        number3 = (TextView) findViewById(R.id.buttom3);
        number4 = (TextView) findViewById(R.id.buttom4);
        number5 = (TextView) findViewById(R.id.buttom5);
        number6 = (TextView) findViewById(R.id.buttom6);
        number7 = (TextView) findViewById(R.id.buttom7);
        number8 = (TextView) findViewById(R.id.buttom8);
        number9 = (TextView) findViewById(R.id.buttom9);
        number0 = (TextView) findViewById(R.id.buttom0);
        enterButton = (TextView) findViewById(R.id.enter);
        startButton = (TextView) findViewById(R.id.start);
        del = (TextView) findViewById(R.id.buttomDel);
        mTimer = (TextView) findViewById(R.id.timer_text_view);
        mNikOfUser = (TextView) findViewById(R.id.user_name_text_view);
        mWinFragment = new WinFragment();
        mFrameLayout = (FrameLayout) findViewById(R.id.win_container);
        mOptionMenu = (ImageView) findViewById(R.id.option_menu_image_view);

        View.OnClickListener clickButton = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String buf;
                buf = mInputNumberView.getText().toString();
                switch (view.getId()) {
                    case R.id.option_menu_image_view:
                        openOptionsMenu();
                        break;
                    case R.id.buttom1:
                        mInputNumberView.setText(buf + 1);
                        break;
                    case R.id.buttom2:
                        mInputNumberView.setText(buf + 2);
                        break;
                    case R.id.buttom3:
                        mInputNumberView.setText(buf + 3);
                        break;
                    case R.id.buttom4:
                        mInputNumberView.setText(buf + 4);
                        break;
                    case R.id.buttom5:
                        mInputNumberView.setText(buf + 5);
                        break;
                    case R.id.buttom6:
                        mInputNumberView.setText(buf + 6);
                        break;
                    case R.id.buttom7:
                        mInputNumberView.setText(buf + 7);
                        break;
                    case R.id.buttom8:
                        mInputNumberView.setText(buf + 8);
                        break;
                    case R.id.buttom9:
                        mInputNumberView.setText(buf + 9);
                        break;
                    case R.id.buttom0:
                        mInputNumberView.setText(buf + 0);
                        break;
                    case R.id.buttomDel:
                        if (buf.length() > 0) {
                            buf = buf.substring(0, buf.length() - 1);
                            mInputNumberView.setText(buf);
                        }
                        break;
                    case R.id.enter:
                        if (start) {
                            if (new RandomNumberGenerator().checkNumberForCorrectInput(mInputNumberView.getText().toString(), DIG)) {
                                getNumber();
                                checkNumberForWin();
                                mInputNumberView.setText("");
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
        mOptionMenu.setOnClickListener(clickButton);
        del.setOnClickListener(clickButton);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveNikName();
        if(mTimerTimer != null) {
            mTimerTimer.cancel();
        }
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

//    public boolean checkNumberForCorrect() {
//        TextView editText = (TextView) findViewById(R.id.editText);
//        String number = editText.getText().toString();
//        if (number.length() == DIG) {
//            for (int i = 0; i < DIG; ++i) {
//                if (number.charAt(i) < '0' || number.charAt(i) > '9') {
//                    return false;
//                }
//            }
//            int numberForChecking = Integer.parseInt(number);
//            int k = 1;
//            for (int i = 0; i < DIG - 1; ++i) {
//                k = k * 10;
//            }
//            if (numberForChecking >= k) {
//                int[] numberArray;
//                numberArray = new int[DIG];
//                for (int i = DIG - 1; i >= 0; --i) {
//                    numberArray[i] = numberForChecking % 10;
//                    numberForChecking = numberForChecking / 10;
//                }
//                for (int i = 0, cnt = 0; i < DIG; ++i) {
//                    for (int j = 0; j < DIG; ++j) {
//                        if (numberArray[i] == numberArray[j]) {
//                            ++cnt;
//                        }
//                        if (cnt > DIG) {
//                            return false;
//                        }
//                    }
//                }
//                return true;
//            } else if (number.charAt(0) == '0' && numberForChecking >= k / 10) {
//                int[] numberArray;
//                numberArray = new int[DIG];
//                for (int i = DIG - 1; i >= 1; --i) {
//                    numberArray[i] = numberForChecking % 10;
//                    numberForChecking = numberForChecking / 10;
//                }
//                for (int i = 0, cnt = 0; i < DIG; ++i) {
//                    for (int j = 0; j < DIG; ++j) {
//                        if (numberArray[i] == numberArray[j]) {
//                            ++cnt;
//                        }
//                        if (cnt > DIG) {
//                            return false;
//                        }
//                    }
//                }
//
//                return true;
//            }
//        }
//        return false;
//    }

    public void getNumber() {
        String inputNumber = mInputNumberView.getText().toString();
        mNumbers.add(inputNumber);
        mMoves.add("" + cntMoves);
//        enteredNumber = Integer.parseInt(number);
//        for (int i = DIG - 1; i >= 0; --i) {
//            enteredArray[i] = enteredNumber % 10;
//            enteredNumber = enteredNumber / 10;
//        }
//        mBulls.add("" + checkingBulls());
//        mCows.add("" + (checkingCows() - checkingBulls()));
        mBulls.add("" + new RandomNumberGenerator().checkNumberOfBulls(mCodedNumber, inputNumber));
        mCows.add("" + new RandomNumberGenerator().checkNumberOfCows(mCodedNumber, inputNumber));
        ++cntMoves;
    }

    public void checkNumberForWin() {
//        if (checkingBulls() == DIG) {
        int numberOfBulls = new RandomNumberGenerator().checkNumberOfBulls(mCodedNumber, mInputNumberView.getText().toString());
        if (numberOfBulls == DIG) {
//            TextView wonText = (TextView) findViewById(R.id.editText);
            mInputNumberView.setText("WON!");
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

//    public int checkingBulls() {
//        int bulls = 0;
//        for (int i = 0; i < DIG; ++i) {
//            if (randomNumber[i] == enteredArray[i]) {
//                ++bulls;
//            }
//        }
//        return bulls;
//    }

//    public int checkingCows() {
//        int cows = 0;
//        int i = 0;
//        int j = 0;
//        for (i = 0; i < DIG; ++i) {
//            for (j = 0; j < DIG; ++j) {
//                if (enteredArray[i] == randomNumber[j]) {
//                    ++cows;
//                }
//            }
//        }
//        return cows;
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String buf = data.getStringExtra("numberofdigits");
                    mode = data.getBooleanExtra("modeState", mode);
                    mNikOfUser.setText(data.getStringExtra("nikOfUser"));
                    createListViewWithMoves();
                    if (mode) {
                        LinearLayout layoutMain = (LinearLayout) findViewById(R.id.mainLyaout);
                        layoutMain.setBackgroundColor(Color.BLACK);
//                        TextView t25 = (TextView) findViewById(R.id.editText);
                        mInputNumberView.setTextColor(Color.WHITE);

                    } else {

                        LinearLayout lyaoutmain = (LinearLayout) findViewById(R.id.mainLyaout);
                        lyaoutmain.setBackgroundColor(Color.WHITE);
//                        TextView t25 = (TextView) findViewById(R.id.editText);
                        mInputNumberView.setTextColor(Color.BLACK);
                    }
                    DIG = Integer.parseInt(buf);
                    if (chekRestart != DIG && start) {
                        chekRestart = DIG;
                        TextView start7 = (TextView) findViewById(R.id.start);
                        start7.setText("Start game");
//                        TextView edittext8 = (TextView) findViewById(R.id.editText);
                        mInputNumberView.setText("" + mCodedNumber);

                        mCodedNumber = "";
                        start = false;
                    }
                } else {
                    Toast.makeText(this, "We  back without change", Toast.LENGTH_LONG).show();
                }
            case 2:
                if (resultCode == RESULT_OK) {
                    mNikOfUser.setText(data.getStringExtra("nikOfUser"));
                    mCurrentVersionOfApp = data.getBooleanExtra("version", mCurrentVersionOfApp);
                    mKeepPassword = data.getBooleanExtra("keepPassword", false);
                    passwordOfUser = data.getStringExtra("password");
                    if (!mCurrentVersionOfApp) {
                        Toast.makeText(this, "Update your app", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    Toast.makeText(this, "You not logined", Toast.LENGTH_LONG).show();
//                    startWelcomePage();
                }
                break;
            default:
                break;
        }
    }

    public void submitStart() {
        if (!start) {
            mCodedNumber = new RandomNumberGenerator().generateRandomNumber(DIG);
            //  crateRandomNumber();
//            TextView start1 = (TextView) findViewById(R.id.start);
            startButton.setText(getResources().getString(R.string.SHOW_NAMBER));
            restartTimer();
            start = true;
//            enteredNumber = 0;
            cntMoves = 1;
            Context context = getApplicationContext();
            String message = "Enter " + DIG + "-digits number, without repeating digits!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

            cleanListView();
            createListViewWithMoves();
//            TextView edittext = (TextView) findViewById(R.id.editText);
            mInputNumberView.setText("");
//            enteredNumber = 0;
        } else {
            TextView start2 = (TextView) findViewById(R.id.start);
            start2.setText("Start game");
//            TextView edittext = (TextView) findViewById(R.id.editText);
            mInputNumberView.setText("" + mCodedNumber);
            cleanListView();
            mCodedNumber = "";
            start = false;
            mTimerTimer.cancel();
        }

    }

    public void startTimer() {
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

    public void restartTimer(){
        mTimerCount = 0;
        startTimer();
    }

    public void cleanListView() {
        mMoves.clear();
        mCows.clear();
        mBulls.clear();
        mNumbers.clear();
    }

//    public void crateRandomNumber() {
//        for (int i = 0, j = 0; i < DIG; ++i) {
//            Random r = new Random();
//            int k = r.nextInt(9);
//            if (i == 0) {
//                randomNumber[i] = k;
//            } else {
//                for (j = 0; j < i; ++j) {
//                    if (randomNumber[j] == k) {
//                        --i;
//                        break;
//                    } else {
//                        randomNumber[i] = k;
//                    }
//                }
//            }
//
//        }
//        for (int i = 0; i < DIG; ++i) {
//            mCodedNumber = mCodedNumber + randomNumber[i];
//        }
//
//    }

    public void createListViewWithMoves() {
        ListView listOfMoves = (ListView) findViewById(R.id.list_of_moves_list_view);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), mMoves, mNumbers, mBulls, mCows, mode);
        listOfMoves.setAdapter(customAdapter);
    }

    public void saveNikName() {
        if (mKeepPassword) {
            mSaveNikName = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = mSaveNikName.edit();
            editor.putString(SAVED_TEXT, mNikOfUser.getText().toString());
            editor.putString(SAVED_PASSWORD, passwordOfUser);
            editor.putBoolean(SAVED_KEEP_PASSWORD, mKeepPassword);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = mSaveNikName.edit();
            editor.putString(SAVED_TEXT, "");
            editor.putString(SAVED_PASSWORD, "");
            editor.putBoolean(SAVED_KEEP_PASSWORD, false);
            editor.commit();
        }
    }

    public void loadNikName() {
        mSaveNikName = getPreferences(MODE_PRIVATE);
        if (mSaveNikName.getBoolean(SAVED_KEEP_PASSWORD, false)) {
            String savedText = mSaveNikName.getString(SAVED_TEXT, "Guest");
            mNikOfUser.setText(savedText);
            passwordOfUser = mSaveNikName.getString(SAVED_PASSWORD, "1111");
            mKeepPassword = mSaveNikName.getBoolean(SAVED_KEEP_PASSWORD, false);
        }
    }

    public void startWelcomePage() {
        if (new CheckConnection().checkConnection(this)) {
            Intent welcomeIntent = new Intent(this, Welcome.class);
            welcomeIntent.putExtra("nikOfUser", mNikOfUser.getText());
            welcomeIntent.putExtra("version", mCurrentVersionOfApp);
            welcomeIntent.putExtra("password", passwordOfUser);
            welcomeIntent.putExtra("keepPassword", mKeepPassword);
            startActivityForResult(welcomeIntent, 2);
        } else {
            Toast.makeText(this, Constants.DISCONNECT_SERVER, Toast.LENGTH_LONG).show();
            mNikOfUser.setText("Guest");
            mKeepPassword = true;
            passwordOfUser = "1111";
            mCurrentVersionOfApp = true;
        }

    }

    public void showWinFragment() {
        mFrameLayout.setVisibility(View.VISIBLE);
//        animationOfViewShow(mFrameLayout);
        new AnimationOfView().enteredView(mFrameLayout);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(R.id.win_container, mWinFragment);
        mTransaction.commit();
        FragmentManager fM = getFragmentManager();
        fM.executePendingTransactions();
        startService(new Intent(this, WinSoundService.class));
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

    }

//    @TargetApi(21)
//    public void animationOfViewShow(View pView) {
//        int cx = (pView.getLeft() + pView.getRight()) / 2;
//        int cy = (pView.getBottom() + pView.getTop()) / 2;
//        int finalRadius = Math.max(pView.getWidth(), pView.getHeight());
//        Animator anim = ViewAnimationUtils.createCircularReveal(pView, cx, cy, 0, finalRadius);
//        anim.start();
//    }

    public void closeWinFragment(View view) {
        mFrameLayout.setVisibility(View.INVISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.remove(mWinFragment);
        mTransaction.commit();
        stopService(new Intent(this, WinSoundService.class));
    }

    public void setWinText() {
        Fragment winFragment = getFragmentManager().findFragmentById(R.id.win_container);
        ((TextView) winFragment.getView().findViewById(R.id.win_text_view)).setText("Congratulation, " + mNikOfUser.getText().toString() + "! You are win!");
        ((TextView) winFragment.getView().findViewById(R.id.win_result_text_view)).setText("You result: " + DIG + "-digits number, your find on " + (cntMoves - 1) + " moves, by " + mTimer.getText());
    }
}
