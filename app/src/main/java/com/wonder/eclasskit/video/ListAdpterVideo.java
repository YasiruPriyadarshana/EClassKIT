package com.wonder.eclasskit.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.wonder.eclasskit.Object.UploadVideo;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdpterVideo extends ArrayAdapter<UploadVideo> {
    private static final String TAG="ListAdapterVideo";
    private Context mContext;
    int mResource;
    private ArrayList<String> keys;
    private ArrayList<UploadVideo> uploadVideos;
    private CallbackDelete mCallback;

    public interface CallbackDelete{
        void onHandledelete();
    }


    public ListAdpterVideo(Context context, int resource, ArrayList<UploadVideo> objects,ArrayList<String> key,CallbackDelete callback) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        keys=key;
        uploadVideos=objects;
        mCallback=callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name=getItem(position).getvName();
        String vurl=getItem(position).getThumbnailUrl();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameVideo=(TextView)convertView.findViewById(R.id.video_title_txt);
        ImageView imageVideo=(ImageView)convertView.findViewById(R.id.video_thumb);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete_video);

        if (Common.limit != 1) {
            delete.setFocusable(false);
            delete.setFocusableInTouchMode(false);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_delete, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                            adb.setMessage("Are you sure?");
                            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UploadVideo uploadP = uploadVideos.get(position);

                                    StorageReference vthumbRef = FirebaseStorage.getInstance().getReferenceFromUrl(uploadP.getThumbnailUrl());
                                    vthumbRef.delete();
                                    StorageReference vidRef = FirebaseStorage.getInstance().getReferenceFromUrl(uploadP.getVideourl());
                                    vidRef.delete();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos/" + Common.uid);
                                    mCallback.onHandledelete();
                                    databaseReference.child(keys.get(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(mContext, "Long press deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("comments/");
//                                databaseReference1.child(uploadP.getName().substring(0, uploadP.getName().length() - 4)).removeValue();

                                }
                            });
                            adb.setNegativeButton("No", null);
                            adb.show();
                            return true;
                        }


                    });
                    popup.show();
                }
            });

        }else {
            delete.setVisibility(View.GONE);
        }

        nameVideo.setText(name.substring(0, name.length() - 4));


        Picasso.with(getContext()).load(vurl).fit().into(imageVideo);

        return convertView;
    }
}
