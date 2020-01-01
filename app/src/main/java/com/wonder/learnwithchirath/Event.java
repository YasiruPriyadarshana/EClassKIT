package com.wonder.learnwithchirath;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Adpter.ListAdapterEvent;
import com.wonder.learnwithchirath.Object.Eventobj;
import com.wonder.learnwithchirath.Object.UploadPDF;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;


public class Event extends Fragment {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_event, container, false);


        databaseReference = FirebaseDatabase.getInstance().getReference("event");
        storage= FirebaseStorage.getInstance().getReference();
        EventListView=(ListView)v.findViewById(R.id.recyclerviewevent);
        eventobjs = new ArrayList<>();
        viewAllFiles();
        return v;
    }


    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Eventobj uploadimg = postSnapshot.getValue(Eventobj.class);
                    eventobjs.add(uploadimg);
                }


                adapter = new ListAdapterEvent(getContext(), R.layout.itemevent, eventobjs);

                if (EventListView.getFooterViewsCount() > 0)
                {
                    EventListView.removeFooterView(v);
                }
                v = getLayoutInflater().inflate(R.layout.footerviewevent, null);
                EventListView.addFooterView(v);


                selectimage = (ImageButton) v.findViewById(R.id.evnt_imgbtn);
                event = (TextView) v.findViewById(R.id.title_in);
                desc = (TextView) v.findViewById(R.id.desc_in);
                time = (TextView) v.findViewById(R.id.time_in);
                updateEvent = (Button) v.findViewById(R.id.add_event);
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
                            title_st=event.getText().toString();
                            desc_st=desc.getText().toString();
                            time_st=time.getText().toString();
                            if (title_st.isEmpty() || desc_st.isEmpty() || time_st.isEmpty()){
                                Toast.makeText(getContext(), "fill details", Toast.LENGTH_SHORT).show();
                            }else {
                                uplodeFile(imgUri);
                            }
                        } else
                            Toast.makeText(getContext(), "select a File", Toast.LENGTH_SHORT).show();
                    }
                });

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

        //imageuploade
        StorageReference reference2 =storage.child("uploads3/"+System.currentTimeMillis()+".png");
        reference2.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();

                while (!uri.isComplete());
                Uri p=uri.getResult();

                Eventobj eventobj=new Eventobj(title_st,desc_st,time_st,p.toString());

                databaseReference.child(databaseReference.push().getKey()).setValue(eventobj);
                Toast.makeText(getContext(), "PDF File upladed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                //pdf uplode

                //end
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double currentProgress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Upoaded: "+(int)currentProgress+"%");
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                adapter.clear();
            }
        });;//end

    }
}
