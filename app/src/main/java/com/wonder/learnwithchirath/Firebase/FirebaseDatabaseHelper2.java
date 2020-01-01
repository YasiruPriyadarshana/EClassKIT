package com.wonder.learnwithchirath.Firebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wonder.learnwithchirath.Object.Timetable;

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
        tRefarenceStudents= tDatabase.getReference("timetable");
    }



    public void addClassDetails(Timetable timetable, final FirebaseDatabaseHelper2.DataStatus dataStatus){
        String key = tRefarenceStudents.push().getKey();
        tRefarenceStudents.child(key).setValue(timetable)
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
