package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.Object.Common;
import com.wonder.learnwithchirath.Object.Quizobj;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;
import java.util.EventListener;

public class ListAdapterQuizList extends RecyclerView.Adapter<ListAdapterQuizList.ViewHolder> {
        private ArrayList<Quizobj> mModel;

        public ListAdapterQuizList(ArrayList<Quizobj> model) {
            mModel = model;
        }

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
                        while (true){
                            if (Common.answer.get(i) > 0) {
                                viewHolder.ques1.setBackgroundColor(Color.parseColor("#E91BD37C"));
                            }
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

