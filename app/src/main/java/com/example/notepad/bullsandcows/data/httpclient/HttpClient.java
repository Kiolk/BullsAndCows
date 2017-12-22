package com.example.notepad.bullsandcows.data.httpclient;

import com.example.notepad.bullsandcows.data.httpclient.models.HttpRequest;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    public HttpResponse post(HttpRequest pRequest) {
        HttpResponse gettingResponse = new HttpResponse();
        OutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(pRequest.getUrl());

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            outputStream = httpURLConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            if (pRequest.getParam() != null) {
                bufferedWriter.write(pRequest.getParam());
            } else {
                throw new IllegalArgumentException("Request without param");
            }
            bufferedWriter.flush();
            httpURLConnection.connect();

            // Read response
            int respondCod = httpURLConnection.getResponseCode();
            StringBuilder responseString = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                responseString.append(line);
            }

            if (respondCod == HttpURLConnection.HTTP_OK) {
                gettingResponse.setResponse(responseString.toString());
                gettingResponse.setHttpOk(true);
            } else {
                gettingResponse.setHttpOk(false);
            }
        } catch (IOException pE) {
            pE.printStackTrace();
            gettingResponse.setHttpError(pE);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gettingResponse;
    }
}
