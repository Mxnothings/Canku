package team.sao.musictool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import team.sao.musictool.activity.SearchActivity;
import team.sao.musictool.activity.SongsActivity;
import team.sao.musictool.adapter.MyFragmentPagerAdapter;
import team.sao.musictool.adapter.SearchListMenuAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.MusicType;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.entity.ListMenuItem;
import team.sao.musictool.fragment.ListMenu;
import team.sao.musictool.fragment.MusicFragment;
import team.sao.musictool.fragment.PlayBarFragment;
import team.sao.musictool.receiver.MusicPlayReceiver;
import team.sao.musictool.service.MusicPlayerService;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

public class MainActivity extends FragmentActivity {

    private int lastTextViewIndex = 0;
    private static int commonTextSize = 14;
    private static int largerTextSize = 22;

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
    private List<Fragment> pagerFragments;
    private PlayBarFragment playBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置沉浸式状态栏
        StatusBarUtil.setStatusBarMode(this, true, R.color.color_netease_white);
        inject(this, MainActivity.class, this);
        initDataAndView();
        initActions();
    }


    private void initDataAndView() {
        views = new ArrayList<>();
        pagerFragments = new ArrayList<>();

        /**我的*/
        ListMenu mine = new ListMenu();
        mine.setOnMenuItemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentBuilder builder = new IntentBuilder().clazz(MainActivity.this, SongsActivity.class);
                switch (position) {
                    case 0: //本地音乐
                        builder.extra(ListMenuItem.EXTRA_NAME, SongsActivity.TYPE_LOCAL_MUSIC);
                        break;
                    case 1: //最近播放
                        builder.extra(ListMenuItem.EXTRA_NAME, SongsActivity.TYPE_RECENT_PLAY);
                        break;
                    case 2: //我的收藏
                        builder.extra(ListMenuItem.EXTRA_NAME, SongsActivity.TYPE_MY_FAVOR);
                        break;
                }
                startActivity(builder.build());
            }
        });
        mine.addListMenuItem(new ListMenuItem(R.drawable.local_music_red, "本地音乐", "", R.drawable.enter_black));
        mine.addListMenuItem(new ListMenuItem(R.drawable.recent_play_red, "最近播放", "", R.drawable.enter_black));
        mine.addListMenuItem(new ListMenuItem(R.drawable.my_favor_red, "我的收藏", "", R.drawable.enter_black));
        pagerFragments.add(mine);

        /**音乐*/
        pagerFragments.add(new MusicFragment());

        /**搜索*/
        ListMenu search = new ListMenu();
        search.setOnMenuItemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new IntentBuilder().clazz(MainActivity.this, SearchActivity.class).extra(MusicType.NAME, MusicType.QQ_MUSIC).build());
                } else if (position == 1) {
                    startActivity(new IntentBuilder().clazz(MainActivity.this, SearchActivity.class).extra(MusicType.NAME, MusicType.NETEASE_MUSIC).build());
                }
            }
        });

        search.addListMenuItem(new ListMenuItem(R.drawable.qqmusic_logo, "QQ音乐", "", R.drawable.enter_black));
        search.addListMenuItem(new ListMenuItem(R.drawable.netease_cloud_music_logo, "网易云音乐", "", R.drawable.enter_black));
        search.setListMenuAdapter(new SearchListMenuAdapter());
        pagerFragments.add(search);

        /**设置viewpager适配器*/
        fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new MyFragmentPagerAdapter(fragmentManager, pagerFragments));

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        playBar = new PlayBarFragment();
        transaction.replace(R.id.playbar, playBar).commit();

        /**toolbar items*/
        items = new HashMap<>();
        items.put(0, tv_mine);
        items.put(1, tv_music);
        items.put(2, tv_dis);

        //开启service
        Intent intent = new Intent(this, MusicPlayerService.class);
        startService(intent);
    }

    private void initActions() {
        items.get(lastTextViewIndex).setTextSize(TypedValue.COMPLEX_UNIT_SP, largerTextSize);
        items.get(lastTextViewIndex).setTextColor(getResources().getColor(R.color.color_netease_text));


        /**添加滑动监听*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //设置字体大小和颜色
                items.get(lastTextViewIndex).setTextSize(TypedValue.COMPLEX_UNIT_SP, commonTextSize);
                items.get(lastTextViewIndex).setTextColor(getResources().getColor(R.color.color_netease_text_lighter));
                items.get((lastTextViewIndex = i)).setTextSize(TypedValue.COMPLEX_UNIT_SP, largerTextSize);
                items.get(lastTextViewIndex).setTextColor(getResources().getColor(R.color.color_netease_text));
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

        /**设置点击menu图标打开侧边栏*/
        icon_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_menu.openDrawer(Gravity.LEFT);
            }
        });

        /***/
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this.getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}
