package com.wonder.eclasskit.Adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wonder.eclasskit.Object.Course;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterMyCourses  extends ArrayAdapter<Course> {
    private static final String TAG="ListAdapterCourses";
    private Context mContext;
    int mResource;
    private String db;
    private ArrayList<String> keys;
    private CallbackDelete mCallback;

    public interface CallbackDelete{
        void onHandledelete();
    }

    public ListAdapterMyCourses(Context context, int resource, ArrayList<Course> objects, ArrayList<String> key, String db,CallbackDelete callback) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.db=db;
        keys=key;
        mCallback=callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String course=getItem(position).getcName();
        String teacher=getItem(position).getcTeacher();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView course_txt=(TextView)convertView.findViewById(R.id.course_name_txt);
        TextView teacher_txt=(TextView)convertView.findViewById(R.id.course_teacher_txt);
        Button delete= (Button)convertView.findViewById(R.id.remove_course);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setMessage("Are you sure?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("student/"+db+"/courses");
                        mCallback.onHandledelete();
                        databaseReference.child(keys.get(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mContext, "Long press deleted", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                adb.setNegativeButton("No",null);
                adb.show();
            }
        });

        course_txt.setText(course);
        teacher_txt.setText(teacher);


        return convertView;
    }
}
