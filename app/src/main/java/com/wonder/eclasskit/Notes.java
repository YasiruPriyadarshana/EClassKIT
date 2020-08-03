package com.wonder.eclasskit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
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
import com.wonder.eclasskit.Adpter.ListAdapter;
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.UploadPDF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class Notes extends AppCompatActivity implements ListAdapter.CallbackDelete{
    private ListView PDFListView;
    private DatabaseReference databaseReference;
    private StorageReference storage;
    private ArrayList<UploadPDF> uploadPDFS;
    private ImageButton selectFile;
    private TextView nameUpfile;
    private Bitmap bitmap;
    private Uri pdfUri;
    private byte[] data1;
    private static String name;
    private Button uplode;
    private Uri url2;
    private View v,v2;
    private ListAdapter adapter;
    private ArrayList<String> keys;
    private int set;
    private ListAdapter.CallbackDelete anInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        databaseReference = FirebaseDatabase.getInstance().getReference("notes/"+ Common.uid);
        storage= FirebaseStorage.getInstance().getReference();

        PDFListView=(ListView)findViewById(R.id.recyclerviewn);
        anInterface=this;
        uploadPDFS = new ArrayList<>();
        viewAllFiles();

        PDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadPDF uploadPDF = uploadPDFS.get(position-1);

                Intent intent = new Intent(Notes.this,Comments.class);


                intent.putExtra("image",uploadPDF.getImgurl());
                intent.putExtra("name",uploadPDF.getName());
                intent.putExtra("url",uploadPDF.getUrl());
                startActivity(intent);
            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectPDF();
        }else {
            Toast.makeText(this, "please provide permission", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectPDF() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 76);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==76 && resultCode == RESULT_OK && data!=null) {
            pdfUri=data.getData();
            String name1=queryName(getContentResolver(),pdfUri);
            name=name1;

            nameUpfile.setText(name1);

            String pathfile = createCopyAndReturnRealPath(this,pdfUri);
            Toast.makeText(this, "a: "+pdfUri.getPathSegments(), Toast.LENGTH_SHORT).show();
            File pdfFile = new File(pathfile);
            GenaratePdfThumb genaratePdf = new GenaratePdfThumb();
            bitmap = genaratePdf.pdfToBitmap(pdfFile);
            selectFile.setImageBitmap(bitmap);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            data1 = baos.toByteArray();
        } else {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
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
    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }

    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    UploadPDF uploadPDF = postSnapshot.getValue(UploadPDF.class);
                    uploadPDFS.add(uploadPDF);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                }


                adapter = new ListAdapter(Notes.this,R.layout.item,uploadPDFS,keys,anInterface,"notes/");

                if (PDFListView.getFooterViewsCount() > 0)
                {
                    PDFListView.removeFooterView(v);
                }if (PDFListView.getHeaderViewsCount() > 0)
                {
                    PDFListView.removeHeaderView(v2);
                }

                v2=getLayoutInflater().inflate(R.layout.header_notes, null);
                v=getLayoutInflater().inflate(R.layout.footerview, null);
                PDFListView.addHeaderView(v2);
                if (Common.limit != 1){
                    PDFListView.addFooterView(v);
                }


                selectFile=(ImageButton)v.findViewById(R.id.pdfImgBtn);
                nameUpfile=(TextView)v.findViewById(R.id.nameofUpfile);
                uplode=(Button)v.findViewById(R.id.update_btn);
                selectFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(Notes.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                            selectPDF();
                        }else {
                            ActivityCompat.requestPermissions(Notes.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                        }
                    }
                });
                uplode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pdfUri!=null) {
                            uplodeFile(pdfUri);

                        }else
                            Toast.makeText(Notes.this, "select a File", Toast.LENGTH_SHORT).show();
                    }
                });
                PDFListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uplodeFile(final Uri pdfUri) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading File");
        progressDialog.show();

        //imageuploade
        StorageReference reference2 =storage.child("PDF/"+System.currentTimeMillis()+".png");
        reference2.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                url2=uri.getResult();

                Uri p=pdfUri;
                //pdf uplode
                StorageReference reference =storage.child("PDF/"+name);
                reference.putFile(p).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url=uri.getResult();

                        UploadPDF uploadPDF=new UploadPDF(name,url.toString(),url2.toString());

                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
                        Toast.makeText(Notes.this, "PDF File upladed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

        return set;
    }

    @Override
    public void onHandledelete() {
        adapter.clear();
    }
}
