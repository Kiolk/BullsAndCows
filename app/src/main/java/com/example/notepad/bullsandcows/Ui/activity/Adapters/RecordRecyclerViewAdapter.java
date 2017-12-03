package com.example.notepad.bullsandcows.Ui.activity.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.notepad.bullsandcows.Data.Models.RecordModel;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.Utils.Converters;
import com.example.notepad.bullsandcows.Utils.CustomFonts;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.util.ArrayList;

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.RecordsViewHolder> {

    ArrayList<RecordsToNet> modelArrayList;
    Context mContext;

    public RecordRecyclerViewAdapter(Context mContext, ArrayList<RecordsToNet> modelArrayList) {
        this.modelArrayList = modelArrayList;
        this.mContext = mContext;
    }

    @Override
    public RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_card_layout, parent, false);
        return new RecordsViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecordsViewHolder holder, int position) {
        RecordsToNet model = modelArrayList.get(position);
        if (position % 2 == 0) {
            holder.mRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ITEM_YELLOW_DARK));
        } else {
            holder.mRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.ITEM_YELLOW_LIGHT));
        }
        if (Integer.parseInt(model.getMoves()) <= 5 && Integer.parseInt(model.getCodes()) == 4) {
            holder.mRelativeLayout.setPadding(10, 10, 10, 10);
            holder.mRelativeLayout.setBackgroundColor(Color.RED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mRelativeLayout.setElevation(24);
            }
        }
        holder.mCodTextView.setText(model.getCodes());
        holder.mDateTextView.setText(Converters.convertTimeToString(model.getDate()));
        holder.mNikNameTextView.setText(model.getNikName());
        holder.mMovesTextView.setText(model.getMoves());
        holder.mTimeTextView.setText(model.getTime());
    }


    @Override
    public int getItemCount() {
        return (null != modelArrayList ? modelArrayList.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder {

        TextView mCodTextView;
        TextView mDateTextView;
        TextView mNikNameTextView;
        TextView mMovesTextView;
        TextView mTimeTextView;
        RelativeLayout mRelativeLayout;


        public RecordsViewHolder(View itemView) {
            super(itemView);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.record_card_relative_layout);
            mCodTextView = (TextView) itemView.findViewById(R.id.coded_number_card_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_win_card_text_view);
            mNikNameTextView = (TextView) itemView.findViewById(R.id.user_name_card_text_view);
            mNikNameTextView.setTypeface(CustomFonts.getTypeFace(mContext, CustomFonts.BLACKGROTESKC));
            mMovesTextView = (TextView) itemView.findViewById(R.id.moves_card_text_view);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time_card_view);
        }


    }
}
