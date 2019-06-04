package team.sao.musictool.entity;

import team.sao.musictool.dao.autodatabase.annotation.Entity;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/4
 * \* Time: 19:12
 * \* Description:
 **/
@Entity("my_favor_song")
public class MyFavorSong extends Song {

    public MyFavorSong() {
    }

    public MyFavorSong(int musicType, String name, String songid, String downloadUrl, String singer, Integer albumid, String albumname, String imgurl, String subtitle, int time) {
        super(musicType, name, songid, downloadUrl, singer, albumid, albumname, imgurl, subtitle, time);
    }

    public MyFavorSong(Song song) {
        super(song.getMusicType(), song.getName(), song.getSongid(), song.getDownloadUrl(), song.getSinger(), song.getAlbumid(), song.getAlbumname(), song.getImgurl(), song.getSubtitle(), song.getTime());
    }

}
