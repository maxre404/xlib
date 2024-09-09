package com.ok.uiframe.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ok.uiframe.fragment.SampleFragment;
import com.ok.uiframe.fragment.SampleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new SampleFragment();
    }

    @Override
    public int getItemCount() {
        return 3; // 假设有三个页面
    }
}
