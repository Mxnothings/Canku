package team.sao.musictool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import team.sao.musictool.adapter.MyFragmentPagerAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.fragment.MineFragment;
import team.sao.musictool.fragment.MusicFragment;
import team.sao.musictool.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

public class MainActivity extends FragmentActivity {

    private int lastTextViewIndex = 0;
    private static int commonTextSize = 14;
    private static int largerTextSize = 20;
    private Map<Integer, TextView> items;

    @ViewID(R.id.view_pager)
    private ViewPager viewPager;
    @ViewID(R.id.tv_mine)
    private TextView tv_mine;
    @ViewID(R.id.tv_music)
    private TextView tv_music;
    @ViewID(R.id.tv_dis)
    private TextView tv_dis;


    private List<View> views;
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inject(this, this);
        initData();
        initViews();

    }


    private void initData() {
        views = new ArrayList<>();

        /**设置viewPager的view*/
//        LayoutInflater inflater = getLayoutInflater();
//        views.add(inflater.inflate(R.layout.fragment_mine, null, false));
//        views.add(inflater.inflate(R.layout.fragment_music, null, false));
//        views.add(inflater.inflate(R.layout.fragment_dis, null, false));

        fragments = new ArrayList<>();
        fragments.add(new MineFragment());
        fragments.add(new MusicFragment());
        fragments.add(new SearchFragment());

        items = new HashMap<>();
        items.put(0, tv_mine);
        items.put(1, tv_music);
        items.put(2, tv_dis);
    }

    private void initViews() {
        items.get(lastTextViewIndex).setTextSize(TypedValue.COMPLEX_UNIT_DIP, largerTextSize);

        fragmentManager = getSupportFragmentManager();
        /**设置适配器*/
        viewPager.setAdapter(new MyFragmentPagerAdapter(fragmentManager, fragments));

        /**添加滑动监听*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                items.get(lastTextViewIndex).setTextSize(TypedValue.COMPLEX_UNIT_DIP, commonTextSize);
                items.get((lastTextViewIndex = i)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, largerTextSize);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        /** 给toolbar的textview添加监听 */
        for (final Map.Entry<Integer, TextView> e : items.entrySet()) {
            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(e.getKey());
                }
            });
        }
    }


}
