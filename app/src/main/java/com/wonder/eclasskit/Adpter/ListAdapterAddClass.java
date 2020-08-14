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
import com.wonder.eclasskit.Object.AddCourse;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterAddClass extends ArrayAdapter<AddCourse> {
    private static final String TAG="ListAdapteraddclass";
    private Context mContext;
    int mResource;
    private ArrayList<String> keys;
    private CallbackDelete mCallback;

    public interface CallbackDelete{
        void onHandledelete();
    }

    public ListAdapterAddClass(Context context, int resource, ArrayList<AddCourse> objects,ArrayList<String> keys,CallbackDelete callback) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.keys=keys;
        mCallback=callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String subname=getItem(position).getSubjectname();
        String subyear=getItem(position).getClass_yr();
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView namef=(TextView)convertView.findViewById(R.id.sub_name_txt);
        TextView timef=(TextView)convertView.findViewById(R.id.class_yr_txt);
        Button delete=(Button)convertView.findViewById(R.id.delete_course);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setMessage("Are you sure?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Teachers/"+ Common.uid+"/newcourse");

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

        namef.setText(subname);
        timef.setText(subyear);


        return convertView;
    }
}
