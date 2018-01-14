package com.example.notepad.bullsandcows.ui.activity.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.converters.TimeConvertersUtil;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.BestUserRecords;

import java.util.List;

public class UserRecordsRecyclerViewAdapter extends RecyclerView.Adapter<UserRecordsRecyclerViewAdapter.UserRecordsViewHolder> {

    private final List<BestUserRecords> mBestUserRecords;

    public UserRecordsRecyclerViewAdapter(final List<BestUserRecords> pBestUserRecords) {
        mBestUserRecords = pBestUserRecords;
    }

    @Override
    public UserRecordsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_best_user_records, parent, false);
        return new UserRecordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserRecordsViewHolder holder, final int position) {
        final BestUserRecords userRecord = mBestUserRecords.get(position);

        if (position % 2 == 0) {
            holder.mCardView.setBackgroundColor(Color.GRAY);
        }

        holder.mCod.setText(userRecord.getCodes());
        holder.mTime.setText(userRecord.getTime());
        holder.mMoves.setText(userRecord.getMoves());
        holder.mDate.setText(TimeConvertersUtil.convertTimeToString(userRecord.getDate(), holder.mCardView.getContext()));
        final String number = Constants.EMPTY_STRING + userRecord.getMNumberGames();
        holder.mNikName.setText(number);
    }

    @Override
    public int getItemCount() {
        return (mBestUserRecords != null ? mBestUserRecords.size() : 0);
    }

    class UserRecordsViewHolder extends RecyclerView.ViewHolder {

        private final TextView mCod;
        private final TextView mNikName;
        private final TextView mDate;
        private final TextView mMoves;
        private final TextView mTime;
        private final CardView mCardView;

        UserRecordsViewHolder(final View pView) {
            super(pView);
            mCod = pView.findViewById(R.id.coded_number_best_card_text_view);
            mDate = pView.findViewById(R.id.date_of_won_best_card_text_view);
            mMoves = pView.findViewById(R.id.moves_for_won_best_card_text_view);
            mNikName = pView.findViewById(R.id.nik_name_best_card_text_view);
            mTime = pView.findViewById(R.id.need_time_for_won_best_card_text_view);
            mCardView = pView.findViewById(R.id.best_user_record_card_view);
        }
    }
}
