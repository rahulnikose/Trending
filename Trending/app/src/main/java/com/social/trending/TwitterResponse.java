package com.social.trending;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xxnikosr on 2018-01-24.
 */

public class TwitterResponse {
    public static String TAG = "TwitterResponse";

    public static String parseClosestPlace(String responseTrends) {

        try {
            JSONArray wrapper = new JSONArray(responseTrends);
            JSONObject trendsObject = wrapper.getJSONObject(0);
            return trendsObject.getString("woeid");

        } catch (JSONException e) {
            Log.e(TAG, "JSONException " + e);
        }
        return null;
    }

    public static String parseCountry(String responseTrends) {

        try {
            JSONArray wrapper = new JSONArray(responseTrends);
            JSONObject trendsObject = wrapper.getJSONObject(0);
            return trendsObject.getString("country");

        } catch (JSONException e) {
            Log.e(TAG, "JSONException " + e);
        }
        return null;
    }

    //This method Access static HomeActivity.TrendList
    //Synchronized will allow only one async task to execute it.
    public static synchronized  List<TrendDetail>  parseTrends(String responseTrends) {

        try
        {
            JSONArray wrapper = new JSONArray(responseTrends);
            JSONObject trendsObject = wrapper.getJSONObject(0);
            String asOf = trendsObject.getString("as_of");
            JSONArray trendsArray = trendsObject.getJSONArray("trends");
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < trendsArray.length(); i++) {
                JSONObject trend = trendsArray.getJSONObject(i);

                TrendDetail trendDetail = new TrendDetail();

                trendDetail.count = Integer.toString(i + 1);
                trendDetail.name = (String) trend.getString("name");
                //trendDetail.url = trend.getString("url");
                //trendDetail.promoted_content = trend.getString("promoted_content");
                //trendDetail.query = trend.getString("query");
                trendDetail.tweet_volume = trend.getString("tweet_volume");

                //Add this detail into list
                HomeActivity.TrendList.add(trendDetail);
            }
        } catch(JSONException e) {
            Log.e(TAG, "JSONException " + e);
        }
        return null;

    }
}
