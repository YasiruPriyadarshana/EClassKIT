package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Object.Reply;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class ListAdapterReply extends ArrayAdapter<Reply> {
    private static final String TAG="ListAdapterReply";
    private Context mContext;
    int mResource;
    private ArrayList<String> keys;
    private DatabaseReference databaseReference;




    public ListAdapterReply(Context context, int resource, ArrayList<Reply> objects,ArrayList<String> keys,DatabaseReference databaseReference) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.keys=keys;
        this.databaseReference=databaseReference;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String user=getItem(position).getUserrep();
        String comment=getItem(position).getReplydesc();
        final String uri=getItem(position).getUrirep();
        final String key=keys.get(position);

        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView nameC=(TextView)convertView.findViewById(R.id.rep_user);
        TextView commentC=(TextView)convertView.findViewById(R.id.rep_comment);
        ImageView imgC=(ImageView)convertView.findViewById(R.id.rep_image);


        nameC.setText(user);
        commentC.setText(comment);

        if(uri!=null) {
            Picasso.with(getContext()).load(uri).into(imgC);
        }
        else{
            imgC.setVisibility(View.GONE);
        }



        return convertView;
    }


}
