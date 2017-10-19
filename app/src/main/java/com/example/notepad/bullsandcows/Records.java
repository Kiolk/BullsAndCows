package com.example.notepad.bullsandcows;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Records extends AppCompatActivity {

    TextView mResultTextView;
    ArrayList<String> mCodNumbers = new ArrayList<>();
    ArrayList<String> mDateWon = new ArrayList<>();
    ArrayList<String> mNikName = new ArrayList<>();
    ArrayList<String> mMovesForWon = new ArrayList<>();
    ArrayList<String> mTimeNeed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        mResultTextView = (TextView) findViewById(R.id.records_text_view);
        WriteReadFile writeReadFile = new WriteReadFile();
        mResultTextView.setText(writeReadFile.readFile(this));
//        RecordObject recordObject = writeReadFile.readDeserileasedObject(this);
//        String resultText = recordObject.getNikName();
//        mResultTextView.setText(resultText);
        String oneInformationAboutWon = writeReadFile.readFile(this);
        String[] detailsAboutOneWon = new String[5];
        detailsAboutOneWon = oneInformationAboutWon.split(",");
        mCodNumbers.add(0, detailsAboutOneWon[0]);
        mDateWon.add(0, detailsAboutOneWon[1]);
        mNikName.add(0, detailsAboutOneWon[2]);
        mMovesForWon.add(0, detailsAboutOneWon[3]);
        mTimeNeed.add(0, detailsAboutOneWon[4]);
        ListView listRecords = (ListView) findViewById(R.id.list_of_record_list_view);
        CustomAdapterForRecords customAdapterForRecords = new CustomAdapterForRecords( mCodNumbers, mDateWon, mNikName, mMovesForWon, mTimeNeed, getApplicationContext());
        listRecords.setAdapter(customAdapterForRecords);
    }
}
