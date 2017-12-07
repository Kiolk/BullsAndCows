package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.models.ItemCountryData;
import com.example.notepad.bullsandcows.ui.activity.adapters.CountrySpinnerAdapter;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CountryUtils;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.ArrayList;

import kiolk.com.github.pen.Pen;

public class RegistrationPageActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private EditText mPassword2;
    private EditText mEmail;
    private EditText mImageUrl;
    private Button mSetImageButton;
    private ImageView mUserImage;
    private Button mRegisterButton;
    private UserDataBase mUser;
    private Spinner mSpinner;
    private Spinner mSpinnerEx;
    private boolean mNikFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_age);

        initView();
        initCountrySpinner();
        mUserName.setOnFocusChangeListener(nameFocusListener);
        mPassword.setOnFocusChangeListener(passFocusListener);
        mPassword2.setOnFocusChangeListener(pass2FocusListener);
        mRegisterButton.setOnClickListener(clickBtn);
    }

    private View.OnFocusChangeListener nameFocusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View pView, boolean pB) {
            if (!pB) {
                UserBaseManager userInfo = new UserBaseManager() {

                    @Override
                    public void nikFreeCallback() {
                        mUserName.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
                        mNikFree = true;
                    }

                    @Override
                    public void nikCorrectPasswordWrongCallback() {
                        mUserName.setTextColor(getResources().getColor(R.color.ERROR_EDIT_TEXT));
                    }
                };
                if (mUserName.length() != 0) {
                    userInfo.checkInfoAboutUser(mUserName.getText().toString(), "1111");
                }

            }
        }
    };

    private View.OnFocusChangeListener passFocusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View pView, boolean pB) {
            if (mPassword.getText().length() > 0) {
                mPassword.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
            }
        }
    };

    private View.OnFocusChangeListener pass2FocusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View pView, boolean pB) {
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
        mSpinner = findViewById(R.id.country_registration_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countries_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setPromptId(R.string.COUNTRY_SPINNER);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RegistrationPageActivity.this, "Country: " + mSpinner.getSelectedItem().toString() + ". Position: " + i, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSpinnerEx = findViewById(R.id.example_country_registration_spinner);
        CountrySpinnerAdapter spinnerAdapter = new CountrySpinnerAdapter(RegistrationPageActivity.this,
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
        mRegisterButton = findViewById(R.id.registration_button);
        mSetImageButton = findViewById(R.id.set_image_registration_button);
        mRegisterButton.setEnabled(false);
    }

    private void enableRegistrationButton() {
        if (mNikFree && mPassword.getText().toString().equals(mPassword2.getText().toString())) {
            mRegisterButton.setEnabled(true);
        }
    }

    private void setInfoAboutUser() {
        mUser = new UserDataBase();
        mUser.setUserName(mUserName.getText().toString());
        mUser.setPassword(mPassword.getText().toString());
        mUser.setCountry(mSpinner.getSelectedItem().toString());
        Log.d(Constants.TAG, "User country: " + mUser.getCountry());
        mUser.setEmail(mEmail.getText().toString());
        mUser.setCountry(CountryUtils.getCountry(mSpinnerEx.getSelectedItemPosition()));
        mUser.setMCountryFlag(CountryUtils.getCountryResources(mUser.getCountry()));
        mUser.setMPhotoUrl(mImageUrl.getText().toString());
        int i = 0;
    }

    View.OnClickListener clickBtn = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            setInfoAboutUser();

            Intent intent = new Intent();
            intent.putExtra(Constants.REGISTRATION_NAME_OF_USER, mUserName.getText().toString());
            intent.putExtra(Constants.REGISTRATION_PASSWORD, mPassword.getText().toString());
            setResult(RESULT_OK, intent);

            UserBaseManager userAdd = new UserBaseManager();
            userAdd.createNewUser(mUser);

            finish();
        }
    };

    public void submitImage(View view){
        Pen.getInstance().getImageFromUrl(mImageUrl.getText().toString()).inputTo(mUserImage);
    }

}
