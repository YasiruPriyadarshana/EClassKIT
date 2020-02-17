package com.wonder.learnwithchirath;


import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.learnwithchirath.Adpter.ListAdapterReview;
import com.wonder.learnwithchirath.Object.Answer;

import java.util.ArrayList;

public class ReviewQuiz extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ArrayList<Answer> answers;
    private ListView ReviewListView;
    private ListAdapterReview adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_quiz);
        databaseReference = FirebaseDatabase.getInstance().getReference("Answer_Student");
        ReviewListView=(ListView)findViewById(R.id.recyclerviewreview);
        answers=new ArrayList<>();
        viewAllFiles();
    }
    private void viewAllFiles() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Answer answer = postSnapshot.getValue(Answer.class);
                    answers.add(answer);

                }


                adapter = new ListAdapterReview(ReviewQuiz.this, R.layout.itemreview,answers);
                ReviewListView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
