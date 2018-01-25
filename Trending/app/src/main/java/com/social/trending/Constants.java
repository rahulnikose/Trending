package com.social.trending;

/**
 * Created by xxnikosr on 2018-01-24.
 */

public class Constants {

    public static final String URL_ROOT_TWITTER_API = "https://api.twitter.com";
    public static final String URL_SEARCH = URL_ROOT_TWITTER_API + "/1.1/search/tweets.json?q=";
    public static final String URL_AUTHENTICATION = URL_ROOT_TWITTER_API + "/oauth2/token";
    public static final String CONSUMER_KEY = "Ke9NwpCcakwfGqSMpX2ycTZRi";
    public static final String CONSUMER_SECRET = "LgDs09SiqjJ4V6solYXOQW7kt0KWrLXoG1cbyCIt63W0cRThvS";

    public static final String URL_INDIA_TRENDING = "https://api.twitter.com/1.1/trends/place.json?id=23424977";
    public static final String URL_GLOBE_TRENDING = "https://api.twitter.com/1.1/trends/place.json?id=1";
    public static final String URL_TRENDS_FOR_WOEID = "https://api.twitter.com/1.1/trends/place.json?id=";
    public static final String URL_CLOSEST_PLACE = "https://api.twitter.com/1.1/trends/closest.json?";

    public static final int ARROUND_ME = 0;
    public static final int GLOBE      = 1;


}
