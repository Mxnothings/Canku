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
 * \* Time: 1:56
 * \* Description:
 **/
public class QQMusicAPI implements MusicAPI {

    public static final String MUSIC_TYPE = MUSIC_TYPE_TECENT;

    public static void main(String[] args) {
        QQMusicAPI api = new QQMusicAPI();
//        for (Song s: api.getSongsFromSongList("6914150646")) {
//            System.out.println(s);
//        }
        for (SongList s : api.getHotSongList(SONGLIST_ORDERTYPE_HOT, 0, 20)) {
            System.out.println(s);
        }
    }

    //获取热门歌单
    public static List<SongList> getHotSongList(String SONGLIST_TYPE, int pagenum, int pagesize) {
        JSONArray songlist_result = null;
        try {
            songlist_result = JSON.parseObject(Jsoup.connect(API_BASE_URL + "/" + MUSIC_TYPE + "/songList/hot?cat=全部&pageSize="
                    + pagesize + "&page=" + pagenum + "&orderType=" + SONGLIST_TYPE)
                    .header("Content-type", "application/x-www-form-urlencoded")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute()
                    .body()).getJSONObject("data").getJSONArray("list")
                    ;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(songlist_result);
        List<SongList> songlists = new ArrayList<>();
        for (Object o : songlist_result) {
            try {
                JSONObject songlist_jo = (JSONObject) o;
                String id = songlist_jo.getString("dissid");
                String imgUrl = songlist_jo.getString("imgurl");
                String name = songlist_jo.getString("dissname");
                Integer songCount = -1;
                Integer playCount = songlist_jo.getInteger("listennum");
                SongList songList = new SongList(MUSIC_TYPE, id, imgUrl, name, songCount, playCount);
                songlists.add(songList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return songlists;
    }

    @Override
    public List<Song> searchSong(String keyword, int pagenum, int pagesize) {
        JSONArray songs_result = null;
        try {
            songs_result = getSongSearchJson(keyword, pagenum, pagesize).getJSONObject("data").getJSONArray("list");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<Song> songs = new ArrayList<>();
        for (Object o : songs_result) {
            try {
                JSONObject song_jo = (JSONObject) o;
                String name = song_jo.getString("songname");
                String songid = song_jo.getString("songmid");
                String singer = song_jo.getJSONArray("singer").getJSONObject(0).getString("name");
                String albumid = song_jo.getString("albumid");
                String albumname = song_jo.getString("albumname");
                String imgurl = getSongImgURL(songid);
                String alia = "";
                Integer duration = song_jo.getInteger("interval");
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
            songs_result = getSongListJson(songlistid).getJSONArray("data").getJSONObject(0).getJSONArray("songlist") ;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<Song> songs = new ArrayList<>();
        for (Object o : songs_result) {
            try {
                JSONObject song_jo = (JSONObject) o;
                String name = song_jo.getString("name");
                String songid = song_jo.getString("mid");
                String singer = song_jo.getJSONArray("singer").getJSONObject(0).getString("name");
                JSONObject album_jo = song_jo.getJSONObject("album");
                String albumid = album_jo.getString("id");
                String albumname = album_jo.getString("name");
                String imgurl = getSongImgURL(songid);
                String alia = album_jo.getString("subtitle");
                Integer duration = song_jo.getInteger("interval");
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
            songlist_result = getSongListSearchJson(keyword, pagenum, pagesize).getJSONObject("data").getJSONArray("list");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<SongList> songlists = new ArrayList<>();
        for (Object o : songlist_result) {
            try {
                JSONObject songlist_jo = (JSONObject) o;
                String id = songlist_jo.getString("dissid");
                String imgUrl = songlist_jo.getString("imgurl");
                String name = songlist_jo.getString("dissname");
                Integer songCount = songlist_jo.getInteger("song_count");
                Integer playCount = songlist_jo.getInteger("listennum");
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
    public String getSongImgURL(String songid) {
        return SONG_IMG_URL(MUSIC_TYPE, songid);
    }


    @Override
    public String getSongPlayURL(String songid) {
        return SONG_PLAY_URL(MUSIC_TYPE, songid);
    }


    @Override
    public String getSongSearchURL(String keyword, int pagenum, int pagesize) {
        return SEARCH_URL(MUSIC_TYPE, keyword, SEARCH_TYPE_SONG, pagenum, pagesize);
    }
}
