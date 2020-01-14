package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Object.Eventobj;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class ListAdapterEvent extends ArrayAdapter<Eventobj> {
    private static final String TAG="ListAdapterEvent";
    private Context mContext;
    int mResource;
    private ArrayList<String> keys;
    private DatabaseReference tRefarenceStudents;
    private CallbackInterfaceE mCallback;

    public interface CallbackInterfaceE{
        void onHandleSelectionE();
    }
    public ListAdapterEvent(Context context, int resource, ArrayList<Eventobj> objects,ArrayList<String> keys,CallbackInterfaceE mCallback) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.mCallback=mCallback;
        this.keys=keys;
        tRefarenceStudents= FirebaseDatabase.getInstance().getReference("event");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String title=getItem(position).getTitle();
        String descrip=getItem(position).getDecription();
        String time=getItem(position).getTime();
        String uri=getItem(position).getUri();
        final String key=keys.get(position);

        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView titlee=(TextView)convertView.findViewById(R.id.title_event);
        TextView descripe=(TextView)convertView.findViewById(R.id.descrip_event);
        TextView timee=(TextView)convertView.findViewById(R.id.time_event);
        ImageView imageV=(ImageView)convertView.findViewById(R.id.image_event);



        titlee.setText(title);
        descripe.setText(descrip);
        timee.setText(time);


        Picasso.with(getContext()).load(uri).into(imageV);


        Button delete=(Button)convertView.findViewById(R.id.delete_event);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tRefarenceStudents.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mCallback.onHandleSelectionE();
                        Toast.makeText(mContext, "Event removed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return convertView;
    }
}
