package com.wonder.learnwithchirath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class GetInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);

        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.add(R.id.fragmentplace, new FragmentName());
        fr.commit();
    }
}
