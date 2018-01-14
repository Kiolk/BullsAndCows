package com.example.notepad.bullsandcows.ui.activity.adapters;

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

import java.util.List;

public class CountrySpinnerAdapter extends ArrayAdapter<ItemCountryData> {

    private final int mGroupId;

    private final List<ItemCountryData> mList;

    private final LayoutInflater mInflater;

    public CountrySpinnerAdapter(@NonNull final Context context, final int resource, final int textViewResourceId, @NonNull final List<ItemCountryData> objects) {
        super(context, resource, textViewResourceId, objects);

        mList = objects;
        mGroupId = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        final View itemView = mInflater.inflate(mGroupId, parent, false);
        final ImageView image = itemView.findViewById(R.id.country_flag_spinner_image_view);
        image.setImageResource(mList.get(position).getResource());
        final TextView countryText = itemView.findViewById(R.id.country_spinner_text_view);
        countryText.setText(mList.get(position).getCountry());
        return itemView;

    }

    @Override
    public View getDropDownView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
