package team.sao.musictool.music.api;

import com.alibaba.fastjson.JSONObject;
import team.sao.musictool.music.entity.Song;
import team.sao.musictool.music.entity.SongList;

import java.io.IOException;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/5
 * \* Time: 22:21
 * \* Description:
 **/
public interface MusicAPI {

    /**
     * 搜索歌曲
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     */
    List<Song> searchSong(String keyword, int pagenum, int pagesize);

    /**
     * 获取歌单中的歌曲
     * @param songlistid
     * @return
     */
    List<Song> getSongsFromSongList(String songlistid);

    /**
     * 搜索歌单
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     */
    List<SongList> searchSongList(String keyword, int pagenum, int pagesize);

    /**
     * 获取歌单的json
     * @param songlistid
     * @return
     */
    JSONObject getSongListJson(String songlistid) throws IOException;

    /**
     * 获取歌曲搜索json
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     */
    JSONObject getSongSearchJson(String keyword, int pagenum, int pagesize) throws IOException;

    /**
     * 获取歌单搜索json
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     */
    JSONObject getSongListSearchJson(String keyword, int pagenum, int pagesize) throws IOException;

    /**
     * 获取歌单搜索的url
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     */
    String getSongListSearchURL(String keyword, int pagenum, int pagesize);

    /**
     * 获取歌单的url
     * @param songlistid
     * @return
     */
    String getSongListURL(String songlistid);

    /**
     * 获取歌词的url
     * @param songid
     * @return
     */
    String getLyricURL(String songid);

    /**
     * 获取歌曲图片url
     * @param songid
     * @return
     */
    String getSongImgURL(String songid);

    /**
     * 获取歌曲的播放URL
     * @param songid
     * @return
     */
    String getSongPlayURL(String songid);

    /**
     * 获取歌曲搜索的url
     * @param keyword
     * @param pagesize
     * @param pagenum
     * @return
     */
    String getSongSearchURL(String keyword, int pagenum, int pagesize);

}
