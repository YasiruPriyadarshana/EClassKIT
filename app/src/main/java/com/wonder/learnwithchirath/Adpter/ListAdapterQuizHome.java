package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wonder.learnwithchirath.Object.QuizHm;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class ListAdapterQuizHome  extends ArrayAdapter<QuizHm> {
    private static final String TAG="ListAdapterQuizHome";
    private Context mContext;
    int mResource;


    public ListAdapterQuizHome(Context context, int resource, ArrayList<QuizHm> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name=getItem(position).getName();
        String time=getItem(position).getTime();
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView namef=(TextView)convertView.findViewById(R.id.quest_home_name);
        TextView timef=(TextView)convertView.findViewById(R.id.time_lbl);
        namef.setText(name);
        timef.setText(time+" Min");


        return convertView;
    }
}
