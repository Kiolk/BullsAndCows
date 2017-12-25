package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.NotePad.myapplication.backend.VersionOfApp;
import com.example.notepad.bullsandcows.BuildConfig;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.AppInfoHolder;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpRequest;
import com.example.notepad.bullsandcows.data.managers.AppInfoCallbacks;
import com.example.notepad.bullsandcows.data.managers.AppInfoManager;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.data.managers.UserLoginCallback;
import com.example.notepad.bullsandcows.ui.activity.fragments.UpdateAppFragment;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

public class WelcomeActivity extends AppCompatActivity implements UpdateAppFragment.UpdateAppListener, View.OnClickListener {

    public static final String IS_CHECKED_KEEP_PASSWORD = "isCheckedKeepPassword";
    public static final String DEFAULT_PASSWORD_FOR_GUEST = "1111";
    public static final int REGISTRATION_REQUEST_CODE = 3;

    private VersionOfApp mVersionOfApp;
    private EditText mLogin;
    private EditText mPassword;
    private CheckBox mCheckBox;
    private UpdateAppFragment mUpdateFragment;
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private FrameLayout mUpdateFrame;

    SharedPreferences mWelcomePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_welcome);

        UserLoginHolder.getInstance().setUserOnline();

        initView();
        checkAppActualVersion();
        loadDataFromPreferences();
    }

    protected void checkAppActualVersion() {
        AppInfoManager appManager = new AppInfoManager();

        appManager.getCurrentAppInfo(new HttpRequest(BuildConfig.BACKEND_APP_VERSION_URL, "name"), new AppInfoCallbacks() {

            @Override
            public void getInfoAppCallback(VersionOfApp versionOfApp) {
                mVersionOfApp = versionOfApp;
                AppInfoHolder.getInstance().setVersionApp(mVersionOfApp);
                String version = AppInfoHolder.getInstance().getVersionApp().getVersionOfApp();

                if (!version.equals(String.valueOf(BuildConfig.VERSION_CODE))) {
                    showUpdateAppFragment();
                }
            }
        });
    }


    private void initView() {
        Button loginButton = findViewById(R.id.login_button);
        Button registrationButton = findViewById(R.id.registration_welcome_button);
        mLogin = findViewById(R.id.login_welcome_page_edit_text);
        mPassword = findViewById(R.id.password_welcome_page_edit_text);
        mCheckBox = findViewById(R.id.keep_password_check_box);
        TextView welcomeInformationTextView = findViewById(R.id.welcome_text_text_view);
        welcomeInformationTextView.setTypeface(CustomFonts.getTypeFace(WelcomeActivity.this, CustomFonts.AASSUANBRK));

        registrationButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        mUpdateFragment = new UpdateAppFragment();
        mUpdateFrame = findViewById(R.id.for_update_fragment_frame_layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (UserLoginHolder.getInstance().getUserInfo() != null) {
            UserLoginHolder.getInstance().setOffline();
        }
        saveDataInPreferences();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REGISTRATION_REQUEST_CODE:

                if (resultCode == RESULT_OK) {
                    mLogin.setText(data.getStringExtra(Constants.REGISTRATION_NAME_OF_USER));
                    mPassword.setText(data.getStringExtra(Constants.REGISTRATION_PASSWORD));
                }

                break;
            default:
                break;
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        UserLoginHolder.getInstance().keepUserOnline();
        startActivity(intent);
        finish();
    }

    private void saveDataInPreferences() {
        mWelcomePreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mWelcomePreferences.edit();

        editor.putString(Constants.NIK_NAME_OF_USER, mLogin.getText().toString());
        editor.putString(Constants.PASSWORD_OF_USER, mPassword.getText().toString());
        editor.putBoolean(IS_CHECKED_KEEP_PASSWORD, mCheckBox.isChecked());

        editor.apply();
    }

    private void loadDataFromPreferences() {
        mWelcomePreferences = getPreferences(MODE_PRIVATE);
        mCheckBox.setChecked(mWelcomePreferences.getBoolean(IS_CHECKED_KEEP_PASSWORD, false));

        if (mCheckBox.isChecked()) {
            mLogin.setText(mWelcomePreferences.getString(Constants.NIK_NAME_OF_USER, getResources().getString(R.string.GUEST)));
            mPassword.setText((mWelcomePreferences.getString(Constants.PASSWORD_OF_USER, DEFAULT_PASSWORD_FOR_GUEST)));
        } else {
            mLogin.setText(getResources().getString(R.string.GUEST));
            mPassword.setText(DEFAULT_PASSWORD_FOR_GUEST);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserLoginHolder.getInstance().keepUserOnline();
    }

    public void showUpdateAppFragment() {
        mUpdateFrame.setVisibility(View.VISIBLE);
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.for_update_fragment_frame_layout, mUpdateFragment);
        //TODO Why do need use commitAllowingStateLoss?
        mFragmentTransaction.commitAllowingStateLoss();
        mFragmentManager = getFragmentManager();
        mFragmentManager.executePendingTransactions();
    }

    private void closeUpdateAppFragment() {
        mUpdateFrame.setVisibility(View.GONE);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.remove(mUpdateFragment);
        mFragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    @Override
    public void closeUpdateFragment() {
        closeUpdateAppFragment();
        finish();
    }

    @Override
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.registration_welcome_button:
                Intent intentRegistration = new Intent(WelcomeActivity.this, RegistrationPageActivity.class);

                intentRegistration.putExtra(Constants.REGISTRATION_NAME_OF_USER, Constants.EMPTY_STRING);
                intentRegistration.putExtra(Constants.REGISTRATION_PASSWORD, Constants.EMPTY_STRING);

                startActivityForResult(intentRegistration, REGISTRATION_REQUEST_CODE);
                break;
            case R.id.login_button:
                String name = mLogin.getText().toString();
                final String password = mPassword.getText().toString();

                if (name.length() > 0 && password.length() > 0) {
                    if (CheckConnection.checkConnection(WelcomeActivity.this)) {

                        UserBaseManager userManager = new UserBaseManager();
                        userManager.getUserInfo(null, name, new UserLoginCallback() {
                            @Override
                            public void getUserInfoCallback(UserDataBase pUserInfo) {
                                if (pUserInfo != null && password.equals(pUserInfo.getPassword())) {
                                    UserLoginHolder.getInstance().initHolder(pUserInfo);
                                    Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.SUCCESS_LOGGED), Toast.LENGTH_LONG).show();
                                    startMainActivity();
                                } else {
                                    Toast.makeText(WelcomeActivity.this, getString(R.string.LOGIN_OR_PASSWORD_WRONG), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        UserLoginHolder.getInstance().setLogged(false);
                        UserLoginHolder.getInstance().setUserName(mLogin.getText().toString());
                        UserLoginHolder.getInstance().setPassword(mPassword.getText().toString());

                        startMainActivity();

                        Toast.makeText(WelcomeActivity.this, getString(R.string.CONTINUE_OFFLINE_GAME), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(WelcomeActivity.this, getString(R.string.LOGIN_OR_PASSWORD_WRONG), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
