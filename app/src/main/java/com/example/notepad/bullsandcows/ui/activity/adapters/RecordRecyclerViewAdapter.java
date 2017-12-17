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
import com.example.notepad.bullsandcows.data.databases.DBOperations;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.ui.activity.listeners.UpdateLaterCallback;
import com.example.notepad.bullsandcows.ui.activity.listeners.UserInfoRecordListener;
import com.example.notepad.bullsandcows.ui.activity.listeners.UserNikClickListener;
import com.example.notepad.bullsandcows.utils.Converters;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.ArrayList;

import kiolk.com.github.pen.Pen;

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.RecordsViewHolder>
        implements UserInfoRecordListener, UpdateLaterCallback {

    private ArrayList<RecordsToNet> modelArrayList;
    private Context mContext;
    private Cursor mCursor;
    private RecordsToNet mRecord;

    protected RecordRecyclerViewAdapter(Context pContext, Cursor pCursor) {
//        this.modelArrayList = pModelArrayList;
        this.mContext = pContext;
        mCursor = pCursor;
    }

    @Override
    public RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_card_layout, parent, false);
        return new RecordsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecordsViewHolder holder, final int position) {
//        final RecordsToNet model = modelArrayList.get(position);
        if (position % 2 == 0) {
            holder.mRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ITEM_YELLOW_DARK));
        } else {
            holder.mRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ITEM_YELLOW_LIGHT));
        }
//        if (Integer.parseInt(model.getMoves()) <= 5 && Integer.parseInt(model.getCodes()) == 4) {
//            holder.mRelativeLayout.setPadding(10, 10, 10, 10);
//            holder.mRelativeLayout.setBackgroundColor(Color.RED);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                holder.mRelativeLayout.setElevation(24);
//            }
//        }
//        holder.mCodTextView.setText(model.getCodes());

        mCursor.moveToPosition(position);


        int indexUpdateOnline = mCursor.getColumnIndex(UserRecordsDB.IS_UPDATE_ONLINE);

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
        long date = mCursor.getLong(mCursor.getColumnIndex(UserRecordsDB.ID));
        String dateString = Converters.convertTimeToString(date);
//        holder.mDateTextView.setText(Converters.convertTimeToString(model.getDate()));
        holder.mDateTextView.setText(dateString);
//        holder.mNikNameTextView.setText(model.getNikName());
        final String nikName = mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.NIK_NAME));
        holder.mNikNameTextView.setText(nikName);
//        holder.mMovesTextView.setText(model.getMoves());
        holder.mMovesTextView.setText(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.MOVES)));
//        holder.mTimeTextView.setText(model.getTime());
        holder.mTimeTextView.setText(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.TIME)));

        String url = mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.USER_PHOTO_URL));
//        Pen.getInstance().getImageFromUrl(model.getUserUrlPhoto()).inputTo(holder.mUserImage);

        if (url != null) {
            Pen.getInstance().getImageFromUrl(url).inputTo(holder.mUserImage);
        } else {
            holder.mUserImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bullgood));
        }
        holder.setClickNikListener(new UserNikClickListener.ClickUserNik() {
            @Override
            public void clickItemNik(View pView, int pPosition) {
//                Toast.makeText(mContext, "NikName of user: " + model.getNikName() + ". Position: " + pPosition, Toast.LENGTH_LONG).show();
                showInfoFragment(nikName);
            }
        });
    }

    private void getRecordForUpdate() {
        RecordsToNet pRecord = new RecordsToNet();
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
//        return (null != modelArrayList ? modelArrayList.size() : 0);
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public String showInfoFragment(String pUserName) {
        return pUserName;
    }

    @Override
    public RecordsToNet updateLateRecordCallback(RecordsToNet pRecord) {
       return pRecord;
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mCodTextView;
        TextView mDateTextView;
        TextView mNikNameTextView;
        TextView mMovesTextView;
        TextView mTimeTextView;
        ImageView mUserImage;
        ImageView mToUpdateResult;
        RelativeLayout mRelativeLayout;
        RecordsToNet mRecordInfo;
        UserNikClickListener.ClickUserNik mUserNikListener;
        UpdateLaterCallback mLaterUpdateCallback;

        RecordsViewHolder(View itemView) {
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
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_name_card_text_view:
                    if (mUserNikListener != null) {
                        mUserNikListener.clickItemNik(v, getAdapterPosition());
                        Toast.makeText(mContext, "NikName of user: " + mNikNameTextView.getText() + ". Position: " + getAdapterPosition(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.online_update_record_image_view:
                    mLaterUpdateCallback.updateLateRecordCallback(mRecordInfo);
                    break;
                default:
                    break;
            }
        }

        void setClickNikListener(UserNikClickListener.ClickUserNik pUserNikListener) {
            mUserNikListener = pUserNikListener;
        }

        void setLaterUpdateCallback(UpdateLaterCallback pLaterUpdateCallback) {
            mLaterUpdateCallback = pLaterUpdateCallback;
        }

        void setRecordInfo(RecordsToNet pRecord){
            mRecordInfo = pRecord;
        }
    }
}
