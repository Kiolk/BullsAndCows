package com.example.notepad.bullsandcows;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Boolean mCurrentVersionAppWelcome;
    String mNameOfUserWelcome;
    Button mConfirmNameButton;
    Button mVisitCheckVersion;
    String mNewVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mUserName = (EditText) findViewById(R.id.user_name_welcome_edit_text);
        mConfirmNameButton = (Button) findViewById(R.id.welcome_confirm_nik_name_button);
        mInfoVersionTextView = (TextView) findViewById(R.id.info_about_version_text_view);
        mVisitCheckVersion = (Button) findViewById(R.id.check_version_app_button);

        Intent intent = getIntent();
        mCurrentVersionAppWelcome = intent.getBooleanExtra("version", false);
        mNameOfUserWelcome = intent.getStringExtra("nikOfUser");
        mUserName.setText(mNameOfUserWelcome);

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
                        mCurrentVersionAppWelcome = true;
                        checkVersionOfApp();
                    default:
                        break;
                }
            }
        };
        mConfirmNameButton.setOnClickListener(clickButton);
        mVisitCheckVersion.setOnClickListener(clickButton);
        new CheckingVersionOfApp().execute(BuildConfig.VERSION_NAME);
    }

    public void checkVersionOfApp() {
        if (mCurrentVersionAppWelcome) {
            mInfoVersionTextView.setText("Your app in actual version");
//            mVisitCheckVersion.setBackgroundColor(Color.YELLOW);
//            mVisitCheckVersion.setText("Visit site of project");
//            mVisitCheckVersion.setTextSize(40);
        } else {
            mInfoVersionTextView.setText("Your app is old version");
            mVisitCheckVersion.setBackgroundColor(Color.RED);
            mVisitCheckVersion.setText("Press for upgrade yor upp for version " + mNewVersion);
            mVisitCheckVersion.setTextSize(40);
        }
    }

    class CheckingVersionOfApp extends AsyncTask <String, Void, String>{

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
                String versionOfAnnOnPhone = "" + BuildConfig.VERSION_CODE;
                if(!(appActualVersion.equalsIgnoreCase(versionOfAnnOnPhone))){

                    mCurrentVersionAppWelcome = false;
                }
                checkVersionOfApp();
            }catch (JSONException pE){
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
}
