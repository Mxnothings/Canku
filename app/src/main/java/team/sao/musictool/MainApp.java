package team.sao.musictool;

import android.app.Application;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/4
 * \* Time: 19:46
 * \* Description:
 **/
public class MainApp extends Application {

    public static final String DOWNLOAD_PATH = "MusicTool";

    private static MainApp app;

    public static MainApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

}
