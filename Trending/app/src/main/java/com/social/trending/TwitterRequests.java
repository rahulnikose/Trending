package com.social.trending;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xxnikosr on 2018-01-24.
 */

public class TwitterRequests {
    private static final String TAG = "TwitterRequests";

    public static String appAuthentication() {

        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder response = null;

        try {
            URL url = new URL(Constants.URL_AUTHENTICATION);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            String accessCredential = Constants.CONSUMER_KEY + ":"
                    + Constants.CONSUMER_SECRET;
            String authorization = "Basic "
                    + Base64.encodeToString(accessCredential.getBytes(),
                    Base64.NO_WRAP);
            String param = "grant_type=client_credentials";

            httpConnection.addRequestProperty("Authorization", authorization);
            httpConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            httpConnection.connect();

            outputStream = httpConnection.getOutputStream();
            outputStream.write(param.getBytes());
            outputStream.flush();
            outputStream.close();
            // int statusCode = httpConnection.getResponseCode();
            // String reason =httpConnection.getResponseMessage();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

        } catch (Exception e) {
            Log.e(TAG, "POST " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return response.toString();
    }

    public static String getClosestSupportedWOEID(String authorization,
                                                  double latitude,
                                                  double longitude,
                                                  Context context) {
        HttpURLConnection httpConnection = null;
        BufferedReader bufferedReader = null;

        StringBuilder response = new StringBuilder();
        String requestClosePlace = Constants.URL_CLOSEST_PLACE+
                "lat="+
                Double.toString(latitude)+
                "&long="+
                Double.toString(longitude);
        try {
            URL url = new URL(requestClosePlace);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");

            JSONObject jsonObjectDocument = new JSONObject(authorization);
            String token = jsonObjectDocument.getString("token_type") + " "
                    + jsonObjectDocument.getString("access_token");
            httpConnection.setRequestProperty("Authorization", token);

            httpConnection.setRequestProperty("Authorization", token);
            httpConnection.setRequestProperty("Content-Type",
                    "application/json");
            httpConnection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

        } catch (Exception e) {
            Log.e(TAG, "GET error: " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();

            }
        }

        return response.toString();
    }
    public static String getTrendsFor(String authorization,
                                      String trendsURL,
                                      Context context) {
        HttpURLConnection httpConnection = null;
        BufferedReader bufferedReader    = null;
        StringBuilder response           = new StringBuilder();

        try {

            URL url = new URL(trendsURL);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");

            // String jsonString = appAuthentication();

            JSONObject jsonObjectDocument = new JSONObject(authorization);
            String token = jsonObjectDocument.getString("token_type") + " "
                    + jsonObjectDocument.getString("access_token");
            httpConnection.setRequestProperty("Authorization", token);

            httpConnection.setRequestProperty("Authorization", token);
            httpConnection.setRequestProperty("Content-Type",
                    "application/json");
            httpConnection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

        } catch (Exception e) {
            Log.e(TAG, "GET " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }

        return response.toString();
    }
}
