package team.sao.musictool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.entity.Song;
import team.sao.musictool.util.IntentBuilder;

import java.io.IOException;

import static team.sao.musictool.config.PlayerInfo.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 20:42
 * \* Description:
 **/
public class MusicPlayReceiver extends BroadcastReceiver {

    public static final String ACTION = ReceiverAction.MUSICPLAY_CENTER;

    private MediaPlayer mediaPlayer;
    private Context mContext;
    private PlayerInfo playerInfo = PlayerInfo.getInstance();

    public MusicPlayReceiver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int operate = intent.getIntExtra(OPERATE, -1);
        IntentBuilder ib = new IntentBuilder();
        switch (operate) {
            case OP_PLAY:          //播放音乐
                playMusic(context, playerInfo.getPlayingSong());
                ib.action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_UI_NOIMG).extra(UPDATE_STATUS, true).send(mContext); //更新ui noimg
                break;
            case OP_PAUSE:         //暂停播放
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playerInfo.setStatus(STATUS_PAUSE);
                    ib.action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
                }
                break;
            case OP_RESUME:         //恢复播放
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    playerInfo.setStatus(STATUS_PLAYING);
                    ib.action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
                }
                break;
            case OP_NEXT_SONG:      //下一首
                playMusic(context, playerInfo.nextSong());
                ib.action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_UI_NOIMG).extra(UPDATE_STATUS, true).send(mContext); //更新ui noimg
                break;
            case OP_PRE_SONG:       //上一首
                playMusic(context, playerInfo.preSong());
                ib.action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_UI_NOIMG).extra(UPDATE_STATUS, true).send(mContext); //更新ui noimg
                break;
            case OP_SEND_PLAYBAR_UPDATE_UI: //给playbar发送更新ui广播
                if (!(playerInfo.getStatus() == STATUS_NOTINIT || playerInfo.getPlayingSong() == null)) {
                    sendUpdateUI();
                }
                break;
        }
    }


    //播放音乐
    private void playMusic(final Context context, final Song song) {
        if (song != null) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("播放错误", "播放" + song + "错误");
                    playerInfo.setStatus(STATUS_PAUSE);
                    new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
                    return true;
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() { //获取图片
                    Bitmap img = getURLimage(song.getImgurl());
                    playerInfo.setSongImg(img);
                    new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_UI_IMG).send(mContext);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() { //加载音乐
                    try {
                        playerInfo.setStatus(STATUS_LOADING);
                        mediaPlayer.setDataSource(context, Uri.parse(song.getDownloadUrl()));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        playerInfo.setStatus(STATUS_PLAYING);
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
                    } catch (IOException e) {
                        e.printStackTrace();
                        playerInfo.setStatus(STATUS_PAUSE);
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
                    }
                }
            }).start();
        }
    }

    private void sendUpdateUI() {
        mContext.sendBroadcast(new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI)
                .extra(UPDATE_STATUS, true)
                .extra(OPERATE, OP_UPDATE_UI)
                .build());
    }

}
