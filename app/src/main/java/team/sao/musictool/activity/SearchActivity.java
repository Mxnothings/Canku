package team.sao.musictool.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import team.sao.musictool.MainApp;
import team.sao.musictool.R;
import team.sao.musictool.adapter.SearchActivityPagerAdapter;
import team.sao.musictool.adapter.SongAdapter;
import team.sao.musictool.adapter.SongListAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.dao.MusicToolDataBase;
import team.sao.musictool.entity.SearchHistory;
import team.sao.musictool.fragment.PlayBarFragment;
import team.sao.musictool.music.MusicAPIHolder;
import team.sao.musictool.music.api.MusicAPI;
import team.sao.musictool.music.entity.Song;
import team.sao.musictool.music.entity.SongList;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.JSONUtil;
import team.sao.musictool.util.StatusBarUtil;
import team.sao.musictool.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;
import static team.sao.musictool.music.config.MusicAPIConfig.MUSIC_TYPENAME;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 14:53
 * \* Description:
 **/
public class SearchActivity extends FragmentActivity {

    private static final int PAGE_SIZE = 20;
    private static final int MAX_PAGE_NUM = 20;

    //当前一些信息
    private int crtSongPagenum = 0;             //当前歌曲的page
    private int crtSongListPagenum = 0;         //当前歌单的page

    private int crtSelectPage = 0;              //当前被选中的page
    private String crtKeyword = null;           //当前的搜索keyword

    private boolean isSongSearched = false;     //歌曲是否已经搜索
    private boolean isSongListSearched = false; //歌单是否已经搜索

    //是否达到最大页
    private boolean isSongToMaxPage = false;
    private boolean isSongListToMaxPage = false;


    private String musicType;

    private PlayerInfo playerInfo = PlayerInfo.getInstance();
    private MusicToolDataBase musicToolDataBase = MusicToolDataBase.getInstance(this);
    private MusicAPI musicAPI;
    private ProgressDialog alert;

    @ViewID(R.id.keyword_input)
    private EditText keywordInput;
    @ViewID(R.id.ic_back)
    private ImageView back;
    @ViewID(R.id.activity_search_tablayout)
    private TabLayout tabLayout;

    @ViewID(R.id.activity_search_viewpager)
    private ViewPager viewPager;
    private List<View> views;
    private List<String> titles;

    private SearchActivityPagerAdapter searchActivityPagerAdapter;

    //歌曲搜索
    private View songs_view;
    private ListView songs_listview;
    private List<Song> songs;
    private SongAdapter songAdapter;

    //歌单搜索
    private View songlist_view;
    private ListView songlist_listview;
    private List<SongList> songLists;
    private SongListAdapter songListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarUtil.setStatusBarMode(this, true, R.color.color_netease_white);
        inject(this, SearchActivity.class, this);
        setSearchTypeInfo();
        initDataAndView();
        initAction();

    }

    @Override
    protected void onDestroy() {
        alert.dismiss();
        super.onDestroy();
    }

    /**
     *
     */
    private void setSearchTypeInfo() {
        musicType = getIntent().getStringExtra(MUSIC_TYPENAME);
        musicAPI = MusicAPIHolder.getAPI(musicType);
    }

    private void initDataAndView() {
        alert = createProgressDialog();

        //初始化viewpager和tablayout
        views = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("歌曲");
        titles.add("歌单");

        //歌曲搜索
        songs = new ArrayList<>();
        songs_view = View.inflate(this, R.layout.song_listview, null);
        songs_listview = songs_view.findViewById(R.id.song_listview);
        songs_listview.setAdapter(songAdapter = new SongAdapter(this, songs));

        //歌单搜索
        songLists = new ArrayList<>();
        songlist_view = View.inflate(this, R.layout.songlist_listview, null);
        songlist_listview = songlist_view.findViewById(R.id.songlist_listview);
        songlist_listview.setAdapter(songListAdapter = new SongListAdapter(this, songLists));


        views.add(songs_view);
        views.add(songlist_view);
        viewPager.setAdapter(searchActivityPagerAdapter = new SearchActivityPagerAdapter(views, titles));
        tabLayout.setupWithViewPager(viewPager);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.playbar, new PlayBarFragment()).commit();
    }

    /**
     * 初始化事件绑定
     */
    private void initAction() {

        //设置搜索
        keywordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keywordTemp = keywordInput.getText().toString();
                    if (!keywordTemp.matches("^(\\s+)|()$")) {
                        crtKeyword = keywordTemp;
                        musicToolDataBase.insert(new SearchHistory(null, keywordInput.getText().toString()));
                        ViewUtil.hideKeyboard(SearchActivity.this, keywordInput);
                        resetDataAndFlag();
                        switch (crtSelectPage) {
                            case 0: //搜索歌曲
                                isSongSearched = true;
                                searchSong(crtKeyword);
                                break;
                            case 1: //搜索歌单
                                isSongListSearched = true;
                                searchSongList(crtKeyword);
                                break;
                        }
                    } else {
                        Toast.makeText(MainApp.getInstance(), "请输入关键词", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        //设置搜索歌曲item点击事件
        songs_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (playerInfo.getPlay_list() != songs) {
                    playerInfo.resetTo(songs, position);
                    new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER)
                            .extra(PlayerInfo.OPERATE, PlayerInfo.OP_PLAY)
                            .send(SearchActivity.this);
                } else {
                    if (!(playerInfo.getPlayingSongIndex() == position)) {
                        playerInfo.setPlayingSongIndex(position);
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER)
                                .extra(PlayerInfo.OPERATE, PlayerInfo.OP_PLAY)
                                .send(SearchActivity.this);
                    }
                }
            }
        });

        //滑动到底部加载更多
        songs_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (isListViewReachBottomEdge(absListView)) {
                            searchSong(keywordInput.getText().toString());
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        //滑动到底部加载更多
        songlist_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (isListViewReachBottomEdge(absListView)) {
                            searchSongList(keywordInput.getText().toString());
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        //歌单被点击
        songlist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new IntentBuilder()
                        .clazz(SearchActivity.this, SongListActivity.class)
                        .extra(SongListActivity.SONGLIST_INFO, JSONUtil.toJSONString(songLists.get(position)))
                        .extra(MUSIC_TYPENAME, musicType)
                        .build());
            }
        });

        //滑动页面搜索
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                crtSelectPage = i;
                switch (i) {
                    case 0: //歌曲被选中
                        if (crtKeyword != null && !isSongSearched) {
                            isSongSearched = true;
                            searchSong(crtKeyword);
                        }
                        break;
                    case 1: //歌单被选中
                        if (crtKeyword != null && !isSongListSearched) {
                            isSongListSearched = true;
                            searchSongList(crtKeyword);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });

    }

    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }

    /**
     * 搜索歌曲
     *
     * @param keyword
     */
    private void searchSong(final String keyword) {
        if (!isSongToMaxPage) {
            alert.show();
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        songAdapter.notifyDataSetChanged();
                        crtSongPagenum++;
                    } else if (msg.what == -1) {
                        Toast.makeText(SearchActivity.this.getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                    alert.hide();
                    super.handleMessage(msg);
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Song> songs1 = null;
                    try {
                        songs1 = musicAPI.searchSong(keyword, crtSongPagenum, PAGE_SIZE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Message message = new Message();
                        if (songs1 == null) {
                            message.what = -1;
                        } else {
                            message.what = 1;
                            isSongToMaxPage = songs1.size() < PAGE_SIZE;
                            songs.addAll(songs1);
                        }
                        handler.sendMessage(message);
                    }
                }
            }).start();
        } else { //歌曲达到最大页数

        }
    }

    /**
     * 搜索歌单
     *
     * @param keyword
     */
    private void searchSongList(final String keyword) {
        if (!isSongListToMaxPage) {
            alert.show();
            Log.i("搜索歌单页数:", crtSongListPagenum + "");
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        songListAdapter.notifyDataSetChanged();
                        crtSongListPagenum++;
                    } else if (msg.what == -1) {
                        Toast.makeText(MainApp.getInstance(), "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                    alert.hide();
                    super.handleMessage(msg);
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<SongList> songLists1 = null;
                    try {
                        songLists1 = musicAPI.searchSongList(keyword, crtSongListPagenum, PAGE_SIZE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Message message = new Message();
                        if (songLists1 == null) {
                            message.what = -1;
                        } else {
                            message.what = 1;
                            isSongListToMaxPage = songLists1.size() < PAGE_SIZE;
                            songLists.addAll(songLists1);
                        }
                        handler.sendMessage(message);
                    }
                }
            }).start();
        } else { //歌单达到最大的page

        }
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("加载中");
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    private void resetDataAndFlag() {
        songs.clear();
        crtSongPagenum = 0;
        crtSongListPagenum = 0;
        isSongSearched = false;
        isSongListSearched = false;
        isSongToMaxPage = false;
        isSongListToMaxPage = false;
    }

}
