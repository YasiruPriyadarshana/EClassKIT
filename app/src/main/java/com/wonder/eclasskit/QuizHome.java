package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.eclasskit.Adpter.ListAdapterQuizHome;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.QuizHm;


import java.util.ArrayList;

public class QuizHome extends AppCompatActivity implements ListAdapterQuizHome.CallbackDelete{
    private DatabaseReference databaseReference;
    private ListView QuizHometListView;
    private ListAdapterQuizHome adapter;
    private ArrayList<QuizHm> quizHms;
    private View v;
    private ArrayList<String> keys,times;
    private ListAdapterQuizHome.CallbackDelete anInterface;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);

        anInterface=this;
        databaseReference = FirebaseDatabase.getInstance().getReference("quizHome/"+ Common.uid);
        QuizHometListView=(ListView)findViewById(R.id.recyclerviewquizhome);
        QuizHometListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Common.limit == 1) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(
                            QuizHome.this);
                    adb.setMessage("Enter quiz password");
                    View v1 = getLayoutInflater().inflate(R.layout.textfield, null);
                    adb.setView(v1);
                    EditText e = (EditText) v1.findViewById(R.id.all_vriable);
                    adb.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("quizHome/"+Common.uid+"/"+keys.get(position));
                            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String passserver=dataSnapshot.child("password").getValue().toString();
                                    String pass=e.getText().toString();
                                    if (TextUtils.equals(passserver,"no")){
                                        Toast.makeText(QuizHome.this, "You can't attempt now", Toast.LENGTH_SHORT).show();
                                    }else if (TextUtils.equals(pass,passserver)){
                                        Intent intent = new Intent(QuizHome.this, quizMain.class);
                                        intent.putExtra("key", keys.get(position));
                                        intent.putExtra("time", times.get(position));
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(QuizHome.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                    adb.setNegativeButton("Cancel", null);
                    adb.show();
                }else{
                    Intent intent = new Intent(QuizHome.this, quizMain.class);
                    intent.putExtra("key", keys.get(position));
                    intent.putExtra("time", times.get(position));
                    startActivity(intent);
                }
            }
        });

        quizHms=new ArrayList<>();
        viewAllFiles();
    }

    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (i==1){
                    adapter.clear();
                }
                keys = new ArrayList<>();
                times=new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    QuizHm quizHm = postSnapshot.getValue(QuizHm.class);
                    quizHms.add(quizHm);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                    times.add(quizHm.getTime());
                }

                adapter = new ListAdapterQuizHome(QuizHome.this, R.layout.itemquizhome, quizHms,keys,anInterface);

                if (Common.limit != 1) {

                    if (QuizHometListView.getFooterViewsCount() > 0) {
                        QuizHometListView.removeFooterView(v);
                    }
                    v = getLayoutInflater().inflate(R.layout.footerviewquiz, null);

                    QuizHometListView.addFooterView(v);


                    Button updatequiz = (Button) v.findViewById(R.id.addnewquiz_home);
                    final EditText quiz_name = (EditText) v.findViewById(R.id.addquiz_name);
                    final EditText quiz_time = (EditText) v.findViewById(R.id.ttl_quiz_time);

                    updatequiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String quiz_txt = quiz_name.getText().toString();
                            String quiz_tm = quiz_time.getText().toString();
                            if (quiz_txt.isEmpty()) {
                                Toast.makeText(QuizHome.this, "Give Name", Toast.LENGTH_SHORT).show();
                            } else if(Integer.parseInt(quiz_tm) <=5){
                                Toast.makeText(QuizHome.this, "Give Time grater then 5 minutes", Toast.LENGTH_SHORT).show();
                            }else {
                                uplodeFile(quiz_txt, quiz_tm);
                            }
                        }
                    });

                }

                QuizHometListView.setAdapter(adapter);
                i=1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uplodeFile(String quiz,String time) {
        QuizHm quizHm=new QuizHm(quiz,"no",time);
        adapter.clear();
        databaseReference.child(databaseReference.push().getKey()).setValue(quizHm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(QuizHome.this, "New Quiz Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHandledelete() {
        adapter.clear();
    }
}
