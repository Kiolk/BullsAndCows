package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.CustomAdapter;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.RecordAsyncTaskPost;
import com.example.notepad.bullsandcows.WriteReadFile;
import com.example.notepad.bullsandcows.data.databases.DBOperations;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.services.WinSoundService;
import com.example.notepad.bullsandcows.ui.activity.fragments.EditProfileFragment;
import com.example.notepad.bullsandcows.ui.activity.fragments.WinFragment;
import com.example.notepad.bullsandcows.ui.activity.listeners.CloseEditProfileListener;
import com.example.notepad.bullsandcows.utils.AnimationOfView;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.LanguageLocale;
import com.example.notepad.bullsandcows.utils.RandomNumberGenerator;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import kiolk.com.github.pen.Pen;

import static com.example.notepad.bullsandcows.utils.Constants.BACK_EPOCH_TIME_NOTATION;

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
    public static final String DEFAULT_NIK_OF_USER = "Guest";
    public static final String DEFAULT_PASSWORD_OF_USER = "1111";
    public static final String ENGLISH_LANGUAGE_COD = "en";
    public static final String START_TIME_KEY = "StartTime";

    private ArrayList<String> mMoves = new ArrayList<>();
    private ArrayList<String> mNumbers = new ArrayList<>();
    private ArrayList<String> mCows = new ArrayList<>();
    private ArrayList<String> mBulls = new ArrayList<>();

    private TextView mInputNumberView;
    private TextView startButton;
    private TextView mTimer;
    private TextView mNikOfUser;
    private FrameLayout mFrameLayout;
    private FrameLayout mEditInfoFrameLayout;

    public static int DIG = 4;
    private String mCodedNumber = "";
    private int cntMoves = 1;
    private boolean start = false;
    private boolean mode;
    private SharedPreferences mSaveNikName;
    private int checkRestart = DIG;
    private long mTimerCount = 0;
    private Timer mTimerTimer = null;
    private WriteReadFile mWriteReadFile = new WriteReadFile();
    private String passwordOfUser;
    private Boolean mKeepPassword;
    private WinFragment mWinFragment;
    private EditProfileFragment mEditProfileFragment;
    private FragmentTransaction mTransaction;
    private String mLanguageLocale;
    private long mStartGameTime;
    private FragmentManager mFragmentManager;
    private CloseEditProfileListener mCloseEditListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        UserLoginHolder.getInstance().setUserOnline();
        setImageLoaderConfiguration();

        initToolBar();
        initializationOfView();
//        loadNikName();
        initEditProfileFragment();
    }

    private void initEditProfileFragment() {
        mEditProfileFragment = new EditProfileFragment();
        mCloseEditListener = new CloseEditProfileListener() {
            @Override
            public void closeFragment() {
                mTransaction = getFragmentManager().beginTransaction();
                mTransaction.remove(mEditProfileFragment);
                mTransaction.commit();
                mFragmentManager.executePendingTransactions();
                mEditInfoFrameLayout.setVisibility(View.GONE);
            }
        };
        mEditProfileFragment.setCloseListener(mCloseEditListener);
        mFragmentManager = getFragmentManager();
    }

    private void setImageLoaderConfiguration() {
        Pen.getInstance().setLoaderSettings()
                .setSavingStrategy(Pen.SAVE_FULL_IMAGE_STRATEGY)
                .setTypeOfCache(Pen.INNER_FILE_CACHE)
                .setSizeInnerFileCache(10L);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INPUT_NUMBER, mInputNumberView.getText().toString());
        outState.putString(CODED_NUMBER, mCodedNumber);
        outState.putString(USER_NAME, mNikOfUser.getText().toString());
        outState.putBoolean(START_STATE, start);
        Boolean mFirstEnteredToApp = true;
        outState.putBoolean(FIRST_ENTERED_TO_APP, mFirstEnteredToApp);
        outState.putStringArrayList(MOVES_ARRAY_LIST, mMoves);
        outState.putStringArrayList(INPUTTED_NUMBER_ARRAY_LIST, mNumbers);
        outState.putStringArrayList(NUMBER_OF_BULLS_ARRAY_LIST, mBulls);
        outState.putStringArrayList(NUMBER_OF_COWS_ARRAY_LIST, mCows);
        outState.putInt(COUNT_OF_MOVES, cntMoves);
        outState.putLong(TIMER_OF_MOVES, mTimerCount);
        outState.putLong(START_TIME_KEY, mStartGameTime);
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
        mStartGameTime = savedInstanceState.getLong(START_TIME_KEY, System.currentTimeMillis());

        if (start) {
            if (mBulls.size() != 0 && !(mBulls.get(mBulls.size() - 1).equals("" + DIG))) {
                startTimer();
            } else if (mBulls.size() == 0) {
                startTimer();
            }
            startButton.setText(getResources().getString(R.string.SHOW_NUMBER));
        }

        createListViewWithMoves();
    }

    public void initializationOfView() {

        mInputNumberView = findViewById(R.id.editText);
        mInputNumberView.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.BLACKGROTESKC));
        TextView number1 = findViewById(R.id.buttom1);
        TextView number2 = findViewById(R.id.buttom2);
        TextView number3 = findViewById(R.id.buttom3);
        TextView number4 = findViewById(R.id.buttom4);
        TextView number5 = findViewById(R.id.buttom5);
        TextView number6 = findViewById(R.id.buttom6);
        TextView number7 = findViewById(R.id.buttom7);
        TextView number8 = findViewById(R.id.buttom8);
        TextView number9 = findViewById(R.id.buttom9);
        TextView number0 = findViewById(R.id.buttom0);
        TextView enterButton = findViewById(R.id.enter);
        startButton = findViewById(R.id.start);
        TextView del = findViewById(R.id.buttomDel);
        mTimer = findViewById(R.id.timer_text_view);
        mTimer.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.DIGITAL_FONT));
        mNikOfUser = findViewById(R.id.user_name_text_view);
        mNikOfUser.setText(UserLoginHolder.getInstance().getUserName());
        TextView mCodOfLanguage = findViewById(R.id.language_cod_text_view);
        mWinFragment = new WinFragment();
        mFrameLayout = findViewById(R.id.win_container);
        ImageView mOptionMenu = findViewById(R.id.option_menu_image_view);
        mEditInfoFrameLayout = findViewById(R.id.for_fragments_in_main_frame_layout);
        passwordOfUser = UserLoginHolder.getInstance().getPassword();

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
//                    case R.id.connection_to_online_image_view:
//                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
//                        startActivity(intent);
//                        finish();
//                        break;
//                    case R.id.language_cod_text_view:
//                        Intent intentLanguage = new Intent(MainActivity.this, ChoiceLanguageActivity.class);
//                        startActivity(intentLanguage);
//                        break;
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
        mCodOfLanguage.setOnClickListener(clickButton);
    }

    private void initToolBar() {
        Toolbar mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(UserLoginHolder.getInstance().getUserName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLoginHolder.getInstance().setOffline();
//        saveNikName();
        if (mTimerTimer != null) {
            mTimerTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        if(!mEditProfileFragment.isVisible()) {
            UserLoginHolder.getInstance().setOffline();
            super.onBackPressed();
        }else{
            mCloseEditListener.closeFragment();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        UserLoginHolder.getInstance().keepUserOnline();
        switch (item.getItemId()) {
            case R.id.rules_item_menu:
                Intent intent = new Intent(this, RulesPageActivity.class);
                startActivity(intent);
                break;
            case R.id.settings_item_menu:
                Intent intent2 = new Intent(this, SettingActivity.class);
                intent2.putExtra(Constants.MODE_STATE, mode);
                intent2.putExtra(Constants.CODED_DIGITS, DIG);
                startActivityForResult(intent2, SETTING_REQUEST_CODE);
                break;
            case R.id.about_item_menu:
                Intent intent3 = new Intent(this, AboutActivity.class);
                startActivity(intent3);
                break;
            case R.id.login_page_item_menu:
                Intent intentLogin = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intentLogin);
                finish();
                break;
            case R.id.record_item_menu:
                Intent intent6 = new Intent(this, RecordsCardActivityFromBD.class);
                startActivity(intent6);
                break;
            case R.id.edit_profile_menu:
                mEditInfoFrameLayout.setVisibility(View.VISIBLE);
                mTransaction = getFragmentManager().beginTransaction();
                mTransaction.add(R.id.for_fragments_in_main_frame_layout, mEditProfileFragment);
                mTransaction.commit();
                mFragmentManager.executePendingTransactions();
                mEditProfileFragment.editUserProfile();
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
            long mDurationGameTime = System.currentTimeMillis() - mStartGameTime;

            note.setDate(BACK_EPOCH_TIME_NOTATION - System.currentTimeMillis());
            note.setTime(mTimer.getText().toString());
            note.setNikName(mNikOfUser.getText().toString());
            note.setMoves(numberOfMoves);
            note.setCodes(numberOfCodedDigits);
            note.setUserUrlPhoto(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl());

            ContentValues cv = new ContentValues();

            cv.put(UserRecordsDB.ID, note.getDate());
            cv.put(UserRecordsDB.NIK_NAME, note.getNikName());
            cv.put(UserRecordsDB.MOVES, cntMoves - 1);
            cv.put(UserRecordsDB.CODES, DIG);
            cv.put(UserRecordsDB.TIME, note.getTime());
            cv.put(UserRecordsDB.USER_PHOTO_URL, note.getUserUrlPhoto());

            new DBOperations().insert(UserRecordsDB.TABLE, cv);
            Cursor cursor = new DBOperations().query();
            Log.d("MyLogs", String.valueOf(cursor.getCount()));
            int movesIndex = cursor.getColumnIndex(UserRecordsDB.MOVES);
            int timeIndex = cursor.getColumnIndex(UserRecordsDB.TIME);
            int nikIndex = cursor.getColumnIndex(UserRecordsDB.NIK_NAME);
            while (cursor.moveToNext()){
                Log.d("MyLogs", cursor.getString(movesIndex) +
                        cursor.getString(nikIndex) + cursor.getString(timeIndex));
            }

            cursor.close();

            BestUserRecords recordForCheck = new BestUserRecords();
            recordForCheck.setCodes(note.getCodes());
            recordForCheck.setDate(note.getDate());
            recordForCheck.setMoves(note.getMoves());
            recordForCheck.setNikName(note.getNikName());
            recordForCheck.setTime(note.getTime());

            UserBaseManager userManager = new UserBaseManager();
            userManager.checkNewBestRecord(recordForCheck);

            new RecordAsyncTaskPost().execute(note);

            mWriteReadFile.writeInFile(numberOfMoves, mNikOfUser.getText().toString(), mTimer.getText().toString(), numberOfCodedDigits, this);
            mWriteReadFile.readFile(this);
            showWinFragment();
            submitStart();
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
                        LinearLayout layoutMain = findViewById(R.id.mainLyaout);
                        layoutMain.setBackgroundColor(Color.BLACK);
                        mInputNumberView.setTextColor(Color.WHITE);

                    } else {

                        LinearLayout layoutMain = findViewById(R.id.mainLyaout);
                        layoutMain.setBackgroundColor(Color.WHITE);
                        mInputNumberView.setTextColor(Color.BLACK);
                    }
                    DIG = Integer.parseInt(buf);
                    if (checkRestart != DIG && start) {
                        checkRestart = DIG;
                        TextView start7 = findViewById(R.id.start);
                        start7.setText(getResources().getString(R.string.START_GAME));
                        mInputNumberView.setText(mCodedNumber); // change this

                        mCodedNumber = "";
                        start = false;
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.WE_BACK_WITHOUT_CHANGE), Toast.LENGTH_LONG).show();
                }
            case REQUEST_CODE_CHOICE_LANGUAGE:
                if (resultCode == RESULT_OK) {
                    mLanguageLocale = data.getStringExtra(Constants.CODE_OF_LANGUAGE);
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
            TextView start2 = findViewById(R.id.start);
            start2.setText(getResources().getString(R.string.START_GAME));
            mInputNumberView.setText(mCodedNumber); //change this
//            cleanListView();
            mCodedNumber = "";
            start = false;
            mTimerTimer.cancel();
        }
    }

    public void startTimer() {
        mTimer.post(new Runnable() {

            @Override
            public void run() {
                new AnimationOfView().enteredView(mTimer);
            }
        });
        mStartGameTime = System.currentTimeMillis();
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
        ListView listOfMoves = findViewById(R.id.list_of_moves_list_view);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), mMoves, mNumbers, mBulls, mCows, mode);
        listOfMoves.setAdapter(customAdapter);
    }

//    public void saveNikName() {
//        if (mKeepPassword) {
//            mSaveNikName = getPreferences(MODE_PRIVATE);
//            SharedPreferences.Editor editor = mSaveNikName.edit();
//            editor.putString(SAVED_TEXT, mNikOfUser.getText().toString());
//            editor.putString(SAVED_PASSWORD, passwordOfUser);
//            editor.putBoolean(SAVED_KEEP_PASSWORD, mKeepPassword);
//            editor.putString(Constants.CODE_OF_LANGUAGE, mLanguageLocale);
//            editor.apply();
//        } else {
//            SharedPreferences.Editor editor = mSaveNikName.edit();
//            editor.putString(SAVED_TEXT, "");
//            editor.putString(SAVED_PASSWORD, "");
//            editor.putBoolean(SAVED_KEEP_PASSWORD, false);
//            editor.putString(Constants.CODE_OF_LANGUAGE, mLanguageLocale);
//            editor.apply();
//        }
//    }
//
//    public void loadNikName() {
//        mSaveNikName = getPreferences(MODE_PRIVATE);
//        if (mSaveNikName.getBoolean(SAVED_KEEP_PASSWORD, false)) {
//            String savedText = mSaveNikName.getString(SAVED_TEXT, DEFAULT_NIK_OF_USER);
//            mNikOfUser.setText(savedText);
//            passwordOfUser = mSaveNikName.getString(SAVED_PASSWORD, DEFAULT_PASSWORD_OF_USER);
//            mKeepPassword = mSaveNikName.getBoolean(SAVED_KEEP_PASSWORD, false);
//            mLanguageLocale = mSaveNikName.getString(Constants.CODE_OF_LANGUAGE, ENGLISH_LANGUAGE_COD);
//            LanguageLocale.setLocale(mLanguageLocale, MainActivity.this);
//            initializationOfView();
//        }
//    }

    public void showWinFragment() {
        mFrameLayout.setVisibility(View.VISIBLE);
        new AnimationOfView().enteredView(mFrameLayout);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(R.id.win_container, mWinFragment);
        mTransaction.commit();
        mFragmentManager.executePendingTransactions();
        startService(new Intent(this, WinSoundService.class));
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.vibrate(500);
        }
        mWinFragment.setWinMessage(UserLoginHolder.getInstance().getUserName(), DIG, cntMoves - 1, mTimer.getText().toString());
    }

    public void closeWinFragment(View view) {
        mFrameLayout.setVisibility(View.INVISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.remove(mWinFragment);
        mTransaction.commit();
        stopService(new Intent(this, WinSoundService.class));
    }
}
