package com.example.notepad.bullsandcows;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.LoadNewVersionOfApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity {

    TextView mInfoVersionTextView;
    EditText mUserName;
    EditText mLogin;
    EditText mPassword;
    Boolean mCurrentVersionAppWelcome;
    Boolean mKeepPassword;
    String mNameOfUserWelcome;
    Button mConfirmNameButton;
    Button mVisitCheckVersion;
    Button mLoginButton;
    Button mRegistrationButton;
    CheckBox mCheckBox;
    String mNewVersion = "";
    String mUrlNewVersionOfApp = "";
    String mNameNewApp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mUserName = (EditText) findViewById(R.id.user_name_welcome_edit_text);
        mConfirmNameButton = (Button) findViewById(R.id.welcome_confirm_nik_name_button);
        mInfoVersionTextView = (TextView) findViewById(R.id.info_about_version_text_view);
        mVisitCheckVersion = (Button) findViewById(R.id.check_version_app_button);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegistrationButton = (Button) findViewById(R.id.registration_welcome_button);
        mLogin = (EditText) findViewById(R.id.login_welcome_page_edit_text);
        mPassword = (EditText) findViewById(R.id.password_welcome_page_edit_text);
        mCheckBox = (CheckBox) findViewById(R.id.keep_password_check_box);

        Intent intent = getIntent();
        mCurrentVersionAppWelcome = intent.getBooleanExtra("version", false);
        mNameOfUserWelcome = intent.getStringExtra("nikOfUser");
        mPassword.setText(intent.getStringExtra("password"));
        mKeepPassword = intent.getBooleanExtra("keepPassword", false);
        mLogin.setText(mNameOfUserWelcome);
        if(mKeepPassword){
            mCheckBox.setChecked(true);
        }

        View.OnClickListener clickButton = new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                switch (pView.getId()) {
                    case R.id.welcome_confirm_nik_name_button:
                        Intent intent = new Intent();
                        intent.putExtra("nikOfUser", mUserName.getText().toString());
                        intent.putExtra("version", mCurrentVersionAppWelcome);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case R.id.check_version_app_button:
                        if(mCurrentVersionAppWelcome){
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Kiolk/BullsAndCows"));
                            startActivity(intent1);
                        }else{
                            if(new CheckConnection().checkConnection(Welcome.this)){

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Welcome.this).
                                        setSmallIcon(R.drawable.bullgood).
                                        setContentTitle("Start download new version").
                                        setContentText("New version of Bulls and cows app");
                                Intent resultIntent = new Intent(Welcome.this, MainActivity.class);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(Welcome.this);
                                stackBuilder.addParentStack(MainActivity.class);
                                stackBuilder.addNextIntent(resultIntent);
                                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                mBuilder.setContentIntent(resultPendingIntent);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1, mBuilder.build());

                                new LoadNewVersionOfApp(Welcome.this, mVisitCheckVersion, mUrlNewVersionOfApp, mNameNewApp);
                                //TODO not very clear representation that new version of application download on phone
                            } else{
                                Toast.makeText(Welcome.this, "Load new version of app not possible. Don't have connection to internet", Toast.LENGTH_LONG).show();
                            }
                            mCurrentVersionAppWelcome = true;
                            checkVersionOfApp();
                        }


                        break;
                    case R.id.registration_welcome_button:
                        Intent intentRegistration = new Intent(Welcome.this, RegistrationPage.class);
                        intentRegistration.putExtra("nameOfUser", "");
                        intentRegistration.putExtra("password", "");
                        startActivityForResult(intentRegistration, 3);
                        break;
                    case R.id.login_button:
                        String name = mLogin.getText().toString();
                        String password = mPassword.getText().toString();
                        if (name.length() > 0 && password.length() > 0) {
                            new checkUserLoginAndPassword().execute(name, password);
                        }else{
                            Toast.makeText(Welcome.this, "Login or password is wrong", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        mConfirmNameButton.setOnClickListener(clickButton);
        mVisitCheckVersion.setOnClickListener(clickButton);
        mRegistrationButton.setOnClickListener(clickButton);
        mLoginButton.setOnClickListener(clickButton);
        new CheckingVersionOfApp().execute(BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 3:
                if (requestCode == RESULT_OK) {
                    //TODO find bug why not send information from Registration page
                    mLogin.setText(data.getStringExtra("nameOfUser"));
                    mPassword.setText(data.getStringExtra("password"));
                    mUserName.setText(data.getStringExtra("nameOfUser"));
                } else {

                }
                break;
            default:
                break;
        }

    }

    public void checkVersionOfApp() {
        if (mCurrentVersionAppWelcome) {
            mInfoVersionTextView.setText("Your app in actual version");
            mVisitCheckVersion.setText(R.string.VISIT_SITE);
            mVisitCheckVersion.setEnabled(true);
            mVisitCheckVersion.setTextColor(Color.WHITE);
        } else {
            mInfoVersionTextView.setText("Your app is old version");
//            mVisitCheckVersion.setBackgroundColor(Color.RED);
            mVisitCheckVersion.setText("Press for upgrade yor app for version " + mNewVersion);
            mVisitCheckVersion.setTextColor(Color.RED);
        }
    }

    class CheckingVersionOfApp extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String pS) {
            super.onPostExecute(pS);
            try {
                JSONObject object = new JSONObject(pS);
                String appActualVersion = object.getString("mVersionOfApp");
                mNewVersion = appActualVersion;
                mUrlNewVersionOfApp = object.getString("mUrlNewVersionOfApp");
                mNameNewApp = object.getString("mNameOfApp");
                String versionOfAnnOnPhone = "" + BuildConfig.VERSION_CODE;
                if (!(appActualVersion.equalsIgnoreCase(versionOfAnnOnPhone))) {

                    mCurrentVersionAppWelcome = false;
                }
                checkVersionOfApp();
            } catch (JSONException pE) {
                pE.printStackTrace();
            }

//            Toast.makeText(Welcome.class, pS, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

// mContext = pPairs[0].first;
            String name = params[0];
            try {

                URL url = new URL(BuildConfig.BACKEND_APP_VERSION_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                Map<String, String> nameValuePair = new HashMap<>();
                nameValuePair.put("name", name);
                String postParams = buildPostDataString(nameValuePair);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(postParams);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                httpURLConnection.connect();

// Read response
                int respondCod = httpURLConnection.getResponseCode();
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                if (respondCod == HttpURLConnection.HTTP_OK) {
                    return response.toString();
                }
                return "Error" + respondCod + httpURLConnection.getResponseMessage();

            } catch (IOException pE) {
                pE.printStackTrace();
            }
            return null;
        }

        public String buildPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }
    private class checkUserLoginAndPassword extends UserCheckExist{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... pStrings) {
            return super.doInBackground(pStrings);
        }

        @Override
        protected void onPostExecute(Boolean pBoolean) {
            super.onPostExecute(pBoolean);
            if(pBoolean == true){
                Toast.makeText(Welcome.this, "You succese logened", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("nikOfUser", mLogin.getText().toString());
                intent.putExtra("version", mCurrentVersionAppWelcome);
                intent.putExtra("password", mPassword.getText().toString());
                if(mCheckBox.isChecked()){
                    mKeepPassword = true;
                }else{
                    mKeepPassword = false;
                }
                intent.putExtra("keepPassword", mKeepPassword);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(Welcome.this, "Login or password is wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

}
