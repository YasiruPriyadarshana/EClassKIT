package com.wonder.learnwithchirath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Adpter.ListAdapterComments;
import com.wonder.learnwithchirath.Object.CommentM;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class Comments extends AppCompatActivity implements ListAdapterComments.CallbackInterface {
    private DatabaseReference databaseReference;
    ListView CommentListView;
    private ArrayList<CommentM> commentMS;
    private ListAdapterComments adapter;
    private View v,v2;
    private StorageReference storage;
    private Button updateComment,addImage;
    private ImageView image;
    private ImageButton imagepdf;
    private EditText desc;
    private TextView na;
    private String cmt_str,name,uri,name1;
    private String[] array;
    private Uri imgUri;
    private ListAdapterComments.CallbackInterface anInterface;
    Bitmap bitmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        name1=getIntent().getStringExtra("name");
        databaseReference = FirebaseDatabase.getInstance().getReference("comments/"+name1.substring(0, name1.length() - 4));
        storage= FirebaseStorage.getInstance().getReference();
        CommentListView=(ListView)findViewById(R.id.recyclerviewcomment);
        commentMS = new ArrayList<>();

        anInterface=this;
        viewAllFiles();

    }



    private void viewAllFiles() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    CommentM commentM = postSnapshot.getValue(CommentM.class);
                    commentMS.add(commentM);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);

                }


                adapter = new ListAdapterComments(getApplicationContext(),R.layout.itemcomment,commentMS,name1,keys,getName(),anInterface);

                if (CommentListView.getFooterViewsCount() > 0)
                {
                    CommentListView.removeFooterView(v);
                }
                if (CommentListView.getHeaderViewsCount() > 0)
                {
                    CommentListView.removeHeaderView(v2);
                }

                v2=getLayoutInflater().inflate(R.layout.header_comment, null);
                v=getLayoutInflater().inflate(R.layout.footerviewcomment, null);
                CommentListView.addHeaderView(v2);
                CommentListView.addFooterView(v);

                image = (ImageView) v.findViewById(R.id.cmtimage_in);
                desc = (EditText) v.findViewById(R.id.comment_in);
                updateComment = (Button) v.findViewById(R.id.addcmt);
                addImage=(Button)v.findViewById(R.id.addimage);


                imagepdf=(ImageButton) v2.findViewById(R.id.pdfpaperimage);
                na=(TextView)v2.findViewById(R.id.nameppr);

                na.setText(name1);
                uri=getIntent().getStringExtra("image");
                Picasso.with(Comments.this).load(uri).into(imagepdf);
                final String url=getIntent().getStringExtra("url");
                imagepdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                        startActivity(intent);
                    }
                });

                addImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(Comments.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            selectIMG();
                        } else {
                            ActivityCompat.requestPermissions(Comments.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                        }
                    }
                });
                updateComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cmt_str=desc.getText().toString();
                        if (cmt_str.isEmpty()){
                            Toast.makeText(Comments.this, "Type comment", Toast.LENGTH_SHORT).show();
                        }else {
                            uplodeFile(imgUri);
                        }
                    }
                });




                CommentListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void uplodeFile(final Uri imgUri) {


        //imageuploade
        if (imgUri == null) {
            CommentM commentobj = new CommentM(getName(), cmt_str,null);

            databaseReference.child(databaseReference.push().getKey()).setValue(commentobj);
            Toast.makeText(Comments.this, "Add new comment", Toast.LENGTH_SHORT).show();
            adapter.clear();
        } else {
            StorageReference reference2 = storage.child("uploads4/" + System.currentTimeMillis() + ".png");
            reference2.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uri.isComplete()) ;
                    Uri p = uri.getResult();

                    CommentM commentobj = new CommentM(getName(), cmt_str, p.toString());

                    databaseReference.child(databaseReference.push().getKey()).setValue(commentobj);
                    Toast.makeText(Comments.this, "Add new comment", Toast.LENGTH_SHORT).show();


                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    adapter.clear();
                }
            });
            ;//end

        }
    }


    public String getName(){
        try {
            FileInputStream fileInputStream = openFileInput("apprequirement.txt");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer =new StringBuffer();


            String lines;
            while ((lines = bufferedReader.readLine()) != null){
                stringBuffer.append(lines + "\n");
            }
            String str =stringBuffer.toString();
            array = str.split(",");

            name=array[0];

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectIMG();
        }else {
            Toast.makeText(Comments.this, "please provide permission", Toast.LENGTH_SHORT).show();
        }
    }


    public void selectIMG() {
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

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                image.setImageBitmap(bitmap);
            }catch (IOException e){
                Toast.makeText(Comments.this, "fucked: "+e, Toast.LENGTH_SHORT).show();
            }

        }else if (requestCode==78 && resultCode == RESULT_OK && data!=null) {
            imgUri=data.getData();

            try {
                Bitmap bitmp = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                image.setImageBitmap(bitmp);
                Toast.makeText(this, "Image added", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                Toast.makeText(Comments.this, "fucked: "+e, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(Comments.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onHandleSelection() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 78);
    }

    @Override
    public void onHandleSelectionClear() {
        adapter.clear();
    }

    @Override
    public Uri getimage() {
        return imgUri;
    }


}
