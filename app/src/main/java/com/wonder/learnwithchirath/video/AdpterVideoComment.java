package com.wonder.learnwithchirath.video;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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
import com.wonder.learnwithchirath.Adpter.ListAdapterReply;
import com.wonder.learnwithchirath.Object.CommentM;
import com.wonder.learnwithchirath.Object.Common;
import com.wonder.learnwithchirath.Object.Reply;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class AdpterVideoComment extends ArrayAdapter<CommentM> {
    private static final String TAG="ListAdapterComment";
    private AdpterVideoComment.CallbackInterface mCallback;
    private Context mContext;
    int mResource;

    private String name1,name;
    private ArrayList<String> keys;
    private StorageReference storage;
    private ListAdapterReply adapter;
    Uri p;
    private ValueEventListener valueEventListener;



    public interface CallbackInterface{
        void onHandleSelectionClear();

        void popUp(String key,String uri);
        void popUpReply(final String key,final String uri,final DatabaseReference dr);

    }
    public AdpterVideoComment(Context context, int resource, ArrayList<CommentM> objects, String name1, ArrayList<String> keys, String name, AdpterVideoComment.CallbackInterface mCallback) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.keys=keys;
        keys=new ArrayList<>();
        this.keys.addAll(keys);
        this.name1=name1;
        this.name=name;
        this.mCallback=mCallback;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        String user=getItem(position).getUsercmt();
        String comment=getItem(position).getCommentdesc();
        String key=keys.get(position);
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("VideoComments/"+ Common.uid+"/" +name1.substring(0, name1.length() - 4)+"/"+key+"/reply");

        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameC=(TextView)convertView.findViewById(R.id.cmt_user);
        TextView commentC=(TextView)convertView.findViewById(R.id.cmt_comment);
        ImageView img=(ImageView)convertView.findViewById(R.id.cmt_image);
        ListView ReplyListView=(ListView)convertView.findViewById(R.id.recyclerviewreply);

        nameC.setText(user);
        commentC.setText(comment);
        img.setVisibility(View.GONE);



        viewAllFiles(ReplyListView,databaseReference2);




        return convertView;
    }


    private void viewAllFiles(ListView ReplyListView,DatabaseReference databaseReference2) {
        final ArrayList<Reply> replies= new ArrayList<>();


        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Reply reply = postSnapshot.getValue(Reply.class);
                    replies.add(reply);
                    String mkey = postSnapshot.getKey();
                    keys.add(mkey);
                }


                adapter = new ListAdapterReply(mContext,R.layout.itemreply,replies,keys,databaseReference2);

                View v=LayoutInflater.from(mContext).inflate(R.layout.footerviewreply, null);
                if (ReplyListView.getFooterViewsCount() > 0)
                {
                    ReplyListView.removeFooterView(v);
                }
                ReplyListView.addFooterView(v);
                Button updateReply = (Button) v.findViewById(R.id.addrep);
                EditText desc = (EditText)v.findViewById(R.id.rep_in);






                updateReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String rep_str=desc.getText().toString();
                        if (rep_str.isEmpty()){
                            Toast.makeText(mContext, "Type Reply", Toast.LENGTH_SHORT).show();
                        }else {
                            uplodeFile(null, databaseReference2, rep_str);
                        }

                    }
                });


                ReplyListView.setAdapter(adapter);


                ReplyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String key=keys.get(position);
                        Reply reply=replies.get(position);
                        mCallback.popUpReply(key,reply.getUrirep(),databaseReference2);


                        return false;
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void uplodeFile(final Uri imgUri,final DatabaseReference dr,final String rep_st) {

        //imageuploade

            Reply replytobj = new Reply(name, rep_st,null);

            dr.child("1"+dr.push().getKey()).setValue(replytobj);
            Toast.makeText(getContext(), "Add new Reply", Toast.LENGTH_SHORT).show();
            mCallback.onHandleSelectionClear();


    }




}
