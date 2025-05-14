package com.bookstore.users.util;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmailValidator {
    private static final String API_KEY = "c6433435f7b8478fbfb03e70494e6a93";
    public static boolean isEmailValid(String email) {
        try {
            final String urlStr = String.format( "https://emailvalidation.abstractapi.com/v1/?api_key=%s&email=%s",
                    API_KEY, email
            );
            URL url = new URL(urlStr);
            JSONObject jsonObject = getObject(url);

            return jsonObject.getJSONObject("is_valid_format").getBoolean("value");
        } catch (Exception e) {
            System.out.printf("Error during %s validation %s\n", email, e.getMessage());
            return false;
        }
    }

    private static JSONObject getObject(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        InputStream inputStream;

        if (status >= 200 && status < 300) {
            inputStream = connection.getInputStream();
        }

        else {
            inputStream = connection.getErrorStream();
        }

        BufferedReader buffer  = new BufferedReader(
                new InputStreamReader(inputStream)
        );

        StringBuilder response  = new StringBuilder();

        String line;
        while ((line = buffer.readLine()) != null) {
            response.append(line);
        }
        buffer.close();

        JSONObject jsonObject = new JSONObject(response.toString());
        return jsonObject;
    }
}
