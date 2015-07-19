package com.sabayrean.welcom.world;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sabayrean.welcom.R;
import com.sabayrean.welcom.adapter.FeedAdapter;
import com.sabayrean.welcom.adapter.FeedItem;
import com.sabayrean.welcom.app.AppConfig;
import com.sabayrean.welcom.app.AppController;
import com.sabayrean.welcom.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LAYLeangsros on 15/07/2015.
 */
public class Feed extends Fragment {

    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private List<FeedItem> feedItems;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_feed, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);

        feedItems = new ArrayList<>();


        /**/
        adapter = new FeedAdapter(getActivity(), feedItems);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /**/


        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(AppConfig.URL_LOGIN);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            try {
                requestFeed();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return view;

    }

    private void requestFeed() throws JSONException {


        String tag_string_req = "req_login";
        final SessionManager pref = new SessionManager(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("This", "Login Response: " + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    parseJsonFeed(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                    createRequestErrorListener();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "feed");
                params.put("id", "" + pref.getId());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }


    private void createRequestErrorListener() {
        Log.e("Error", "getting date problem");
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(i);
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("saying"));
                //item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("time"));

                // url might be null sometimes
                //String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
                //item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
