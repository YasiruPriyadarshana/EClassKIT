package com.wonder.learnwithchirath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.learnwithchirath.Adpter.ListAdapterQuizHome;
import com.wonder.learnwithchirath.Object.QuizHm;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class QuizHome extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ListView QuizHometListView;
    private ListAdapterQuizHome adapter;
    private ArrayList<QuizHm> quizHms;
    private View v;
    ArrayList<String> keys,times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        databaseReference = FirebaseDatabase.getInstance().getReference("quizHome");
        QuizHometListView=(ListView)findViewById(R.id.recyclerviewquizhome);
        QuizHometListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(QuizHome.this,quizMain.class);
                intent.putExtra("key",keys.get(position));
                intent.putExtra("time",times.get(position));
                startActivity(intent);
            }
        });
        quizHms=new ArrayList<>();
        viewAllFiles();
    }

    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys = new ArrayList<>();
                times=new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    QuizHm quizHm = postSnapshot.getValue(QuizHm.class);
                    quizHms.add(quizHm);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                    times.add(quizHm.getTime());
                }

                adapter = new ListAdapterQuizHome(QuizHome.this, R.layout.itemquizhome, quizHms);

                if (QuizHometListView.getFooterViewsCount() > 0)
                {
                    QuizHometListView.removeFooterView(v);
                }
                v = getLayoutInflater().inflate(R.layout.footerviewquiz, null);
                QuizHometListView.addFooterView(v);


                Button updatequiz = (Button) v.findViewById(R.id.addnewquiz_home);
                final EditText quiz_name=(EditText)v.findViewById(R.id.addquiz_name);
                final EditText quiz_time=(EditText)v.findViewById(R.id.ttl_quiz_time);

                updatequiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            String quiz_txt=quiz_name.getText().toString();
                            String quiz_tm=quiz_time.getText().toString();
                            if (quiz_txt.isEmpty() && quiz_tm.isEmpty()){
                                Toast.makeText(QuizHome.this, "fill details", Toast.LENGTH_SHORT).show();
                            }else {
                                uplodeFile(quiz_txt,quiz_tm);
                            }
                    }
                });

                QuizHometListView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uplodeFile(String quiz,String time) {
        QuizHm quizHm=new QuizHm(quiz,time);
        databaseReference.child(databaseReference.push().getKey()).setValue(quizHm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(QuizHome.this, "New Quiz Added", Toast.LENGTH_SHORT).show();
                adapter.clear();
            }
        });
    }
}
