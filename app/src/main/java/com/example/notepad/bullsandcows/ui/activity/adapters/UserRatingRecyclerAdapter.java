package com.example.notepad.bullsandcows.ui.activity.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;

//TODO convert cursor to List<Model> then use in adopter
public class UserRatingRecyclerAdapter extends RecyclerView.Adapter<UserRatingRecyclerAdapter.UserRatingViewHolder> {

    private final Cursor mCursor;

    private Context mContext;

    public UserRatingRecyclerAdapter(final Cursor pCursor) {
        mCursor = pCursor;
    }

    @Override
    public UserRatingViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_rating, parent, false);
        mContext = parent.getContext();
        return new UserRatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserRatingViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        final String userName = mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.NIK_NAME));
        final String currentUserName = UserLoginHolder.getInstance().getUserName();
        final String notUpdateResult = mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.IS_UPDATE_ONLINE));
        final Resources resources = mContext.getResources();

        if (currentUserName.equals(userName) && !"0".equals(notUpdateResult)) {
            holder.mItemLayout.setBackgroundColor(resources.getColor(R.color.USER_UPDATE_RESULT));
        } else if (currentUserName.equals(userName) && "0".equals(notUpdateResult)) {
            holder.mItemLayout.setBackgroundColor(resources.getColor(R.color.USER_NOT_UPDATE_RESULT));
        } else if ("0".equals(notUpdateResult)) {
            holder.mItemLayout.setBackgroundColor(resources.getColor(R.color.NOT_UPDATE_RESULT));
        } else {
            holder.mItemLayout.setBackgroundColor(Color.WHITE);
        }
        holder.mPosition.setText(String.valueOf(position + 1));
        holder.mUserName.setText(userName);
        holder.mMoves.setText(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.MOVES)));
        holder.mTime.setText(mCursor.getString(mCursor.getColumnIndex(UserRecordsDB.TIME)));
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    class UserRatingViewHolder extends RecyclerView.ViewHolder {

        TextView mPosition;
        TextView mUserName;
        TextView mMoves;
        TextView mTime;
        LinearLayout mItemLayout;

        UserRatingViewHolder(final View itemView) {
            super(itemView);
            mPosition = itemView.findViewById(R.id.user_position_rating_card_text_view);
            mUserName = itemView.findViewById(R.id.user_name_rating_card_text_view);
            mMoves = itemView.findViewById(R.id.moves_rating_card_text_view);
            mTime = itemView.findViewById(R.id.time_rating_card_text_view);
            mItemLayout = itemView.findViewById(R.id.item_rating_card_layout);
        }
    }
}
