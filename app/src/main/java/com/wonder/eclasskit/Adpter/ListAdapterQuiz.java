package com.wonder.eclasskit.Adpter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wonder.eclasskit.FragmentQuestion;

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
