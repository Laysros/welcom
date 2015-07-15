package com.sabayrean.welcom.world;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sabayrean.welcom.R;

/**
 * Created by LAYLeangsros on 15/07/2015.
 */
public class Feed extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_feed, container, false);


        // Inflate the layout for this fragment
        return view;
    }
}
