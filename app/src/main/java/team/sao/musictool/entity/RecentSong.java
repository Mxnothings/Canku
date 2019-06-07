package team.sao.musictool.entity;

import team.sao.musictool.dao.autodatabase.annotation.Entity;
import team.sao.musictool.music.entity.Song;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/4
 * \* Time: 19:11
 * \* Description:
 **/
@Entity("recent_song")
public class RecentSong extends Song {

    public RecentSong() {
    }

    public RecentSong(String musicType, String name, String songid, String singer, String albumid, String albumname, String imgurl, String alia, int duration) {
        super(musicType, name, songid, singer, albumid, albumname, imgurl, alia, duration);
    }

    public RecentSong(Song song) {
        super(song.getMusicType(), song.getName(), song.getSongid(), song.getSinger(), song.getAlbumid(), song.getAlbumname(), song.getImgurl(), song.getAlia(), song.getDuration());
    }

}
