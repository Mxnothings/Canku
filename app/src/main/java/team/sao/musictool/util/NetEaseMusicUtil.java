package team.sao.musictool.util;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import team.sao.musictool.config.MusicType;
import team.sao.musictool.entity.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/28
 * \* Time: 1:27
 * \* Description:
 **/
public class NetEaseMusicUtil {

    public static void main(String[] args) throws IOException {
        System.out.println(getSongsByKeyword("园游会", 1, 20).get(0).getDownloadUrl());
    }

    public static final String API_URL = "https://api.imjad.cn/cloudmusic/";
    public static final String SONG_SEARCH_URL = API_URL + "?type=search&s=#{keyword}&limit=#{end}&offset=#{begin}";
    public static final String SONG_PLAY_BASE_URL = API_URL + "?type=song&id=#{songid}&search_type=1";


    /**
     * 根据关键词查找歌曲信息
     *
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     * @throws IOException
     */
    public static List<Song> getSongsByKeyword(String keyword, int pagenum, int pagesize) throws IOException {
        List<Song> songslist = new ArrayList<>();
        //获取歌歌曲信息表的jsonarray
        JSONArray songsinfo = getSongsJsonBykeyword(keyword, pagenum, pagesize).getJSONObject("result").getJSONArray("songs");
        for (Object o : songsinfo) {
            JSONObject songinfo = (JSONObject) o;
            String songid = songinfo.getString("id");
            String name = songinfo.getString("name");
            String singer_name = songinfo.getJSONArray("ar").getJSONObject(0).getString("name");
            Integer album_id = songinfo.getJSONObject("al").getInteger("id");
            String album_name = songinfo.getJSONObject("al").getString("name");
            Song song = new Song(MusicType.NETEASE_MUSIC, name, songid, null, singer_name, album_id, album_name, null, "暂无");
            songslist.add(song);
        }
        return songslist;
    }

    public static String getSongPlayUrl(String songid) {
        try {
            return JSON.parseObject
                    (Jsoup.connect(SONG_PLAY_BASE_URL.replaceAll("#\\{songid\\}", songid)).method(Connection.Method.GET).ignoreContentType(true).execute().body())
                    .getJSONArray("data").getJSONObject(0).getString("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param keyword
     * @param pagenum
     * @param pagesize
     * @return
     * @throws Exception
     */
    public static JSONObject getSongsJsonBykeyword(String keyword, int pagenum, int pagesize) throws IOException {
        return JSON.parseObject(Jsoup.connect(getSongSearchUrl(keyword, pagenum, pagesize)).method(Connection.Method.GET).ignoreContentType(true).execute().body());
    }

    /**
     * 获取关键词搜索的url
     *
     * @param keyword
     * @param pagenum  从1开始
     * @param pagesize
     * @return
     */
    public static String getSongSearchUrl(String keyword, int pagenum, int pagesize) {
        int begin = (pagenum - 1) * pagesize;
        int end = begin + pagesize;
        return SONG_SEARCH_URL.replaceAll("#\\{keyword\\}", keyword).replaceAll("#\\{begin\\}", Integer.toString(begin)).replaceAll("#\\{end\\}", Integer.toString(end));
    }


}
