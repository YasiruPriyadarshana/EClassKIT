package com.wonder.eclasskit.Adpter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;


import com.wonder.eclasskit.FragmentQuestion;

import java.util.List;

public class ListAdapterQuiz extends FragmentStatePagerAdapter {
    private int mCurrentPosition = -1;
    private static final String TAG="ListAdapterQuiz";
    private Context mContext;
    private List<FragmentQuestion> fragmentQuestions;
    private int change;

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

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition && container instanceof DynamicHeightViewPager) {
            Fragment fragment = (Fragment) object;
            DynamicHeightViewPager pager = (DynamicHeightViewPager) container;

            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }



    public void deletePage(int position)
    {
        fragmentQuestions.remove(position);
        notifyDataSetChanged();
        change=position;
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }


}
