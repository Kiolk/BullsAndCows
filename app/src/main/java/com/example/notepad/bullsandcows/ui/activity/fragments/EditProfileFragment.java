package com.example.notepad.bullsandcows.ui.activity.fragments;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.ui.activity.adapters.CountrySpinnerAdapter;
import com.example.notepad.bullsandcows.ui.activity.listeners.CloseEditProfileListener;
import com.example.notepad.bullsandcows.utils.CountryUtils;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import kiolk.com.github.pen.Pen;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private TextView mUserNik;
    private EditText mUrlUserPhoto;
    private EditText mFirstPassword;
    private EditText mSecondPassword;
    private EditText mUserSex;
    private EditText mUserAge;
    private EditText mUserEmail;
    private EditText mAboutItself;
    private Button mSaveButton;
    private Button mSubmitPhoto;
    private Spinner mCountrySpinner;
    private ImageView mUserPhoto;
    private CloseEditProfileListener mCloseListener;
    private UserDataBase mUserUpdateInfo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_edit_profile, null);

        fragmentView.setBackgroundColor(Color.GRAY);

        mUserNik = fragmentView.findViewById(R.id.user_name_edit_fragment_text_view);
        mUrlUserPhoto = fragmentView.findViewById(R.id.image_url_edit_fragment_edit_text);
        mFirstPassword = fragmentView.findViewById(R.id.first_password_edit_fragment_edit_text);
        mSecondPassword = fragmentView.findViewById(R.id.second_password_edit_fragment_edit_text);
        mUserSex = fragmentView.findViewById(R.id.sex_edit_fragment_edit_text);
        mUserAge = fragmentView.findViewById(R.id.age_edit_fragment_edit_text);
        mUserEmail = fragmentView.findViewById(R.id.email_edit_fragment_edit_text);
        mAboutItself = fragmentView.findViewById(R.id.about_itself_edit_fragment_edit_text);

        mCountrySpinner = fragmentView.findViewById(R.id.example_country_registration_spinner);
        CountrySpinnerAdapter spinnerAdapter = new CountrySpinnerAdapter(getActivity().getBaseContext(),
                R.layout.item_country_spinner_layout, 2, CountryUtils.getCountryList());
        mCountrySpinner.setAdapter(spinnerAdapter);

        mSaveButton = fragmentView.findViewById(R.id.save_changes_edit_fragment_button);
        mSaveButton.setOnClickListener(this);
        mSubmitPhoto = fragmentView.findViewById(R.id.set_image_edit_fragment_button);
        mSubmitPhoto.setOnClickListener(this);
        mUserPhoto = fragmentView.findViewById(R.id.user_image_edit_fragment_image_view);

        return fragmentView;
    }

    public void editUserProfile() {
        mUserNik.setText(UserLoginHolder.getInstance().getUserName());

        if (UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl() != null) {
            Pen.getInstance()
                    .getImageFromUrl(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl())
                    .inputTo(mUserPhoto);
            mUrlUserPhoto.setText(UserLoginHolder.getInstance().getUserInfo().getMPhotoUrl());
        }

        String password = UserLoginHolder.getInstance().getPassword();
        mFirstPassword.setText(password);
        mSecondPassword.setText(password);

        if (UserLoginHolder.getInstance().getUserInfo().getMAge() != null) {
            mUserAge.setText(String.valueOf(UserLoginHolder.getInstance().getUserInfo().getMAge()));
        }

        if (UserLoginHolder.getInstance().getUserInfo().getMSex() != null) {
            mUserSex.setText(UserLoginHolder.getInstance().getUserInfo().getMSex());
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

    public void setCloseListener(CloseEditProfileListener pListener) {
        mCloseListener = pListener;
    }

    private void closeFragment() {
        mCloseListener.closeFragment();
    }

    @Override
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.set_image_edit_fragment_button:
                Pen.getInstance()
                        .getImageFromUrl(mUrlUserPhoto.getText().toString())
                        .inputTo(mUserPhoto);
                break;
            case R.id.save_changes_edit_fragment_button:

                if (updateInformation()) {
                    new UserBaseManager().patchNewUserInformation(mUserUpdateInfo);
                    closeFragment();
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
            mUserUpdateInfo.setMSex(mUserSex.getText().toString());
            mUserUpdateInfo.setMAge(Integer.parseInt(mUserAge.getText().toString()));
            mUserUpdateInfo.setEmail(mUserEmail.getText().toString());
            String country = CountryUtils.getCountry(mCountrySpinner.getSelectedItemPosition());
            mUserUpdateInfo.setCountry(country);

            return true;
        } catch (Exception pE) {
            pE.getStackTrace();
            String m = pE.getMessage();
//            if(pE.getMessage().equals("NumberFormatException")){
//
//            }
            return false;
        }
    }
}
