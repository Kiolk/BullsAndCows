package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.AppInfoHolder;
import com.example.notepad.bullsandcows.services.UpdateAppService;

import kiolk.com.github.pen.utils.ConstantsUtil;

public class UpdateAppFragment extends Fragment implements View.OnClickListener {

    public interface UpdateAppListener {
        void closeUpdateFragment();
    }

    private UpdateAppListener mUpdateListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mUpdateListener = (UpdateAppListener) activity;
        } catch (Exception pE) {
            pE.getStackTrace();
            super.onAttach(activity);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_application, container, false);

        TextView titleNewVersion = view.findViewById(R.id.title_new_features_text_view);
        String title = getString(R.string.NEW_VERSION_TITLE) + " "+ AppInfoHolder.getInstance().getVersionApp().getNameOfApp();
        titleNewVersion.setText(title);

        TextView newFeatures = view.findViewById(R.id.new_features_app_text_view);
        newFeatures.setText(listNewFeaturesGenerator());

        Button updateButton = view.findViewById(R.id.update_app_fragment_button);
        updateButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d(ConstantsUtil.Log.LOG_TAG, "Press update button on Fragment");

        Intent uploadAppIntent = new Intent(getActivity(), UpdateAppService.class);
        getActivity().startService(uploadAppIntent);

        mUpdateListener.closeUpdateFragment();
    }

    private String listNewFeaturesGenerator(){
        String [] newAppFeatures = AppInfoHolder.getInstance().getVersionApp().getmNewVersionFeatures();
        StringBuilder resultString = new StringBuilder();
        int size = newAppFeatures.length;

        for(int i = 0; i < size; ++i){
            resultString.append(newAppFeatures[i]);
            if(i != size -1){
                resultString.append("\n");
            }
        }

        return resultString.toString();
    }
}
