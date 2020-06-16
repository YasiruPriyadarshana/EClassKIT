package com.wonder.eclasskit.Adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.wonder.eclasskit.MyResult;
import com.wonder.eclasskit.Object.AddCourse;
import com.wonder.eclasskit.Object.QuizHm;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterAddClass extends ArrayAdapter<AddCourse> {
    private static final String TAG="ListAdapterQuizHome";
    private Context mContext;
    int mResource;
    private ArrayList<String> keys;


    public ListAdapterAddClass(Context context, int resource, ArrayList<AddCourse> objects, ArrayList<String> keys) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.keys=keys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String subname=getItem(position).getSubjectname();
        String subyear=getItem(position).getClass_yr();
        final String key=keys.get(position);
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView namef=(TextView)convertView.findViewById(R.id.sub_name_txt);
        TextView timef=(TextView)convertView.findViewById(R.id.class_yr_txt);
        namef.setText(subname);
        timef.setText(subyear);


        return convertView;
    }
}
