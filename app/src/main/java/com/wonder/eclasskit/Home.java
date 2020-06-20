package com.wonder.eclasskit;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.eclasskit.Object.AddCourse;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Enroll;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class Home extends Fragment {
    private ImageButton quiz;
    private ImageButton timeTable;
    private Button copy;
    private EditText redeem;
    private DatabaseReference databaseReference,databaseRfTeacher;
    private String tKey;
    private FirebaseUser user;
    private Enroll enroll;
    private String year,subject;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        copy=(Button)view.findViewById(R.id.copy_redeem);
        redeem=(EditText)view.findViewById(R.id.redeem_code_txt);

        if (TextUtils.isEmpty(Common.uid)) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            Common.uid = user.getUid();

        }
        if (Common.limit == 1){
            redeem.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
        }
        Intent intent = requireActivity().getIntent();
        year=intent.getStringExtra("year");
        subject=intent.getStringExtra("sub");

        quiz=view.findViewById(R.id.quiz);
        timeTable=view.findViewById(R.id.classTimetb);

        databaseReference = FirebaseDatabase.getInstance().getReference("EnrollmentKey/");

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

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String redeemcd=redeem.getText().toString();
                String teacher=Common.uid;
                if (TextUtils.isEmpty(subject)){
                    databaseRfTeacher=FirebaseDatabase.getInstance().getReference("Teachers/"+Common.uid);
                    databaseRfTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            subject = dataSnapshot.child("subject").getValue().toString();
                            year = dataSnapshot.child("syear").getValue().toString();
                            enroll = new Enroll(teacher, "", subject, year);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {

                }
                databaseReference.child(redeemcd).setValue(enroll).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Redeem copied", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

//    public void readFile() {
//        try {
//            FileInputStream fileInputStream = requireActivity().openFileInput("teachercourse.txt");
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuffer stringBuffer = new StringBuffer();
//
//
//            String lines;
//            while ((lines = bufferedReader.readLine()) != null) {
//                stringBuffer.append(lines + "\n");
//            }
//            String str = stringBuffer.toString();
//            String[] array = str.split(",");
//            tKey=array[0];
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
