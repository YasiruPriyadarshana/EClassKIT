package com.wonder.eclasskit;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wonder.eclasskit.Adpter.ListAdapterEvent;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Eventobj;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;


public class Event extends Fragment implements ListAdapterEvent.CallbackInterfaceE{
    private DatabaseReference databaseReference;
    private StorageReference storage;
    private ArrayList<Eventobj> eventobjs;
    private ListView EventListView;
    private ImageButton selectimage;
    private TextView event,desc,time;
    private Button updateEvent;
    private Uri imgUri;
    private byte[] data1;
    private String title_st,desc_st,time_st;
    private View v;
    private ListAdapterEvent adapter;
    private ListAdapterEvent.CallbackInterfaceE anInterface;
    private int day,month,year,hour,minute;
    private int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_event, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("event/"+ Common.uid);
        storage= FirebaseStorage.getInstance().getReference();
        EventListView=(ListView)v.findViewById(R.id.recyclerviewevent);
        eventobjs = new ArrayList<>();


        viewAllFiles();
        anInterface=this;
        return v;
    }


    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Eventobj uploadimg = postSnapshot.getValue(Eventobj.class);
                    eventobjs.add(uploadimg);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                }
//                Collections.reverse(eventobjs);

                adapter = new ListAdapterEvent(getContext(), R.layout.itemevent, eventobjs, keys,anInterface);


                if (Common.limit != 1) {

                    if (EventListView.getFooterViewsCount() > 0) {
                        EventListView.removeFooterView(v);
                    }
                    v = getLayoutInflater().inflate(R.layout.footerviewevent, null);

                    EventListView.addFooterView(v);

                    selectimage = (ImageButton) v.findViewById(R.id.evnt_imgbtn);
                    event = (TextView) v.findViewById(R.id.title_in);
                    desc = (TextView) v.findViewById(R.id.desc_in);
                    time = (Button) v.findViewById(R.id.date);
                    updateEvent = (Button) v.findViewById(R.id.add_event);

                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar c=Calendar.getInstance();
                            year = c.get(Calendar.YEAR);
                            month = c.get(Calendar.MONTH);
                            day = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    yearFinal=year;
                                    monthFinal=month+1;
                                    dayFinal=dayOfMonth;

                                    Calendar c=Calendar.getInstance();

                                    TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            hourFinal=hourOfDay;
                                            minuteFinal=minute;

                                            time.setText(""+yearFinal+"-"+monthFinal+"-"+dayFinal+" "+hourFinal+":"+minuteFinal);
                                        }
                                    }, hour, minute, false);
                                    timePickerDialog.show();
                                }
                            }, year, month, day);
                            datePickerDialog.show();
                        }
                    });

                    selectimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                selectIMG();
                            } else {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                            }
                        }
                    });
                    updateEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (imgUri != null) {
                                title_st = event.getText().toString();
                                desc_st = desc.getText().toString();
                                time_st = time.getText().toString();
                                if (title_st.isEmpty() || desc_st.isEmpty() || time_st.isEmpty()) {
                                    Toast.makeText(getContext(), "fill details", Toast.LENGTH_SHORT).show();
                                } else {
                                    uplodeFile(imgUri);
                                }
                            } else
                                Toast.makeText(getContext(), "select a File", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                EventListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectIMG();
        }else {
            Toast.makeText(getContext(), "please provide permission", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectIMG() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 76);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==76 && resultCode == RESULT_OK && data!=null) {
            imgUri=data.getData();

            String fullPath = Commons.getPath(imgUri,getContext());


            Toast.makeText(getContext(), "see: "+fullPath, Toast.LENGTH_SHORT).show();



            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imgUri);
                selectimage.setImageBitmap(bitmap);
            }catch (IOException e){
                Toast.makeText(getContext(), "fucked: "+e, Toast.LENGTH_SHORT).show();
            }




        } else {
            Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uplodeFile(final Uri imgUri) {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading File");
        progressDialog.show();

        adapter.clear();
        //imageuploade
        StorageReference reference2 =storage.child("Event/"+System.currentTimeMillis()+".png");
        reference2.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();

                while (!uri.isComplete());
                Uri p=uri.getResult();

                Eventobj eventobj=new Eventobj(title_st,desc_st,time_st,p.toString());

                databaseReference.child(databaseReference.push().getKey()).setValue(eventobj);
                Toast.makeText(getContext(), "Event Details uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                //pdf uplode

                //end
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double currentProgress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded: "+(int)currentProgress+"%");

            }
        });//end

    }

    @Override
    public void onHandleSelectionE() {
        adapter.clear();
    }
}
