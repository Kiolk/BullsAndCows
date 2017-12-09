package com.example.notepad.bullsandcows.ui.activity.activiteis;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.notepad.bullsandcows.ChoiceLanguageActivity;
import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.holders.UserLoginHolder;
import com.example.notepad.bullsandcows.utils.Constants;

public class SettingActivity extends AppCompatActivity {

    public static int numberOfDigits = 4;
    Button modButton;
    private Button mChoiceLanguage;
    static boolean mode;
//    EditText mNikEditText;
    //comm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserLoginHolder.getInstance().setUserOnline();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_setting);
        modButton = (Button) findViewById(R.id.modeButton);
//        mNikEditText = (EditText) findViewById(R.id.set_nik_edit_text);
        mChoiceLanguage = (Button) findViewById(R.id.choice_language_button);

        Intent intent = getIntent();
        numberOfDigits = intent.getIntExtra(Constants.CODED_DIGITS, 4);
        mode = intent.getBooleanExtra("modeState", mode);
        if (!mode) {
            modButton.setText("Night mode");
        } else {
            modButton.setText("Daily mode");
        }
//        mNikEditText.setText(intent.getStringExtra("nikOfUser"));
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("" + numberOfDigits);



        View.OnClickListener clickButton = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.modeButton:
                        if (!mode) {
                            modButton.setText("Daily mode");
                            mode = true;
                        } else {
                            modButton.setText("Night mode");
                            mode = false;
                        }
                        break;
                    case R.id.choice_language_button:
                        Intent intent2 = new Intent(SettingActivity.this, ChoiceLanguageActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        };
        modButton.setOnClickListener(clickButton);
        mChoiceLanguage.setOnClickListener(clickButton);
    }



    public void submitMinus(View view) {
        if (numberOfDigits > 1) {
            numberOfDigits -= 1;
            TextView text = (TextView) findViewById(R.id.text);
            text.setText("" + numberOfDigits);
        }
    }

    public void submitPlus(View view) {
        if (numberOfDigits < 10) {
            numberOfDigits += 1;
            TextView text = (TextView) findViewById(R.id.text);
            text.setText("" + numberOfDigits);
        }
    }

    public void submitApply(View view) {
        TextView text = (TextView) findViewById(R.id.text);
        String number = text.getText().toString();
        Intent intent3 = new Intent();
        intent3.putExtra(Constants.CODED_DIGITS, number);
        intent3.putExtra(Constants.MODE_STATE, mode);
//        intent3.putExtra("nikOfUser", mNikEditText.getText().toString());
        setResult(RESULT_OK, intent3);
        UserLoginHolder.getInstance().setOffline();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLoginHolder.getInstance().setOffline();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserLoginHolder.getInstance().setOffline();
    }
}
