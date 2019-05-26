package team.sao.musictool.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/26
 * \* Time: 16:26
 * \* Description:
 **/
public class MyPagerAdapter extends PagerAdapter {

    private List<View> viewLists;

    public MyPagerAdapter() {
    }

    public MyPagerAdapter(List<View> viewLists) {
        this.viewLists = viewLists;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewLists.get(position));
        return viewLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }
}
