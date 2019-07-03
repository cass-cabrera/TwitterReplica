package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@Parcel
public class Tweet {
    public String body;
    public long uuid;
    public User user;
    public String createdAt;
    public String date;

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uuid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.date = jsonObject.getString("created_at");
        return tweet;
    }


    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(date).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
