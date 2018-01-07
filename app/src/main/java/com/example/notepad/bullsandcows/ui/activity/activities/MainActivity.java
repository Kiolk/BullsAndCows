package com.example.notepad.bullsandcows.ui.activity.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.models.UserMoveModel;
import com.example.notepad.bullsandcows.services.WinSoundService;
import com.example.notepad.bullsandcows.ui.activity.adapters.MovesRecyclerViewAdapter;
import com.example.notepad.bullsandcows.ui.activity.fragments.EditProfileFragment;
import com.example.notepad.bullsandcows.ui.activity.fragments.UsersRatingFragment;
import com.example.notepad.bullsandcows.ui.activity.fragments.WinFragment;
import com.example.notepad.bullsandcows.ui.activity.listeners.CloseEditProfileListener;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.animation.AnimationOfView;
import com.example.notepad.bullsandcows.utils.animation.SlideAnimationUtil;
import com.example.notepad.bullsandcows.utils.logic.Game;
import com.example.notepad.bullsandcows.utils.logic.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

import kiolk.com.github.pen.Pen;

import static com.example.notepad.bullsandcows.utils.Constants.EMPTY_STRING;

//TODO should be splited on small classes - single responsibility principle
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String INPUT_NUMBER = "InputNumber";
    public static final String DEFAULT_VALUE_FOR_STRING = "Error";
    public static final String CODED_NUMBER = "codedNumber";
    public static final String START_STATE = "startState";
    public static final String NUMBER_OF_CODED_DIGITS = "numberOfCodedDigits";
    public static final int SETTING_REQUEST_CODE = 1;
    public static final int VIBRATION_MILLISECONDS = 500;

    //TODO List/ArrayList
    private List<UserMoveModel> mUserMoves;

    private TextView mInputNumberView;
    private TextView startButton;
    private TextView mTimer;
    private TextView mNikUserBar;
    private TextView mDayRating;
    private TextView mCodedNumberTitle;

    private RecyclerView mUserMovesRecycler;
    private MovesRecyclerViewAdapter mMovesRecyclerAdapter;

    private FrameLayout mFrameLayout;
    private FrameLayout mEditInfoFrameLayout;

    public static int DIG = 4;
    private String mCodedNumber = "";
    private boolean start = false;
    private int checkRestart = DIG;
    private WinFragment mWinFragment;
    private UsersRatingFragment mRatingFragment;
    private EditProfileFragment mEditProfileFragment;
    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;
    private CloseEditProfileListener mCloseEditListener;
    private Game.WinWaiter mWinWaiter;
    private Game mGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        UserLoginHolder.getInstance().setUserOnline();
        mUserMoves = new ArrayList<>();
        initToolBar();
        initializationOfView();
        initEditProfileFragment();
        initWinCallback();
        mGame = new Game(this, mTimer, mWinWaiter);
        intiUserBar();
        setupRecyclerView();
    }

    private void initEditProfileFragment() {
        mEditProfileFragment = new EditProfileFragment();
        mCloseEditListener = new CloseEditProfileListener() {
            @Override
            public void onCloseFragment() {

                closeFragment(mEditProfileFragment);
//                mTransaction = getFragmentManager().beginTransaction();
//                mTransaction.remove(mEditProfileFragment);
//                mTransaction.commit();
//                mFragmentManager.executePendingTransactions();
//                mEditInfoFrameLayout.setVisibility(View.GONE);
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
        outState.putBoolean(START_STATE, start);
        outState.putInt(NUMBER_OF_CODED_DIGITS, DIG);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInputNumberView.setText(savedInstanceState.getString(INPUT_NUMBER, DEFAULT_VALUE_FOR_STRING));
        //TODO put to Serializable / Parcelable
        mCodedNumber = savedInstanceState.getString(CODED_NUMBER, DEFAULT_VALUE_FOR_STRING);
        start = savedInstanceState.getBoolean(START_STATE, false);
        DIG = savedInstanceState.getInt(NUMBER_OF_CODED_DIGITS, 4);
    }

    public void initializationOfView() {
        //TODO refactor to tags
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

        startButton = findViewById(R.id.start);

        mUserMovesRecycler = findViewById(R.id.moves_recycler_view);

        mInputNumberView = findViewById(R.id.editText);
        SlideAnimationUtil.slideInFromLeft(this, mInputNumberView);
//        mInputNumberView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_from_right));
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
        del.setOnClickListener(this);
    }

    private void initToolBar() {
        final Toolbar mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(UserLoginHolder.getInstance().getUserName());
        }
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_bull_good2));
        View.OnClickListener navigationButton = new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                if (mRatingFragment.isVisible()) {
                    closeRatingFragment();
                } else {
                    showRatingFragment();
                }
            }
        };

        mToolBar.setNavigationOnClickListener(navigationButton);
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
    }

    @Override
    public void onBackPressed() {
        if (!mEditProfileFragment.isVisible()) {
            UserLoginHolder.getInstance().setOffline();
            super.onBackPressed();
        } else {
            mCloseEditListener.onCloseFragment();
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
//        mEditInfoFrameLayout.setVisibility(View.VISIBLE);
        showFragment(R.id.for_fragments_in_main_frame_layout, mEditProfileFragment);
//        mTransaction = getFragmentManager().beginTransaction();
//        mTransaction.add(R.id.for_fragments_in_main_frame_layout, mEditProfileFragment);
//        mTransaction.commit();
//        mFragmentManager.executePendingTransactions();
        mEditProfileFragment.editUserProfile();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String buf = data.getStringExtra(Constants.CODED_DIGITS);
                    DIG = Integer.parseInt(buf);
                    getUserDayRate();
                    if (checkRestart != DIG && start) {
                        checkRestart = DIG;
                        startButton.setText(getResources().getString(R.string.START_GAME));
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
            mCodedNumber = mGame.startGame(DIG);
            mUserMoves.clear();
            mMovesRecyclerAdapter.notifyDataSetChanged();
            startButton.setText(getResources().getString(R.string.SHOW_NUMBER));
            start = true;
            mInputNumberView.setText(Constants.EMPTY_STRING);
        } else {
            mGame.cancelGame();
            startButton.setText(getResources().getString(R.string.START_GAME));
            mInputNumberView.setText(mCodedNumber);
            mCodedNumber = Constants.EMPTY_STRING;
            start = false;
        }
    }

    public void showWinFragment() {
        mFrameLayout.setVisibility(View.VISIBLE);
        new AnimationOfView().enteredView(mFrameLayout);

        showFragment(R.id.win_container, mWinFragment);
//        mTransaction = getFragmentManager().beginTransaction();
//        mTransaction.add(R.id.win_container, mWinFragment);
//        mTransaction.commit();
//        mFragmentManager.executePendingTransactions();

        startService(new Intent(this, WinSoundService.class));
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (v != null) {
            v.vibrate(VIBRATION_MILLISECONDS);
        }

        mWinFragment.setWinMessage(mGame.getWinMessage());
    }

    public void closeWinFragment(View view) {
        mFrameLayout.setVisibility(View.INVISIBLE);

        closeFragment(mWinFragment);
//        mTransaction = getFragmentManager().beginTransaction();
//        mTransaction.remove(mWinFragment);
//        mTransaction.commit();

        stopService(new Intent(this, WinSoundService.class));
    }

    private void showRatingFragment() {
        mEditInfoFrameLayout.setPadding(0, 0, 100, 0);
        SlideAnimationUtil.slideInFromLeft(this, mEditInfoFrameLayout);
        mFrameLayout.setVisibility(View.VISIBLE);
        showFragment(R.id.for_fragments_in_main_frame_layout, mRatingFragment);
//        mTransaction = getFragmentManager().beginTransaction();
//        mTransaction.add(R.id.for_fragments_in_main_frame_layout, mRatingFragment);
//        mTransaction.commit();
//        mFragmentManager.executePendingTransactions();
        mRatingFragment.showRating(DIG);
    }

    private void closeRatingFragment() {
        SlideAnimationUtil.slideOutToLeft(this, mEditInfoFrameLayout);
        mFrameLayout.setVisibility(View.INVISIBLE);
//        mTransaction = getFragmentManager().beginTransaction();
//        mTransaction.remove(mRatingFragment);
//        mTransaction.commit();
//        closeFragment(mRatingFragment);
        mEditInfoFrameLayout.setPadding(0, 0, 0, 0);
    }

    private void showFragment(final int pContainer, final Fragment pFragment) {
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(pContainer, pFragment);
        mTransaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    private void closeFragment(final Fragment pFragment) {
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.remove(pFragment);
        mTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        String buf;
        buf = mInputNumberView.getText().toString();
        //TODO buf += Integer.valueOf(v.getTag().toString());
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
                        mGame.checkNumber(mInputNumberView.getText().toString(), mUserMoves, mMovesRecyclerAdapter);
                        mInputNumberView.setText("");
                    } else {
                        Context context = getApplicationContext();
                        String message = getResources().getString(R.string.ENTER_NUMBER) + DIG + getResources().getString(R.string.TOAST_MESSAGE_WITH_RULE);
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, message, duration);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }
                break;
            case R.id.start:
                submitStart();
                closeWinFragment(null);
                break;
            case R.id.day_rating_user_bar_text_view:
                if (mRatingFragment.isVisible()) {
                    closeRatingFragment();
                } else {
                    showRatingFragment();
                }
                break;
            default:
                break;
        }
    }

    private void getUserDayRate() {
        String codedTitle = getString(R.string.CODED) + String.valueOf(DIG);
        mCodedNumberTitle.setText(codedTitle);
        int position = mGame.calculateUserRating(DIG);
        if (position == 0) {
            mDayRating.setText(R.string.DASH);
        } else {
            mDayRating.setText(String.valueOf(position));
        }
    }

    private void initWinCallback() {
        mWinWaiter = new Game.WinWaiter() {

            @Override
            public void onWinCallback() {
                mInputNumberView.setText(R.string.WON_MESSAGE);
                showWinFragment();
                //TODO name is not clear
                submitStart();
                //TODO rename
                getUserDayRate();
            }
        };
    }

    public void setupRecyclerView(){
        mMovesRecyclerAdapter = new MovesRecyclerViewAdapter(mUserMoves);
        mUserMovesRecycler.setHasFixedSize(false);
        mUserMovesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true ));
        mUserMovesRecycler.setAdapter(mMovesRecyclerAdapter);
    }
}
