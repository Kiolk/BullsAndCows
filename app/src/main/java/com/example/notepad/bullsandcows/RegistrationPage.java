package com.example.notepad.bullsandcows;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.utils.ParsUserFactory;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import java.util.Timer;
import java.util.TimerTask;

public class RegistrationPage extends AppCompatActivity {

//    private
    EditText mUserNAme;
    EditText mPassword;
    EditText mPassword2;
    EditText mCountry;
    EditText mEmail;
    Button mRegisterButton;
    UserDataBase mUser;
    TextView infoTextView;

    private View.OnFocusChangeListener PasswordFocusChangeListener = new View.OnFocusChangeListener() {

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_age);

//        initViews method
        mUserNAme = (EditText) findViewById(R.id.name_registration_edit_text);
        mPassword = (EditText) findViewById(R.id.password_registration_edit_text);
        mPassword2 = (EditText) findViewById(R.id.password2_registration_edit_text);
        mCountry = (EditText) findViewById(R.id.country_registration_edit_text);
        mEmail = (EditText) findViewById(R.id.email_registration_edit_text);
        mRegisterButton = (Button) findViewById(R.id.registration_button);
        mRegisterButton.setEnabled(false);
        infoTextView = (TextView) findViewById(R.id.information_status_text_view);


//        implement View.OnCliclListener
        View.OnClickListener clickBtn = new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                infoTextView.setText(getResources().getString(R.string.message_for_registration));
                infoTextView.setBackgroundColor(Color.WHITE);
                mUser = new UserDataBase();
                mUser.setUserName(mUserNAme.getText().toString());
                mUser.setPassword(mPassword.getText().toString());
                mUser.setCountry(mCountry.getText().toString());
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

        mPassword2.setOnFocusChangeListener(PasswordFocusChangeListener);
    }

//    move to file
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

    class CheckingExistUser extends UserCheckExist{

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
