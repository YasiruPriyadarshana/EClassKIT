package com.wonder.learnwithchirath;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.wonder.learnwithchirath.Adpter.ListAdapter;
import com.wonder.learnwithchirath.Adpter.ListAdpterVideo;
import com.wonder.learnwithchirath.Object.Common;
import com.wonder.learnwithchirath.Object.UploadPDF;
import com.wonder.learnwithchirath.Object.UploadVideo;
import com.wonder.learnwithchirath.video.VideoPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import in.gauriinfotech.commons.Commons;

import static android.app.Activity.RESULT_OK;


public class VideoHome extends Fragment  {

    private View v1;
    private ListView VideoListView;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    private ArrayList<UploadVideo> uploadVideos;
    private ImageButton selectFile;
    private TextView nameUpfile;
    private Bitmap bitmap;
    private Uri videoUri;
    private byte[] data1;
    private static String name;
    private Button uplode;
    private Uri url2;
    private View v,v2;
    private ListAdpterVideo adapter;
    private ArrayList<String> keys;
    private int set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v1 = inflater.inflate(R.layout.fragment_videohome, container, false);
        // Inflate the layout for this fragment




        databaseReference = FirebaseDatabase.getInstance().getReference("Videos/"+ Common.uid);
        storage= FirebaseStorage.getInstance().getReference();

        VideoListView=(ListView)v1.findViewById(R.id.recyclerviewvideo);

        uploadVideos = new ArrayList<>();
        viewAllFiles();

        VideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadVideo uploadVideo = uploadVideos.get(position);

                Intent intent = new Intent(getActivity(), VideoPlayer.class);
                intent.putExtra("name",uploadVideo.getvName());
                intent.putExtra("url",uploadVideo.getVideourl());
                startActivity(intent);
            }
        });
/*
        PDFListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos=position;
                AlertDialog.Builder adb = new AlertDialog.Builder(
                        Notes.this);
                adb.setMessage("Are you sure?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UploadPDF uploadP=uploadPDFS.get(pos-1);
                        Toast.makeText(Notes.this, "link: "+uploadP.getImgurl(), Toast.LENGTH_SHORT).show();
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(uploadP.getImgurl());
                        photoRef.delete();
                        StorageReference pdfRef = FirebaseStorage.getInstance().getReferenceFromUrl(uploadP.getUrl());
                        pdfRef.delete();
                        adapter.clear();
                        databaseReference.child(keys.get(pos-1)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Notes.this, "Long press deleted", Toast.LENGTH_SHORT).show();
                            }
                        });

                        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("comments/");
                        databaseReference.child(uploadP.getName().substring(0, uploadP.getName().length() - 4)).removeValue();
                    }
                });
                adb.setNegativeButton("No",null);
                adb.show();

                return true;
            }
        });


 */

        return v1;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            selectVideo();
        }else {
            Toast.makeText(getActivity(), "please provide permission", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectVideo() {
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 56);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==56 && resultCode == RESULT_OK && data!=null) {
            videoUri=data.getData();
            String name1=queryName(getActivity().getContentResolver(),videoUri);


            name=name1;


            String fullPath = Commons.getPath(videoUri,getContext());

            nameUpfile.setText(name1);
            Toast.makeText(getActivity(), "see: "+fullPath, Toast.LENGTH_SHORT).show();


            Bitmap bitmap1 = ThumbnailUtils.createVideoThumbnail(fullPath, MediaStore.Video.Thumbnails.MINI_KIND);
            selectFile.setImageBitmap(bitmap1);










            selectFile.setDrawingCacheEnabled(true);
            selectFile.buildDrawingCache();
            Bitmap bitmap = selectFile.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            data1 = baos.toByteArray();
        } else {
            Toast.makeText(getActivity(), "Please Select A Video", Toast.LENGTH_SHORT).show();
        }
    }

    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    UploadVideo uploadVideo = postSnapshot.getValue(UploadVideo.class);
                    uploadVideos.add(uploadVideo);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                }


                adapter = new ListAdpterVideo(getActivity(),R.layout.itemvideo,uploadVideos);

                if (VideoListView.getFooterViewsCount() > 0)
                {
                    VideoListView.removeFooterView(v);
                }

                v=getLayoutInflater().inflate(R.layout.footerviewvideo, null);
                VideoListView.addFooterView(v);

                selectFile=(ImageButton)v.findViewById(R.id.vidImgBtn);
                nameUpfile=(TextView)v.findViewById(R.id.nameofVfile);
                uplode=(Button)v.findViewById(R.id.vUpdate_btn);
                selectFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                            selectVideo();
                        }else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                        }
                    }
                });
                uplode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (videoUri!=null) {
                            uplodeFile(videoUri);

                        }else
                            Toast.makeText(getActivity(), "select a File", Toast.LENGTH_SHORT).show();
                    }
                });
                VideoListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uplodeFile(final Uri vidUri) {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading File");
        progressDialog.show();

        //videoimage
        StorageReference reference2 =storage.child("videos/"+System.currentTimeMillis()+".png");
        reference2.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                url2=uri.getResult();

                Uri p=vidUri;
                //video uplode
                StorageReference reference =storage.child("videos/"+name);
                reference.putFile(p).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url=uri.getResult();

                        UploadVideo uploadVideo=new UploadVideo(url.toString(),name,url2.toString());

                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadVideo);
                        Toast.makeText(getActivity(), "Video File uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double currentProgress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded: "+(int)currentProgress+"%");
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        adapter.clear();
                    }
                });
                //end
            }
        });//end

    }

    private int permission() {
        set=2;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        set=1;
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        set=2;
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

        return set;
    }


}
