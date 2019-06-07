package team.sao.musictool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import team.sao.musictool.MainApp;
import team.sao.musictool.R;
import team.sao.musictool.adapter.SongAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.fragment.PlayBarFragment;
import team.sao.musictool.music.MusicAPIHolder;
import team.sao.musictool.music.api.MusicAPI;
import team.sao.musictool.music.config.MusicAPIConfig;
import team.sao.musictool.music.entity.Song;
import team.sao.musictool.music.entity.SongList;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.JSONUtil;
import team.sao.musictool.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/7
 * \* Time: 16:32
 * \* Description:
 **/
public class SongListActivity extends FragmentActivity {

    public static final String SONGLIST_INFO = "SONGLIST_INFO";

    @ViewID(R.id.ic_back)
    private ImageView back;
    @ViewID(R.id.activity_songlist_img)
    private ImageView img;
    @ViewID(R.id.activity_songlist_name)
    private TextView name;
    @ViewID(R.id.activity_songlist_song_count)
    private TextView songCount;
    @ViewID(R.id.activity_songlist_play_count)
    private TextView playCount;

    @ViewID(R.id.loading_layout)
    private LinearLayout loading_layout;
    @ViewID(R.id.iv_loading)
    private ImageView iv_loading;

    @ViewID(R.id.activity_songlist_listview)
    private ListView song_listview;
    private List<Song>  songs;
    private SongAdapter adapter;
    private SongList songList;

    private MusicAPI musicAPI;
    private String musicType;
    private PlayerInfo playerInfo = PlayerInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);
        inject(this, SongListActivity.class, this);
        StatusBarUtil.setStatusBarMode(this, true, R.color.color_netease_white);
        initDataAndViews();
        initActions();
    }

    private void initDataAndViews() {

        Intent intent = getIntent();
        songList = (SongList) JSONUtil.parseJSONToObject(intent.getStringExtra(SONGLIST_INFO), SongList.class);
        //初始化musicApi
        musicType = intent.getStringExtra(MusicAPIConfig.MUSIC_TYPENAME);
        musicAPI = MusicAPIHolder.getAPI(musicType);
        //初始化歌单信息界面
        Glide.with(this).load(songList.getImgUrl()).into(img);
        name.setText(songList.getName());
        songCount.setText(songList.getSongCount() + "首");
        playCount.setText("播放" + songList.getPlayCount() + "次");

        Glide.with(this).load(R.drawable.loading).into(iv_loading);

        //初始化listview
        songs = new ArrayList<>();
        song_listview.setAdapter(adapter = new SongAdapter(this, songs));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.playbar, new PlayBarFragment()).commit();


        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loading_layout.setVisibility(View.INVISIBLE);
                if (msg.what == 1) {
                    adapter.notifyDataSetChanged();
                } else if (msg.what == -1) {
                    Toast.makeText(MainApp.getInstance(), "暂无数据", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

        //获取歌单歌曲
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Song> songs1 = null;
                try {
                    Log.i("获取歌单中的歌曲,id:", songList.getId());
                    songs1 = musicAPI.getSongsFromSongList(songList.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Message message = new Message();
                    if (songs1 == null) {
                        message.what = -1;
                    } else {
                        message.what = 1;
                        songs.addAll(songs1);
                    }
                    handler.sendMessage(message);
                }
            }
        }).start();


    }

    private void initActions() {
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongListActivity.this.finish();
            }
        });

        song_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (playerInfo.getPlay_list() != songs) {
                    playerInfo.resetTo(songs, position);
                    new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER)
                            .extra(PlayerInfo.OPERATE, PlayerInfo.OP_PLAY)
                            .send(SongListActivity.this);
                } else {
                    if (!(playerInfo.getPlayingSongIndex() == position)) {
                        playerInfo.setPlayingSongIndex(position);
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER)
                                .extra(PlayerInfo.OPERATE, PlayerInfo.OP_PLAY)
                                .send(SongListActivity.this);
                    }
                }
            }
        });

    }

}
