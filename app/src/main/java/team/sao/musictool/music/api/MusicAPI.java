package team.sao.musictool.music.api;

import com.alibaba.fastjson.JSONObject;
import team.sao.musictool.music.entity.Song;

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
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     */
    List<Song> searchSongs(String keyword, int pagenum, int pagesize) throws IOException;


    /**
     * 获取搜索结果
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     */
    JSONObject getSongSearchResult(String keyword, int pagenum, int pagesize) throws IOException;

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
