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
 * \* Date: 2019/6/5
 * \* Time: 21:10
 * \* Description:
 **/
public class NetEaseMusicUtilV2 {

    public static final String SEARCH_TYPE_SONG = "song";
    public static final String SEARCH_TYPE_SINGER = "singer";
    public static final String SEARCH_TYPE_ALBUM = "album";
    public static final String SEARCH_TYPE_SONGLIST = "songList";
    public static final String SEARCH_TYPE_VIDEO = "video";
    public static final String SEARCH_TYPE_RADIO = "radio";
    public static final String SEARCH_TYPE_USER = "user";
    public static final String SEARCH_TYPE_LRC = "lrc";

    public static final String API_URL = "https://v1.itooi.cn";
    public static final String SEARCH_URL = API_URL + "/netease/search?keyword=#{keyword}&type=#{type}&pageSize=#{pagesize}&page=#{pagenum}";


    public static void main(String[] args) throws IOException {
        System.out.println(getSongsJsonByKeyword("你好",20, 0));
    }

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
            songsinfo = getSongsJsonByKeyword(keyword, pagenum, pagesize).getJSONObject("data").getJSONArray("songs");
        } catch (Exception e) {
            return null;
        }
        for (Object o : songsinfo) {
            JSONObject songinfo = (JSONObject) o;
            String songid = songinfo.getString("id");
            String name = songinfo.getString("name");
            String singer_name = songinfo.getJSONArray("ar").getJSONObject(0).getString("name");
            String downloadUrl = songinfo.getString("url");
            String imgurl = songinfo.getJSONObject("al").getString("picUrl");
            Integer time = songinfo.getInteger("dt");
            Song song = new Song(MusicType.NETEASE_MUSIC, name, songid, downloadUrl, singer_name, null, "暂无信息", imgurl, null, time == null ? 0 : time);
            songslist.add(song);
        }
        return songslist;
    }

    public static JSONObject getSongsJsonByKeyword(String keyword, int pagenum, int pagesize) throws IOException {
        return JSON.parseObject(Jsoup.connect(getSearchURL(keyword, SEARCH_TYPE_SONG, pagenum, pagesize))
                .method(Connection.Method.GET)
                .header("Content-type", "application/x-www-form-urlencoded")
                .ignoreContentType(true).execute().body());
    }


    public static String getSearchURL(String keyword, String type, int pagesize, int pagenum) {
        return API_URL + "/netease/search?keyword=" + keyword + "&type=" + type + "&pageSize=" + pagesize + "&page=" + pagenum;
    }



}
