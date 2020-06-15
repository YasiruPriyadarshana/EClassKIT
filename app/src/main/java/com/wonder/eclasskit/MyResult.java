package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonder.eclasskit.Adpter.RecyclerAdapterMyResult;
import com.wonder.eclasskit.Object.Answer;
import com.wonder.eclasskit.Object.Result;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyResult extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Answer Myanswer;
    private TextView name;
    private TextView marks;
    private Answer answer;
    private String[] array;
    private String Name;
    private ArrayList<Result> results;
    private RecyclerAdapterMyResult adapter;
    private RecyclerView ResultListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_result);

        String key=getIntent().getStringExtra("key");
        databaseReference = FirebaseDatabase.getInstance().getReference("quizHome/"+key+"/Answer_Student/");
        name=(TextView)findViewById(R.id.myname);
        marks=(TextView)findViewById(R.id.mymarks);
        results=new ArrayList<>();
        readFile();
        viewName();
        viewResult();
    }

    private void viewName() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    answer = postSnapshot.getValue(Answer.class);
                    if (TextUtils.equals(answer.getName(),Name)) {
                        Myanswer = answer;
                    }
                }
                if (Myanswer!= null) {
                    name.setText(Myanswer.getName());
                    marks.setText(Myanswer.getResult());
                }else {
                    name.setText(Name);
                    marks.setText("Please attempt to the quiz");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void readFile(){
        try {
            FileInputStream fileInputStream = openFileInput("apprequirement.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer =new StringBuffer();


            String lines;
            while ((lines = bufferedReader.readLine()) != null){
                stringBuffer.append(lines + "\n");
            }
            String str =stringBuffer.toString();
            array = str.split(",");

            Name=array[0];

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void viewResult(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Answer answerM = postSnapshot.getValue(Answer.class);
                    Result result = new Result(answerM.getName(), answerM.getResult());
                    results.add(result);

                }


                adapter=new RecyclerAdapterMyResult(results,getApplicationContext());
                LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                ResultListView=(RecyclerView) findViewById(R.id.recyclerviewrank);
                ResultListView.setLayoutManager(layoutManager);
                ResultListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
