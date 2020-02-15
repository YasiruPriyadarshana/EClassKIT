package com.wonder.learnwithchirath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.wonder.learnwithchirath.Adpter.ListAdapterQuiz;
import com.wonder.learnwithchirath.Adpter.ListAdapterQuizList;
import com.wonder.learnwithchirath.Object.Common;
import com.wonder.learnwithchirath.Object.Quizobj;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class quizMain extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView QuizListView;
    private ArrayList<Quizobj> uploadQuizS;
    private ListAdapterQuizList adapter;
    private ListAdapterQuiz adapterQuiz;
    private View v;
    TabLayout mTabLayout;
    ViewPager mPager;
    private Context context;
    private List<FragmentQuestion> fragmentlist=new ArrayList<>();
    private Button next;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        databaseReference = FirebaseDatabase.getInstance().getReference("quiz");

        mTabLayout=findViewById(R.id.quiztablayout);
        mPager=findViewById(R.id.quizviewpager);
        next=findViewById(R.id.addnew_quest);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=mPager.getCurrentItem()+1;
                if (position==adapterQuiz.getCount()){
                    Intent intent=new Intent(quizMain.this,MainActivity.class);
                    startActivity(intent);
                }

                mPager.setCurrentItem(position);

            }
        });

        context=this;

        viewAllFiles();
    }


    private void viewAllFiles() {
        uploadQuizS = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Quizobj uploadQuizobj = postSnapshot.getValue(Quizobj.class);
                    uploadQuizS.add(uploadQuizobj);
                }


                adapter = new ListAdapterQuizList(uploadQuizS);

                LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
                QuizListView = (RecyclerView) findViewById(R.id.recyclerquizlist);
                QuizListView.setLayoutManager(layoutManager);

                QuizListView.setAdapter(adapter);

                getFragmentList();

                adapterQuiz= new ListAdapterQuiz(getSupportFragmentManager(),1,fragmentlist);
                mPager.setAdapter(adapterQuiz);
                mTabLayout.setupWithViewPager(mPager);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getFragmentList() {
        for (int i=0;i<uploadQuizS.size();i++){
            FragmentQuestion fragmentQuestion=new FragmentQuestion(uploadQuizS.get(i),i);
            fragmentlist.add(fragmentQuestion);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_quest) {
            Intent intent=new Intent(this,addQuestions.class);
            startActivity(intent);
            return true;
        }if (id == R.id.review_quest) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
