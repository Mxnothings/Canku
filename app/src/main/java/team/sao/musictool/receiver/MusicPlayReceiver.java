package team.sao.musictool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.entity.Song;
import team.sao.musictool.fragment.PlayBarFragment;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.JSONUtil;

import java.io.IOException;

import static team.sao.musictool.config.PlayerInfo.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 20:42
 * \* Description:
 **/
public class MusicPlayReceiver extends BroadcastReceiver {

    public static final String ACTION = "team.sao.musictool.receiver.musicplay";

    private MediaPlayer mediaPlayer;
    private Context mContext;
    private Song crtSong;
    private int status = -1;

    public MusicPlayReceiver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int operate = intent.getIntExtra(OPERATE, -1);
        IntentBuilder ib = new IntentBuilder();
        switch (operate) {
            case OP_PLAY:          //播放音乐
                Song song = (Song) JSONUtil.parseJSONToObject(intent.getStringExtra(SONG), Song.class);
                crtSong = song;
                playMusic(context, song);
                ib.action(PlayBarFragment.ACTION).extra(OPERATE, OP_UPDATE_UI).extra(SONG, intent.getStringExtra(SONG)).send(mContext);
                break;
            case OP_PAUSE:         //暂停播放
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    ib.action(PlayBarFragment.ACTION).extra(STATUS, STATUS_PAUSE).send(mContext);
                    status = 0;
                }
                break;
            case OP_RESUME:         //恢复播放
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    ib.action(PlayBarFragment.ACTION).extra(STATUS, STATUS_PLAYING).send(mContext);
                    status = 1;
                }
                break;
            case OP_SEND_PLAYBAR_UPDATE_UI:
                Log.i("OP_SNED_PLAYER_UPDATE", "onReceive: 接收到发送更新playbarUI[status" + status + crtSong);
                if (!(status == -1 || crtSong == null)) {
                    sendPlaybarUpdateUI();
                }
                break;
        }
    }


    //播放音乐
    private void playMusic(final Context context, final Song song) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("播放错误", "播放" + song + "错误");
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(song.getDownloadUrl()));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    new IntentBuilder().action(PlayBarFragment.ACTION).extra(STATUS, STATUS_PLAYING).send(mContext);
                    status = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendPlaybarUpdateUI() {
        mContext.sendBroadcast(new IntentBuilder().action(PlayBarFragment.ACTION)
                .extra(OPERATE, OP_UPDATE_UI)
                .extra(SONG, JSONUtil.toJSONString(crtSong))
                .extra(STATUS, status)
                .build());
    }


    public Song getCrtSong() {
        return crtSong;
    }
}
