package com.wonder.learnwithchirath.Adpter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;


import com.wonder.learnwithchirath.Object.Review;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;


public class RecyclerAdapterReview extends RecyclerView.Adapter<RecyclerAdapterReview.ViewHolder>{
    private ArrayList<Review> mModel;
    public RecyclerAdapterReview(ArrayList<Review> model) {
       this.mModel=model;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question,percentage;
        public ViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.number_quest);
            percentage = (TextView) view.findViewById(R.id.percentage_quiz);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemreviewquestion, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.question.setText("Question "+mModel.get(i).getNumber());
        viewHolder.percentage.setText(String.format("%.1f",mModel.get(i).getPer())+"%");
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }
}
