package com.social.trending;

import android.content.Context;
import android.os.AsyncTask;

import static com.social.trending.Constants.URL_GLOBE_TRENDING;
import static com.social.trending.Constants.URL_TRENDS_FOR_WOEID;
import static com.social.trending.TwitterResponse.parseTrends;

/**
 * Created by xxnikosr on 2018-01-24.
 */

public class NetworkAsyncTask extends AsyncTask<String, Integer, String> {

    public static String TAG = "NetworkAsyncTask";
    public int GET_TRENDS_FOR = 0;
    public static boolean isRunning;
    @Override
    public void onPreExecute() {

        //Clear stale trend list.
        HomeActivity.ClearAdapter();
        HomeActivity.swipeRefreshLayout.setRefreshing(true);
        isRunning = true;
    }
    private Context mContext;

    public NetworkAsyncTask (Context context, int where){
        mContext = context;
        GET_TRENDS_FOR = where;

    }
    @Override
    public String doInBackground(String... params) {

        String authResponse = TwitterRequests.appAuthentication();

        switch(GET_TRENDS_FOR) {
            case Constants.GLOBE:
                String trends = TwitterRequests.getTrendsFor(
                        authResponse,
                        URL_GLOBE_TRENDING,
                        mContext);
                //Failed to retrieve trends
                if(trends != null ){
                    parseTrends(trends);
                }
                break;
            case Constants.ARROUND_ME:

                if(HomeActivity.mLocation != null ) {
                    String place = TwitterRequests.getClosestSupportedWOEID(
                            authResponse,
                            HomeActivity.mLocation.getLatitude(),
                            HomeActivity.mLocation.getLongitude(),
                            mContext);

                    String woeid = TwitterResponse.parseClosestPlace(place);
                    trends = TwitterRequests.getTrendsFor(
                            authResponse,
                            URL_TRENDS_FOR_WOEID+woeid,
                            mContext);

                    if(trends != null ){
                        parseTrends(trends);
                    }
                }
                break;
        }

        return null;
    }

    @Override
    public void onProgressUpdate(Integer... values) {

    }

    @Override
    public void onPostExecute(String result) {
        isRunning = false;
        HomeActivity.UpdateAdapter();
        HomeActivity.swipeRefreshLayout.setRefreshing(false);

    }
}
