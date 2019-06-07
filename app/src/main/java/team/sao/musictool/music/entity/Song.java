package team.sao.musictool.music.entity;

import com.alibaba.fastjson.annotation.JSONField;
import team.sao.musictool.dao.autodatabase.annotation.DBColumn;
import team.sao.musictool.dao.autodatabase.db.DataType;
import team.sao.musictool.music.config.MusicAPIConfig;

/****
 * @author:MrWangx
 * @description
 * @Date 2018/12/23 3:33
 *****/
public class Song {

    private String musicType;      //音乐类型
    private String name;        //歌名
    @DBColumn(primaryKey = true)
    private String songid;     //歌曲mid
    private String singer;      //歌手
    private String albumid;     //专辑id
    private String albumname;   //专辑名
    private String imgurl;
    private String alia;    //别名
    @DBColumn(type = DataType.INTEGER)
    private int duration;        //时长

    public Song() {

    }

    public Song(String musicType, String name, String songid, String singer, String albumid, String albumname, String imgurl, String alia, int duration) {
        this.musicType = musicType;
        this.name = name;
        this.songid = songid;
        this.singer = singer;
        this.albumid = albumid;
        this.albumname = albumname;
        this.imgurl = imgurl;
        this.alia = alia;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Song{" +
                "musicType='" + musicType + '\'' +
                ", name='" + name + '\'' +
                ", songid='" + songid + '\'' +
                ", singer='" + singer + '\'' +
                ", albumid='" + albumid + '\'' +
                ", albumname='" + albumname + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", alia='" + alia + '\'' +
                ", duration=" + duration +
                '}';
    }

    @JSONField(serialize = false)
    public String getFormatTime() {
        return String.format("%02d:%02d", duration / 60, duration % 60);
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    @JSONField(serialize = false)
    public String SONG_PLAY_URL() {
        return MusicAPIConfig.SONG_PLAY_URL(musicType, songid);
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlia() {
        return alia;
    }

    public void setAlia(String alia) {
        this.alia = alia;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
