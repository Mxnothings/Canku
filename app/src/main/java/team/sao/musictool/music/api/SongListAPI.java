package team.sao.musictool.music.api;

import org.json.JSONObject;
import team.sao.musictool.music.entity.Song;

import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/6
 * \* Time: 23:37
 * \* Description:
 **/
public interface SongListAPI {

    /**
     * 获取歌单里歌曲
     * @param songlistid
     * @return
     */
    List<Song> getSongList(String songlistid);

    /**
     * 获取热门歌单
     * @param pagesize
     * @param pagenum
     * @return
     */
    List<Song> getHotSongList(int pagenum, int pagesize);


    /**
     * 获取歌单的URL
     * @param songlistid
     * @return
     */
    String getSongListURL(String songlistid);



}
