package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Object.Eventobj;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class ListAdapterEvent extends ArrayAdapter<Eventobj> {
    private static final String TAG="ListAdapterEvent";
    private Context mContext;
    int mResource;


    public ListAdapterEvent(Context context, int resource, ArrayList<Eventobj> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String title=getItem(position).getTitle();
        String descrip=getItem(position).getDecription();
        String time=getItem(position).getTime();
        String uri=getItem(position).getUri();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView titlee=(TextView)convertView.findViewById(R.id.title_event);
        TextView descripe=(TextView)convertView.findViewById(R.id.descrip_event);
        TextView timee=(TextView)convertView.findViewById(R.id.time_event);
        ImageView imageV=(ImageView)convertView.findViewById(R.id.image_event);

        titlee.setText(title);
        descripe.setText(descrip);
        timee.setText(time);


        Picasso.with(getContext()).load(uri).into(imageV);

        return convertView;
    }
}
