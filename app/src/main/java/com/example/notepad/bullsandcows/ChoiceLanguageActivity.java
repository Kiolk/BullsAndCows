package com.example.notepad.bullsandcows;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.LanguageLocale;

import java.util.Locale;

public class ChoiceLanguageActivity extends AppCompatActivity {

    private Button mEnglishButton;
    private Button mBelarusButton;
    private Locale mLocale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_language);
        mEnglishButton = (Button) findViewById(R.id.english_language_button);
        mBelarusButton = (Button) findViewById(R.id.belarus_language_button);

        View.OnClickListener clickButton = new View.OnClickListener() {

            @Override
            public void onClick(View pView) {
                String language = "en";
                Intent resultIntent = new Intent();
                switch(pView.getId()){
                    case R.id.english_language_button:
                        language = "en";
                        resultIntent.putExtra(Constants.CODE_OF_LANGUAGE, language);
                        break;
                    case R.id.belarus_language_button:
                        language = "be";
                        resultIntent.putExtra(Constants.CODE_OF_LANGUAGE, language);
                        break;
                    default:
                        break;
                }
//                setLocale(language);
                LanguageLocale.setLocale(language, ChoiceLanguageActivity.this);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        };

        mBelarusButton.setOnClickListener(clickButton);
        mEnglishButton.setOnClickListener(clickButton);


    }

//    private void setLocale(String pLanguage) {
//        if(pLanguage.equalsIgnoreCase("")){
//            return;
//        }
//        mLocale = new Locale(pLanguage);
////        saveLocale(pLanguage);
//        Locale.setDefault(mLocale);
//        Configuration configuration = new Configuration();
//        configuration.setLocale(mLocale);
//        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
////        updateText();
//    }
}
