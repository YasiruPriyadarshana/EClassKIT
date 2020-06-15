package com.wonder.eclasskit.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wonder.eclasskit.Object.Answer;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterReview extends ArrayAdapter<Answer> {
    private static final String TAG = "ListAdapterReview";
    private Context mContext;
    int mResource;



    public ListAdapterReview(@NonNull Context context, int resource, ArrayList<Answer> objects) {
        super(context, resource,objects);
        mContext=context;
        mResource=resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name_st=getItem(position).getName();
        String result_st=getItem(position).getResult();
        int marks=0;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView names = (TextView) convertView.findViewById(R.id.name_st_ans);
        TextView result = (TextView) convertView.findViewById(R.id.marks_st);

        names.setText(name_st);

        result.setText(result_st);

        return convertView;
    }
}