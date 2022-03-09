package com.ebook.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ebook.Adapter.VideoDataAdapter;
import com.ebook.Constant.Constant;
import com.ebook.Model.Video;
import com.ebook.R;
import com.ebook.Utility.Authorization;
import com.ebook.WebService.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentVideoList extends Fragment {

    private View view;
    private Context mContext;
    private SwipeRefreshLayout swiperefresh;
    private RecyclerView rvdata;
    private String SubjectID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_list, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            SubjectID = bundle.getString("SubjectID", null);
        }
        mContext = container.getContext();
        initView(view);
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

    private void getDataApiCall() {
        try {
            JSONObject mObject = new JSONObject();
            mObject.put("SubjectID", SubjectID);

            RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebServices.GetVideoList, mObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response : ", String.valueOf(response));
                    Constant.VideoList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONArray data = jsonObject.getJSONArray("Data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                Video video = new Video();

                                video.setID(obj.getString("ID"));
                                video.setName(obj.getString("Name"));
                                video.setURL(obj.getString("VideoUrl"));
                                video.setDateTime(obj.getString("DateTime"));

                                Constant.VideoList.add(video);
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
        String name = FragmentVideoList.class.getSimpleName();
        VideoDataAdapter dataAdapter = new VideoDataAdapter(mContext, Constant.VideoList, name);
        rvdata.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvdata.setHasFixedSize(true);
        rvdata.setDrawingCacheEnabled(true);
        rvdata.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvdata.setAdapter(dataAdapter);
    }

    private void initView(View view) {
        rvdata = (RecyclerView) view.findViewById(R.id.rvdata);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    }
}