package com.wonder.eclasskit.Adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.R;
import com.wonder.eclasskit.Object.UploadPDF;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<UploadPDF> {
    private static final String TAG="ListAdapter";
    private Context mContext;
    int mResource;
    private ArrayList<UploadPDF> uploadPDFS;
    private ArrayList<String> keys;
    private CallbackDelete mCallback;
    private DatabaseReference databaseReference;


    public interface CallbackDelete{
        void onHandledelete();
    }

    public ListAdapter(Context context, int resource, ArrayList<UploadPDF> objects,ArrayList<String> keys,CallbackDelete mCallback,String ref) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.mCallback=mCallback;
        this.keys=keys;
        keys=new ArrayList<>();
        this.keys.addAll(keys);
        uploadPDFS=objects;
        databaseReference = FirebaseDatabase.getInstance().getReference(ref+ Common.uid);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name=getItem(position).getName();
        String url2=getItem(position).getImgurl();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView namef=(TextView)convertView.findViewById(R.id.nameoffile);
        ImageView imageV=(ImageView)convertView.findViewById(R.id.pdfView);

        ImageButton delete = (ImageButton)convertView.findViewById(R.id.delete_npm);
        delete.setFocusable(false);
        delete.setFocusableInTouchMode(false);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                adb.setMessage("Are you sure you want to delete this PDF?");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        UploadPDF uploadP=uploadPDFS.get(position);
//                        Toast.makeText(mContext, "link: "+uploadP.getImgurl(), Toast.LENGTH_SHORT).show();
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(uploadP.getImgurl());
                        photoRef.delete();
                        StorageReference pdfRef = FirebaseStorage.getInstance().getReferenceFromUrl(uploadP.getUrl());
                        pdfRef.delete();
                        mCallback.onHandledelete();
                        databaseReference.child(keys.get(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mContext, "Long press deleted", Toast.LENGTH_SHORT).show();
                            }
                        });

                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("comments/");
                        databaseReference1.child(uploadP.getName().substring(0, uploadP.getName().length() - 4)).removeValue();
                    }
                });
                adb.setNegativeButton("No",null);
                adb.show();


            }
        });

        namef.setText(name);


        Picasso.with(getContext()).load(url2).into(imageV);

        return convertView;
    }
}




