package team.sao.musictool.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import team.sao.musictool.MainApp;
import team.sao.musictool.music.entity.Song;

import java.io.File;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/9
 * \* Time: 14:35
 * \* Description:
 **/
public class DownloadTool {


    public static void downloadSong(Song song) {
        DownloadManager dManager = (DownloadManager) MainApp.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(song.SONG_PLAY_URL()));
        File dir = new File(MainApp.DOWNLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        request.setDestinationInExternalPublicDir(MainApp.DOWNLOAD_PATH, song.getName() + " - " + song.getSinger() + ".mp3");
        request.setTitle("下载音乐");
        request.setDescription("正在下载" + song.getName() + "-" + song.getSinger());
        dManager.enqueue(request);
        Toast.makeText(MainApp.getInstance(), "正在下载  " + song.getName() + " - " + song.getSinger(), Toast.LENGTH_SHORT).show();
    }

}
