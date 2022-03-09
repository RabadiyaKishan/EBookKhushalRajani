package com.ebook.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebook.Adapter.DataAdapter;
import com.ebook.Constant.Constant;
import com.ebook.Model.PDF;
import com.ebook.R;
import com.ebook.Utility.Authorization;
import com.ebook.WebService.WebServices;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentPDFList extends Fragment {

    private View view;
    private Context mContext;
    private String SubjectID;
    private RecyclerView rvdata;
    private SwipeRefreshLayout swiperefresh;
    private DataAdapter dataAdapter;
    private InterstitialAd mInterstitialAd;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_p_d_f_list, container, false);
        mContext = container.getContext();
        mActivity = (Activity) container.getContext();
        initView(view);
        InterstitialAd();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            SubjectID = bundle.getString("SubjectID", null);
        }
        getDataApiCall();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataApiCall();
                swiperefresh.setRefreshing(false);
            }
        });
        return view;
    }

    private void InterstitialAd() {
        MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(mContext, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("Loaded", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("Error", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mActivity);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    private void initView(View view) {
        rvdata = (RecyclerView) view.findViewById(R.id.rvdata);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    }

    private void getDataApiCall() {
        try {
            JSONObject mObject = new JSONObject();
            mObject.put("SubjectID", SubjectID);

            RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebServices.GetPDFList, mObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response : ", String.valueOf(response));
                    Constant.PDFList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONArray data = jsonObject.getJSONArray("Data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                PDF pdf = new PDF();

                                pdf.setID(obj.getString("ID"));
                                pdf.setName(obj.getString("Name"));
                                pdf.setURL(obj.getString("PDFUrl"));
                                pdf.setDateTime(obj.getString("DateTime"));

                                Constant.PDFList.add(pdf);
                            }
                            setData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", Authorization.GetAuthorizationHeader(mContext));
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        String name = FragmentPDFList.class.getSimpleName();
        dataAdapter = new DataAdapter(mContext, Constant.PDFList, name);
        rvdata.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvdata.setHasFixedSize(true);
        rvdata.setDrawingCacheEnabled(true);
        rvdata.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvdata.setAdapter(dataAdapter);
    }
}