package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cz.msebera.android.httpclient.Header;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

public class ComposeActivity extends AppCompatActivity {

    EditText edit;
    Button button;
    TwitterClient client;
    TextView charCount;

    public static final String RESULT_TWEET_KEY = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        edit = findViewById(R.id.editText);
        client = TwitterApp.getRestClient(this);
        charCount = findViewById(R.id.charCount);
        button = findViewById(R.id.button);

        charCount.setText("280");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
            }
        });

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                charCount.setText(Integer.toString(280 - edit.getText().length()));
            }
        });
    }

    private void sendTweet() {
        client.sendTweet(edit.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200) {
                    try {
                        JSONObject responseJson = new JSONObject(new String(responseBody));
                        Tweet tweet = Tweet.fromJSON(responseJson);

                        Intent resultData = new Intent();
                        resultData.putExtra(RESULT_TWEET_KEY, Parcels.wrap(tweet));

                        setResult(RESULT_OK, resultData);
                        finish();
                    } catch (JSONException e) {
                        Log.e("ComposeActivity", "Error parsing response1");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("ComposeActivity", "Error parsing response2");
            }
        });
    }


}
