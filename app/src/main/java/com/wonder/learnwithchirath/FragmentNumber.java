package com.wonder.learnwithchirath;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentNumber extends Fragment {
    private Button bt_Number;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_number, container, false);


        bt_Number=(Button)view.findViewById(R.id.bt_number);

        bt_Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft= requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentplace, new FragmentEmail());
                ft.commit();
            }
        });

        return view;
    }
}
