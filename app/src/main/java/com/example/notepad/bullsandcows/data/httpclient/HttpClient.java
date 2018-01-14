package com.example.notepad.bullsandcows.data.httpclient;

import com.example.notepad.bullsandcows.data.httpclient.models.HttpRequest;
import com.example.notepad.bullsandcows.data.httpclient.models.HttpResponse;
import com.example.notepad.bullsandcows.utils.IOCloseUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.notepad.bullsandcows.utils.Constants.UTF_8;

public class HttpClient {

    private static final String POST_REQUEST_METHOD = "POST";

    public HttpResponse post(final HttpRequest pRequest) {
        final HttpResponse gettingResponse = new HttpResponse();
        OutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader reader = null;

        try {
            final URL url = new URL(pRequest.getUrl());

            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(POST_REQUEST_METHOD);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            outputStream = httpURLConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
            if (pRequest.getParam() != null) {
                bufferedWriter.write(pRequest.getParam());
            } else {
                throw new IllegalArgumentException("Request without param");
            }
            bufferedWriter.flush();
            httpURLConnection.connect();

            // Read response
            final int respondCod = httpURLConnection.getResponseCode();
            final StringBuilder responseString = new StringBuilder();
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
        } catch (final IOException pE) {
            gettingResponse.setHttpError(pE);
        } finally {
            new IOCloseUtils().addForClose(bufferedWriter).addForClose(outputStream).addForClose(reader).close();
        }

        return gettingResponse;
    }
}
