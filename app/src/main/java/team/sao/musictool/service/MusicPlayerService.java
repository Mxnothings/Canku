package team.sao.musictool.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 3:00
 * \* Description:
 **/
public class MusicPlayerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
