package team.sao.musictool.music.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import team.sao.musictool.music.api.MusicAPI;
import team.sao.musictool.music.entity.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.music.config.MusicAPIConfig.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/6
 * \* Time: 2:23
 * \* Description:
 **/
public class KugouMusicAPI implements MusicAPI {

    public static final String MUSIC_TYPE = MUSIC_TYPE_KUGOU;


    @Override
    public List<Song> searchSongs(String keyword, int pagenum, int pagesize) throws IOException {
        JSONArray songs_result = null;
        try {
            songs_result = getSongSearchResult(keyword, pagenum, pagesize).getJSONObject("data").getJSONArray("info");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<Song> songs = new ArrayList<>();
        for (Object o : songs_result) {
            JSONObject song_jo = (JSONObject) o;
            String name = song_jo.getString("songname");
            String songid = song_jo.getString("hash");
            String downloadUrl = getSongPlayURL(songid);
            String singer = song_jo.getString("singername");
            String albumid = song_jo.getString("album_audio_id");
            String albumname = song_jo.getString("album_name");
            String imgurl = getSongImgURL(songid);
            String alia = song_jo.getString("topic");
            Integer duration = song_jo.getInteger("duration");

            songs.add(new Song(MUSIC_TYPE, name, songid, downloadUrl, singer, albumid, albumname, imgurl, alia, duration));
        }
        return songs;
    }


    @Override
    public JSONObject getSongSearchResult(String keyword, int pagenum, int pagesize) throws IOException {
        return JSON.parseObject(Jsoup.connect(getSongSearchURL(keyword, pagenum, pagesize))
                .header("Content-type", "application/x-www-form-urlencoded")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute()
                .body())
                ;
    }


    @Override
    public String getLyricURL(String songid) {
        return LYRIC_URL(MUSIC_TYPE, songid);
    }


    @Override
    public String getSongPlayURL(String songid) {
        return SONG_PLAY_URL(MUSIC_TYPE, songid);
    }


    @Override
    public String getSongImgURL(String songid) {
        return SONG_IMG_URL(MUSIC_TYPE, songid);
    }


    @Override
    public String getSongSearchURL(String keyword, int pagenum, int pagesize) {
        return SEARCH_URL(MUSIC_TYPE, keyword, SEARCH_TYPE_SONG, pagenum, pagesize);
    }
}