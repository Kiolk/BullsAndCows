package com.example.notepad.bullsandcows;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

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

public class ServletPostAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    Context mContext;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String pS) {
        super.onPostExecute(pS);
        Toast.makeText(mContext, pS, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(Pair<Context, String>... pPairs) {
        mContext = pPairs[0].first;
        String name = pPairs[0].second;
        try {

            URL url = new URL("http://10.0.2.2:8080/hello");
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
