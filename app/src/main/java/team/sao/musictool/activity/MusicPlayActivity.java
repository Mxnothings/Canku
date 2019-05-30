package team.sao.musictool.activity;

import android.app.Activity;
import android.os.Bundle;
import team.sao.musictool.R;
import team.sao.musictool.util.StatusBarUtil;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 15:25
 * \* Description:
 **/
public class MusicPlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        StatusBarUtil.setStatusBarMode(this, false, R.color.color_netease_text);
    }
}
