package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Object.CommentM;
import com.wonder.learnwithchirath.Object.Timetable;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class ListAdapterComments extends ArrayAdapter<CommentM> {
    private static final String TAG="ListAdapterComment";
    private Context mContext;
    int mResource;

    public ListAdapterComments(Context context, int resource, ArrayList<CommentM> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String user=getItem(position).getUsercmt();
        String comment=getItem(position).getCommentdesc();
        String uri=getItem(position).getUricmt();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameC=(TextView)convertView.findViewById(R.id.cmt_user);
        TextView commentC=(TextView)convertView.findViewById(R.id.cmt_comment);
        ImageView imgC=(ImageView)convertView.findViewById(R.id.cmt_image);

        nameC.setText(user);
        commentC.setText(comment);


        Picasso.with(getContext()).load(uri).into(imgC);


        return convertView;
    }
}
