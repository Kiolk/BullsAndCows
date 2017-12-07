package com.example.notepad.bullsandcows.ui.activity.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.models.ItemCountryData;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CountrySpinnerAdapter extends ArrayAdapter<ItemCountryData> {

    Context mContext;

    int mGroupId;

    ArrayList<ItemCountryData> mList;

    LayoutInflater mInflater;

    public CountrySpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<ItemCountryData> objects) {
        super(context, resource, textViewResourceId, objects);

        mList = (ArrayList<ItemCountryData>) objects;
        mGroupId = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = mInflater.inflate(mGroupId, parent, false);
        ImageView image = itemView.findViewById(R.id.country_flag_spinner_image_view);
        image.setImageResource(mList.get(position).getResource());
        TextView countryText = itemView.findViewById(R.id.country_spinner_text_view);
        countryText.setText(mList.get(position).getCountry());
        return itemView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
