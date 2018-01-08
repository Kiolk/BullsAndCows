package com.example.notepad.bullsandcows.ui.activity.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private static final String INPUT_NUMBER = "InputNumber";
    private static final String DEFAULT_VALUE_FOR_STRING = "Error";
    private static final String CODED_NUMBER = "codedNumber";
    private static final String START_STATE = "startState";
    private static final String NUMBER_OF_CODED_DIGITS = "numberOfCodedDigits";
    private static final int SETTING_REQUEST_CODE = 1;
    private static final int VIBRATION_MILLISECONDS = 500;
    private static final String BACKGROUND_PICTURE_URL = "https://i.pinimg.com/736x/f0/f6/56/f0f656e5f331aa25ba8dd38447435be4--cow-pics-highland-cattle.jpg";
    private static final int DEFAULT_NUMBER_CODED_DIGITS = 4;

    private int DIG = DEFAULT_NUMBER_CODED_DIGITS;
    private int checkRestart = DIG;

    private boolean start;

    private String mCodedNumber = EMPTY_STRING;

    private List<UserMoveModel> mUserMoves;

    private TextView mInputNumberView;
    private TextView mTimer;
    private TextView mCodedNumberTitle;
    private TextView mStartTextView;
    private TextView mActualRating;
    private TextView mDailyRating;

    private LinearLayout mButtonsLayout;
    private LinearLayout mInputLayout;

    private RecyclerView mUserMovesRecycler;
    private MovesRecyclerViewAdapter mMovesRecyclerAdapter;

    private Game mGame;
    private Game.WinWaiter mWinWaiter;

    private FrameLayout mFrameLayout;
    private FrameLayout mEditInfoFrameLayout;

    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;

    private WinFragment mWinFragment;
    private UsersRatingFragment mRatingFragment;
    private EditProfileFragment mEditProfileFragment;

    private CloseEditProfileListener mCloseEditListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        mUserMoves = new ArrayList<>();

        initToolBar();
        initializationOfView();
        initEditProfileFragment();
        initWinCallback();

        mGame = new Game(this, mTimer, mWinWaiter, mActualRating);

        setUserRating();
        setupRecyclerView();
    }

    private void initEditProfileFragment() {
        mEditProfileFragment = new EditProfileFragment();
        mCloseEditListener = new CloseEditProfileListener() {

            @Override
            public void onCloseFragment() {
                SlideAnimationUtil.slideOutToRight(MainActivity.this, mEditInfoFrameLayout, new SlideAnimationUtil.SlideAnimationListener() {

                    @Override
                    public void animationEnd() {
                        closeFragment(mEditProfileFragment);
                    }
                }, SlideAnimationUtil.NORMAL);
            }
        };
        mEditProfileFragment.setCloseListener(mCloseEditListener);
        mFragmentManager = getFragmentManager();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INPUT_NUMBER, mInputNumberView.getText().toString());
        outState.putString(CODED_NUMBER, mCodedNumber);
        outState.putBoolean(START_STATE, start);
        outState.putInt(NUMBER_OF_CODED_DIGITS, DIG);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInputNumberView.setText(savedInstanceState.getString(INPUT_NUMBER, DEFAULT_VALUE_FOR_STRING));
        //TODO put to Serializable / Parcelable
        mCodedNumber = savedInstanceState.getString(CODED_NUMBER, DEFAULT_VALUE_FOR_STRING);
        start = savedInstanceState.getBoolean(START_STATE, false);
        DIG = savedInstanceState.getInt(NUMBER_OF_CODED_DIGITS, 4);
    }

    public void initializationOfView() {

        final ImageView backgroundImage = findViewById(R.id.background_picture_image_view);
        Pen.getInstance().getImageFromUrl(BACKGROUND_PICTURE_URL).inputTo(backgroundImage);
        mActualRating = findViewById(R.id.actual_rating_position_text_view);
        mDailyRating = findViewById(R.id.daily_rating_position_text_view);

        mCodedNumberTitle = findViewById(R.id.coded_title_user_bar_text_vie);

        mStartTextView = findViewById(R.id.start_game_text_view);
        SlideAnimationUtil.slideInFromRight(this, mStartTextView, null, SlideAnimationUtil.NORMAL);

        mButtonsLayout = findViewById(R.id.buttons_block_linear_layout);
        mInputLayout = findViewById(R.id.input_number_linear_layout);

        mUserMovesRecycler = findViewById(R.id.moves_recycler_view);

        mInputNumberView = findViewById(R.id.editText);
        mInputNumberView.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.BLACKGROTESKC));

        mTimer = findViewById(R.id.time_text_view);
        mTimer.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.DIGITAL_FONT));

        mWinFragment = new WinFragment();
        mRatingFragment = new UsersRatingFragment();
        mFrameLayout = findViewById(R.id.win_container);
        mEditInfoFrameLayout = findViewById(R.id.for_fragments_in_main_frame_layout);
    }

    private void initToolBar() {
        final Toolbar mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(UserLoginHolder.getInstance().getUserName());
        }
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_supervisor_account_black_24px, null));
        final View.OnClickListener navigationButton = new View.OnClickListener() {

            @Override
            public void onClick(final View pView) {
                if (mRatingFragment.isVisible()) {
                    closeRatingFragment();
                } else {
                    showRatingFragment();
                }
            }
        };

        mToolBar.setNavigationOnClickListener(navigationButton);
    }

    @Override
    public void onBackPressed() {
        if (!mEditProfileFragment.isVisible()) {
            super.onBackPressed();
        } else {
            mCloseEditListener.onCloseFragment();
        }
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        UserLoginHolder.getInstance().keepUserOnline();
        final Intent intent;

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
                intent = new Intent(this, WelcomeActivity.class);
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
        SlideAnimationUtil.slideInFromRight(this, mEditInfoFrameLayout, null, SlideAnimationUtil.NORMAL);
        showFragment(R.id.for_fragments_in_main_frame_layout, mEditProfileFragment);
        mEditProfileFragment.editUserProfile();
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case SETTING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    final String buf = data.getStringExtra(Constants.CODED_DIGITS);
                    DIG = Integer.parseInt(buf);
                    setUserRating();

                    if (checkRestart != DIG && start) {
                        checkRestart = DIG;
                        mStartTextView.setText(getResources().getString(R.string.START_GAME));
                        mInputNumberView.setText(mCodedNumber);
                        mCodedNumber = EMPTY_STRING;
                        start = false;
                    }
                } else {
                    Snackbar.make(mEditInfoFrameLayout, R.string.WE_BACK_WITHOUT_CHANGE, BaseTransientBottomBar.LENGTH_LONG).show();
                }
            default:
                break;
        }
    }

    public void startGameEvent() {
        if (!start) {
            mCodedNumber = mGame.startGame(DIG);
            mUserMoves.clear();
            mMovesRecyclerAdapter.notifyDataSetChanged();
            mStartTextView.setText(getResources().getString(R.string.SHOW_NUMBER));
            start = true;
            mInputNumberView.setText(Constants.EMPTY_STRING);

            if (mButtonsLayout.getVisibility() == View.GONE) {
                mButtonsLayout.setVisibility(View.VISIBLE);
                mInputLayout.setVisibility(View.VISIBLE);
                SlideAnimationUtil.slideInToTop(this, mButtonsLayout, null, SlideAnimationUtil.NORMAL);
                SlideAnimationUtil.slideInFromLeft(this, mInputLayout, null, SlideAnimationUtil.NORMAL);
            }

        } else {
            mGame.cancelGame();
            mStartTextView.setText(getResources().getString(R.string.START_GAME));
            mInputNumberView.setText(mCodedNumber);
            mCodedNumber = Constants.EMPTY_STRING;
            start = false;
        }
    }

    public void showWinFragment() {
        mFrameLayout.setVisibility(View.VISIBLE);
        new AnimationOfView().enteredView(mFrameLayout);

        showFragment(R.id.win_container, mWinFragment);

        startService(new Intent(this, WinSoundService.class));
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_MILLISECONDS, 0));
        }

        mWinFragment.setWinMessage(mGame.getWinMessage());
    }

    public void closeWinFragment(final View view) {
        mFrameLayout.setVisibility(View.INVISIBLE);

        closeFragment(mWinFragment);

        stopService(new Intent(this, WinSoundService.class));
    }

    protected void showRatingFragment() {
        mEditInfoFrameLayout.setPadding(0, 0, 100, 0);

        SlideAnimationUtil.slideInFromLeft(this, mEditInfoFrameLayout, null, SlideAnimationUtil.FASTER);

        mFrameLayout.setVisibility(View.VISIBLE);
        showFragment(R.id.for_fragments_in_main_frame_layout, mRatingFragment);
        mRatingFragment.showRating(DIG);
    }

    protected void closeRatingFragment() {
        SlideAnimationUtil.slideOutToLeft(this, mEditInfoFrameLayout, new SlideAnimationUtil.SlideAnimationListener() {

            @Override
            public void animationEnd() {
                closeFragment(mRatingFragment);
                mEditInfoFrameLayout.setPadding(0, 0, 0, 0);
            }
        }, SlideAnimationUtil.FASTER);
        mFrameLayout.setVisibility(View.INVISIBLE);
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

    public void onClickButton(final View pView) {
        String buf = mInputNumberView.getText().toString();
        switch (pView.getId()) {
            case R.id.buttomDel:
                if (!buf.isEmpty()) {
                    buf = buf.substring(0, buf.length() - 1);
                    mInputNumberView.setText(buf);
                }
                break;
            case R.id.enter:
                if (start) {
                    if (new RandomNumberGenerator().checkNumberForCorrectInput(mInputNumberView.getText().toString(), DIG)) {
                        mGame.checkNumber(mInputNumberView.getText().toString(), mUserMoves, mMovesRecyclerAdapter);
                        mInputNumberView.setText(EMPTY_STRING);
                    } else {
                        mGame.sendToast();
                    }
                }
                break;
            case R.id.start_game_text_view:
                startGameEvent();
                closeWinFragment(null);
                break;
            default:
                final String tag = (String) pView.getTag();
                buf += tag;
                mInputNumberView.setText(buf);
                break;
        }
    }

    private void setUserRating() {
        mCodedNumberTitle.setText(String.valueOf(DIG));
        final int position = mGame.calculateUserRating(DIG);
        if (position == 0) {
            mDailyRating.setText(R.string.DASH);
        } else {
            mDailyRating.setText(String.valueOf(position));
        }
    }

    private void initWinCallback() {
        mWinWaiter = new Game.WinWaiter() {

            @Override
            public void onWinCallback() {
                mInputNumberView.setText(R.string.WON_MESSAGE);
                showWinFragment();
                startGameEvent();
                setUserRating();
            }
        };
    }

    public void setupRecyclerView() {
        mMovesRecyclerAdapter = new MovesRecyclerViewAdapter(mUserMoves);
        mUserMovesRecycler.setHasFixedSize(false);
        mUserMovesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mUserMovesRecycler.setAdapter(mMovesRecyclerAdapter);
    }
}
