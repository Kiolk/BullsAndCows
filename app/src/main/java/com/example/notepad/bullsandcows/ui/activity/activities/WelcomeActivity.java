package com.example.notepad.bullsandcows.ui.activity.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.bullsandcows.BuildConfig;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.AppInfoHolder;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpRequest;
import com.example.notepad.bullsandcows.data.managers.AppInfoManager;
import com.example.notepad.bullsandcows.data.managers.OnResultCallback;
import com.example.notepad.bullsandcows.data.managers.UserBaseManager;
import com.example.notepad.bullsandcows.ui.activity.fragments.UpdateAppFragment;
import com.example.notepad.bullsandcows.utils.CheckConnection;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.CustomFonts;
import com.example.notepad.bullsandcows.utils.animation.SlideAnimationUtil;
import com.example.notepad.myapplication.backend.VersionOfApp;
import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

import kiolk.com.github.pen.Pen;
import kiolk.com.github.pen.utils.MD5Util;

import static com.example.notepad.bullsandcows.utils.Constants.INT_FALSE_VALUE;
import static com.example.notepad.bullsandcows.utils.Constants.INT_TRUE_VALUE;

public class WelcomeActivity extends AppCompatActivity implements UpdateAppFragment.UpdateAppListener, View.OnClickListener {

    public static final String IS_CHECKED_KEEP_PASSWORD = "isCheckedKeepPassword";
    public static final String DEFAULT_PASSWORD_FOR_GUEST = "1111";
    public static final int REGISTRATION_REQUEST_CODE = 3;
    public static final String REQUEST_PARAM = "name";
    public static final String BACKGROUND_WELCOME_IMAGE = "https://media.mnn.com/assets/images/2015/02/highland-3.jpg";
    public static final String BACKGROUND_WELCOME_IMAGE_OTHER = "https://www.nationalgeographic.com/content/dam/photography/photos/000/252/25295.jpg";

    private VersionOfApp mVersionOfApp;

    private EditText mLogin;
    private EditText mPassword;

    private CheckBox mCheckBox;

    private UpdateAppFragment mUpdateFragment;

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;

    private FrameLayout mUpdateFrame;

    private SharedPreferences mWelcomePreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_welcome);

        initView();
        checkAppActualVersion();
        loadDataFromPreferences();
    }

    protected void checkAppActualVersion() {

        mUpdateFragment = new UpdateAppFragment();

        final AppInfoManager appManager = new AppInfoManager();

        appManager.getCurrentAppInfo(new HttpRequest(BuildConfig.BACKEND_APP_VERSION_URL, REQUEST_PARAM), new OnResultCallback<VersionOfApp>() {

            @Override
            public void onSuccess(final VersionOfApp pResult) {
                mVersionOfApp = pResult;
                AppInfoHolder.getInstance().setVersionApp(mVersionOfApp);
                final String version = AppInfoHolder.getInstance().getVersionApp().getVersionOfApp();
                if (!version.equals(String.valueOf(BuildConfig.VERSION_CODE))) {
                    showUpdateAppFragment();
                }
            }

            @Override
            public void onError(final Exception pException) {

            }
        });
    }

    private void initView() {

        final ImageView backgroundImage = findViewById(R.id.background_welcome_image);
        Pen.getInstance().getImageFromUrl(BACKGROUND_WELCOME_IMAGE).inputTo(backgroundImage);
        Pen.getInstance().getImageFromUrl(BACKGROUND_WELCOME_IMAGE_OTHER).inputTo(backgroundImage);

        final LinearLayout upperBlock = findViewById(R.id.upper_block_welcome_linear_layout);
        SlideAnimationUtil.slideInFromTop(this, upperBlock, null, SlideAnimationUtil.NORMAL);
        final LinearLayout middleBlock = findViewById(R.id.middle_block_welcome_linear_layout);
        SlideAnimationUtil.slideInFromRight(this, middleBlock, null, SlideAnimationUtil.NORMAL);

        final Button loginButton = findViewById(R.id.login_button);
        final Button registrationButton = findViewById(R.id.registration_welcome_button);
        final Button visitSiteButton = findViewById(R.id.visit_site_button);
        SlideAnimationUtil.slideInToTop(this, visitSiteButton, null, SlideAnimationUtil.NORMAL);
        mLogin = findViewById(R.id.login_welcome_page_edit_text);
        mPassword = findViewById(R.id.password_welcome_page_edit_text);
        mCheckBox = findViewById(R.id.keep_password_check_box);

        final TextView welcomeInformationTextView = findViewById(R.id.welcome_text_text_view);
        welcomeInformationTextView.setTypeface(CustomFonts.getTypeFace(this, CustomFonts.AASSUANBRK));

        registrationButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        mUpdateFrame = findViewById(R.id.for_update_fragment_frame_layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDataInPreferences();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
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
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveDataInPreferences() {
        mWelcomePreferences = getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor editor = mWelcomePreferences.edit();

        editor.putString(Constants.NIK_NAME_OF_USER, mLogin.getText().toString());
        editor.putBoolean(IS_CHECKED_KEEP_PASSWORD, mCheckBox.isChecked());

        editor.apply();
    }

    private void loadDataFromPreferences() {
        mWelcomePreferences = getPreferences(MODE_PRIVATE);
        mCheckBox.setChecked(mWelcomePreferences.getBoolean(IS_CHECKED_KEEP_PASSWORD, false));

        if (mCheckBox.isChecked()) {
            mLogin.setText(mWelcomePreferences.getString(Constants.NIK_NAME_OF_USER, getResources().getString(R.string.GUEST)));
        } else {
            mLogin.setText(getResources().getString(R.string.GUEST));
            mPassword.setText(DEFAULT_PASSWORD_FOR_GUEST);
        }
    }

    public void showUpdateAppFragment() {
        mUpdateFrame.setVisibility(View.VISIBLE);
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.for_update_fragment_frame_layout, mUpdateFragment);
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
    public void onClick(final View pView) {
        switch (pView.getId()) {
            case R.id.registration_welcome_button:
                final Intent intentRegistration = new Intent(this, RegistrationPageActivity.class);

                intentRegistration.putExtra(Constants.REGISTRATION_NAME_OF_USER, Constants.EMPTY_STRING);
                intentRegistration.putExtra(Constants.REGISTRATION_PASSWORD, Constants.EMPTY_STRING);

                startActivityForResult(intentRegistration, REGISTRATION_REQUEST_CODE);
                break;
            case R.id.login_button:
                final String name = mLogin.getText().toString();
                final String password = mPassword.getText().toString();

                if (!name.isEmpty() && !password.isEmpty()) {
                    if (CheckConnection.checkConnection(this)) {
                        checkCorrectUserInformation(name, password);
                    } else {
                        UserLoginHolder.getInstance().setLogged(false);
                        UserLoginHolder.getInstance().setUserName(mLogin.getText().toString());
                        UserLoginHolder.getInstance().setPassword(mPassword.getText().toString());

                        startMainActivity();

                        Toast.makeText(this, getString(R.string.CONTINUE_OFFLINE_GAME), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.LOGIN_OR_PASSWORD_WRONG), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void checkCorrectUserInformation(final String pName, final String pPassword) {
        final UserBaseManager userManager = new UserBaseManager();
        userManager.getUserInfo(null, pName, new OnResultCallback<UserDataBase>() {

            @Override
            public void onSuccess(final UserDataBase pUserInfo) {
                UserLoginHolder.getInstance().initHolder(pUserInfo);

                if (pUserInfo.getMSex() == null) {
                    final String token = tokenGeneration(pUserInfo);
                    pUserInfo.setMSex(token);

                    final UserBaseManager userBaseManager = new UserBaseManager();
                    userBaseManager.patchNewUserInformation(pUserInfo, new OnResultCallback<UserDataBase>() {

                        @Override
                        public void onSuccess(final UserDataBase pResult) {
                            if (token.equals(pUserInfo.getMSex())) {
                                UserLoginHolder.getInstance().initHolder(pUserInfo);
                                checkForSavingToken(pUserInfo.getUserName(), token);
                            }
                            Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.SUCCESS_LOGGED), Toast.LENGTH_LONG).show();
                            startMainActivity();
                        }

                        @Override
                        public void onError(final Exception pException) {
                            Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.TRY_LOGIN_LATE), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    UserLoginHolder.getInstance().initHolder(pUserInfo);
                    checkForSavingToken(pUserInfo.getUserName(), pUserInfo.getMSex());
                    Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.SUCCESS_LOGGED), Toast.LENGTH_LONG).show();
                    startMainActivity();
                }
            }

            @Override
            public void onError(final Exception pException) {
                Toast.makeText(WelcomeActivity.this, getString(R.string.LOGIN_OR_PASSWORD_WRONG), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected String tokenGeneration(final UserDataBase pUserInfo) {
        final String builderString = pUserInfo.getUserName() +
                pUserInfo.getPassword();

        return MD5Util.getHashString(builderString);
    }

    private void checkForSavingToken(final String userName, final String token) {

        if (mCheckBox.isChecked()) {
            UserLoginHolder.getInstance().keepUserData(null, null, INT_FALSE_VALUE);
            UserLoginHolder.getInstance().keepUserData(userName, token, INT_TRUE_VALUE);
        } else {
            UserLoginHolder.getInstance().keepUserData(null, null, INT_FALSE_VALUE);
        }
    }

    public void visitSite(final View view){
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Kiolk/BullsAndCows"));
        startActivity(intent);
    }
}
