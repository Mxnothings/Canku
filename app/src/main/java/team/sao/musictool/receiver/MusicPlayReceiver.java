package team.sao.musictool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.dao.MusicToolDataBase;
import team.sao.musictool.entity.RecentSong;
import team.sao.musictool.music.MusicAPIHolder;
import team.sao.musictool.music.entity.Song;
import team.sao.musictool.util.IntentBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MusicToolDataBase musicToolDataBase;
    private LoadPlyerSongTask songTask;
    private LoadImgTask imgTask;
    private LoadLyricTask lyricTask;

    private volatile boolean s = true;

    public MusicPlayReceiver(final Context mContext) {
        this.mContext = mContext;
        this.musicToolDataBase = MusicToolDataBase.getInstance(mContext);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int operate = intent.getIntExtra(OPERATE, -1);
        switch (operate) {
            case OP_PLAY:          //播放音乐
                playMusic(context, playerInfo.getPlayingSong());
                break;
            case OP_PAUSE:         //暂停播放
                pause();
                break;
            case OP_RESUME:         //恢复播放
                resume();
                break;
            case OP_NEXT_SONG:      //下一首
                nextSong();
                break;
            case OP_PRE_SONG:       //上一首
                preSong();
                break;
            case OP_SEND_UPDATE_UI: //发送更新ui广播
                sendUpdateUI();
                break;
            case OP_SEEKTO:
                seekTo(intent.getIntExtra(POSITION, 0));
                break;
        }
    }


    //播放音乐
    private void playMusic(final Context context, final Song song) {
        musicToolDataBase.insert(new RecentSong(song));
        if (song != null) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                s = false;
                mediaPlayer.release();
                mediaPlayer = null;
                s = true;
            }
            if (songTask != null && !songTask.isCancelled()) {
                songTask.cancel(true);
            }
            if (imgTask != null && !imgTask.isCancelled()) {
                imgTask.cancel(true);
            }
            if (lyricTask != null && !lyricTask.isCancelled()) {
                lyricTask.cancel(true);
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
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //设置音乐播放完
                @Override
                public void onCompletion(MediaPlayer mp) {
                    nextSong();
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() { //设置seek完成
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    if (playerInfo.getStatus() == STATUS_PLAYING) {
                        mp.start();
                    }
                }
            });
            playerInfo.setStatus(STATUS_PAUSE);
            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_UI_NOIMG).extra(UPDATE_STATUS, true).send(mContext); //更新ui noimg

            imgTask = new LoadImgTask();
            imgTask.execute(song.getImgurl()); //加载图片
            lyricTask = new LoadLyricTask();
            lyricTask.execute(); //加载歌词
            songTask = new LoadPlyerSongTask(context);
            songTask.execute(song.SONG_PLAY_URL()); //加载音乐
        }
    }

    private void nextSong() {
        Song song = playerInfo.nextSong();
        if (song != null) {
            playMusic(mContext, song);
        }
    }

    private void preSong() {
        Song song = playerInfo.preSong();
        if (song != null) {
            playMusic(mContext, song);
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playerInfo.setStatus(STATUS_PAUSE);
            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
        }
    }

    private void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playerInfo.setStatus(STATUS_PLAYING);
            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
        }
    }

    private void seekTo(int position) {
        if (mediaPlayer != null) {
            Log.i("seek", "接收到seek" + position);
            mediaPlayer.seekTo(position);
            playerInfo.setPosition(position);
        }
    }

    private void startUpdateProgress() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (s) {
                    try {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            int position = mediaPlayer.getCurrentPosition();
                            playerInfo.setPosition(position);
                            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_PROGRESS).extra(POSITION, position).send(mContext);
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendUpdateUI() {
        if (!(playerInfo.getStatus() == STATUS_NOTINIT || playerInfo.getPlayingSong() == null)) {
            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI)
                    .extra(UPDATE_STATUS, true)
                    .extra(OPERATE, OP_UPDATE_UI)
                    .send(mContext);
        }
    }

    private class LoadLyricTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Song song = playerInfo.getPlayingSong();
            String lyric = MusicAPIHolder.getAPI(song.getMusicType()).getLyric(song.getSongid());
            return lyric;
        }

        @Override
        protected void onPostExecute(String s) {
            playerInfo.setLyric(s);
            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_LYRIC).send(mContext);
        }
    }

    private class LoadPlyerSongTask extends AsyncTask<String, Integer, String>{

        private Context context;

        public LoadPlyerSongTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(STATUS_LOADING);
                mediaPlayer.setDataSource(context, Uri.parse(strings[0]));
                mediaPlayer.prepare();
                mediaPlayer.start();
                publishProgress(STATUS_PLAYING);
            } catch (IOException e) {
                e.printStackTrace();
                playerInfo.setStatus(STATUS_PAUSE);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            playerInfo.setStatus(values[0]);
            switch (values[0]) {
                case STATUS_LOADING:
                    break;
                case STATUS_PLAYING:
                    playerInfo.getPlayingSong().setDuration(mediaPlayer.getDuration() / 1000);
                    startUpdateProgress();
                    break;
                case STATUS_PAUSE:
                    break;
            }
            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(UPDATE_STATUS, true).send(mContext);
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private class LoadImgTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Bitmap img = getURLimage(strings[0]);
            playerInfo.setSongImg(img);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new IntentBuilder().action(ReceiverAction.MUSICPLAY_UI).extra(OPERATE, OP_UPDATE_UI_IMG).send(mContext);
        }

    }


}
