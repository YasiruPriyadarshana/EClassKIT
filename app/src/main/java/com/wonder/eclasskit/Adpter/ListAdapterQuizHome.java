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
import com.wonder.eclasskit.Object.QuizHm;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterQuizHome  extends ArrayAdapter<QuizHm> {
    private static final String TAG="ListAdapterQuizHome";
    private Context mContext;
    int mResource;
    private ArrayList<String> keys;


    public ListAdapterQuizHome(Context context, int resource, ArrayList<QuizHm> objects,ArrayList<String> keys) {
        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        this.keys=keys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name=getItem(position).getName();
        String time=getItem(position).getTime();
        final String key=keys.get(position);
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView namef=(TextView)convertView.findViewById(R.id.quest_home_name);
        TextView timef=(TextView)convertView.findViewById(R.id.time_lbl);
        namef.setText(name);
        timef.setText(time+" Min");

        final ImageButton result=(ImageButton) convertView.findViewById(R.id.myresult_btn);
        result.setFocusable(false);
        result.setFocusableInTouchMode(false);
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view;
                PopupMenu popup = new PopupMenu(v.getContext(),v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.memu_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent=new Intent(getContext(), MyResult.class);
                        intent.putExtra("key",key);
                        mContext.startActivity(intent);
                        return false;
                    }
                });
                popup.show();
            }
        });

        return convertView;
    }

}