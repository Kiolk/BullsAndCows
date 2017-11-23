package com.example.notepad.bullsandcows;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.notepad.bullsandcows.Ui.activity.Activiteis.RegistrationPage;
import com.example.notepad.bullsandcows.Utils.CheckConnection;
import com.example.notepad.bullsandcows.Utils.Constants;
import com.example.notepad.bullsandcows.Utils.CustomFonts;
import com.example.notepad.bullsandcows.Utils.LoadNewVersionOfApp;

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

    public static final String IS_CHECKED_KEEP_PASSWORD = "isCheckedKeepPassword";
    public static final String DEFAULT_PASSWORD_FOR_GUEST = "1111";
    public static final int REGISTRATION_REQUEST_CODE = 3;
    TextView mInfoVersionTextView;
    TextView mWelcomeInformationTextView;
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
    Boolean mIsJoinToOnline;

    SharedPreferences mPreferencesFromWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initialization();
        if (CheckConnection.checkConnection(Welcome.this)) {
            new CheckingVersionOfApp().execute(BuildConfig.VERSION_NAME);
        }
        loadDataFromPreferences();
    }

    private void initialization() {
        mInfoVersionTextView = (TextView) findViewById(R.id.info_about_version_text_view);
        mVisitCheckVersion = (Button) findViewById(R.id.check_version_app_button);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegistrationButton = (Button) findViewById(R.id.registration_welcome_button);
        mLogin = (EditText) findViewById(R.id.login_welcome_page_edit_text);
        mPassword = (EditText) findViewById(R.id.password_welcome_page_edit_text);
        mCheckBox = (CheckBox) findViewById(R.id.keep_password_check_box);
        mWelcomeInformationTextView = (TextView) findViewById(R.id.welcome_text_text_view);
        mWelcomeInformationTextView.setTypeface(CustomFonts.getTypeFace(Welcome.this, CustomFonts.AASSUANBRK));


        View.OnClickListener clickButton = new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                switch (pView.getId()) {
                    case R.id.check_version_app_button:
                        if (CheckConnection.checkConnection(Welcome.this)) {
                            if (mCurrentVersionAppWelcome) {
                                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Kiolk/BullsAndCows"));
                                startActivity(intent1);
                            } else {
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
                                mCurrentVersionAppWelcome = true;
                                checkVersionOfApp();
                            }
                        } else {
                            CheckConnection.showToastAboutDisconnection(Welcome.this);
                        }
                        break;
                    case R.id.registration_welcome_button:
                        Intent intentRegistration = new Intent(Welcome.this, RegistrationPage.class);
                        intentRegistration.putExtra("nameOfUser", "");
                        intentRegistration.putExtra("password", "");
                        startActivityForResult(intentRegistration, REGISTRATION_REQUEST_CODE);
                        break;
                    case R.id.login_button:
                        String name = mLogin.getText().toString();
                        String password = mPassword.getText().toString();
                        if (name.length() > 0 && password.length() > 0) {
                            if (CheckConnection.checkConnection(Welcome.this)) {
                                new CheckUserLoginAndPassword().execute(name, password);
                            } else {
                                mIsJoinToOnline = false;
                                startMainActivity();
                                Toast.makeText(Welcome.this, "You continue offline game", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Welcome.this, "Login or password is wrong", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        mVisitCheckVersion.setOnClickListener(clickButton);
        mRegistrationButton.setOnClickListener(clickButton);
        mLoginButton.setOnClickListener(clickButton);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDataInPreferences();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REGISTRATION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //TODO find bug why not send information from Registration page
                    mLogin.setText(data.getStringExtra("nameOfUser"));
                    mPassword.setText(data.getStringExtra("password"));
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
//            new TypeWriter(Welcome.this).animateText(mInfoVersionTextView.getText());
            mVisitCheckVersion.setText(R.string.VISIT_SITE);
            mVisitCheckVersion.setEnabled(true);
            mVisitCheckVersion.setTextColor(Color.WHITE);
        } else {
            mInfoVersionTextView.setText("Your app is old version");
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

    private class CheckUserLoginAndPassword extends UserCheckExist {

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
            if (pBoolean == true) {
                Toast.makeText(Welcome.this, getResources().getString(R.string.SUCCESS_LOGGED), Toast.LENGTH_LONG).show();
                mIsJoinToOnline = true;
                startMainActivity();
            } else {
                Toast.makeText(Welcome.this, "Login or password is wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(Welcome.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.NIK_NAME_OF_USER, mLogin.getText().toString());
        intent.putExtra(Constants.PASSWORD_OF_USER,mPassword.getText().toString());
        intent.putExtra(Constants.KEEP_PASSWORD, mCheckBox.isChecked());
        intent.putExtra(Constants.JOIN_TO_ONLINE, mIsJoinToOnline);
        startActivity(intent);
        finish();
    }

    private void saveDataInPreferences() {
        mPreferencesFromWelcome = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferencesFromWelcome.edit();
        editor.putString(Constants.NIK_NAME_OF_USER, mLogin.getText().toString());
        editor.putString(Constants.PASSWORD_OF_USER, mPassword.getText().toString());
        editor.putBoolean(IS_CHECKED_KEEP_PASSWORD, mCheckBox.isChecked());
        editor.commit();
    }

    private void loadDataFromPreferences() {
        mPreferencesFromWelcome = getPreferences(MODE_PRIVATE);
        mCheckBox.setChecked(mPreferencesFromWelcome.getBoolean(IS_CHECKED_KEEP_PASSWORD, false));
        if (mCheckBox.isChecked()) {
            mLogin.setText(mPreferencesFromWelcome.getString(Constants.NIK_NAME_OF_USER, getResources().getString(R.string.GUEST)));
            mPassword.setText((mPreferencesFromWelcome.getString(Constants.PASSWORD_OF_USER, DEFAULT_PASSWORD_FOR_GUEST)));
        } else {
            mLogin.setText(getResources().getString(R.string.GUEST));
            mPassword.setText(DEFAULT_PASSWORD_FOR_GUEST);
//            mPassword.setEnabled(false);
        }
    }

}
