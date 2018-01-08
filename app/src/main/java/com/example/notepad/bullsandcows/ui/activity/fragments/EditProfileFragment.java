package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.managers.UserLoginCallback;
import com.example.notepad.bullsandcows.ui.activity.adapters.CountrySpinnerAdapter;
import com.example.notepad.bullsandcows.ui.activity.listeners.CloseEditProfileListener;
import com.example.notepad.bullsandcows.utils.CountryUtils;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import kiolk.com.github.pen.Pen;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private EditText mUrlUserPhoto;
    private EditText mFirstPassword;
    private EditText mSecondPassword;
    private EditText mUserAge;
    private EditText mUserEmail;
    private EditText mAboutItself;

    private ImageView mUserPhoto;

    private Spinner mCountrySpinner;

    private CloseEditProfileListener mCloseListener;
    private UserDataBase mUserUpdateInfo;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_edit_profile, null);

        fragmentView.setBackgroundColor(Color.GRAY);

        initView(fragmentView);

        return fragmentView;
    }

    private void initView(final View pFragmentView) {
        final TextView confirmPasswords = pFragmentView.findViewById(R.id.additional_information_text_view);
        confirmPasswords.setText(getResources().getString(R.string.CONFIRM_PASSWORDS_FOR_SAVE_CHANGES));

        mUrlUserPhoto = pFragmentView.findViewById(R.id.image_url_edit_tet);
        mFirstPassword = pFragmentView.findViewById(R.id.password_registration_edit_text);
        mSecondPassword = pFragmentView.findViewById(R.id.password2_registration_edit_text);

        final EditText waste = pFragmentView.findViewById(R.id.name_registration_edit_text);
        waste.setVisibility(View.GONE);

        mUserAge = pFragmentView.findViewById(R.id.age_registration_edit_text);
        mUserEmail = pFragmentView.findViewById(R.id.email_registration_edit_text);
        mAboutItself = pFragmentView.findViewById(R.id.short_description_edit_text);

        mCountrySpinner = pFragmentView.findViewById(R.id.example_country_registration_spinner);
        final SpinnerAdapter spinnerAdapter = new CountrySpinnerAdapter(getActivity().getBaseContext(),
                R.layout.item_country_spinner_layout, 2, CountryUtils.getCountryList());
        mCountrySpinner.setAdapter(spinnerAdapter);

        final Button saveButton = pFragmentView.findViewById(R.id.save_changes_edit_fragment_button);
        saveButton.setOnClickListener(this);

        final Button submitPhoto = pFragmentView.findViewById(R.id.set_image_registration_button);
        submitPhoto.setOnClickListener(this);

        mUserPhoto = pFragmentView.findViewById(R.id.user_image_registration_image_view);
    }

    public void editUserProfile() {

        if (UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl() != null) {
            Pen.getInstance()
                    .getImageFromUrl(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl())
                    .inputTo(mUserPhoto);
            mUrlUserPhoto.setText(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl());
        }

        if (UserLoginHolder.getInstance().getUserInfo().getMAge() != null) {
            mUserAge.setText(String.valueOf(UserLoginHolder.getInstance().getUserInfo().getMAge()));
        }

        if (UserLoginHolder.getInstance().getUserInfo().getEmail() != null) {
            mUserEmail.setText(UserLoginHolder.getInstance().getUserInfo().getEmail());
        }

        if (UserLoginHolder.getInstance().getUserInfo().getCountry() != null) {
            mCountrySpinner.setSelection(CountryUtils.getCountryId(UserLoginHolder
                    .getInstance().getUserInfo().getCountry()));
        }

        if (UserLoginHolder.getInstance().getUserInfo().getMShortDescription() != null) {
            mAboutItself.setText(UserLoginHolder.getInstance().getUserInfo().getMShortDescription());
        }
    }

    public void setCloseListener(final CloseEditProfileListener pListener) {
        mCloseListener = pListener;
    }

    protected void closeFragment() {
        mCloseListener.onCloseFragment();
    }

    @Override
    public void onClick(final View pView) {
        switch (pView.getId()) {
            case R.id.set_image_registration_button:
                Pen.getInstance()
                        .getImageFromUrl(mUrlUserPhoto.getText().toString())
                        .inputTo(mUserPhoto);
                break;
            case R.id.save_changes_edit_fragment_button:

                final String userPassword = UserLoginHolder.getInstance().getUserInfo().getPassword();
                final String setPassword = mFirstPassword.getText().toString();

                if (updateInformation() && userPassword.equals(setPassword)) {
                    new UserBaseManager().patchNewUserInformation(mUserUpdateInfo, new UserLoginCallback() {

                        @Override
                        public void getUserInfoCallback(final UserDataBase pUserInfo) {
                            if (pUserInfo != null) {
                                Toast.makeText(getActivity().getBaseContext(), R.string.PROFILE_SUCCES_UPDATE, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), R.string.PROFILE_NOT_UPDATE, Toast.LENGTH_LONG).show();
                            }
                            closeFragment();
                        }
                    });
                }

                break;
            default:
                break;
        }
    }

    private boolean updateInformation() {
        try {
            mUserUpdateInfo = UserLoginHolder.getInstance().getUserInfo();

            mUserUpdateInfo.setPassword(mSecondPassword.getText().toString());
            mUserUpdateInfo.setMPhotoUrl(mUrlUserPhoto.getText().toString());
            mUserUpdateInfo.setMAge(Integer.parseInt(mUserAge.getText().toString()));
            mUserUpdateInfo.setEmail(mUserEmail.getText().toString());
            final String country = CountryUtils.getCountry(mCountrySpinner.getSelectedItemPosition());
            mUserUpdateInfo.setCountry(country);
            mUserUpdateInfo.setMShortDescription(mAboutItself.getText().toString());
            return true;
        } catch (final Exception pE) {
            pE.getStackTrace();

            return false;
        }
    }
}
