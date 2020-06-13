package com.wonder.learnwithchirath;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class VideoHome extends Fragment  {

    private View v1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v1 = inflater.inflate(R.layout.fragment_videohome, container, false);
        // Inflate the layout for this fragment

        return v1;
    }




}
