package com.example.notepad.bullsandcows.ui.activity.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.ui.activity.listeners.UpdateLaterCallback;
import com.example.notepad.bullsandcows.ui.activity.listeners.UserInfoRecordListener;
import com.example.notepad.bullsandcows.ui.activity.listeners.UserNikClickListener;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.converters.TimeConvertersUtil;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import kiolk.com.github.pen.Pen;

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.RecordsViewHolder>
        implements UserInfoRecordListener, UpdateLaterCallback {

    private final Context mContext;
    private final Cursor mCursor;
    private RecordsToNet mRecord;

    protected RecordRecyclerViewAdapter(final Context pContext, final Cursor pCursor) {
        this.mContext = pContext;
        mCursor = pCursor;
    }

    @Override
    public RecordsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_card_layout, parent, false);
        return new RecordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecordsViewHolder holder, final int position) {

        if (position % 2 == 0) {
            holder.mRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ITEM_YELLOW_DARK));
        } else {
            holder.mRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ITEM_YELLOW_LIGHT));
        }

        mCursor.moveToPosition(position);

        final int indexUpdateOnline = mCursor.getColumnIndex(UserRecordsDB.IS_UPDATE_ONLINE);

        if (mCursor.getString(indexUpdateOnline) != null
                && mCursor.getString(indexUpdateOnline)
                .equals(UserRecordsDB.NOT_UPDATE_ONLINE_HACK) &&
                UserLoginHolder.getInstance().getUserName()
                        .equals(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.NIK_NAME)))) {
            holder.setLaterUpdateCallback(this);
            holder.mToUpdateResult.setVisibility(View.VISIBLE);
            getRecordForUpdate();
            holder.setRecordInfo(mRecord);
        } else {
            holder.mToUpdateResult.setVisibility(View.INVISIBLE);
        }

        holder.mCodTextView.setText(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.CODES)));
        final long date = mCursor.getLong(mCursor.getColumnIndex(UserRecordsDB.ID));
        final String dateString = TimeConvertersUtil.convertTimeToString(date, mContext);
        holder.mDateTextView.setText(dateString);
        final String nikName = mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.NIK_NAME));
        holder.mNikNameTextView.setText(nikName);
        holder.mMovesTextView.setText(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.MOVES)));
        holder.mTimeTextView.setText(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.TIME)));

        final String url = mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.USER_PHOTO_URL));
            Pen.getInstance().getImageFromUrl(url).inputTo(holder.mUserImage);
        holder.setClickNikListener(new UserNikClickListener.ClickUserNik() {
            @Override
            public void clickItemNik(final View pView, final int pPosition) {
                showInfoFragment(nikName);
            }
        });
    }

    //TODO introduce new class like Editor that will prepare and post data to backend.
    //TODO adaptor can't know about it
    private void getRecordForUpdate() {
        final RecordsToNet pRecord = new RecordsToNet();
        pRecord.setNikName(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.NIK_NAME)));
        pRecord.setUserUrlPhoto(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.USER_PHOTO_URL)));
        pRecord.setMoves(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.MOVES)));
        pRecord.setCodes(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.CODES)));
        pRecord.setTime(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.TIME)));
        pRecord.setDate(mCursor.getLong(mCursor.getColumnIndex(UserRecordsDB.ID)));
        mRecord = pRecord;
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public long getItemId(final int position) {
        return super.getItemId(position);
    }

    @Override
    public String showInfoFragment(final String pUserName) {
        return pUserName;
    }

    @Override
    public RecordsToNet updateLateRecordCallback(final RecordsToNet pRecord) {
        return pRecord;
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mCodTextView;
        private final TextView mDateTextView;
        private final TextView mNikNameTextView;
        private final TextView mMovesTextView;
        private final TextView mTimeTextView;
        private final ImageView mUserImage;
        private final ImageView mToUpdateResult;
        private final RelativeLayout mRelativeLayout;
        private RecordsToNet mRecordInfo;
        private UserNikClickListener.ClickUserNik mUserNikListener;
        private UpdateLaterCallback mLaterUpdateCallback;

        RecordsViewHolder(final View itemView) {
            super(itemView);
            mRelativeLayout = itemView.findViewById(R.id.record_card_relative_layout);
            mCodTextView = itemView.findViewById(R.id.coded_number_card_text_view);
            mDateTextView = itemView.findViewById(R.id.date_win_card_text_view);
            mNikNameTextView = itemView.findViewById(R.id.user_name_card_text_view);
            mNikNameTextView.setTypeface(CustomFonts.getTypeFace(mContext, CustomFonts.BLACKGROTESKC));
            mMovesTextView = itemView.findViewById(R.id.moves_card_text_view);
            mTimeTextView = itemView.findViewById(R.id.time_card_view);
            mNikNameTextView.setOnClickListener(this);
            mUserImage = itemView.findViewById(R.id.picture_of_user_image_view);
            mToUpdateResult = itemView.findViewById(R.id.online_update_record_image_view);
            mToUpdateResult.setOnClickListener(this);
        }


        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.user_name_card_text_view:
                    if (mUserNikListener != null) {
                        mUserNikListener.clickItemNik(v, getAdapterPosition());
//                        Toast.makeText(mContext, "NikName of user: " + mNikNameTextView.getText() + ". Position: " + getAdapterPosition(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.online_update_record_image_view:
                    mLaterUpdateCallback.updateLateRecordCallback(mRecordInfo);
                    break;
                default:
                    break;
            }
        }

        void setClickNikListener(final UserNikClickListener.ClickUserNik pUserNikListener) {
            mUserNikListener = pUserNikListener;
        }

        void setLaterUpdateCallback(final UpdateLaterCallback pLaterUpdateCallback) {
            mLaterUpdateCallback = pLaterUpdateCallback;
        }

        void setRecordInfo(final RecordsToNet pRecord) {
            mRecordInfo = pRecord;
        }
    }
}
