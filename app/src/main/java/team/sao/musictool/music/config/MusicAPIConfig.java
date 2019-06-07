package team.sao.musictool.music.config;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/6
 * \* Time: 0:27
 * \* Description:
 **/
public class MusicAPIConfig {

    //API域名
    public static final String API_BASE_URL = "https://v1.itooi.cn";

    //搜索
    public static final String SEARCH_BASE_URL = "/search?keyword=${keyword}&type=${search_type}&pageSize=${pagesize}&page=${pagenum}";
    //歌曲播放
    public static final String SONG_PLAY_BASE_URL = "/url?id=${songid}";
    //歌曲图片
    public static final String SONG_IMG_BASE_URL = "/pic?id=${songid}";
    //歌词
    public static final String LYRIC_BASE_URL = "/lrc?id=${songid}";
    //歌单
    public static final String SONGLIST_BASE_URL = "/songList?id=${songlistid}";



    //搜索类型
    public static final String SEARCH_TYPE_SONG = "song";
    public static final String SEARCH_TYPE_SINGER = "singer";
    public static final String SEARCH_TYPE_ALBUM = "album";
    public static final String SEARCH_TYPE_SONGLIST = "songList";
    public static final String SEARCH_TYPE_VIDEO = "video";
    public static final String SEARCH_TYPE_RADIO = "radio";
    public static final String SEARCH_TYPE_USER = "user";
    public static final String SEARCH_TYPE_LRC = "lrc";

    //音乐类型
    public static final String MUSIC_TYPENAME = "MUSIC_TYPENAME";
    public static final String MUSIC_TYPE_TECENT = "tencent";
    public static final String MUSIC_TYPE_NETEASE = "netease";
    public static final String MUSIC_TYPE_KUGOU = "kugou";
    public static final String MUSIC_TYPE_KUWO = "kuwo";
    public static final String MUSIC_TYPE_MIGU = "migu";
    public static final String MUSIC_TYPE_BAIDU = "baidu";


    public static void main(String[] args) {
        System.out.println(SONG_PLAY_URL(MUSIC_TYPE_NETEASE, "424264505"));
    }

    /**
     * 获取搜索的URL
     * @param MUSIC_TYPE 音乐类型 网易 腾讯 酷我等
     * @param keyword   关键词
     * @param SEARCH_TYPE 搜索类型
     * @param pagesize
     * @param pagenum
     * @return
     */
    public static String SEARCH_URL(String MUSIC_TYPE, String keyword, String SEARCH_TYPE, int pagenum, int pagesize) {
        return API_BASE_URL + "/" + MUSIC_TYPE + "/search?keyword=" + keyword + "&type=" + SEARCH_TYPE + "&pageSize=" + pagesize + "&page=" + pagenum;
    }

    /**
     * 获取歌曲播放地址
     * @param MUSIC_TYPE
     * @param songid
     * @return
     */
    public static String SONG_PLAY_URL(String MUSIC_TYPE, String songid) {
        return API_BASE_URL + "/" + MUSIC_TYPE + "/url?id=" + songid;
    }

    /**
     * 获取歌曲图片url
     * @param MUSICTYPE
     * @param songid
     * @return
     */
    public static String SONG_IMG_URL(String MUSICTYPE, String songid) {
        return API_BASE_URL + "/" + MUSICTYPE + "/pic?id=" + songid;
    }

    /**
     * 获取歌词的url
     * @param MUSICTYPE
     * @param songid
     * @return
     */
    public static String LYRIC_URL(String MUSICTYPE, String songid) {
        return API_BASE_URL + "/" + MUSICTYPE + "/lrc?id=" + songid;
    }

    /**
     * 获取歌单的url
     * @param MUSICTYPE
     * @param songlistid
     * @return
     */
    public static String SONGLIST_URL(String MUSICTYPE, String songlistid) {
        return API_BASE_URL + "/" + MUSICTYPE + "/songList?id=" + songlistid;
    }

}
