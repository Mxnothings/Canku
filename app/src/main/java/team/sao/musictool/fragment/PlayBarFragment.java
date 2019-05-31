package team.sao.musictool.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import team.sao.musictool.R;
import team.sao.musictool.activity.MusicPlayActivity;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.entity.Song;
import team.sao.musictool.receiver.MusicPlayReceiver;
import team.sao.musictool.util.IntentBuilder;
import team.sao.musictool.util.JSONUtil;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;
import static team.sao.musictool.config.PlayerInfo.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 13:58
 * \* Description:
 **/
public class PlayBarFragment extends Fragment {

    public static final String ACTION = "team.sao.musictool.receiver.playbar";

    @ViewID(R.id.playbar_img)
    private ImageView img;
    @ViewID(R.id.playbar_songname)
    private TextView songname;
    @ViewID(R.id.playbar_singer)
    private TextView singer;
    @ViewID(R.id.playbar_play_pause)
    private ImageView play_pause;
    @ViewID(R.id.playbar_nextsong)
    private ImageView nextsong;

    private PlayBarReceiver playBarReceiver;
    private Activity mActivity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_bar, null);
        inject(this, PlayBarFragment.class, view);
        initActions();
        new IntentBuilder().action(MusicPlayReceiver.ACTION).extra(PlayerInfo.OPERATE, PlayerInfo.OP_SEND_PLAYBAR_UPDATE_UI).send(getContext());
        return view;
    }

    private void initActions() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MusicPlayActivity.class);
                startActivity(intent);
            }
        });
        play_pause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    play_pause.setAlpha(0.5f);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    IntentBuilder ib = new IntentBuilder();
                    switch (playBarReceiver.status) {
                        case STATUS_PAUSE:
                            ib.action(MusicPlayReceiver.ACTION).extra(OPERATE, OP_RESUME).send(mActivity);
                            break;
                        case STATUS_PLAYING:
                            ib.action(MusicPlayReceiver.ACTION).extra(OPERATE, OP_PAUSE).send(mActivity);
                            break;
                    }
                    play_pause.setAlpha(1f);
                    return true;
                }
                return false;
            }
        });
        nextsong.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    nextsong.setAlpha(0.5f);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    nextsong.setAlpha(1f);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
    }


    private void register() { //注册广播
        playBarReceiver = new PlayBarReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        mActivity.registerReceiver(playBarReceiver, intentFilter);
    }

    private void unRegister() {
        if (playBarReceiver != null) {
            mActivity.unregisterReceiver(playBarReceiver);
        }
    }


    public class PlayBarReceiver extends BroadcastReceiver {

        int status;

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(PlayerInfo.STATUS, -1);
            int opt = intent.getIntExtra(PlayerInfo.OPERATE, -1);
            this.status = status == -1 ? this.status : status;

            switch (status) {
                case -1:
                    break;
                case STATUS_PAUSE: //接受到暂停
                    play_pause.setImageResource(R.drawable.play_red);
                    break;
                case STATUS_PLAYING:  //接收到播放
                    play_pause.setImageResource(R.drawable.pause_red);
                    break;
            }

            switch (opt) {
                case -1:
                    break;
                case PlayerInfo.OP_UPDATE_UI: //更新ui
                    final Song song = (Song) JSONUtil.parseJSONToObject(intent.getStringExtra(SONG), Song.class);
                    Log.i("song", song + "");
                    if (song != null) {
                        songname.setText(song.getName());
                        singer.setText(song.getSinger());
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageResource(R.drawable.music_logo_red);
                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("imgurl", song.getImgurl());
                                final Bitmap bitmap = getURLimage(song.getImgurl());
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        img.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }).start();
                    }
                    break;
            }
        }
    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            Log.i("imgurl", url);
            Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).ignoreContentType(true).execute();
            bmp = BitmapFactory.decodeStream(response.bodyStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

}
