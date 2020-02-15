package com.wonder.learnwithchirath.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.wonder.learnwithchirath.FragmentQuestion;
import com.wonder.learnwithchirath.Object.Quizobj;
import com.wonder.learnwithchirath.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterQuiz extends FragmentPagerAdapter {
    private static final String TAG="ListAdapterQuiz";
    private Context mContext;
    List<FragmentQuestion> fragmentQuestions;

    public ListAdapterQuiz(@NonNull FragmentManager fm, int behavior, List<FragmentQuestion> fragmentQuestions) {
        super(fm, behavior);
            this.fragmentQuestions = fragmentQuestions;

    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentQuestions.get(position);
    }
    @Override
    public int getCount() {
        return fragmentQuestions.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return new StringBuilder("Q").append(position+1).toString();
    }




}
