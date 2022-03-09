package com.ebook.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ebook.BuildConfig;
import com.ebook.Constant.Constant;
import com.ebook.Fragment.FragmentHome;
import com.ebook.Fragment.FragmentPDFList;
import com.ebook.Fragment.FragmentSubject;
import com.ebook.Fragment.FragmentVideoList;
import com.ebook.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

public class ActivityDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private String UserName, Email, ImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mContext = ActivityDashboard.this;
        MobileAds.initialize(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        //displaySelectedScreen(R.id.nav_daily_tips);

        FragmentHome mFragmentHome = new FragmentHome();
        setFragment(mFragmentHome);

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        SharedPreferences sharedpreferences = mContext.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
        UserName = sharedpreferences.getString("Name", "");
        Email = sharedpreferences.getString("Email", "");
        ImageURL = sharedpreferences.getString("Image", "");

        View headerView = navigationView.getHeaderView(0);
        ImageView profile_image = (ImageView) headerView.findViewById(R.id.profile_image);
        TextView name = (TextView) headerView.findViewById(R.id.name);
        TextView email = (TextView) headerView.findViewById(R.id.email);

        name.setText(UserName);
        email.setText(Email);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_photo_user)
                .error(R.drawable.ic_photo_user);

        Glide.with(this).load(ImageURL).apply(options).into(profile_image);

        TextView version_code = findViewById(R.id.version_code);
        version_code.setText("Current Version " + versionName);
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(mContext)
                    .setIcon(R.mipmap.ic_dialog)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want exit ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_home:
                FragmentHome mFragmentHome = new FragmentHome();
                setFragment(mFragmentHome);
                break;
            case R.id.nav_pdf:
                FragmentSubject mFragmentSubject = new FragmentSubject();
                String name = FragmentPDFList.class.getSimpleName();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                mFragmentSubject.setArguments(bundle);
                setFragment(mFragmentSubject);
                break;
            case R.id.nav_video:
                FragmentSubject mFragmentSubjec = new FragmentSubject();
                String name_ = FragmentVideoList.class.getSimpleName();
                Bundle bundl = new Bundle();
                bundl.putString("name", name_);
                mFragmentSubjec.setArguments(bundl);
                setFragment(mFragmentSubjec);
                break;
            case R.id.nav_feedback:

                break;
            case R.id.nav_share:

                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen(item.getItemId());
        return true;
    }
}