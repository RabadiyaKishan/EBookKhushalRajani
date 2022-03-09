package com.ebook.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ebook.Adapter.HomeAdapter;
import com.ebook.Constant.Constant;
import com.ebook.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentHome extends Fragment {

    String UserName, Email, ImageURL;
    private Context mContext;
    private View view;
    private AdView mAdView;
    private GridView GridMenu;
    private Intent mIntent;
    private Activity mActivity;
    private CircleImageView user_photo;
    private TextView user_name, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = container.getContext();
        mActivity = (Activity) container.getContext();
        MobileAds.initialize(mContext);
        MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d("adError", adError.toString());
                Toast.makeText(mContext, "" + adError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        FindViewByID();
        Body();
        return view;
    }

    private void Body() {
        String[] Name = {
                "Home", "PDF", "Videos", "Feedback", "Share", "Logout"
        };
        int[] Images = {
                R.drawable.home, R.drawable.pdf, R.drawable.video, R.drawable.feedback, R.drawable.share, R.drawable.logout
        };

        SharedPreferences sharedpreferences = mContext.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
        UserName = sharedpreferences.getString("Name", "");
        Email = sharedpreferences.getString("Email", "");
        ImageURL = sharedpreferences.getString("Image", "");

        user_name.setText(UserName);
        email.setText(Email);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_photo_user)
                .error(R.drawable.ic_photo_user);

        Glide.with(this).load(ImageURL).apply(options).into(user_photo);

        HomeAdapter gridAdapter = new HomeAdapter(mActivity, mContext, Name, Images);
        GridMenu.setAdapter(gridAdapter);
    }

    private void FindViewByID() {
        GridMenu = view.findViewById(R.id.GridMenu);
        user_name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.email);
        user_photo = view.findViewById(R.id.user_photo);
    }

}