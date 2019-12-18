package com.wonder.learnwithchirath;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefarenceStudents;
    private List<Student> students= new ArrayList<>();

    public interface  DataStatus{
        void DataIsLoaded(List<Student> students, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mRefarenceStudents= mDatabase.getReference("student");
    }

    public void addstudent(Student student,final DataStatus dataStatus){
        String key = mRefarenceStudents.push().getKey();
        mRefarenceStudents.child(key).setValue(student)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
}
