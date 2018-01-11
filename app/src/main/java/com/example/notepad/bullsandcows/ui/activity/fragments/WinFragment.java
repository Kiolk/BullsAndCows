package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.utils.animation.SlideAnimationUtil;

public class WinFragment extends Fragment {

    private TextView mResultText;

    private ImageView mBullImage;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.win_fragment, null);

        mResultText = view.findViewById(R.id.win_result_text_view);
        mBullImage = view.findViewById(R.id.bull_image_view);

        return view;
    }

    public void setWinMessage(final CharSequence pWinMessage) {
        mBullImage.setVisibility(View.VISIBLE);

        SlideAnimationUtil.slideInFromLeft(getActivity().getBaseContext(), mBullImage, new SlideAnimationUtil.SlideAnimationListener() {

            @Override
            public void animationEnd() {
                mResultText.setVisibility(View.VISIBLE);
                mResultText.setText(pWinMessage);
            }
        }, SlideAnimationUtil.SLOWLY);
    }
}
