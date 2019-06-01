package team.sao.musictool.config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import team.sao.musictool.entity.Song;

import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 21:19
 * \* Description:
 **/
public class PlayerInfo {

    public static final String SONG = "SONG";
    public static final String OPERATE = "OPTERATE";
    public static final String STATUS = "STATUS";
    public static final String UPDATE_STATUS = "UPDATE_STATUS";
    public static final String POSITION= "POSITION";

    public static final int OP_PAUSE = 0;                       //暂停
    public static final int OP_PLAY = 1;                        //播放
    public static final int OP_NEXT_SONG = 2;                   //下一首
    public static final int OP_PRE_SONG = 3;                    //上一首
    public static final int OP_RESUME = 4;                      //恢复播放
    public static final int OP_UPDATE_UI = 5;                   //更新ui
    public static final int OP_UPDATE_UI_NOIMG = 6;             //更新ui
    public static final int OP_UPDATE_UI_IMG = 7;               //更新ui
    public static final int OP_SEND_UPDATE_UI = 8;              //发送更新UI信息
    public static final int OP_SEEKTO = 9;                      //
    public static final int OP_UPDATE_PROGRESS = 10;

    //播放状态
    public static final int STATUS_NOTINIT = -1;
    public static final int STATUS_PAUSE = 0;
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_LOADING = 2;


    private static class Holder {
        private static PlayerInfo playerInfo = new PlayerInfo();
    }

    private PlayerInfo() {
    }

    public static PlayerInfo getInstance() {
        return Holder.playerInfo;
    }

    private List<Song> play_list;
    private Bitmap songImg;
    private int playingSongIndex = -1;
    private int status = STATUS_NOTINIT;
    private int position = 0;
    private Song playingSong;

    public Bitmap getSongImg() {
        return songImg;
    }

    public synchronized void setSongImg(Bitmap songImg) {
        this.songImg = songImg;
    }

    public Song getPlayingSong() {
        return playingSong;
    }

    public synchronized void setPlayingSong(Song playingSong) {
        this.playingSong = playingSong;
    }

    public List<Song> getPlay_list() {
        return play_list;
    }

    public synchronized void setPlay_list(List<Song> play_list) {
        this.play_list = play_list;
    }

    public synchronized Song nextSong() {
        if (playingSongIndex > -1 && playingSongIndex < play_list.size() - 1) {
            return playingSong = play_list.get(++playingSongIndex);
        }
        return null;
    }

    public synchronized Song preSong() {
        if (playingSongIndex > 0 && playingSongIndex < play_list.size() - 1) {
            return playingSong = play_list.get(--playingSongIndex);
        }
        return null;
    }

    public int getPlayingSongIndex() {
        return playingSongIndex;
    }

    public synchronized void setPlayingSongIndex(int playingSongIndex) {
        if (play_list != null && playingSongIndex > -1 && playingSongIndex < play_list.size()) {
            this.playingSongIndex = playingSongIndex;
            this.playingSong = play_list.get(playingSongIndex);
        }
    }

    public int getStatus() {
        return status;
    }

    public synchronized void setStatus(int status) {
        this.status = status;
    }

    public boolean isStatus(int STATUS) {
        return this.status == STATUS;
    }

    public int getPosition() {
        return position;
    }

    public synchronized void setPosition(int position) {
        this.position = position;
    }

    //获取图片
    public static Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            Log.i("imgurl", url);
            Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).ignoreContentType(true).execute();
            bmp = BitmapFactory.decodeStream(response.bodyStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

}
