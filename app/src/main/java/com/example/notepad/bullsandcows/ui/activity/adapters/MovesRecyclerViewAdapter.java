package com.example.notepad.bullsandcows.ui.activity.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.models.UserMoveModel;

import java.util.List;

public class MovesRecyclerViewAdapter extends RecyclerView.Adapter<MovesRecyclerViewAdapter.MovesViewHolder> {

    private final List<UserMoveModel> mUserMoves;

    public MovesRecyclerViewAdapter(final List<UserMoveModel> pUserMoves) {
        mUserMoves = pUserMoves;
    }

    @Override
    public MovesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_moves, parent, false);
        return new MovesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovesViewHolder holder, final int position) {
        holder.mMoveNumber.setText(mUserMoves.get(position).getMove());
        holder.mInputNumber.setText(mUserMoves.get(position).getInputNumber());
        holder.mCowsNumber.setText(mUserMoves.get(position).getCows());
        holder.mBullsNumber.setText(mUserMoves.get(position).getBulls());
    }

    @Override
    public int getItemCount() {
        return mUserMoves != null ? mUserMoves.size() : 0;
    }

    class MovesViewHolder extends RecyclerView.ViewHolder {

        private final TextView mMoveNumber;
        private final TextView mInputNumber;
        private final TextView mBullsNumber;
        private final TextView mCowsNumber;

        MovesViewHolder(final View itemView) {
            super(itemView);
            mMoveNumber = itemView.findViewById(R.id.move_number_card_moves_text_view);
            mInputNumber = itemView.findViewById(R.id.input_number_card_moves_text_view);
            mBullsNumber = itemView.findViewById(R.id.bulls_card_moves_text_view);
            mCowsNumber = itemView.findViewById(R.id.cows_card_moves_text_view);
        }
    }
}
