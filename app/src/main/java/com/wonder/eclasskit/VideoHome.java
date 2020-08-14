package com.wonder.eclasskit;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.wonder.eclasskit.video.ListAdpterVideo;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.UploadVideo;
import com.wonder.eclasskit.video.VideoPlayer;
import java.util.ArrayList;



import static android.app.Activity.RESULT_OK;


public class VideoHome extends Fragment  implements ListAdpterVideo.CallbackDelete{

    private ListView VideoListView;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    private ArrayList<UploadVideo> uploadVideos;
    private ImageButton selectFile;
    private TextView nameUpfile;
    private Uri videoUri;
    private byte[] data1;
    private static String name;
    private Button uplode;
    private Uri url2;
    private View v,v1;
    private ListAdpterVideo adapter;
    private ArrayList<String> keys;
    private Bitmap bitmap1;
    private int set;
    private ListAdpterVideo.CallbackDelete anInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v1 = inflater.inflate(R.layout.fragment_videohome, container, false);
        // Inflate the layout for this fragment

        anInterface=this;

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


            nameUpfile.setText(name1);


/*
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                bitmap1 = ThumbnailUtils.createVideoThumbnail(fullPath, MediaStore.Video.Thumbnails.MINI_KIND);
                selectFile.setImageBitmap(bitmap1);

            }else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){
                try {
                    File thumbFile = new File(fullPath);
                    Size thumbSize = new Size(200, 200);
                    bitmap1 = ThumbnailUtils.createVideoThumbnail(thumbFile, thumbSize,null);
                    selectFile.setImageBitmap(bitmap1);
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Ex:"+e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
            data1 = baos.toByteArray();
*/
            Glide.with(requireContext())
                    .asBitmap()
                    .load(videoUri)
                    .into(selectFile);



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


                adapter = new ListAdpterVideo(getActivity(),R.layout.itemvideo,uploadVideos,keys,anInterface);

                if (VideoListView.getFooterViewsCount() > 0)
                {
                    VideoListView.removeFooterView(v);
                }

                v=getLayoutInflater().inflate(R.layout.footerviewvideo, null);
                if (Common.limit != 1) {
                    VideoListView.addFooterView(v);
                }

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
//        StorageReference reference2 =storage.child("videos/"+System.currentTimeMillis()+".png");
//        adapter.clear();
//        reference2.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
//                while (!uri.isComplete());
//                url2=uri.getResult();
//
//                Uri p=vidUri;
                //video uplode
                StorageReference reference =storage.child("videos/"+name);
                reference.putFile(vidUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url=uri.getResult();

                        UploadVideo uploadVideo=new UploadVideo(url.toString(),name);

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
                });
                //end
            }
//        });//end
//
//    }

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


    @Override
    public void onHandledelete() {
        adapter.clear();
    }
}
