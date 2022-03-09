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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebook.Adapter.ShowDataAdapter;
import com.ebook.Constant.Constant;
import com.ebook.Model.Subjects;
import com.ebook.R;
import com.ebook.Utility.Authorization;
import com.ebook.WebService.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentSubject extends Fragment {

    private String name;
    private View view;
    private RecyclerView rvdata;
    private SwipeRefreshLayout swiperefresh;
    private ShowDataAdapter showDataAdapter;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subject, container, false);
        mContext = container.getContext();
        initView(view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name = bundle.getString("name", null);
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

    private void initView(View view) {
        rvdata = (RecyclerView) view.findViewById(R.id.rvdata);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
    }

    private void setData() {
        showDataAdapter = new ShowDataAdapter(mContext, Constant.SubjectList, name);
        rvdata.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvdata.setHasFixedSize(true);
        rvdata.setDrawingCacheEnabled(true);
        rvdata.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvdata.setAdapter(showDataAdapter);
    }

    private void getDataApiCall() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, WebServices.GetSubjectList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response : ", response);
                        Constant.SubjectList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("Data");
                            if (data != null && data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);

                                    Subjects subject = new Subjects();

                                    subject.setID(obj.getString("ID"));
                                    subject.setName(obj.getString("Name"));
                                    subject.setImage(obj.getString("Image"));
                                    subject.setDateTime(obj.getString("DateTime"));

                                    Constant.SubjectList.add(subject);
                                }
                                setData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error : ", "" + error);
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
    }
}