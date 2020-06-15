package com.wonder.eclasskit.Adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.wonder.eclasskit.Object.Eventobj;
import com.wonder.eclasskit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    public View getView(int position,View convertView, ViewGroup parent) {

        String title=getItem(position).getTitle();
        String descrip=getItem(position).getDecription();
        final String time=getItem(position).getTime();
        final String uri=getItem(position).getUri();
        final String key=keys.get(position);

        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView titlee=(TextView)convertView.findViewById(R.id.title_event);
        TextView descripe=(TextView)convertView.findViewById(R.id.descrip_event);
        ImageView imageV=(ImageView)convertView.findViewById(R.id.image_event);


        final TextView day=(TextView)convertView.findViewById(R.id.days);
        final TextView hour=(TextView)convertView.findViewById(R.id.hours);
        final TextView minute=(TextView)convertView.findViewById(R.id.minutes);
        final TextView second=(TextView)convertView.findViewById(R.id.seconds);
        final TextView event=(TextView)convertView.findViewById(R.id.event);

        titlee.setText(title);
        descripe.setText(descrip);
        final Handler handler = new Handler();
        final View convertV=convertView;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,1000);
                try {
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date futureDate = dateFormat.parse(time);
                    Date currentDate = new Date();

                    if (!currentDate.after(futureDate)){
                        long diff =futureDate.getTime() - currentDate.getTime();

                        long days = diff / (24*60*60*1000);
                        diff -= days*(24*60*60*1000);
                        long hours = diff / (60*60*1000);
                        diff -= hours*(60*60*1000);
                        long minutes = diff / (60*1000);
                        diff -= minutes*(60*1000);
                        long seconds = diff/1000;

                        day.setText(""+ String.format("%02d", days));
                        hour.setText(""+ String.format("%02d", hours));
                        minute.setText(""+ String.format("%02d", minutes));
                        second.setText(""+ String.format("%02d", seconds));
                    }else {
                        Date temp = futureDate;
                        Calendar c = Calendar.getInstance();
                        c.setTime(temp);
                        c.add(Calendar.DATE, 1);
                        temp = c.getTime();

                        if (!currentDate.after(temp)){
                            event.setText("The Event Started!");
                        }else {
                            event.setText("The Event Ended!");
                        }
                        convertV.findViewById(R.id.LL1).setVisibility(View.GONE);
                        convertV.findViewById(R.id.LL2).setVisibility(View.GONE);
                        convertV.findViewById(R.id.LL3).setVisibility(View.GONE);
                        convertV.findViewById(R.id.LL4).setVisibility(View.GONE);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
        handler.postDelayed(runnable, 1*1000);

//        timee.setText(time);


        Picasso.with(getContext()).load(uri).into(imageV);


        Button delete=(Button)convertView.findViewById(R.id.delete_event);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mCallback.onHandleSelectionE();
                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri);
                                photoRef.delete();
                                tRefarenceStudents.child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(mContext, "Event removed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

            }
        });

        return convertView;
    }

    private void countDownStart() {
    }


}
