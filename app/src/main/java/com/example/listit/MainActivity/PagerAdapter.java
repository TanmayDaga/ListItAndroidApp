package com.example.listit.MainActivity;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class PagerAdapter extends FragmentStateAdapter {

    private List<String> mCategoryNames;
    private static final String LOG_TAG = PagerAdapter.class.getSimpleName();

    public PagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);

    }

    public void setLists(List<String> categoryNames) {
        mCategoryNames = categoryNames;
        notifyDataSetChanged();
    }

    public String getCategoryByPosition(int position) {
        return mCategoryNames.get(position);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ListItFragment(mCategoryNames.get(position));
    }

    @Override
    public int getItemCount() {
        if (mCategoryNames == null) return 0;
        return mCategoryNames.size();
    }

    public String getTitle(int position) {
        return mCategoryNames.get(position);
    }

}
