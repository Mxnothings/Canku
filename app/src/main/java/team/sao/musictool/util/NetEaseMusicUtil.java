package team.sao.musictool.util;

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
//        System.out.println(getSongsJsonBykeyword("园游会", 1, 20));
//        System.out.println(getSongSearchUrl("园游会", 1, 20));
//        getSongsByKeyword("园游会", 1, 20);
    }

    public static final String API_URL = "https://api.itooi.cn/music/netease/search";
    public static final String SONG_SEARCH_URL = API_URL + "?key=579621905&type=search_green&s=#{keyword}&limit=#{end}&offset=#{begin}";


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
        JSONArray songsinfo = null;
        try {
            songsinfo = getSongsJsonBykeyword(keyword, pagenum, pagesize).getJSONArray("data");
        } catch (Exception e) {
            return null;
        }
        for (Object o : songsinfo) {
            JSONObject songinfo = (JSONObject) o;
            String songid = songinfo.getString("id");
            String name = songinfo.getString("name");
            String singer_name = songinfo.getString("singer");
            String downloadUrl = songinfo.getString("url");
            String imgurl = songinfo.getString("pic");
            String time = songinfo.getString("time");
            Song song = new Song(MusicType.NETEASE_MUSIC, name, songid, downloadUrl, singer_name, null, "暂无信息", imgurl, null, time);
            songslist.add(song);
        }
        return songslist;
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
