package team.sao.musictool.activity;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import team.sao.musictool.MainApp;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.dao.MusicToolDataBase;
import team.sao.musictool.entity.MyFavorSong;
import team.sao.musictool.entity.Song;
import team.sao.musictool.receiver.MusicPlayReceiver;
import team.sao.musictool.util.FastBlurUtil;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.StatusBarUtil;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;
import static team.sao.musictool.config.PlayerInfo.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 15:25
 * \* Description:
 **/
public class MusicPlayActivity extends Activity implements View.OnTouchListener {

    public static final String ACTION = ReceiverAction.MUSICPLAY_UI;

    @ViewID(R.id.background_blur)
    private ImageView background_blur;
    @ViewID(R.id.ic_back)
    private ImageView back;
    @ViewID(R.id.songname)
    private TextView songname;
    @ViewID(R.id.crttime)
    private TextView crttime;
    @ViewID(R.id.total_time)
    private TextView totalTime;
    @ViewID(R.id.seek_progress)
    private SeekBar seekProgress;
    @ViewID(R.id.singer)
    private TextView singer;
    @ViewID(R.id.album_img)
    private ImageView aimg;
    @ViewID(R.id.ic_like)
    private ImageView like;
    @ViewID(R.id.ic_download)
    private ImageView download;
    @ViewID(R.id.ic_more)
    private ImageView more;
    @ViewID(R.id.ic_presong)
    private ImageView presong;
    @ViewID(R.id.ic_play_pause)
    private ImageView play_pause;
    @ViewID(R.id.ic_nextsong)
    private ImageView nextsong;

    private PlayerInfo playerInfo = PlayerInfo.getInstance();
    private MusicPlayActivityReceiver receiver;
    private boolean isSeeking = false;
    private MusicToolDataBase musicToolDataBase = MusicToolDataBase.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        inject(this, MusicPlayActivity.class, this);
        StatusBarUtil.setStatusBarMode(this, false, R.color.color_netease_text);
        register();
        initDataAndView();
        initAction();
        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_SEND_UPDATE_UI).send(MusicPlayActivity.this); //发送请求更新ui广播
    }

    @Override
    protected void onDestroy() {
        unRegister();
        super.onDestroy();
    }

    private void initDataAndView() {
        Song song = playerInfo.getPlayingSong();
        Bitmap img = playerInfo.getSongImg();
        if (song != null) {
            songname.setText(song.getName());
            singer.setText(song.getSinger());
            if (img != null) {
                aimg.setImageBitmap(img);
            }
            switch (playerInfo.getStatus()) {
                case STATUS_LOADING:
                case STATUS_PAUSE: //暂停
                    play_pause.setImageResource(R.drawable.play_white);
                    break;
                case STATUS_PLAYING:  //播放
                    play_pause.setImageResource(R.drawable.pause_white);
                    break;

            }
        }
    }

    private void initAction() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayActivity.this.finish();
            }
        });
        like.setOnTouchListener(this);
        download.setOnTouchListener(this);
        more.setOnTouchListener(this);
        presong.setOnTouchListener(this);
        play_pause.setOnTouchListener(this);
        nextsong.setOnTouchListener(this);
        seekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && !(playerInfo.isStatus(STATUS_LOADING) || playerInfo.isStatus(STATUS_NOTINIT))) {
                    crttime.setText(formatTime(progress)); //拖动进度条时间更新
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { //发送seek
                Log.i("Seek", "onStopTrackingTouch: seekto" + seekBar.getProgress());
                new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_SEEKTO).extra(POSITION, seekBar.getProgress() * 1000).send(MusicPlayActivity.this);
                isSeeking = false;
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            v.setAlpha(0.5f);
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            v.setAlpha(1.0f);
            switch (v.getId()) {
                case R.id.ic_presong: //上一首
                    if (!playerInfo.isStatus(STATUS_NOTINIT)) {
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_PRE_SONG).send(MusicPlayActivity.this);
                    }
                    break;
                case R.id.ic_play_pause: //暂停播放
                    if (!playerInfo.isStatus(STATUS_NOTINIT)) {
                        IntentBuilder ib = new IntentBuilder();
                        switch (playerInfo.getStatus()) {
                            case STATUS_PAUSE:
                                ib.action(MusicPlayReceiver.ACTION).extra(OPERATE, OP_RESUME).send(MusicPlayActivity.this);
                                break;
                            case STATUS_PLAYING:
                                ib.action(MusicPlayReceiver.ACTION).extra(OPERATE, OP_PAUSE).send(MusicPlayActivity.this);
                                break;
                        }
                    }
                    break;
                case R.id.ic_nextsong: //下一首
                    if (!playerInfo.isStatus(STATUS_NOTINIT)) {
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_NEXT_SONG).send(MusicPlayActivity.this);
                    }
                    break;
                case R.id.ic_like: //喜欢
                    if (playerInfo.getPlayingSong() != null) {
                        String msg ;
                        if (playerInfo.isMyFavor()) {
                            int flag = musicToolDataBase.deleteByPrimaryKey(MyFavorSong.class, "'" + playerInfo.getPlayingSong().getSongid() + "'");
                            if (flag > 0) {
                                msg = "取消喜欢成功";
                                playerInfo.setMyFavor(false);
                                like.setImageResource(R.drawable.like_empty_white);
                            } else {
                                msg = "取消喜欢失败";
                            }
                        } else {
                            boolean flag = musicToolDataBase.insert(new MyFavorSong(playerInfo.getPlayingSong()));
                            if (flag) {
                                msg = "添加喜欢成功";
                                playerInfo.setMyFavor(true);
                                like.setImageResource(R.drawable.like_fill_red);
                            } else {
                                msg = "添加喜欢失败";
                            }
                        }
                        Toast.makeText(MainApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    private void register() { //注册广播
        receiver = new MusicPlayActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(receiver, intentFilter);
    }

    private void unRegister() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private String formatTime(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }

    public class MusicPlayActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int opt = intent.getIntExtra(OPERATE, -1);
            boolean isUpdateStatus = intent.getBooleanExtra(UPDATE_STATUS, false);
            if (isUpdateStatus) {
                switch (playerInfo.getStatus()) {
                    case STATUS_LOADING:
                    case STATUS_PAUSE: //暂停
                        play_pause.setImageResource(R.drawable.play_white);
                        break;
                    case STATUS_PLAYING:  //播放
                        play_pause.setImageResource(R.drawable.pause_white);
                        seekProgress.setClickable(true);
                        break;

                }
            }
            if (opt != -1) {
                switch (opt) {
                    case OP_UPDATE_UI: //更新所有ui
                        Song song = playerInfo.getPlayingSong();
                        if (song != null) {
                            seekProgress.setMax(song.getTime());
                            songname.setText(song.getName());
                            singer.setText(song.getSinger());
                            totalTime.setText(song.getFormatTime());
                            like.setImageResource(playerInfo.isMyFavor() ? R.drawable.like_fill_red : R.drawable.like_empty_white);
                            if (playerInfo.getSongImg() != null) {
                                aimg.setImageBitmap(playerInfo.getSongImg());
                                background_blur.setImageBitmap(FastBlurUtil.toBlur(playerInfo.getSongImg(), 10));
                            }
                            int position = playerInfo.getPosition();
                            seekProgress.setProgress(position);
                            crttime.setText(formatTime(position));
                        }
                        break;
                    case OP_UPDATE_UI_IMG: //更新img的ui
                        if (playerInfo.getSongImg() != null) {
                            aimg.setImageBitmap(playerInfo.getSongImg());
                            background_blur.setImageBitmap(FastBlurUtil.toBlur(playerInfo.getSongImg(), 10));
                        }
                        break;
                    case OP_UPDATE_UI_NOIMG: //更新除img的ui
                        final Song song1 = playerInfo.getPlayingSong();
                        if (song1 != null) {
                            songname.setText(song1.getName());
                            singer.setText(song1.getSinger());
                            like.setImageResource(playerInfo.isMyFavor() ? R.drawable.like_fill_red : R.drawable.like_empty_white);
                            aimg.setImageResource(R.drawable.ic_music_logo_white);
                            totalTime.setText(song1.getFormatTime());
                            crttime.setText(formatTime(0));
                            seekProgress.setProgress(0);
                            seekProgress.setMax(song1.getTime());
                        }
                        break;
                    case OP_UPDATE_PROGRESS:
                        if (!isSeeking) {
                            int position = intent.getIntExtra(POSITION, 0);
                            crttime.setText(formatTime(position));
                            seekProgress.setProgress(position);
                            break;
                        }
                }
            }
        }
    }
}
