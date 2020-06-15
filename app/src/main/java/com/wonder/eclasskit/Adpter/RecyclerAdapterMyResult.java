package com.wonder.eclasskit.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wonder.eclasskit.Object.Result;
import com.wonder.eclasskit.R;

import java.util.ArrayList;

public class RecyclerAdapterMyResult extends RecyclerView.Adapter<RecyclerAdapterMyResult.ViewHolder>{
    private ArrayList<Result> mModel;
    private Context mContext;
    public RecyclerAdapterMyResult(ArrayList<Result> model, Context context) {
        this.mModel=model;
        mContext=context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,marks;
        ImageView trophy;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameofRanker);
            marks = (TextView) view.findViewById(R.id.resultofRanker);
            trophy = (ImageView) view.findViewById(R.id.rankimg);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemmyresult, viewGroup, false);
        return new RecyclerAdapterMyResult.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterMyResult.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(mModel.get(i).getName());
        viewHolder.marks.setText(mModel.get(i).getMarks());
        if (Integer.parseInt(mModel.get(i).getMarks())>1) {
            viewHolder.trophy.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.trophy_place_1));
        }else {
            viewHolder.trophy.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.trophy_place_2));
        }
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

}
