package com.sabayrean.welcom.profile;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sabayrean.welcom.R;
import com.sabayrean.welcom.app.AppConfig;
import com.sabayrean.welcom.app.AppController;
import com.sabayrean.welcom.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by LAYLeangsros on 13/07/2015.
 */
public class FramentProfile extends Fragment {

    private Double latitude, longitude;
    private TextView tvCity, tvCoutry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.profile_about, container, false);
        tvCity = (TextView) layout.findViewById(R.id.city);
        tvCoutry = (TextView) layout.findViewById(R.id.country);

        initValueAbout();



        return layout;
    }
    private void initValueAbout() {
        String tag_string_req = "req_login";
        final SessionManager pref = new SessionManager(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("This", "Login Response: " + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if(!object.getBoolean("error")) {
                        Geocoder geocoder;

                        latitude = object.getDouble("latitude");
                        longitude = object.getDouble("longitude");

                        List<Address> addresses = null;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());


                        Log.d("getttt", "ladf" + latitude);
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        //String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                        tvCity.setText(city);
                        tvCoutry.setText(country);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
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
                params.put("tag", "get_about");
                params.put("id", "" + pref.getId());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
}
