package com.wonder.learnwithchirath.Adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wonder.learnwithchirath.Object.Result;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;

public class RecyclerAdapterMyResult extends RecyclerView.Adapter<RecyclerAdapterMyResult.ViewHolder>{
    private ArrayList<Result> mModel;
    public RecyclerAdapterMyResult(ArrayList<Result> model) {
        this.mModel=model;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,marks;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameofRanker);
            marks = (TextView) view.findViewById(R.id.resultofRanker);
        }
    }

    @Override
    public RecyclerAdapterMyResult.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemmyresult, viewGroup, false);
        return new RecyclerAdapterMyResult.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterMyResult.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(mModel.get(i).getName());
        viewHolder.marks.setText(mModel.get(i).getMarks());
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

}
