package com.wonder.learnwithchirath;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wonder.learnwithchirath.Object.Common;


public class Home extends Fragment {
    private ImageButton quiz;
    private ImageButton timeTable;
    private FirebaseUser user;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        user= FirebaseAuth.getInstance().getCurrentUser();
        Common.uid= user.getUid();

        quiz=view.findViewById(R.id.quiz);
        timeTable=view.findViewById(R.id.classTimetb);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),QuizHome.class);
                startActivity(intent);
            }
        });
        timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Class.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
