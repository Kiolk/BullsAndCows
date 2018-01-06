package com.example.notepad.bullsandcows.utils.logic;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.models.UserMoveModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import java.util.List;
import java.util.Timer;

import static com.example.notepad.bullsandcows.utils.Constants.BACK_EPOCH_TIME_NOTATION;

public class Game {

    public interface WinWaiter{
        void onWinCallback();
    }

    private static final int S_X_OFFSET = 0;
    private static final int S_Y_OFFSET = 0;
    private static final String TIMESTAMP_MM_SS = "mm:ss";

    private final Context mContext;
    private int mNumberCodedDigits;
    private int mTimerCount;
    private int mCountMoves;
    private long mStartGameTime;
    private String mCodedNumber;
    private TextView mTimerView;

    private GameTimer mGameTimer;
    private RandomNumberGenerator mRandomNumberGenerator;
    private Timer mTimer;
    private WinWaiter mWinWaiter;

    public Game(final Context pContext, final TextView pTimerView, WinWaiter pWinCallback) {
        mContext = pContext;
        mTimerView = pTimerView;
        mRandomNumberGenerator = new RandomNumberGenerator();
        mWinWaiter = pWinCallback;
    }

    public String startGame(final int pNumberCodedDigits) {
        mNumberCodedDigits = pNumberCodedDigits;
        mCodedNumber = new RandomNumberGenerator().generateRandomNumber(mNumberCodedDigits);

        mCountMoves = 0;
        sendToast();
        startTimer();

        return mCodedNumber;
    }

    public void cancelGame() {
        mGameTimer.cancelTimer();
        mCountMoves = 0;
    }

    public void checkNumber(final String pInputNumber, List<UserMoveModel> pUserMoves) {
        mCountMoves++;

        final UserMoveModel newMove = new UserMoveModel();
        newMove.setInputNumber(pInputNumber);
        newMove.setMove(String.valueOf(mCountMoves));
        int numberOfBulls = mRandomNumberGenerator.checkNumberOfBulls(mCodedNumber, pInputNumber);
        newMove.setBulls(String.valueOf(numberOfBulls));
        newMove.setCows(String.valueOf(mRandomNumberGenerator.checkNumberOfCows(mCodedNumber, pInputNumber)));

        pUserMoves.add(newMove);

        if(numberOfBulls == mNumberCodedDigits){
            submitResultInBD();
            mWinWaiter.onWinCallback();
        }
    }

    private void submitResultInBD() {

        RecordsToNet note = new RecordsToNet();

        note.setDate(BACK_EPOCH_TIME_NOTATION - System.currentTimeMillis());
        note.setTime(mGameTimer.getWinTime());
        note.setNikName(UserLoginHolder.getInstance().getUserName());
        note.setMoves(String.valueOf(mCountMoves));
        note.setCodes(String.valueOf(mNumberCodedDigits));

        if (UserLoginHolder.getInstance().getUserInfo() != null) {
            note.setUserUrlPhoto(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl());
        }

        ContentValues contentValues = ModelConverterUtil.fromRecordToNetToCv(note);

        if (CheckConnection.checkConnection(mContext)) {
            contentValues.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.UPDATE_ONLINE_HACK);
        } else {
            contentValues.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.NOT_UPDATE_ONLINE_HACK);
        }

        mContext.getContentResolver().insert(RecordsContentProvider.CONTENT_URI, contentValues);

        BestUserRecords recordForCheck = ModelConverterUtil.fromRecordToNetToBestUserRecords(note);

        if (UserLoginHolder.getInstance().isLogged()) {

            new UserBaseManager().checkNewBestRecord(recordForCheck);
            RecordsManager recordsManager = new RecordsManager();
            recordsManager.postRecordOnBackend(note, null);
        }

        mContext.startService(new Intent(mContext, WaiterNewRecordsService.class));
//        cancelGame();
    }

    private void sendToast() {
        final String message = mContext.getResources().getString(R.string.ENTER_NUMBER) +
                mNumberCodedDigits +
                mContext.getResources().getString(R.string.TOAST_MESSAGE_WITH_RULE);

        final Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, S_X_OFFSET, S_Y_OFFSET);
        toast.show();
    }

    private void startTimer() {
        mGameTimer = new GameTimer();
        mGameTimer.startTimer(new GameTimer.Callback() {

            @Override
            public void refreshTimer(final String pNewTime) {
                mTimerView.setText(pNewTime);
            }
        });
    }
}
