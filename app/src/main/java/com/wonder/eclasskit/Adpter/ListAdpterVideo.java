package com.wonder.eclasskit.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wonder.eclasskit.Object.UploadVideo;
import com.wonder.eclasskit.R;

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
        String vurl=getItem(position).getThumbnailUrl();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameVideo=(TextView)convertView.findViewById(R.id.video_title_txt);
        ImageView imageVideo=(ImageView)convertView.findViewById(R.id.video_thumb);

        nameVideo.setText(name);


        Picasso.with(getContext()).load(vurl).into(imageVideo);

        return convertView;
    }
}
