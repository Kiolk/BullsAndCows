package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notepad.bullsandcows.R;

public class AboutDeveloperFragment extends Fragment {

    public AboutDeveloperFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_about, container, false);
    }
}
