package com.wonder.eclasskit.Adpter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wonder.eclasskit.Object.Common;
import com.wonder.eclasskit.Object.Quizobj;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class ListAdapterQuizList extends RecyclerView.Adapter<ListAdapterQuizList.ViewHolder> {
    private ArrayList<Quizobj> mModel;

    public ListAdapterQuizList(ArrayList<Quizobj> model) {
            mModel = model;
    }
    int j=0;



    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView ques1;
            public ViewHolder(View view) {
                super(view);
                ques1 = (TextView) view.findViewById(R.id.quizno);
            }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemquizlist, viewGroup, false);
            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int i) {
        viewHolder.ques1.setText(String.valueOf(i+1));

        Thread myThred=new Thread(){
                public void run(){
                    try {
                        while (i<=viewHolder.getPosition()){
                            if (Common.completelist.get(i)==1){
                                viewHolder.ques1.setBackgroundColor(Color.parseColor("#D50000"));
                            }else if (Common.completelist.get(i) > 1) {
                                viewHolder.ques1.setBackgroundColor(Color.parseColor("#E91BD37C"));
                            }else {
                                viewHolder.ques1.setBackgroundColor(Color.parseColor("#D2D4D5"));
                            }
                            j=i;
                     }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
        };
        myThred.start();

    }

    @Override
    public int getItemCount() {
            return mModel.size();
    }

}

