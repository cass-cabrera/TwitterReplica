package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class TweetAdapter  extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tweet tweet = mTweets.get(i);


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

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvBody = itemView.findViewById(R.id.tvBody);
            date = itemView.findViewById(R.id.date);
        }
    }
}
