package com.wonder.eclasskit.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wonder.eclasskit.Object.Course;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterMyCourses  extends ArrayAdapter<Course> {
    private static final String TAG="ListAdapterCourses";
    private Context mContext;
    int mResource;


    public ListAdapterMyCourses(Context context, int resource, ArrayList<Course> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String course=getItem(position).getcName();
        String teacher=getItem(position).getcTeacher();


        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView course_txt=(TextView)convertView.findViewById(R.id.course_name_txt);
        TextView teacher_txt=(TextView)convertView.findViewById(R.id.course_teacher_txt);

        course_txt.setText(course);
        teacher_txt.setText(teacher);


        return convertView;
    }
}
