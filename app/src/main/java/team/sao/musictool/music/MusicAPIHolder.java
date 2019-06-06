package team.sao.musictool.music;

import team.sao.musictool.music.api.MusicAPI;
import team.sao.musictool.music.api.impl.*;

import static team.sao.musictool.music.config.MusicAPIConfig.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/6
 * \* Time: 3:31
 * \* Description:
 **/
public class MusicAPIHolder {

    public static QQMusicAPI qqMusicAPI = null;

    public static NeteaseMusicAPI neteaseMusicAPI = null;

    public static KugouMusicAPI kugouMusicAPI = null;

    public static KuwoMusicAPI kuwoMusicAPI = null;

    public static BaiduMusicAPI baiduMusicAPI = null;


    public static MusicAPI getAPI(String MUSIC_TYPE) {
        MusicAPI api = null;
        switch (MUSIC_TYPE) {
            case MUSIC_TYPE_TECENT:
                api = QQMusicAPIHolder.musicAPI;
                break;
            case MUSIC_TYPE_NETEASE:
                api = NeteaseMusicAPIHolder.musicAPI;
                break;
            case MUSIC_TYPE_KUGOU:
                api = KugouMusicAPIHolder.musicAPI;
                break;
            case MUSIC_TYPE_KUWO:
                api = KuwoMusicAPIHolder.musicAPI;
                break;
            case MUSIC_TYPE_BAIDU:
                api = BaiduMusicAPIHolder.musicAPI;
                break;
        }
        return api;
    }

    private static class QQMusicAPIHolder {
        private static QQMusicAPI musicAPI = new QQMusicAPI();
    }

    private static class NeteaseMusicAPIHolder {
        private static NeteaseMusicAPI musicAPI = new NeteaseMusicAPI();
    }

    private static class KugouMusicAPIHolder {
        private static KugouMusicAPI musicAPI = new KugouMusicAPI();
    }

    private static class KuwoMusicAPIHolder {
        private static KuwoMusicAPI musicAPI = new KuwoMusicAPI();
    }

    private static class BaiduMusicAPIHolder {
        private static BaiduMusicAPI musicAPI = new BaiduMusicAPI();
    }



}
