package team.sao.musictool.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/7
 * \* Time: 2:10
 * \* Description:
 **/
public class SearchActivityPagerAdapter extends PagerAdapter {

    List<View> views;
    List<String> titles;

    public SearchActivityPagerAdapter(List<View> views, List<String> titles) {
        this.views = views;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }
}
