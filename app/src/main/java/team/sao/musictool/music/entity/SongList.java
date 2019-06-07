package team.sao.musictool.music.entity;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/7
 * \* Time: 13:57
 * \* Description:
 **/
public class SongList {

    private String musicType;
    private String id;
    private String imgUrl;
    private String name;
    private int songCount;
    private int playCount;

    public SongList() {
    }

    public SongList(String musicType, String id, String imgUrl, String name, int songCount, int playCount) {
        this.musicType = musicType;
        this.id = id;
        this.imgUrl = imgUrl;
        this.name = name;
        this.songCount = songCount;
        this.playCount = playCount;
    }

    @Override
    public String toString() {
        return "SongList{" +
                "musicType='" + musicType + '\'' +
                ", id='" + id + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", name='" + name + '\'' +
                ", songCount=" + songCount +
                ", playCount=" + playCount +
                '}';
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}
