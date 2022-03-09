package com.ebook.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ebook.R;
import com.ebook.Utility.Authorization;

public class ActivitySplash extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = ActivitySplash.this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String AuthorizationHeader = Authorization.GetAuthorizationHeader(mContext);
                if (AuthorizationHeader != null) {
                    startActivity(new Intent(mContext, ActivityDashboard.class));
                } else {
                    startActivity(new Intent(ActivitySplash.this, ActivityLogin.class));
                }
                ActivitySplash.this.finish();
            }
        }, 4000);
    }
}