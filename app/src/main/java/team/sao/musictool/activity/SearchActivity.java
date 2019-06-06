package team.sao.musictool.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import team.sao.musictool.R;
import team.sao.musictool.adapter.SongListViewAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.dao.MusicToolDataBase;
import team.sao.musictool.entity.SearchHistory;
import team.sao.musictool.fragment.PlayBarFragment;
import team.sao.musictool.music.MusicAPIHolder;
import team.sao.musictool.music.api.MusicAPI;
import team.sao.musictool.music.entity.Song;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.StatusBarUtil;
import team.sao.musictool.util.ViewUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;
import static team.sao.musictool.music.config.MusicAPIConfig.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 14:53
 * \* Description:
 **/
public class SearchActivity extends FragmentActivity {

    private static final int PAGE_SIZE = 20;
    private static final int MAX_PAGE_NUM = 20;

    private String musicType;
    private List<Song> songs;
    private int crtindex = -1;
    private PlayerInfo playerInfo = PlayerInfo.getInstance();
    private MusicToolDataBase musicToolDataBase = MusicToolDataBase.getInstance(this);
    private MusicAPI musicAPI;

    private ProgressDialog alert;

    @ViewID(R.id.keyword_input)
    private EditText keywordInput;
    @ViewID(R.id.activity_songs_icon_back)
    private ImageView icon_back;
    @ViewID(R.id.activity_search_title)
    private TextView title;
    @ViewID(R.id.activity_songlist)
    private ListView songlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarUtil.setStatusBarMode(this, true, R.color.color_netease_white);
        inject(this, SearchActivity.class, this);
        setSearchTypeInfo();
        initAction();
        initDataAndView();

//        List<SearchHistory> data = musicToolDataBase.select(SearchHistory.class, "where keyword like ?", new String[]{"%你%"});
//        if (data != null) {
//            Log.i("历史搜索,结果数量", data.size() + "");
//            for (SearchHistory s : data) {
//                Log.i("历史搜索", s + "");
//            }
//        } else {
//            Log.i("历史搜索", "没有");
//        }

    }


    /**
     *
     */
    private void setSearchTypeInfo() {
        musicType = getIntent().getStringExtra(MUSIC_TYPENAME);
        String titleString = null;
        switch (musicType) {
            case MUSIC_TYPE_TECENT:
                titleString = "QQ音乐搜索";
                break;
            case MUSIC_TYPE_NETEASE:
                titleString = "网易云音乐搜索";
                break;
            case MUSIC_TYPE_KUGOU:
                titleString = "酷狗音乐搜索";
                break;
            case MUSIC_TYPE_KUWO:
                titleString = "酷我音乐搜索";
                break;
            case MUSIC_TYPE_BAIDU:
                titleString = "百度音乐搜索";
                break;
        }
        musicAPI = MusicAPIHolder.getAPI(musicType);
        title.setText(titleString);
    }

    private void initDataAndView() {
        alert = createProgressDialog();
        songs = new ArrayList<>();
        songlist.setAdapter(new SongListViewAdapter(this, songs));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.playbar, new PlayBarFragment()).commit();
    }

    /**
     * 初始化事件绑定
     */
    private void initAction() {
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });

        //设置搜索
        keywordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    musicToolDataBase.insert(new SearchHistory(null, keywordInput.getText().toString()));
                    ViewUtil.hideKeyboard(SearchActivity.this, keywordInput);
                    String keyword = keywordInput.getText().toString();
                    songs.clear();
                    alert.show();
                    search(keyword, songs);
                    return true;
                }
                return false;
            }
        });

        songlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    }

    /**
     * 搜索
     *
     * @param keyword
     * @param songs
     */
    private void search(final String keyword, final List<Song> songs) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    ((BaseAdapter) songlist.getAdapter()).notifyDataSetChanged();
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
                    songs1 = musicAPI.searchSongs(keyword, 0, PAGE_SIZE);
                } catch (IOException e) {
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

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("搜索中");
        progressDialog.setCancelable(false);
        return progressDialog;
    }


}
