package com.example.notepad.bullsandcows.ui.activity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.managers.OnResultCallback;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.ui.activity.adapters.CountrySpinnerAdapter;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CountryUtils;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import kiolk.com.github.pen.Pen;

public class RegistrationPageActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean mNikFree;

    private UserDataBase mUser;

    private EditText mUserName;
    private EditText mPassword;
    private EditText mPassword2;
    private EditText mEmail;
    private EditText mImageUrl;
    private EditText mShortDescription;
    private EditText mUserAge;

    private ImageView mUserImage;

    private Button mRegisterButton;

    private Spinner mSpinnerEx;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        initView();
        initCountrySpinner();
    }

    private final View.OnFocusChangeListener nameFocusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(final View pView, final boolean pB) {
            if (!pB) {
                if (mUserName.length() != 0) {
                    final UserBaseManager userInfo = new UserBaseManager();

                    userInfo.getUserInfo(null, mUserName.getText().toString(), new OnResultCallback<UserDataBase>() {

                        @Override
                        public void onSuccess(final UserDataBase pResult) {
                            if (!pResult.getUserName().equals(mUserName.getText().toString())) {
                                mUserName.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
                                mNikFree = true;
                            } else {
                                mUserName.setTextColor(getResources().getColor(R.color.ERROR_EDIT_TEXT));
                            }
                        }

                        @Override
                        public void onError(final Exception pException) {
                            mUserName.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
                            mNikFree = true;
                        }
                    });
                }
            }
        }
    };

    private final View.OnFocusChangeListener passFocusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(final View pView, final boolean pB) {
            if (mPassword.getText().length() > 0) {
                mPassword.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
            }
        }
    };

    private final View.OnFocusChangeListener pass2FocusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(final View pView, final boolean pB) {
            if (mPassword2.getText().toString().equals(mPassword.getText().toString())) {
                mPassword2.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
                enableRegistrationButton();
            } else {
                mPassword2.setTextColor(getResources().getColor(R.color.ERROR_EDIT_TEXT));
                mRegisterButton.setEnabled(false);
            }
        }
    };

    private void initCountrySpinner() {

        mSpinnerEx = findViewById(R.id.example_country_registration_spinner);
        final SpinnerAdapter spinnerAdapter = new CountrySpinnerAdapter(this,
                R.layout.item_country_spinner_layout, 2, CountryUtils.getCountryList());
        mSpinnerEx.setAdapter(spinnerAdapter);
    }

    private void initView() {
        mUserName = findViewById(R.id.name_registration_edit_text);
        mPassword = findViewById(R.id.password_registration_edit_text);
        mPassword2 = findViewById(R.id.password2_registration_edit_text);
        mEmail = findViewById(R.id.email_registration_edit_text);
        mImageUrl = findViewById(R.id.image_url_edit_tet);
        mUserImage = findViewById(R.id.user_image_registration_image_view);
        mShortDescription = findViewById(R.id.short_description_edit_text);

        mUserAge = findViewById(R.id.age_registration_edit_text);
        final TextView descriptionText = findViewById(R.id.information_status_text_view);
        descriptionText.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.AASSUANBRK));

        mRegisterButton = findViewById(R.id.registration_button);
        final Button setImageButton = findViewById(R.id.set_image_registration_button);
        mRegisterButton.setEnabled(false);

        mUserName.setOnFocusChangeListener(nameFocusListener);
        mPassword.setOnFocusChangeListener(passFocusListener);
        mPassword2.setOnFocusChangeListener(pass2FocusListener);

        mRegisterButton.setOnClickListener(this);
        setImageButton.setOnClickListener(this);
    }

    protected void enableRegistrationButton() {
        if (mNikFree && mPassword.getText().toString().equals(mPassword2.getText().toString())) {
            mRegisterButton.setEnabled(true);
        }
    }

    private void setInfoAboutUser() {
        mUser = new UserDataBase();

        mUser.setUserName(mUserName.getText().toString());
        mUser.setPassword(mPassword.getText().toString());
        mUser.setEmail(mEmail.getText().toString());
        mUser.setCountry(CountryUtils.getCountry(mSpinnerEx.getSelectedItemPosition()));
        mUser.setMCountryFlag(CountryUtils.getCountryResources(mUser.getCountry()));
        mUser.setMPhotoUrl(mImageUrl.getText().toString());
        mUser.setMAge(Integer.parseInt(mUserAge.getText().toString()));
        mUser.setMShortDescription(mShortDescription.getText().toString());
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.set_image_registration_button:
                Pen.getInstance().getImageFromUrl(mImageUrl.getText().toString()).inputTo(mUserImage);
                break;
            case R.id.registration_button:
                setInfoAboutUser();

                final Intent intent = new Intent();
                intent.putExtra(Constants.REGISTRATION_NAME_OF_USER, mUserName.getText().toString());
                intent.putExtra(Constants.REGISTRATION_PASSWORD, mPassword.getText().toString());
                setResult(RESULT_OK, intent);

                final UserBaseManager userAdd = new UserBaseManager();
                userAdd.createNewUser(mUser);

                finish();
                break;
            default:
                break;
        }
    }
}
