package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.Tables;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.services.WinSoundService;
import com.example.notepad.bullsandcows.ui.activity.adapters.MovesListCustomAdapter;
import com.example.notepad.bullsandcows.ui.activity.fragments.EditProfileFragment;
import com.example.notepad.bullsandcows.ui.activity.fragments.UsersRatingFragment;
import com.example.notepad.bullsandcows.ui.activity.fragments.WinFragment;
import com.example.notepad.bullsandcows.ui.activity.listeners.CloseEditProfileListener;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.animation.AnimationOfView;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.bullsandcows.utils.converters.QueryConverterUtil;
import com.example.notepad.bullsandcows.utils.logic.RandomNumberGenerator;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import kiolk.com.github.pen.Pen;

import static com.example.notepad.bullsandcows.utils.Constants.BACK_EPOCH_TIME_NOTATION;
import static com.example.notepad.bullsandcows.utils.Constants.EMPTY_STRING;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String INPUT_NUMBER = "InputNumber";
    public static final String DEFAULT_VLUE_FOR_STRING = "Error";
    public static final String CODED_NUMBER = "codedNumber";
    //    public static final String USER_NAME = "userName";
    public static final String START_STATE = "startState";
    public static final String MOVES_ARRAY_LIST = "movesArrayList";
    public static final String INPUTTED_NUMBER_ARRAY_LIST = "inputtedNumberArrayList";
    public static final String NUMBER_OF_BULLS_ARRAY_LIST = "numberOfBullsArrayList";
    public static final String NUMBER_OF_COWS_ARRAY_LIST = "numberOfCowsArrayList";
    public static final String COUNT_OF_MOVES = "countOfMoves";
    public static final String TIMER_OF_MOVES = "timerOfMoves";
    public static final String NUMBER_OF_CODED_DIGITS = "numberOfCodedDigits";
    public static final int SETTING_REQUEST_CODE = 1;
    public static final String START_TIME_KEY = "StartTime";
    public static final int VIBRATION_MILLISECONDS = 500;
    public static final String TIMESTAMP_MM_SS = "mm:ss";

    private ArrayList<String> mMoves = new ArrayList<>();
    private ArrayList<String> mNumbers = new ArrayList<>();
    private ArrayList<String> mCows = new ArrayList<>();
    private ArrayList<String> mBulls = new ArrayList<>();

    private TextView mInputNumberView;
    private TextView startButton;
    private TextView mTimer;
    private TextView mNikUserBar;
    private TextView mDayRating;
    private TextView mCodedNumberTitle;

    private FrameLayout mFrameLayout;
    private FrameLayout mEditInfoFrameLayout;

    public static int DIG = 4;
    private String mCodedNumber = "";
    private int cntMoves = 1;
    private boolean start = false;
    private boolean mode;
    private int checkRestart = DIG;
    private long mTimerCount = 0;
    private Timer mTimerTimer = null;
    private WinFragment mWinFragment;
    private UsersRatingFragment mRatingFragment;
    private EditProfileFragment mEditProfileFragment;
    private FragmentTransaction mTransaction;
    private long mStartGameTime;
    private FragmentManager mFragmentManager;
    private CloseEditProfileListener mCloseEditListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        UserLoginHolder.getInstance().setUserOnline();

        initToolBar();
        initializationOfView();
        initEditProfileFragment();
        intiUserBar();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INPUT_NUMBER, mInputNumberView.getText().toString());
        outState.putString(CODED_NUMBER, mCodedNumber);
//        outState.putString(USER_NAME, mNikOfUser.getText().toString());
        outState.putBoolean(START_STATE, start);
        outState.putStringArrayList(MOVES_ARRAY_LIST, mMoves);
        outState.putStringArrayList(INPUTTED_NUMBER_ARRAY_LIST, mNumbers);
        outState.putStringArrayList(NUMBER_OF_BULLS_ARRAY_LIST, mBulls);
        outState.putStringArrayList(NUMBER_OF_COWS_ARRAY_LIST, mCows);
        outState.putInt(COUNT_OF_MOVES, cntMoves);
        outState.putLong(TIMER_OF_MOVES, mTimerCount);
        outState.putLong(START_TIME_KEY, mStartGameTime);
        outState.putInt(NUMBER_OF_CODED_DIGITS, DIG);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInputNumberView.setText(savedInstanceState.getString(INPUT_NUMBER, DEFAULT_VLUE_FOR_STRING));
        mCodedNumber = savedInstanceState.getString(CODED_NUMBER, DEFAULT_VLUE_FOR_STRING);
//        mNikOfUser.setText(savedInstanceState.getString(USER_NAME, DEFAULT_VLUE_FOR_STRING));
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
        TextView del = findViewById(R.id.buttomDel);
//        TextView mCodOfLanguage = findViewById(R.id.language_cod_text_view);

        startButton = findViewById(R.id.start);

        mInputNumberView = findViewById(R.id.editText);
        mInputNumberView.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.BLACKGROTESKC));

        mTimer = findViewById(R.id.timer_text_view);
        mTimer.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.DIGITAL_FONT));

        mWinFragment = new WinFragment();
        mRatingFragment = new UsersRatingFragment();
        mFrameLayout = findViewById(R.id.win_container);
        mEditInfoFrameLayout = findViewById(R.id.for_fragments_in_main_frame_layout);

        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);
        number0.setOnClickListener(this);
        enterButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
//        mOptionMenu.setOnClickListener(this);
        del.setOnClickListener(this);
//        mCodOfLanguage.setOnClickListener(this);
    }

    private void initToolBar() {
        final Toolbar mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
//        mToolBar.setliste

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(UserLoginHolder.getInstance().getUserName());
//            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.main_collapsing_layout);
//            collapsingToolbarLayout.setTitle(UserLoginHolder.getInstance().getUserName());
//            ImageView appBarImage = findViewById(R.id.image_collapsing_layout);
//            if (UserLoginHolder.getInstance().getUserInfo()!= null) {
//                Pen.getInstance().getImageFromUrl(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl()).inputTo(appBarImage);
//            }
//                GetBitmapCallback getBitmapCallback = new GetBitmapCallback() {
//                    @Override
//                    public Bitmap getBitmap(Bitmap pBitmapFromLoader) {
////                    mToolBar.setLogo(new BitmapDrawable(getResources(), pBitmapFromLoader));
//////                    mToolBar.setLogo(R.drawable.ic_cow_good);
//                        //TODO set userPhoto in toolBar. How Set additional views in tool bar?
//                        mToolBar.findViewById(R.id.user_photo_tool_bar_image_view).setBackground(new BitmapDrawable(getResources(), pBitmapFromLoader));
////                    mToolBar.findViewById(R.id.user_nik_tool_bar_text_view).des
////     getSupportActionBar().setLogo(new BitmapDrawable(getResources(), pBitmapFromLoader));
//                        return null;
//                    }
//                };
////
////                Pen.getInstance().getImageFromUrl(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl())
////                        .getBitmapDirect(getBitmapCallback);
//            }
        }
//        setSupportActionBar(mToolBar);
    }

    private void intiUserBar() {
        ImageView mUserPhoto = findViewById(R.id.photo_user_bar_image_view);
        mNikUserBar = findViewById(R.id.nik_user_bar_text_view);
        mDayRating = findViewById(R.id.day_rating_user_bar_text_view);
        mDayRating.setOnClickListener(this);
        mCodedNumberTitle = findViewById(R.id.coded_title_user_bar_text_vie);

        if (UserLoginHolder.getInstance().getUserImageUrl() != null) {
            Pen.getInstance().getImageFromUrl(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl()).inputTo(mUserPhoto);
        }

        mNikUserBar.setText(UserLoginHolder.getInstance().getUserName());
        getUserDayRate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLoginHolder.getInstance().setOffline();

        if (mTimerTimer != null) {
            mTimerTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mEditProfileFragment.isVisible()) {
            UserLoginHolder.getInstance().setOffline();
            super.onBackPressed();
        } else {
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
        Intent intent;

        switch (item.getItemId()) {
            case R.id.rules_item_menu:
                intent = new Intent(this, RulesPageActivity.class);
                startActivity(intent);
                break;
            case R.id.settings_item_menu:
                intent = new Intent(this, SettingActivity.class);
                intent.putExtra(Constants.MODE_STATE, mode);
                intent.putExtra(Constants.CODED_DIGITS, DIG);
                startActivityForResult(intent, SETTING_REQUEST_CODE);
                break;
            case R.id.about_item_menu:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.login_page_item_menu:
                intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.record_item_menu:
                intent = new Intent(this, RecordsCardActivityFromCursorLoaderActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_profile_menu:
                showEditProfileFragment();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditProfileFragment() {
        mEditInfoFrameLayout.setVisibility(View.VISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(R.id.for_fragments_in_main_frame_layout, mEditProfileFragment);
        mTransaction.commit();
        mFragmentManager.executePendingTransactions();
        mEditProfileFragment.editUserProfile();
    }

    public void getNumber() {
        String inputNumber = mInputNumberView.getText().toString();
        mNumbers.add(inputNumber);
        mMoves.add(String.valueOf(cntMoves));
        mBulls.add(String.valueOf(new RandomNumberGenerator().checkNumberOfBulls(mCodedNumber, inputNumber)));
        mCows.add(String.valueOf(new RandomNumberGenerator().checkNumberOfCows(mCodedNumber, inputNumber)));
        ++cntMoves;
    }

    public void checkNumberForWin() {
        int numberOfBulls = new RandomNumberGenerator().checkNumberOfBulls(mCodedNumber, mInputNumberView.getText().toString());

        if (numberOfBulls == DIG) {
            mInputNumberView.setText(R.string.WON_MESSAGE);
            mTimerTimer.cancel();

            RecordsToNet note = new RecordsToNet();

            note.setDate(BACK_EPOCH_TIME_NOTATION - System.currentTimeMillis());
            note.setTime(mTimer.getText().toString());
            note.setNikName(mNikUserBar.getText().toString());
            note.setMoves(String.valueOf(cntMoves - 1));
            note.setCodes(String.valueOf(DIG));

            if (UserLoginHolder.getInstance().getUserInfo() != null) {
                note.setUserUrlPhoto(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl());
            }

            ContentValues cv = ModelConverterUtil.fromRecordToNetToCv(note);

            if (CheckConnection.checkConnection(this)) {
                cv.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.UPDATE_ONLINE_HACK);
            } else {
                cv.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.NOT_UPDATE_ONLINE_HACK);
            }

//            new DBOperations().insert(UserRecordsDB.TABLE, cv);
            getContentResolver().insert(RecordsContentProvider.CONTENT_URI, cv);

            BestUserRecords recordForCheck = ModelConverterUtil.fromRecordToNetToBestUserRecords(note);

            if (UserLoginHolder.getInstance().isLogged()) {

                new UserBaseManager().checkNewBestRecord(recordForCheck);
//                new RecordAsyncTaskPost().execute(note);
                RecordsManager recordsManager = new RecordsManager();
                recordsManager.postRecordOnBackend(note, null);
            }

            showWinFragment();
            submitStart();
            getUserDayRate();

            startService(new Intent(this, WaiterNewRecordsService.class));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String buf = data.getStringExtra(Constants.CODED_DIGITS);
                    mode = data.getBooleanExtra(Constants.MODE_STATE, mode);
                    createListViewWithMoves();
                    DIG = Integer.parseInt(buf);
                    getUserDayRate();
                    if (checkRestart != DIG && start) {
                        checkRestart = DIG;
                        TextView start7 = findViewById(R.id.start);
                        start7.setText(getResources().getString(R.string.START_GAME));
                        mInputNumberView.setText(mCodedNumber); // change this

                        mCodedNumber = EMPTY_STRING;
                        start = false;
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.WE_BACK_WITHOUT_CHANGE), Toast.LENGTH_LONG).show();
                }
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
            mInputNumberView.setText(Constants.EMPTY_STRING);
        } else {
            TextView start2 = findViewById(R.id.start);
            start2.setText(getResources().getString(R.string.START_GAME));
            mInputNumberView.setText(mCodedNumber);
            mCodedNumber = Constants.EMPTY_STRING;
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
        mTimer.setText(Constants.EMPTY_STRING);
        mTimerTimer = new Timer();

        mTimerTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Date pDate = new Date(mTimerCount * 1000);
//                        Locale locale = new Locale(Locale.ENGLISH);
                        DateFormat format = new SimpleDateFormat(TIMESTAMP_MM_SS, Locale.ENGLISH);
//                        DateFormat format = getData //new SimpleDateFormat(TIMESTAMP_MM_SS);
                        String formatedTime = format.format(pDate);
                        mTimer.setText(String.valueOf(formatedTime));
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
        MovesListCustomAdapter movesListCustomAdapter = new MovesListCustomAdapter(getApplicationContext(), mMoves, mNumbers, mBulls, mCows, mode);
        listOfMoves.setAdapter(movesListCustomAdapter);
    }

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
            v.vibrate(VIBRATION_MILLISECONDS);
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

    private void showRatingFragment(){
        mFrameLayout.setVisibility(View.VISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(R.id.win_container, mRatingFragment);
        mTransaction.commit();
        mFragmentManager.executePendingTransactions();
        mRatingFragment.showRating(DIG);
    }

    private void closeRatingFragment() {
        mFrameLayout.setVisibility(View.INVISIBLE);
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.remove(mRatingFragment);
        mTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        String buf;
        buf = mInputNumberView.getText().toString();
        switch (v.getId()) {
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
                closeWinFragment(null);
                break;
            case R.id.day_rating_user_bar_text_view:
                if(mRatingFragment.isVisible()){
                    closeRatingFragment();
                }else {
                    showRatingFragment();
                }
                break;
            default:
                break;
        }
    }

    private void getUserDayRate() {
//        String[] request = new String[]{EMPTY_STRING, String.valueOf(DIG), Tables.LAST_DAY};
        String sortOrder = UserRecordsDB.MOVES + Tables.ASC + ", " + UserRecordsDB.TIME + Tables.ASC;
        int position = 0;
        boolean hasResult = false;

        HashMap<String, String> selectionArgs = new HashMap<>();
        selectionArgs.put(UserRecordsDB.NIK_NAME, EMPTY_STRING);
        selectionArgs.put(UserRecordsDB.CODES, String.valueOf(DIG));
        selectionArgs.put(UserRecordsDB.ID, Tables.LAST_DAY);

//        Cursor cursor = getContentResolver().query(RecordsContentProvider.CONTENT_URI,
//                null, null, request, sortOrder);
        QuerySelectionArgsModel readySelection = QueryConverterUtil.convertSelectionArg(selectionArgs);

        Cursor cursor = getContentResolver().query(RecordsContentProvider.CONTENT_URI,
                null, readySelection.getSelection(), readySelection.getSelectionArgs(), sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(UserRecordsDB.NIK_NAME))
                        .equals(UserLoginHolder.getInstance().getUserName())) {
                    ++position;
                    hasResult = true;
                    break;

                }
                ++position;
            } while (!cursor.getString(cursor.getColumnIndex(UserRecordsDB.NIK_NAME))
                    .equals(UserLoginHolder.getInstance().getUserName()) && !cursor.isLast() && cursor.moveToNext());
        } else {
            position = 0;
        }

        if (cursor != null && cursor.getCount() == position && !hasResult) {
            position = 0;
        }

        if (cursor != null) {
            cursor.close();
        }

        String codedTitle = getString(R.string.CODED) + String.valueOf(DIG);
        mCodedNumberTitle.setText(codedTitle);
        if (position == 0) {
            mDayRating.setText(R.string.DASH);
        } else {
            mDayRating.setText(String.valueOf(position));
        }
    }
}
