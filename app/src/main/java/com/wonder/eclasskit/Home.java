package com.wonder.eclasskit;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private TextView coursename;
    private DatabaseReference databaseReference,databaseRfTeacher;
    private String tKey;
    private FirebaseUser user;
    private Enroll enroll;
    private String year,subject,tname,delkey;
    private String intentenroll;
    private int i=0;


    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        copy=(Button)view.findViewById(R.id.copy_redeem);
        redeem=(EditText)view.findViewById(R.id.redeem_code_txt);
        quiz=view.findViewById(R.id.quiz);
        timeTable=view.findViewById(R.id.classTimetb);
        coursename=view.findViewById(R.id.course_Home_txt);

        if (TextUtils.isEmpty(Common.uid)) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            Common.uid = user.getUid();
            Common.uidmain = Common.uid;
        }
        if (Common.limit == 1){
            Common.uidmain = Common.uid;
            redeem.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
            readStudentId();
            coursename.setText(Common.stcoursename);
        }





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
                Intent intent=new Intent(getActivity(), TimeTable.class);
                startActivity(intent);
            }
        });
        if (Common.limit != 1){
            Intent intent = requireActivity().getIntent();
            year=intent.getStringExtra("year");
            subject=intent.getStringExtra("sub");
            intentenroll=intent.getStringExtra("enroll");
            if (!TextUtils.isEmpty(intentenroll)){
                i=1;
            }
            setEnrollment();
        }

        return view;
    }



    private void readStudentId(){
        try {
            FileInputStream fileInputStream = requireActivity().openFileInput("student.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer =new StringBuffer();


            String lines;
            while ((lines = bufferedReader.readLine()) != null){
                stringBuffer.append(lines + "\n");
            }
            String str =stringBuffer.toString();
            Common.studentId =str;

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setEnrollment(){

        //show emrollment key
        databaseRfTeacher = FirebaseDatabase.getInstance().getReference("Teachers/" + Common.uidmain + "/Main");
        if (TextUtils.isEmpty(redeem.getText().toString())) {
            if (i == 1) {
                DatabaseReference databaseRfTeacher2 = FirebaseDatabase.getInstance().getReference("Teachers/" + Common.uidmain);
                databaseRfTeacher2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        coursename.setText(dataSnapshot.child("/newcourse/" + Common.uid + "/subjectname").getValue().toString());
                        if (dataSnapshot.hasChild("/newcourse/" + Common.uid + "/sredeem")) {
                            String setkey = dataSnapshot.child("/newcourse/" + Common.uid + "/sredeem").getValue().toString();
                            redeem.setText(setkey);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            } else {
                databaseRfTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("sredeem")) {
                            coursename.setText(dataSnapshot.child("subject").getValue().toString());
                            String setkey = dataSnapshot.child("sredeem").getValue().toString();
                            tname = dataSnapshot.child("name").getValue().toString();
                            redeem.setText(setkey);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }}

//add emrollment key
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String redeemcd=redeem.getText().toString();
                String teacher=Common.uid;

                //main course
                if (redeemcd.isEmpty()) {
                    Toast.makeText(getActivity(), "Give Enrollment Key", Toast.LENGTH_SHORT).show();
                }else if (redeemcd.length() < 6){
                    Toast.makeText(getActivity(), "Too Short (need 6 character)", Toast.LENGTH_SHORT).show();
                }else {
                    if (TextUtils.isEmpty(subject)){
                        databaseRfTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChild("name")){
                                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                                    adb.setMessage("Please set teacher name");
                                    adb.setPositiveButton("Set now", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent1=new Intent(getActivity(), AboutTeacher.class);
                                            startActivity(intent1);
                                        }
                                    });
                                    adb.setNegativeButton("Cancel",null);

                                    adb.show();
                                }else if (!dataSnapshot.hasChild("subject")){
                                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                                    adb.setMessage("Please set subject name");
                                    adb.setPositiveButton("Set now", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent2=new Intent(getActivity(),AddNewCourse.class);
                                            startActivity(intent2);
                                        }
                                    });
                                    adb.setNegativeButton("Cancel",null);

                                    adb.show();
                                }else {
                                    subject = dataSnapshot.child("subject").getValue().toString();
                                    year = dataSnapshot.child("syear").getValue().toString();
                                    tname = dataSnapshot.child("name").getValue().toString();
                                    enroll = new Enroll(teacher, tname, subject, year);
                                    if (dataSnapshot.hasChild("sredeem")) {
                                        delkey = dataSnapshot.child("sredeem").getValue().toString();
                                        databaseReference.child(delkey).setValue(null);
                                    }
                                    databaseReference.child(redeemcd).setValue(enroll).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            subject = "";
                                            Toast.makeText(getActivity(), "Enroll key copied", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    databaseRfTeacher.child("sredeem").setValue(redeemcd);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {

                        //other coursese
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Teachers/"+Common.uidmain+"/newcourse/"+Common.uid);
                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("sredeem")){
                                    delkey =  dataSnapshot.child("sredeem").getValue().toString();
                                    databaseReference.child(delkey).setValue(null);
                                    //fix ignore same value ---------------------------------------------
                                }

                                enroll = new Enroll(teacher, tname, subject, year);
                                databaseReference.child(redeemcd).setValue(enroll).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Enroll key copied For Subject", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                databaseRef.child("sredeem").setValue(redeemcd);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                }
            }
        });
    }

}
