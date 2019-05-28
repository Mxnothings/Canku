package team.sao.musictool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import team.sao.musictool.adapter.BaseListMenuAdapter;
import team.sao.musictool.adapter.MyFragmentPagerAdapter;
import team.sao.musictool.adapter.SearchListMenuAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.entity.ListMenuItem;
import team.sao.musictool.fragment.ListMenu;
import team.sao.musictool.fragment.MusicFragment;
import team.sao.musictool.util.ViewUtil;

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
    @ViewID(R.id.toolbar)
    private Toolbar toolbar;
    @ViewID(R.id.drawer_menu)
    private DrawerLayout drawer_menu;
    @ViewID(R.id.menu)
    private NavigationView menu;
    @ViewID(R.id.icon_menu)
    private ImageView icon_menu;


    private List<View> views;
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置沉浸式状态栏
        ViewUtil.initWindows(this, getResources().getColor(R.color.colorPrimary));
        inject(this, MainActivity.class, this);
        initData();
        initViews();

    }


    private void initData() {
        views = new ArrayList<>();

        fragments = new ArrayList<>();

        ListMenu mine = new ListMenu();
        mine.setOnMenuItemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        mine.addListMenuItem(new ListMenuItem(R.drawable.local_music, "本地音乐", "0", R.drawable.enter_black));
        mine.addListMenuItem(new ListMenuItem(R.drawable.recent_music, "最近音乐", "0", R.drawable.enter_black));
        mine.addListMenuItem(new ListMenuItem(R.drawable.favor_music, "收藏音乐", "0", R.drawable.enter_black));
        fragments.add(mine);

        fragments.add(new MusicFragment());

        ListMenu search = new ListMenu();
        search.addListMenuItem(new ListMenuItem(R.drawable.qqmusic_logo, "QQ音乐", "", R.drawable.enter_black));
        search.addListMenuItem(new ListMenuItem(R.drawable.netease_cloud_music_logo, "网易云音乐", "", R.drawable.enter_black));
        search.setListMenuAdapter(new SearchListMenuAdapter());
        fragments.add(search);

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
                //设置字体大小
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

        drawer_menu.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        icon_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_menu.openDrawer(Gravity.LEFT);
            }
        });

    }
}
