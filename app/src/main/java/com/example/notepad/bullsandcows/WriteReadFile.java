package com.example.notepad.bullsandcows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class WriteReadFile {

    public void writeInFile(String pMoves, String pNikName, String pTime, String pNumberOfCodedDigits, Context pContext) {
//        FileOutputStream fOS = new FileOutputStream("Result.txt", MODE_PRIVATE);
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(("Result.txt", MODE_PRIVATE)));
        FileOutputStream fileOutputStream = null;
        Long timeOfWon = System.currentTimeMillis() + (3600000*3);
        Date date = new Date(timeOfWon);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        DateFormat dateFormat = simpleDateFormat;
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        String wonTimeString = dateFormat.format(date);
        try{
            fileOutputStream = pContext.openFileOutput("Result.txt", MODE_PRIVATE);
            String text = pNumberOfCodedDigits + "," + wonTimeString + "," + pNikName + ","  + pMoves + "," + pTime;
            fileOutputStream.write(text.getBytes());
            Toast.makeText(pContext, "Success save", Toast.LENGTH_LONG).show();
        } catch (IOException pE){
            pE.printStackTrace();
            Toast.makeText(pContext, pE.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            try{
                if(fileOutputStream != null){
                    fileOutputStream.close();
                }
            }catch (IOException pE){
                pE.printStackTrace();
                Toast.makeText(pContext, pE.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


//        try {
//            PrintWriter out = new PrintWriter("Result.txt");
//            out.println(pDate + "," + pNikName + "," + pTime);
//            out.close();
//        } catch (Exception pE) {
//            pE.printStackTrace();
//        }
    }

    public String readFile(Context pContext) {
        FileInputStream fileInputStream =null;
        try {
            fileInputStream = pContext.openFileInput("Result.txt");
            byte[] buff = new byte[fileInputStream.available()];
            fileInputStream.read(buff);
            String resultText = new String(buff);
            Toast.makeText(pContext, resultText, Toast.LENGTH_LONG).show();
            return resultText;
        } catch (
                Exception pE)

        {
            pE.printStackTrace();
            Toast.makeText(pContext, pE.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            try{
                fileInputStream.close();
            }catch (IOException pE){
                pE.printStackTrace();
                Toast.makeText(pContext, pE.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return "Not show";
    }

    public void writeSerileazedObject (RecordObject pRecordObject, Context pContext){
        try{
            FileOutputStream fOP = pContext.openFileOutput("Record.dat", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fOP);
            oos.writeObject(pRecordObject);
        }catch (IOException pE){
            pE.printStackTrace();
        }
    }

    public RecordObject readDeserileasedObject(Context pContext) {
            try {
                FileInputStream fIS = pContext.openFileInput("Record.dat");
                ObjectInputStream objectInputStream = new ObjectInputStream(fIS);
                RecordObject recordObject = (RecordObject)objectInputStream.readObject();
                return recordObject;
            }catch (Exception pE){
                pE.printStackTrace();
            }
    return null;
    }
}
