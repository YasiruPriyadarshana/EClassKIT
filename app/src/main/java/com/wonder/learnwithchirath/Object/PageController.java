package com.wonder.learnwithchirath.Object;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wonder.learnwithchirath.Class;
import com.wonder.learnwithchirath.Event;
import com.wonder.learnwithchirath.Home;
import com.wonder.learnwithchirath.NotesAndPastPapers;

public class PageController extends FragmentPagerAdapter {
    int tabCount;
    public PageController(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount=tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Home();
            case 1:
                return new NotesAndPastPapers();
            case 2:
                return new Event();
            case 3:
                return new Class();
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

