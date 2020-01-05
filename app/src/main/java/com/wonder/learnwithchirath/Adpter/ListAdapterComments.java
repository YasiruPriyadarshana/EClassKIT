package com.wonder.learnwithchirath.Adpter;


import android.content.Context;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


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

import com.wonder.learnwithchirath.Comments;
import com.wonder.learnwithchirath.Object.CommentM;
import com.wonder.learnwithchirath.Object.Reply;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class ListAdapterComments extends ArrayAdapter<CommentM> {
    private static final String TAG="ListAdapterComment";
    private Context mContext;
    int mResource;
    private DatabaseReference databaseReference2;
    private ListAdapterReply adapter;
    private View v;
    private String key,name1,name;
    private ArrayList<String> keys;
    private StorageReference storage;
    private Button updateReply;
    private EditText desc;
    private String rep_str;

    public ListAdapterComments(Context context, int resource, ArrayList<CommentM> objects,String name1,ArrayList<String> keys,String name) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.keys=keys;
        keys=new ArrayList<>();
        this.keys.addAll(keys);
        this.name1=name1;
        this.name=name;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String user=getItem(position).getUsercmt();
        String comment=getItem(position).getCommentdesc();
        String uri=getItem(position).getUricmt();
        key=keys.get(position);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("comments/"+name1.substring(0, name1.length() - 4)+"/"+key+"/reply");
        storage= FirebaseStorage.getInstance().getReference();
//        Toast.makeText(getContext(),"comments/"+name1.substring(0, name1.length() - 4)+"/"+key, Toast.LENGTH_SHORT).show();
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameC=(TextView)convertView.findViewById(R.id.cmt_user);
        TextView commentC=(TextView)convertView.findViewById(R.id.cmt_comment);
        ImageView imgC=(ImageView)convertView.findViewById(R.id.cmt_image);
        ListView ReplyListView=(ListView)convertView.findViewById(R.id.recyclerviewreply);
        nameC.setText(user);
        commentC.setText(comment);


        Picasso.with(getContext()).load(uri).into(imgC);
        viewAllFiles(ReplyListView);



        return convertView;
    }


    private void viewAllFiles(final ListView ReplyListView) {
        final ArrayList<Reply> replies= new ArrayList<>();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Reply reply = postSnapshot.getValue(Reply.class);
                    replies.add(reply);

                }


                adapter = new ListAdapterReply(mContext,R.layout.itemreply,replies);

                if (ReplyListView.getFooterViewsCount() > 0)
                {
                    ReplyListView.removeFooterView(v);
                }

                v=LayoutInflater.from(mContext).inflate(R.layout.footerviewreply, null);
                ReplyListView.addFooterView(v);
                updateReply = (Button) v.findViewById(R.id.addrep);
                desc = (EditText) v.findViewById(R.id.rep_in);
                updateReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rep_str=desc.getText().toString();
                        uplodeFile(null);
                    }
                });


                ReplyListView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(ReplyListView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uplodeFile(final Uri imgUri) {
        //imageuploade
        if (imgUri == null) {
            Reply replytobj = new Reply(name, rep_str,null);

            databaseReference2.child(databaseReference2.push().getKey()).setValue(replytobj);
            Toast.makeText(getContext(), "Add new comment", Toast.LENGTH_SHORT).show();
            adapter.clear();
        } else {
            StorageReference reference2 = storage.child("uploads5/" + System.currentTimeMillis() + ".png");
            reference2.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uri.isComplete()) ;
                    Uri p = uri.getResult();

                    Reply replytobj = new Reply(name, rep_str, p.toString());

                    databaseReference2.child(databaseReference2.push().getKey()).setValue(replytobj);
                    Toast.makeText(getContext(), "Add new comment", Toast.LENGTH_SHORT).show();


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

    public static void setListViewHeightBasedOnChildren(ListView myListView) {
        ListAdapter adapter = myListView.getAdapter();
        if (myListView != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View item= adapter.getView(i, null, myListView);
                item.measure(0, 0);
                totalHeight += item.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = totalHeight + (myListView.getDividerHeight() * (adapter.getCount() - 1));
            myListView.setLayoutParams(params);
        }
    }
}
