package team.sao.musictool.dao;

import android.content.Context;
import team.sao.musictool.dao.autodatabase.AutoDataBase;
import team.sao.musictool.entity.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 17:22
 * \* Description:
 **/
public class MusicToolDataBase extends AutoDataBase {

    public static final String DB_NAME = "MusicTool";
    private static MusicToolDataBase musicToolDataBase = null;


    public static MusicToolDataBase getInstance(Context context) {
        if (musicToolDataBase == null) {
            synchronized (MusicToolDataBase.class) {
                if (musicToolDataBase == null) {
                    return musicToolDataBase = new MusicToolDataBase(context);
                }
            }
        }
        return musicToolDataBase;
    }

    public MusicToolDataBase(Context context) {
        super(context, DB_NAME, 1, LocalSong.class, RecentSong.class, MyFavorSong.class, SearchHistory.class);
    }

}
