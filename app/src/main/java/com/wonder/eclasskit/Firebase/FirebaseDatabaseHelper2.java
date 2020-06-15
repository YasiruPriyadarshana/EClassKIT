package com.wonder.eclasskit.Firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Timetable;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper2 {
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

    public FirebaseDatabaseHelper2() {
        tDatabase = FirebaseDatabase.getInstance();
        tRefarenceStudents= tDatabase.getReference("timetable/"+ Common.uid);
    }



    public void addClassDetails(Timetable timetable, final FirebaseDatabaseHelper2.DataStatus dataStatus){
        String day=timetable.getDate();
        String grade=timetable.getGrade();
        if (grade.equals("Grade 9 (English Medium)")){
            grade="1_";
        }else if (grade.equals("Grade 10(English Medium)")) {
            grade="2_";
        } else if (grade.equals("Grade 11(English Medium)")) {
            grade="3_";
        }

        if (day.equals("Monday")) {
            day="1";
        } else if (day.equals("Tuesday")) {
            day="2";
        } else if (day.equals("Wednesday")) {
            day="3";
        } else if (day.equals("Thursday")) {
            day="4";
        } else if (day.equals("Friday")) {
            day="5";
        } else if (day.equals("Saturday")) {
            day="6";
        } else if (day.equals("Sunday")) {
            day="7";
        }
        String key = tRefarenceStudents.push().getKey();
        tRefarenceStudents.child(day+grade+key).setValue(timetable)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void deleteClassDetails(String key,final DataStatus dataStatus){
        tRefarenceStudents.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }


}
