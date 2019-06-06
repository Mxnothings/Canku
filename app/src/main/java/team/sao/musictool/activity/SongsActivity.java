package team.sao.musictool.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import team.sao.musictool.MainApp;
import team.sao.musictool.R;
import team.sao.musictool.adapter.SongListViewAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.dao.MusicToolDataBase;
import team.sao.musictool.entity.*;
import team.sao.musictool.fragment.PlayBarFragment;
import team.sao.musictool.music.entity.Song;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.StatusBarUtil;

import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

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
    private PlayerInfo playerInfo = PlayerInfo.getInstance();

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
        songslist.setAdapter(songListViewAdapter = new SongListViewAdapter(this, songs));

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
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    v.setAlpha(1.0f);
                    //提示确定
                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(SongsActivity.this);
                    aBuilder.setTitle("提示");
                    aBuilder.setMessage("确定删除所有吗?");
                    aBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int retult = musicToolDataBase.deleteAll(entityClass);
                            if (retult > 0) {
                                Toast.makeText(MainApp.getInstance(), "删除记录:" + retult, Toast.LENGTH_SHORT).show();
                            }
                            songs.clear();
                            songListViewAdapter.notifyDataSetChanged();
                        }
                    });
                    aBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    aBuilder.show();
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

        songslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (playerInfo.getPlay_list() != songs) {
                    playerInfo.resetTo(songs, position);
                    new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER)
                            .extra(PlayerInfo.OPERATE, PlayerInfo.OP_PLAY)
                            .send(SongsActivity.this);
                } else {
                    if (!(playerInfo.getPlayingSongIndex() == position)) {
                        playerInfo.setPlayingSongIndex(position);
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER)
                                .extra(PlayerInfo.OPERATE, PlayerInfo.OP_PLAY)
                                .send(SongsActivity.this);
                    }
                }
            }
        });

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {

        }
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
