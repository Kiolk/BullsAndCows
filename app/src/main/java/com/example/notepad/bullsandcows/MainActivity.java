package com.example.notepad.bullsandcows;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
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
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.LanguageLocale;
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
    public static final String NUMBER_OF_CODED_DIGITS = "numberOfCodedDigits";
    public static final int REQUEST_CODE_CHOICE_LANGUAGE = 5;
    public static final int SETTING_REQUEST_CODE = 1;
    public static final String RULES = "Rules";
    public static final String SETTING = "Setting";
    public static final String ABOUT_APP = "About app";
    public static final String RECORDS = "Records";
    public static final String ONLINE_RECORDS = "Online records";
    public static final String DEFAULT_NIK_OF_USER = "Guest";
    public static final String DEFAULT_PASSWORD_OF_USER = "1111";
    public static final String ENGLISH_LANGUAGE_COD = "en";

    ArrayList<String> mMoves = new ArrayList<>();
    ArrayList<String> mNumbers = new ArrayList<>();
    ArrayList<String> mCows = new ArrayList<>();
    ArrayList<String> mBulls = new ArrayList<>();

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
    TextView mCodOfLanguage;
    ImageView mOptionMenu;
    ImageView mJoinToOnlineImage;
    FrameLayout mFrameLayout;

    public static int DIG = 4;
    String mCodedNumber = "";
    int cntMoves = 1;
    boolean start = false;
    boolean mode;
    SharedPreferences mSaveNikName;
    int checkRestart = DIG;
    private long mTimerCount = 0;
    Timer mTimerTimer = null;
    WriteReadFile mWriteReadFile = new WriteReadFile();
    Boolean mFirstEnteredToApp = true;
    Boolean mIsJoinToOnline;
    String passwordOfUser;
    Boolean mKeepPassword;
    WinFragment mWinFragment;
    FragmentTransaction mTransaction;
    String mLanguageLocale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mLanguageLocale = savedInstanceState.getString(Constants.CODE_OF_LANGUAGE, ENGLISH_LANGUAGE_COD);
            LanguageLocale.setLocale(mLanguageLocale, MainActivity.this);
        }
        initializationOfView();
        loadNikName();
//        new LanguageLocale().setLocale(mLanguageLocale, MainActivity.this);
//        if(mFirstEnteredToApp) {
//            mFirstEnteredToApp = false;
//            startWelcomePage();
//        }
        Intent intent = getIntent();
        mNikOfUser.setText(intent.getStringExtra(Constants.NIK_NAME_OF_USER));
        passwordOfUser = intent.getStringExtra(Constants.PASSWORD_OF_USER);
        mIsJoinToOnline = intent.getBooleanExtra(Constants.JOIN_TO_ONLINE, false);
        mKeepPassword = intent.getBooleanExtra(Constants.KEEP_PASSWORD, false);
        if (mIsJoinToOnline) {
            mJoinToOnlineImage.setBackground(getResources().getDrawable(R.drawable.myrect_green));
        } else {
            mJoinToOnlineImage.setBackground(getResources().getDrawable(R.drawable.myrect_red));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        outState.putInt(NUMBER_OF_CODED_DIGITS, DIG);
        outState.putString(Constants.CODE_OF_LANGUAGE, mLanguageLocale);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mLanguageLocale = savedInstanceState.getString(Constants.CODE_OF_LANGUAGE, "en");
        LanguageLocale.setLocale(mLanguageLocale, MainActivity.this);
        super.onRestoreInstanceState(savedInstanceState);
        mInputNumberView.setText(savedInstanceState.getString(INPUT_NUMBER, DEFAULT_VLUE_FOR_STRING));
        mCodedNumber = savedInstanceState.getString(CODED_NUMBER, DEFAULT_VLUE_FOR_STRING);
        mNikOfUser.setText(savedInstanceState.getString(USER_NAME, DEFAULT_VLUE_FOR_STRING));
        start = savedInstanceState.getBoolean(START_STATE, false);
        DIG = savedInstanceState.getInt(NUMBER_OF_CODED_DIGITS, 4);
        cntMoves = savedInstanceState.getInt(COUNT_OF_MOVES, 1);

        mMoves = savedInstanceState.getStringArrayList(MOVES_ARRAY_LIST);
        mNumbers = savedInstanceState.getStringArrayList(INPUTTED_NUMBER_ARRAY_LIST);
        mBulls = savedInstanceState.getStringArrayList(NUMBER_OF_BULLS_ARRAY_LIST);
        mCows = savedInstanceState.getStringArrayList(NUMBER_OF_COWS_ARRAY_LIST);
        mTimerCount = savedInstanceState.getLong(TIMER_OF_MOVES, 0);

        if (start ){
             if(mBulls.size() != 0 && !(mBulls.get(mBulls.size() - 1).equals("" + DIG))) {
                 startTimer();
             }else if (mBulls.size() == 0){
                 startTimer();
             }
            startButton.setText(getResources().getString(R.string.SHOW_NUMBER));
        }

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
        mCodOfLanguage = (TextView) findViewById(R.id.language_cod_text_view);
        mWinFragment = new WinFragment();
        mFrameLayout = (FrameLayout) findViewById(R.id.win_container);
        mOptionMenu = (ImageView) findViewById(R.id.option_menu_image_view);
        mJoinToOnlineImage = (ImageView) findViewById(R.id.connection_to_online_image_view);

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
                        buf += 1;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom2:
                        buf += 2;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom3:
                        buf += 3;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom4:
                        buf += 4;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom5:
                        buf += 5;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom6:
                        buf += 6;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom7:
                        buf += 7;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom8:
                        buf += 8;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom9:
                        buf += 9;
                        mInputNumberView.setText(buf);
                        break;
                    case R.id.buttom0:
                        buf += 0;
                        mInputNumberView.setText(buf);
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
                                String message = getResources().getString(R.string.ENTER_NUMBER) + DIG + getResources().getString(R.string.TOAST_MESSAGE_WITH_RULE);
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
                    case R.id.connection_to_online_image_view:
                        Intent intent = new Intent(MainActivity.this, Welcome.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.language_cod_text_view:
                        Intent intentLanguage = new Intent(MainActivity.this, ChoiceLanguageActivity.class);
                        startActivityForResult(intentLanguage, REQUEST_CODE_CHOICE_LANGUAGE, null);
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
        mJoinToOnlineImage.setOnClickListener(clickButton);
        mCodOfLanguage.setOnClickListener(clickButton);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveNikName();
        if (mTimerTimer != null) {
            mTimerTimer.cancel();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, RULES);
        menu.add(0, 2, 1, SETTING);
        menu.add(0, 3, 2, ABOUT_APP);
        menu.add(0, 4, 3, RECORDS);
        menu.add(0, 5, 4, ONLINE_RECORDS);
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
                intent2.putExtra(Constants.MODE_STATE, mode);
//                intent2.putExtra("nikOfUser", mNikOfUser.getText());
                intent2.putExtra(Constants.CODED_DIGITS, DIG);
                startActivityForResult(intent2, SETTING_REQUEST_CODE);
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

    public static void main(String[] argc) {
    }

    public void getNumber() {
        String inputNumber = mInputNumberView.getText().toString();
        mNumbers.add(inputNumber);
        mMoves.add("" + cntMoves);
        mBulls.add("" + new RandomNumberGenerator().checkNumberOfBulls(mCodedNumber, inputNumber));
        mCows.add("" + new RandomNumberGenerator().checkNumberOfCows(mCodedNumber, inputNumber));
        ++cntMoves;
    }

    public void checkNumberForWin() {
        int numberOfBulls = new RandomNumberGenerator().checkNumberOfBulls(mCodedNumber, mInputNumberView.getText().toString());
        if (numberOfBulls == DIG) {
            mInputNumberView.setText(R.string.WON_MESSAGE);
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
            submitStart(); //add this now
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String buf = data.getStringExtra(Constants.CODED_DIGITS);
                    mode = data.getBooleanExtra(Constants.MODE_STATE, mode);
//                    mNikOfUser.setText(data.getStringExtra("nikOfUser"));
                    createListViewWithMoves();
                    if (mode) {
                        LinearLayout layoutMain = (LinearLayout) findViewById(R.id.mainLyaout);
                        layoutMain.setBackgroundColor(Color.BLACK);
                        mInputNumberView.setTextColor(Color.WHITE);

                    } else {

                        LinearLayout lyaoutmain = (LinearLayout) findViewById(R.id.mainLyaout);
                        lyaoutmain.setBackgroundColor(Color.WHITE);
                        mInputNumberView.setTextColor(Color.BLACK);
                    }
                    DIG = Integer.parseInt(buf);
                    if (checkRestart != DIG && start) {
                        checkRestart = DIG;
                        TextView start7 = (TextView) findViewById(R.id.start);
                        start7.setText(getResources().getString(R.string.START_GAME));
                        mInputNumberView.setText(mCodedNumber); // change this

                        mCodedNumber = "";
                        start = false;
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.WE_BACK_WITHOUT_CHANGE), Toast.LENGTH_LONG).show();
                }
//            case 2:
//                if (resultCode == RESULT_OK) {
//                    mNikOfUser.setText(data.getStringExtra("nikOfUser"));
//                    mCurrentVersionOfApp = data.getBooleanExtra("version", mCurrentVersionOfApp);
//                    mKeepPassword = data.getBooleanExtra("keepPassword", false);
//                    passwordOfUser = data.getStringExtra("password");
//                    if (!mCurrentVersionOfApp) {
//                        Toast.makeText(this, "Update your app", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//                } else {
//                    Toast.makeText(this, "You not logged", Toast.LENGTH_LONG).show();
//                }
//                break;
            case REQUEST_CODE_CHOICE_LANGUAGE:
                if(resultCode == RESULT_OK) {
                    mLanguageLocale = data.getStringExtra(Constants.CODE_OF_LANGUAGE);
//                LanguageLocale.setLocale(mLanguageLocale, this);
//                mCodOfLanguage.setText(getResources().getString(R.string.LANGUAGE_COD));
                }
                break;
            default:
                break;
        }
    }

    public void submitStart() {
        if (!start) {
            mCodedNumber = new RandomNumberGenerator().generateRandomNumber(DIG);
            startButton.setText(getResources().getString(R.string.SHOW_NUMBER));
            restartTimer();
            start = true;
            cntMoves = 1;
            Context context = getApplicationContext();
            String message = getResources().getString(R.string.ENTER_NUMBER) + DIG + getResources().getString(R.string.TOAST_MESSAGE_WITH_RULE);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();

            cleanListView();
            createListViewWithMoves();
            mInputNumberView.setText("");
        } else {
            TextView start2 = (TextView) findViewById(R.id.start);
            start2.setText(getResources().getString(R.string.START_GAME));
            mInputNumberView.setText(mCodedNumber); //change this
//            cleanListView();
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

    public void restartTimer() {
        mTimerCount = 0;
        startTimer();
    }

    public void cleanListView() {
        mMoves.clear();
        mCows.clear();
        mBulls.clear();
        mNumbers.clear();
    }

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
            editor.putString(Constants.CODE_OF_LANGUAGE, mLanguageLocale);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = mSaveNikName.edit();
            editor.putString(SAVED_TEXT, "");
            editor.putString(SAVED_PASSWORD, "");
            editor.putBoolean(SAVED_KEEP_PASSWORD, false);
            editor.putString(Constants.CODE_OF_LANGUAGE, mLanguageLocale);
            editor.commit();
        }
    }

    public void loadNikName() {
        mSaveNikName = getPreferences(MODE_PRIVATE);
        if (mSaveNikName.getBoolean(SAVED_KEEP_PASSWORD, false)) {
            String savedText = mSaveNikName.getString(SAVED_TEXT, DEFAULT_NIK_OF_USER);
            mNikOfUser.setText(savedText);
            passwordOfUser = mSaveNikName.getString(SAVED_PASSWORD, DEFAULT_PASSWORD_OF_USER);
            mKeepPassword = mSaveNikName.getBoolean(SAVED_KEEP_PASSWORD, false);
            mLanguageLocale = mSaveNikName.getString(Constants.CODE_OF_LANGUAGE, ENGLISH_LANGUAGE_COD);
            LanguageLocale.setLocale(mLanguageLocale, MainActivity.this);
            initializationOfView();
        }
    }

//    public void startWelcomePage() {
//        if (new CheckConnection().checkConnection(this)) {
//            Intent welcomeIntent = new Intent(this, Welcome.class);
//            welcomeIntent.putExtra("nikOfUser", mNikOfUser.getText());
//            welcomeIntent.putExtra("version", mCurrentVersionOfApp);
//            welcomeIntent.putExtra("password", passwordOfUser);
//            welcomeIntent.putExtra("keepPassword", mKeepPassword);
//            startActivityForResult(welcomeIntent, 2);
//        } else {
//            Toast.makeText(this, Constants.DISCONNECT_SERVER, Toast.LENGTH_LONG).show();
//            mNikOfUser.setText("Guest");
//            mKeepPassword = true;
//            passwordOfUser = "1111";
//            mCurrentVersionOfApp = true;
//        }
//
//    }

    public void showWinFragment() {
        mFrameLayout.setVisibility(View.VISIBLE);
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

    public void closeWinFragment(View view) {
        mFrameLayout.setVisibility(View.INVISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.remove(mWinFragment);
        mTransaction.commit();
        stopService(new Intent(this, WinSoundService.class));
    }

    public void setWinText() {
        Fragment winFragment = getFragmentManager().findFragmentById(R.id.win_container);
        ((TextView) winFragment.getView().findViewById(R.id.win_text_view)).setText(getResources().getString(R.string.CONGRATULATIONS) + mNikOfUser.getText().toString() + getResources().getString(R.string.YOU_WIN));
        ((TextView) winFragment.getView().findViewById(R.id.win_result_text_view)).setText(getResources().getString(R.string.YOUR_RESULT) + DIG + getResources().getString(R.string.NUMBER_OF_DIGITS) + (cntMoves - 1) + getResources().getString(R.string.WIN_TIME) + mTimer.getText());
    }

//    private class joinForOnline extends UserCheckExist {
//
//        @Override
//        protected void onPostExecute(Boolean pBoolean) {
//            super.onPostExecute(pBoolean);
//            if (pBoolean) {
//
//            } else {
//
//            }
//        }
//    }

}
