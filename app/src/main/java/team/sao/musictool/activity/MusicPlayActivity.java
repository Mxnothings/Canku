package team.sao.musictool.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.util.StatusBarUtil;

import static team.sao.musictool.annotation.AnnotationProcesser.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 15:25
 * \* Description:
 **/
public class MusicPlayActivity extends Activity {

    @ViewID(R.id.ic_back)
    private ImageView back;
    @ViewID(R.id.songname)
    private TextView songname;
    @ViewID(R.id.singer)
    private TextView singer;
    @ViewID(R.id.album_img)
    private ImageView aimg;
    @ViewID(R.id.ic_like)
    private ImageView like;
    @ViewID(R.id.ic_download)
    private ImageView download;
    @ViewID(R.id.ic_more)
    private ImageView more;
    @ViewID(R.id.ic_presong)
    private ImageView presong;
    @ViewID(R.id.ic_play_pause)
    private ImageView play_pause;
    @ViewID(R.id.ic_nextsong)
    private ImageView nextsong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        inject(this, MusicPlayActivity.class, this);
        StatusBarUtil.setStatusBarMode(this, false, R.color.color_netease_text);
        initAction();
    }

    private void initAction() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayActivity.this.finish();
            }
        });
    }
}
