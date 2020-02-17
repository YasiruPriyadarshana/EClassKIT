package com.wonder.learnwithchirath;


import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.learnwithchirath.Adpter.ListAdapterReview;
import com.wonder.learnwithchirath.Adpter.RecyclerAdapterReview;
import com.wonder.learnwithchirath.Object.Answer;
import com.wonder.learnwithchirath.Object.Review;

import java.util.ArrayList;

public class ReviewQuiz extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ArrayList<Answer> answers;
    private ListView ReviewListView;
    private ListAdapterReview adapter;
    private RecyclerView ReviewAnsListView;
    private RecyclerAdapterReview adapterReview;
    private ArrayList<Review> reviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_quiz);
        databaseReference = FirebaseDatabase.getInstance().getReference("Answer_Student");
        ReviewListView=(ListView)findViewById(R.id.recyclerviewreview);
        answers=new ArrayList<>();
        reviews=new ArrayList<>();
        viewAllFiles();

    }

    private void viewAllFiles2() {
        ReviewAnsListView=(RecyclerView)findViewById(R.id.recyclerreviewlist);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        ReviewAnsListView.setLayoutManager(layoutManager);
        ReviewAnsListView.setLayoutManager(new GridLayoutManager(ReviewQuiz.this,3));
        adapterReview=new RecyclerAdapterReview(reviews);
        ReviewAnsListView.setAdapter(adapterReview);
    }

    private void viewAllFiles() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=1;
                double correct=0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Answer answer = postSnapshot.getValue(Answer.class);
                    answers.add(answer);
                    if (answer.getAnswer().get(i)>0){
                        correct++;
                    }
                    Review review=new Review(i,(correct/3.0)*100.0);
                    i++;
                    reviews.add(review);
                }





                adapter = new ListAdapterReview(ReviewQuiz.this, R.layout.itemreview,answers);
                ReviewListView.setAdapter(adapter);
                viewAllFiles2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
