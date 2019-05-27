package team.sao.musictool.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import team.sao.musictool.R;
import team.sao.musictool.adapter.SongListViewAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.MusicType;
import team.sao.musictool.entity.Song;
import team.sao.musictool.util.QQMusicUtil;
import team.sao.musictool.util.ViewUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 14:53
 * \* Description:
 **/
public class SearchActivity extends Activity {

    private static final int PAGE_SIZE = 20;
    private static final int MAX_PAGE_NUM = 20;

    private int musicType;
    private List<Song> songs;

    private ProgressDialog alert;

    @ViewID(R.id.keyword_input)
    private EditText keywordInput;
    @ViewID(R.id.icon_back)
    private ImageView icon_back;
    @ViewID(R.id.activity_search_title)
    private TextView title;
    @ViewID(R.id.activity_songlist)
    private ListView songlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewUtil.initWindows(this, getResources().getColor(R.color.colorPrimary));
        inject(this, this);
        setSearchTypeInfo();
        initAction();

        alert = createProgressDialog();

        songs = new ArrayList<>();
        songlist.setAdapter(new SongListViewAdapter(this, songs));


    }


    /**
     *
     */
    private void setSearchTypeInfo() {
        musicType = getIntent().getIntExtra("musicType", MusicType.QQ_MUSIC);
        String titleString = null;
        if (musicType == MusicType.QQ_MUSIC) {
            titleString = "QQ音乐搜索";
        } else if (musicType == MusicType.NETEASE_MUSIC) {
            titleString = "网易云音乐搜索";
        }
        title.setText(titleString);
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

    }

    /**
     * 搜索
     * @param keyword
     * @param songs
     */
    private void search(final String keyword, final List<Song> songs) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    ((BaseAdapter) songlist.getAdapter()).notifyDataSetChanged();
                    alert.hide();
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    songs.addAll(QQMusicUtil.getSongsByKeyword(keyword, 1, PAGE_SIZE));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Message message = new Message();
                    message.what = 1;
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