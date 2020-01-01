package com.wonder.learnwithchirath;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wonder.learnwithchirath.Adpter.CWebVideoView;


public class Home extends Fragment {
    WebView videoView;
    CWebVideoView cWebVideoView;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        String url="https://www.youtube.com/watch?time_continue=20&v=78l-iSI--Es";

        videoView = (WebView) view.findViewById(R.id.video);
        videoView.setVisibility(View.VISIBLE);
        cWebVideoView = new CWebVideoView(getContext(), videoView);
        cWebVideoView.load(url);






        return view;

    }

}
