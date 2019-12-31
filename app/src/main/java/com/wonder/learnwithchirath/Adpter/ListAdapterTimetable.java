package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.wonder.learnwithchirath.Object.Timetable;
import com.wonder.learnwithchirath.R;
import java.util.ArrayList;

public class ListAdapterTimetable extends ArrayAdapter<Timetable> {
    private static final String TAG="ListAdapterTimetable";
    private Context mContext;
    int mResource;


    public ListAdapterTimetable(Context context, int resource, ArrayList<Timetable> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         String date=getItem(position).getDate();
         String grade=getItem(position).getGrade();
         String time=getItem(position).getTime();
         String institute=getItem(position).getInstitute();
         String gcalss=getItem(position).getGcalss();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView dateb=(TextView)convertView.findViewById(R.id.dayofweek);
        Button gradeb=(Button)convertView.findViewById(R.id.mgrade);
        Button timeb=(Button)convertView.findViewById(R.id.mtime);
        Button instituteb=(Button)convertView.findViewById(R.id.minstitute);
        Button gcalssb=(Button)convertView.findViewById(R.id.mclass);

        if (date.isEmpty()){
            dateb.setVisibility(View.GONE);
        }else {
            dateb.setText(date);
        }
        gradeb.setText(grade);
        timeb.setText(time);
        instituteb.setText(institute);
        gcalssb.setText(gcalss);




        return convertView;
    }
}
