package team.sao.musictool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 0:59
 * \* Description:
 **/
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
