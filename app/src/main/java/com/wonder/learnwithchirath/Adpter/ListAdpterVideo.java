package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Object.UploadPDF;
import com.wonder.learnwithchirath.Object.UploadVideo;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class ListAdpterVideo extends ArrayAdapter<UploadVideo> {
    private static final String TAG="ListAdapterVideo";
    private Context mContext;
    int mResource;


    public ListAdpterVideo(Context context, int resource, ArrayList<UploadVideo> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name=getItem(position).getvName();
        String vurl=getItem(position).getVideourl();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

//        TextView namef=(TextView)convertView.findViewById(R.id.nameoffile);
//        ImageView imageV=(ImageView)convertView.findViewById(R.id.pdfView);

//        namef.setText(name);


//        Picasso.with(getContext()).load(vurl).into(imageV);

        return convertView;
    }
}