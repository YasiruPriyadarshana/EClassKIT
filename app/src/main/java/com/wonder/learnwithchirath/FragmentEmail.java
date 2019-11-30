package com.wonder.learnwithchirath;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentEmail extends Fragment {
    private Button bt_Email;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_email, container, false);


        bt_Email=(Button)view.findViewById(R.id.bt_email);
        bt_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft=requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentplace, new FragmentClass(), null);
                ft.commit();
            }
        });

        return view;
    }


}
