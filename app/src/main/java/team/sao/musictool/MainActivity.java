package team.sao.musictool;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.*;
import android.widget.ImageView;
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
        initWindows();
//        if(Build.VERSION.SDK_INT >= 21) {
//            Window window = getWindow();
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }

        inject(this, this);
        initData();
        initViews();

    }


    private void initData() {
        views = new ArrayList<>();

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


    /**
     * 沉浸式状态栏
     */
    private void initWindows() {
        Window window = getWindow();
        int color = getResources().getColor(R.color.colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            //设置导航栏颜色
            window.setNavigationBarColor(color);
            ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置contentview为fitsSystemWindows
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
            //给statusbar着色
            View view = new View(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this)));
            view.setBackgroundColor(color);
            contentView.addView(view);
        }
    }


    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private class DrawerMenuToggle extends ActionBarDrawerToggle {

        public DrawerMenuToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        public DrawerMenuToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

    }


}
