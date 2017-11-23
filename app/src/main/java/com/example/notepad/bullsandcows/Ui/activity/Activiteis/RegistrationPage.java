package com.example.notepad.bullsandcows.Ui.activity.Activiteis;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.NewUserPost;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.UserCheckExist;
import com.example.notepad.bullsandcows.Utils.Constants;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

public class RegistrationPage extends AppCompatActivity {

    EditText mUserNAme;
    EditText mPassword;
    EditText mPassword2;
//    EditText mCountry;
    EditText mEmail;
    Button mRegisterButton;
    UserDataBase mUser;
    TextView infoTextView;
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_age);
        mUserNAme = (EditText) findViewById(R.id.name_registration_edit_text);
        mPassword = (EditText) findViewById(R.id.password_registration_edit_text);
        mPassword2 = (EditText) findViewById(R.id.password2_registration_edit_text);
//        mCountry = (EditText) findViewById(R.id.country_registration_edit_text);
        mEmail = (EditText) findViewById(R.id.email_registration_edit_text);
        mRegisterButton = (Button) findViewById(R.id.registration_button);
        mRegisterButton.setEnabled(false);
        infoTextView = (TextView) findViewById(R.id.information_status_text_view);

        mSpinner = (Spinner) findViewById(R.id.country_registration_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countries_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setPromptId(R.string.COUNTRY_SPINNER);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RegistrationPage.this, "Country: " + mSpinner.getSelectedItem().toString() + ". Position: " + i, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        View.OnClickListener clickBtn = new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                infoTextView.setText(getResources().getString(R.string.message_for_registration));
                infoTextView.setBackgroundColor(Color.WHITE);
                mUser = new UserDataBase();
                mUser.setUserName(mUserNAme.getText().toString());
                mUser.setPassword(mPassword.getText().toString());
              //  mUser.setCountry(mCountry.getText().toString());
                mUser.setCountry(mSpinner.getSelectedItem().toString());
                Log.d(Constants.TAG, "User country: " + mUser.getCountry());
                mUser.setEmail(mEmail.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("nameOfUser", mUserNAme.getText().toString());
                intent.putExtra("password", mPassword.getText().toString());
                setResult(RESULT_OK, intent);
                new CheckingExistUser().execute(mUser.getUserName());
            }
        };

        mRegisterButton.setOnClickListener(clickBtn);

        mUserNAme.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View pView, boolean pB) {
                if(!pB){
                     new CheckingAvaliableNikName().execute(mUserNAme.getText().toString());
                }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View pView, boolean pB) {
                if(mPassword.getText().toString() != null){
                    mPassword.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
                }
            }
        });

        mPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View pView, boolean pB) {
                if(mPassword2.getText().toString().equals(mPassword.getText().toString())){
                    mPassword2.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
                    if(mUserNAme.getText().toString() != null) {
                        mRegisterButton.setEnabled(true);
                    }
                }else{
                    mPassword2.setTextColor(getResources().getColor(R.color.ERROR_EDIT_TEXT));
                    mRegisterButton.setEnabled(false);
                }
            }
        });
    }

    class eExsmol extends NewUserPost {

        @Override
        protected String doInBackground(UserDataBase... pUserDataBases) {
            return super.doInBackground(pUserDataBases);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String pS) {
//            ParsUserFactory user = new ParsUserFactory();
//             com.example.NotePad.myapplication.backend.UserDataBase oneUser = user.parseForUserData(pS);
//            if(pS.equals("f")){
//                Toast.makeText(RegistrationPage.this, pS, Toast.LENGTH_LONG).show();
//            }else{
//                Toast.makeText(RegistrationPage.this, pS, Toast.LENGTH_LONG).show();
//            }
            super.onPostExecute(pS);
        }
    }

    class CheckingExistUser extends UserCheckExist {

        @Override
        protected void onPostExecute(Boolean pBoolean) {
            super.onPostExecute(pBoolean);
            if (pBoolean){
                Toast.makeText(RegistrationPage.this, "User exist", Toast.LENGTH_LONG).show();
                infoTextView.setText(getResources().getString(R.string.user_already_exist));
                infoTextView.setBackgroundColor(Color.RED);
            }else{
                Toast.makeText(RegistrationPage.this, "User not exist in server", Toast.LENGTH_LONG).show();
                new NewUserPost().execute(mUser);
                finish();
            }
        }
    }

    class CheckingAvaliableNikName extends UserCheckExist{

        @Override
        protected void onPostExecute(Boolean pBoolean) {
            super.onPostExecute(pBoolean);
            if(pBoolean){
                mUserNAme.setTextColor(getResources().getColor(R.color.ERROR_EDIT_TEXT));
//                mRegisterButton.setEnabled(false);
            }else{
                mUserNAme.setTextColor(getResources().getColor(R.color.CORRECT_EDIT_TEXT));
//                mRegisterButton.setEnabled(true);
            }
        }
    }
}