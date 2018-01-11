package com.example.notepad.bullsandcows.utils.logic;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.Tables;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.RecordsManager;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.models.QuerySelectionArgsModel;
import com.example.notepad.bullsandcows.data.models.UserMoveModel;
import com.example.notepad.bullsandcows.data.providers.RecordsContentProvider;
import com.example.notepad.bullsandcows.services.WaiterNewRecordsService;
import com.example.notepad.bullsandcows.ui.activity.adapters.MovesRecyclerViewAdapter;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.converters.ModelConverterUtil;
import com.example.notepad.bullsandcows.utils.converters.QuerySelectionFormer;
import com.example.notepad.bullsandcows.utils.converters.TimeConvertersUtil;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.notepad.bullsandcows.utils.Constants.BACK_EPOCH_TIME_NOTATION;
import static com.example.notepad.bullsandcows.utils.Constants.EMPTY_STRING;

public class Game {

    private static final String FIRST_POSITION = "1";
    private List<RecordsToNet> mRatingList;

    public interface WinWaiter {

        void onWinCallback();
    }

    private static final int S_X_OFFSET = 0;
    private static final int S_Y_OFFSET = 0;

    private final Context mContext;
    private int mNumberCodedDigits;
    private int mCountMoves;
    private String mCodedNumber;
    private final TextView mTimerView;
    private final TextView mRatingPosition;

    private GameTimer mGameTimer;
    private final RandomNumberGenerator mRandomNumberGenerator;
    private final WinWaiter mWinWaiter;

    public Game(final Context pContext, final TextView pTimerView, final WinWaiter pWinCallback, final TextView pRatingPosition) {
        mContext = pContext;
        mTimerView = pTimerView;
        mRandomNumberGenerator = new RandomNumberGenerator();
        mWinWaiter = pWinCallback;
        mRatingPosition = pRatingPosition;
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

    public void checkNumber(final String pInputNumber, final List<UserMoveModel> pUserMoves, final MovesRecyclerViewAdapter pMovesAdapter) {
        mCountMoves++;

        final UserMoveModel newMove = new UserMoveModel();
        newMove.setInputNumber(pInputNumber);
        newMove.setMove(String.valueOf(mCountMoves));
        final int numberOfBulls = mRandomNumberGenerator.checkNumberOfBulls(mCodedNumber, pInputNumber);
        newMove.setBulls(String.valueOf(numberOfBulls));
        newMove.setCows(String.valueOf(mRandomNumberGenerator.checkNumberOfCows(mCodedNumber, pInputNumber)));

        pUserMoves.add(0, newMove);

        pMovesAdapter.notifyDataSetChanged();

        if (numberOfBulls == mNumberCodedDigits) {
            submitResultInBD();
            mWinWaiter.onWinCallback();
        }
    }

    private void submitResultInBD() {

        final RecordsToNet note = new RecordsToNet();

        note.setDate(BACK_EPOCH_TIME_NOTATION - System.currentTimeMillis());
        note.setTime(mGameTimer.getWinTime());
        note.setNikName(UserLoginHolder.getInstance().getUserName());
        note.setMoves(String.valueOf(mCountMoves));
        note.setCodes(String.valueOf(mNumberCodedDigits));

        if (UserLoginHolder.getInstance().getUserInfo() != null) {
            note.setUserUrlPhoto(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl());
        }

        final ContentValues contentValues = ModelConverterUtil.fromRecordToNetToCv(note);

        if (CheckConnection.checkConnection(mContext)) {
            contentValues.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.UPDATE_ONLINE_HACK);
        } else {
            contentValues.put(UserRecordsDB.IS_UPDATE_ONLINE, UserRecordsDB.NOT_UPDATE_ONLINE_HACK);
        }

        mContext.getContentResolver().insert(RecordsContentProvider.CONTENT_URI, contentValues);

        final BestUserRecords recordForCheck = ModelConverterUtil.fromRecordToNetToBestUserRecords(note);

        if (UserLoginHolder.getInstance().isLogged()) {

            new UserBaseManager().checkNewBestRecord(recordForCheck);
            final RecordsManager recordsManager = new RecordsManager();
            recordsManager.postRecordOnBackend(note, null);
        }

        mContext.startService(new Intent(mContext, WaiterNewRecordsService.class));
    }

    public void sendToast() {
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
                mRatingPosition.setText(getActualPosition(false));
            }
        });
    }

    public String getWinMessage() {
        return mContext.getResources().getString(R.string.CONGRATULATIONS) +
                UserLoginHolder.getInstance().getUserName() +
                mContext.getResources().getString(R.string.YOU_WIN) +
                mContext.getResources().getString(R.string.YOUR_RESULT) +
                mCountMoves +
                mContext.getResources().getString(R.string.WIN_TIME) +
                mGameTimer.getWinTime() +(Integer.parseInt(getActualPosition(true)) - 1);
    }

    public int calculateUserRating(final int pNumberCodedDigits) {
        //TODO move to separate class that works with DB.
        //example Operation with AsyncTask or Loader
//        String[] request = new String[]{EMPTY_STRING, String.valueOf(DIG), Tables.LAST_DAY};
        final String sortOrder = UserRecordsDB.MOVES + Tables.ASC + ", " + UserRecordsDB.TIME + Tables.ASC;
        int position = 0;
        boolean hasResult = false;

        //TODO clear all warnings Map vs HashMap, List vs ArrayList
        final Map<String, String> selectionArgs = new HashMap<>();
        selectionArgs.put(UserRecordsDB.NIK_NAME, EMPTY_STRING);
        selectionArgs.put(UserRecordsDB.CODES, String.valueOf(pNumberCodedDigits));
        selectionArgs.put(UserRecordsDB.ID, Tables.LAST_DAY);

//        Cursor cursor = getContentResolver().query(RecordsContentProvider.CONTENT_URI,
//                null, null, request, sortOrder);
        final QuerySelectionArgsModel readySelection = QuerySelectionFormer.convertSelectionArg(selectionArgs);

        final Cursor cursor = mContext.getContentResolver().query(RecordsContentProvider.CONTENT_URI,
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
            setRatingList(cursor);
        } else {
            position = 0;
            setRatingList(null);
        }

        if (cursor != null && cursor.getCount() == position && !hasResult) {
            position = 0;
        }

        if (cursor != null) {
            cursor.close();
        }
        return position;
    }

    private void setRatingList(final Cursor pCursor) {
        if (pCursor == null) {
            mRatingList = new ArrayList<>();
        } else {
            pCursor.moveToFirst();
            mRatingList = new ArrayList<>();
            do {
                mRatingList.add(UserRecordsDB.convertFromCursor(pCursor));
            } while (pCursor.moveToNext());
        }
    }

    private String getActualPosition(final boolean isWin) {

        if (mRatingList.isEmpty()) {
            return FIRST_POSITION;
        }

        final int countMoves;
        if (isWin) {
            countMoves = mCountMoves;
        } else {
            countMoves = mCountMoves + 1;
        }

        int actualPosition = 0;
        final int actualTime = TimeConvertersUtil.gameTimeToSeconds(mGameTimer.getWinTime());
        do {
            actualPosition++;
            if (countMoves == Integer.parseInt(mRatingList.get(actualPosition - 1).getMoves())) {
                if (actualTime < TimeConvertersUtil.gameTimeToSeconds(mRatingList.get(actualPosition - 1).getTime())) {
                    return String.valueOf(actualPosition);
                }
            } else if (countMoves < Integer.parseInt(mRatingList.get(actualPosition - 1).getMoves())) {
                return String.valueOf(actualPosition);
            }
        } while (mRatingList.size() != actualPosition);
        return String.valueOf(actualPosition);
    }
}
