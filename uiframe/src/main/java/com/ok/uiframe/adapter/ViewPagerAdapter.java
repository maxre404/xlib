package com.ok.uiframe.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ok.uiframe.fragment.SampleFragment;
import com.ok.uiframe.fragment.SampleFragment;
import com.ok.uiframe.fragment.VideoTestFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return VideoTestFragment.Companion.getInstance(""+position);
    }

    @Override
    public int getItemCount() {
        return 8; // 假设有三个页面
    }
}
