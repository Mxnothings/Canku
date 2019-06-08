package team.sao.musictool.music.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import team.sao.musictool.music.api.MusicAPI;
import team.sao.musictool.music.entity.Song;
import team.sao.musictool.music.entity.SongList;

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

    public static void main(String[] args) {
        MusicAPI api = new KugouMusicAPI();
////        for (SongList s : api.searchSongList("孤独", 0, 20)) {
////            System.out.println(s);
////        }
//        for (Song s: api.getSongsFromSongList("122891")) {
//            System.out.println(s);
//        }
    }

    @Override
    public List<Song> searchSong(String keyword, int pagenum, int pagesize) {
        JSONArray songs_result = null;
        try {
            songs_result = getSongSearchJson(keyword, pagenum, pagesize).getJSONObject("data").getJSONArray("info");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<Song> songs = new ArrayList<>();
        for (Object o : songs_result) {
            try {
                JSONObject song_jo = (JSONObject) o;
                String name = song_jo.getString("songname");
                String songid = song_jo.getString("hash");
                String singer = song_jo.getString("singername");
                String albumid = song_jo.getString("album_id");
                String albumname = song_jo.getString("album_name");
                String imgurl = getSongImgURL(songid);
                String alia = song_jo.getString("topic");
                Integer duration = song_jo.getInteger("duration");

                songs.add(new Song(MUSIC_TYPE, name, songid, singer, albumid, albumname, imgurl, alia, duration));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return songs;
    }

    @Override
    public List<Song> getSongsFromSongList(String songlistid) {
        JSONArray songs_result = null;
        try {
            songs_result = getSongListJson(songlistid).getJSONObject("data").getJSONArray("info");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<Song> songs = new ArrayList<>();
        for (Object o : songs_result) {
            try {
                JSONObject song_jo = (JSONObject) o;
                String[] name_singer = song_jo.getString("filename").split(" - ");
                String name = name_singer[0];
                String songid = song_jo.getString("hash");
                String singer = name_singer.length > 0 ? name_singer[1] : "";
                String albumid = song_jo.getString("album_id");
                String albumname = "暂无";
                String imgurl = getSongImgURL(songid);
                String alia = "";
                Integer duration = song_jo.getInteger("duration");

                songs.add(new Song(MUSIC_TYPE, name, songid, singer, albumid, albumname, imgurl, alia, duration));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return songs;
    }

    @Override
    public List<SongList> searchSongList(String keyword, int pagenum, int pagesize) {
        JSONArray songlist_result = null;
        try {
            songlist_result = getSongListSearchJson(keyword, pagenum, pagesize).getJSONObject("data").getJSONArray("info");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<SongList> songlists = new ArrayList<>();
        for (Object o : songlist_result) {
            try {
                JSONObject songlist_jo = (JSONObject) o;
                String id = songlist_jo.getString("specialid");
                String imgUrl = songlist_jo.getString("imgurl").replaceAll("\\{size\\}", "150");
                String name = songlist_jo.getString("specialname");
                Integer songCount = songlist_jo.getInteger("songcount");
                Integer playCount = songlist_jo.getInteger("playcount");
                SongList songList = new SongList(MUSIC_TYPE, id, imgUrl, name, songCount, playCount);
                songlists.add(songList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return songlists;
    }

    @Override
    public JSONObject getSongListJson(String songlistid) throws IOException {
        return JSON.parseObject(Jsoup.connect(getSongListURL(songlistid))
                .header("Content-type", "application/x-www-form-urlencoded")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute()
                .body())
                ;
    }


    @Override
    public JSONObject getSongSearchJson(String keyword, int pagenum, int pagesize) throws IOException {
        return JSON.parseObject(Jsoup.connect(getSongSearchURL(keyword, pagenum, pagesize))
                .header("Content-type", "application/x-www-form-urlencoded")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute()
                .body())
                ;
    }

    @Override
    public JSONObject getSongListSearchJson(String keyword, int pagenum, int pagesize) throws IOException {
        return JSON.parseObject(Jsoup.connect(getSongListSearchURL(keyword, pagenum, pagesize))
                .header("Content-type", "application/x-www-form-urlencoded")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .execute()
                .body())
                ;
    }

    @Override
    public String getLyric(String songid) {
        String lyric = null;
        try {
            lyric = Jsoup.connect(LYRIC_URL(MUSIC_TYPE, songid))
                    .header("Content-type", "application/x-www-form-urlencoded")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lyric;
    }

    @Override
    public String getSongListSearchURL(String keyword, int pagenum, int pagesize) {
        return SEARCH_URL(MUSIC_TYPE, keyword, SEARCH_TYPE_SONGLIST, pagenum, pagesize);
    }

    @Override
    public String getSongListURL(String songlistid) {
        return SONGLIST_URL(MUSIC_TYPE, songlistid);
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
