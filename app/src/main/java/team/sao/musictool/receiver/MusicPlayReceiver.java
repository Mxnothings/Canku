package team.sao.musictool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.entity.Song;
import team.sao.musictool.fragment.PlayBarFragment;
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

    public MusicPlayReceiver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int operate = intent.getIntExtra(OPERATE, -1);
        switch (operate) {
            case OP_PLAY:          //播放音乐
                Song song = (Song) JSONUtil.parseJSONToObject(intent.getStringExtra(SONG), Song.class);
                playMusic(context, song);
                Intent intent1 = new Intent();
                intent1.setAction(PlayBarFragment.ACTION);
                intent1.putExtra(OPERATE, OP_UPDATE_UI);
                intent1.putExtra(SONG, intent.getStringExtra(SONG));
                mContext.sendBroadcast(intent1);
                break;
            case OP_PAUSE:         //暂停播放
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    Intent intent2 = new Intent();
                    intent2.setAction(PlayBarFragment.ACTION);
                    intent2.putExtra(STATUS, STATUS_PAUSE);
                    mContext.sendBroadcast(intent2);
                }
                break;
            case OP_RESUME:         //恢复播放
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    Intent intent3 = new Intent();
                    intent3.setAction(PlayBarFragment.ACTION);
                    intent3.putExtra(STATUS, STATUS_PLAYING);
                    mContext.sendBroadcast(intent3);
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
                    Intent intent = new Intent();
                    intent.setAction(PlayBarFragment.ACTION);
                    intent.putExtra(STATUS, STATUS_PLAYING);
                    mContext.sendBroadcast(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
