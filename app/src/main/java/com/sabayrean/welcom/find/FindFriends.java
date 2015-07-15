package com.sabayrean.welcom.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sabayrean.welcom.R;


/**
 * Created by LAYLeangsros on 12/07/2015.
 */


public class FindFriends extends Fragment {

    public FindFriends() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.find_friends, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }


}
