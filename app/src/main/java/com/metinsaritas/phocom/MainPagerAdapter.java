package com.metinsaritas.phocom;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.metinsaritas.phocom.Types.FragmentWithTitle;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<FragmentWithTitle> fragments = new ArrayList<>();
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.add(new MainCreateFragment().setTitle("Phocom"));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
