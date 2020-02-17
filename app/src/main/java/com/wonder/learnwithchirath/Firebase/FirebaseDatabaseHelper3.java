package com.wonder.learnwithchirath.Firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wonder.learnwithchirath.Object.Answer;
import com.wonder.learnwithchirath.Object.Timetable;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper3 {
    private FirebaseDatabase tDatabase;
    private DatabaseReference tRefarenceStudents;

    private List<Timetable> timetables= new ArrayList<>();
    Uri url2;



    public interface  DataStatus{
        void DataIsLoaded(List<Timetable> timetables, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper3(String name) {
        tDatabase = FirebaseDatabase.getInstance();
        tRefarenceStudents= tDatabase.getReference("quizHome/"+name+"/Answer_Student");
    }



    public void addAnswerDetails(Answer answer, final FirebaseDatabaseHelper3.DataStatus dataStatus){

        String key = tRefarenceStudents.push().getKey();
        tRefarenceStudents.child(key).setValue(answer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void deleteAnswerDetails(String key,final FirebaseDatabaseHelper3.DataStatus dataStatus){
        tRefarenceStudents.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

}
