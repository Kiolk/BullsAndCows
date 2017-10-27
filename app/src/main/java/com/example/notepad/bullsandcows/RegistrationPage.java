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

public class RegistrationPage extends AppCompatActivity {

    EditText mUserNAme;
    EditText mPassword;
    EditText mPassword2;
    EditText mCountry;
    EditText mEmail;
    Button mRegisterButton;
    UserDataBase mUser;
    TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_age);
        mUserNAme = (EditText) findViewById(R.id.name_registration_edit_text);
        mPassword = (EditText) findViewById(R.id.password_registration_edit_text);
        mPassword2 = (EditText) findViewById(R.id.password2_registration_edit_text);
        mCountry = (EditText) findViewById(R.id.country_registration_edit_text);
        mEmail = (EditText) findViewById(R.id.email_registration_edit_text);
        mRegisterButton = (Button) findViewById(R.id.registration_button);
        infoTextView = (TextView) findViewById(R.id.information_status_text_view);


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
}
