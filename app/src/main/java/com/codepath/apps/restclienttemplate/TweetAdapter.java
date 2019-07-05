package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.w3c.dom.Text;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TweetAdapter  extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Tweet tweet;
    static Context context;

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);

        final ViewHolder viewHolder = new ViewHolder(tweetView, mTweets.get(i));
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        tweet = mTweets.get(i);


        viewHolder.tvUserName.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.date.setText(tweet.getRelativeTimeAgo());


        Glide.with(context).load(tweet.user.profileImageUrl).into(viewHolder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView date;
        public ImageButton like;
        public ImageButton retweet;
        TwitterClient client;


        public ViewHolder(final View itemView, final Tweet tweet) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvBody = itemView.findViewById(R.id.tvBody);
            date = itemView.findViewById(R.id.date);
            like = itemView.findViewById(R.id.likeButt);
            retweet = itemView.findViewById(R.id.retweetButt);
            client = TwitterApp.getRestClient(context);

            like.setOnClickListener(new View.OnClickListener() {
                Drawable globalWrap;
                @Override
                public void onClick(View v) {
                    if(!tweet.isLiked) {
                        client.likeTweet(tweet.uuid, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Drawable newDraw = itemView.getResources().getDrawable(R.drawable.ic_vector_heart);
                                globalWrap = DrawableCompat.wrap(newDraw);
                                DrawableCompat.setTint(globalWrap, Color.RED);
                                tweet.isLiked = !tweet.isLiked;
                                like.setImageDrawable(globalWrap);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.e("TweetAdapter", "Error liking tweet");                            }
                        });
                    }
                    else {
                         client.unlikeTweet(tweet.uuid, new AsyncHttpResponseHandler() {
                             @Override
                             public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                 Drawable newDraw = itemView.getResources().getDrawable(R.drawable.ic_vector_heart_stroke);
                                 globalWrap = DrawableCompat.wrap(newDraw);
                                 DrawableCompat.setTint(globalWrap, Color.BLACK);
                                 tweet.isLiked = !tweet.isLiked;
                                 like.setImageDrawable(globalWrap);
                             }

                             @Override
                             public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                 Log.e("TweetAdapter", "Error unliking tweet");
                             }
                         });
                    }
                }
            });

            retweet.setOnClickListener(new View.OnClickListener() {
                Drawable globalWrap;
                @Override
                public void onClick(View v) {
                    if(!tweet.isRetweet){
                        client.retweet(tweet.uuid, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Drawable newDraw = itemView.getResources().getDrawable(R.drawable.ic_vector_retweet);
                                globalWrap = DrawableCompat.wrap(newDraw);
                                DrawableCompat.setTint(globalWrap, Color.BLUE);
                                tweet.isRetweet = !tweet.isRetweet;
                                retweet.setImageDrawable(globalWrap);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.e("TweetAdapter", "Error retweeting tweet");
                            }
                        });
                    }
                    else {
                        client.unRetweet(tweet.uuid, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Drawable newDraw = itemView.getResources().getDrawable(R.drawable.ic_vector_retweet_stroke);
                                globalWrap = DrawableCompat.wrap(newDraw);
                                DrawableCompat.setTint(globalWrap, Color.BLACK);
                                tweet.isRetweet = !tweet.isRetweet;
                                retweet.setImageDrawable(globalWrap);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.e("TweetAdapter", "Error unretweeting tweet");
                            }
                        });
                    }
                }
            });

        }
    }
}
