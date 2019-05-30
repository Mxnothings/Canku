package team.sao.musictool.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;
import team.sao.musictool.receiver.MusicPlayReceiver;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 3:00
 * \* Description:
 **/
public class MusicPlayerService extends Service {

    private MusicPlayReceiver musicPlayReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        regist();
        Toast.makeText(this, "MusicPlayer Service onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        unRegist();
        super.onDestroy();
        Toast.makeText(this, "MusicPlayer Service onDestroy", Toast.LENGTH_SHORT).show();
    }

    private void regist() {
        musicPlayReceiver = new MusicPlayReceiver(MusicPlayerService.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayReceiver.ACTION);
        registerReceiver(musicPlayReceiver, intentFilter);
    }

    private void unRegist() {
        if (musicPlayReceiver != null) {
            unregisterReceiver(musicPlayReceiver);
        }
    }

}
