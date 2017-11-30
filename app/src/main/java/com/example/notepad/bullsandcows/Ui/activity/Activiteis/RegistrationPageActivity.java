package com.example.notepad.bullsandcows.Ui.activity.Activiteis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notepad.bullsandcows.Data.Managers.UserBaseManager;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.Utils.Constants;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

public class RegistrationPageActivity extends AppCompatActivity {

    private EditText mUserNAme;
    private EditText mPassword;
    private EditText mPassword2;
    private EditText mEmail;
    private Button mRegisterButton;
    private UserDataBase mUser;
    private Spinner mSpinner;
    private boolean mNikFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_age);

        initView();
        initCountrySpinner();
        mUserNAme.setOnFocusChangeListener(nameFocusListener);
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
                        mUserNAme.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
                        mNikFree = true;
                    }

                    @Override
                    public void nikCorrectPasswordWrongCallback() {
                        mUserNAme.setTextColor(getResources().getColor(R.color.ERROR_EDIT_TEXT));
                    }
                };
                if (mUserNAme.length() != 0) {
                    userInfo.checkInfoAboutUser(mUserNAme.getText().toString(), "1111");
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
    }

    private void initView() {
        mUserNAme = findViewById(R.id.name_registration_edit_text);
        mPassword = findViewById(R.id.password_registration_edit_text);
        mPassword2 = findViewById(R.id.password2_registration_edit_text);
        mEmail = findViewById(R.id.email_registration_edit_text);
        mRegisterButton = findViewById(R.id.registration_button);
        mRegisterButton.setEnabled(false);
    }

    private void enableRegistrationButton() {
        if (mNikFree && mPassword.getText().toString().equals(mPassword2.getText().toString())) {
            mRegisterButton.setEnabled(true);
        }
    }

    private void setInfoAboutUser() {
        mUser = new UserDataBase();
        mUser.setUserName(mUserNAme.getText().toString());
        mUser.setPassword(mPassword.getText().toString());
        mUser.setCountry(mSpinner.getSelectedItem().toString());
        Log.d(Constants.TAG, "User country: " + mUser.getCountry());
        mUser.setEmail(mEmail.getText().toString());
    }

    View.OnClickListener clickBtn = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            setInfoAboutUser();

            Intent intent = new Intent();
            intent.putExtra("nameOfUser", mUserNAme.getText().toString());
            intent.putExtra("password", mPassword.getText().toString());
            setResult(RESULT_OK, intent);

            UserBaseManager userAdd = new UserBaseManager();
            userAdd.createNewUser(mUser);
            Toast.makeText(RegistrationPageActivity.this, "User not exist in server", Toast.LENGTH_LONG).show();
            finish();
        }
    };
}
