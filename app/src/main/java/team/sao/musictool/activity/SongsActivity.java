package team.sao.musictool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import team.sao.musictool.MainApp;
import team.sao.musictool.R;
import team.sao.musictool.adapter.SongListViewAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.dao.MusicToolDataBase;
import team.sao.musictool.entity.*;
import team.sao.musictool.fragment.PlayBarFragment;
import team.sao.musictool.util.StatusBarUtil;

import java.util.LinkedList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/4
 * \* Time: 0:17
 * \* Description:
 **/
public class SongsActivity extends FragmentActivity {

    public static final String TYPE_LOCAL_MUSIC = "本地音乐";
    public static final String TYPE_RECENT_PLAY = "最近播放";
    public static final String TYPE_MY_FAVOR = "我的收藏";

    private MusicToolDataBase musicToolDataBase = MusicToolDataBase.getInstance(this);
    private FragmentManager fragmentManager;
    private PlayBarFragment playBar;
    private List<Song> songs;
    private SongListViewAdapter songListViewAdapter;

    @ViewID(R.id.activity_songs_icon_back)
    private ImageView ic_back;
    @ViewID(R.id.activity_songs_songslist)
    private ListView songslist;
    @ViewID(R.id.activity_songs_title)
    private TextView title;
    @ViewID(R.id.activity_songs_ic_trash)
    private ImageView ic_trash;

    private Class entityClass;
    private String songsType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        StatusBarUtil.setStatusBarMode(SongsActivity.this, true, R.color.color_netease_white);
        inject(this, SongsActivity.class, this);
        init(getIntent());
        initDataAndView();
        initActions();
    }

    private void init(Intent intent) {
        songsType = intent.getStringExtra(ListMenuItem.EXTRA_NAME);
        switch (songsType) {
            case TYPE_LOCAL_MUSIC:
                entityClass = LocalSong.class;
                break;
            case TYPE_RECENT_PLAY:
                entityClass = RecentSong.class;
                break;
            case TYPE_MY_FAVOR:
                entityClass = MyFavorSong.class;
                break;
        }
    }

    private void initDataAndView() {
        title.setText(songsType);

        //playbar
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.playbar, playBar = new PlayBarFragment()).commit();

        //获取歌曲
        songs = musicToolDataBase.selectTableAll(entityClass);
        songslist.setAdapter(songListViewAdapter = new SongListViewAdapter(this, songs, true));

    }

    private void initActions() {
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongsActivity.this.finish();
            }
        });

        ic_trash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    v.setAlpha(0.5f);
                } else if (action == MotionEvent.ACTION_UP) {
                    v.setAlpha(1.0f);
                    int retult = musicToolDataBase.deleteAll(entityClass);
                    if (retult > 0) {
                        Toast.makeText(MainApp.getInstance(), "删除记录:" + retult, Toast.LENGTH_SHORT).show();
                    }
                    songs.clear();
                    songListViewAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        songslist.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
            }
        });

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Song> tSongs = musicToolDataBase.selectTableAll(entityClass);
        songs.clear();
        songs.addAll(tSongs);
        songListViewAdapter.notifyDataSetChanged();
    }
}
