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
import team.sao.musictool.util.JSONUtil;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;
import static team.sao.musictool.config.PlayerInfo.SONG;

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
    @ViewID(R.id.playbar_play)
    private ImageView play;
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

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MusicPlayActivity.class);
                startActivity(intent);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MusicPlayReceiver.ACTION);
                if (playBarReceiver.status == PlayerInfo.STATUS_PAUSE) {
                    intent.putExtra(PlayerInfo.OPERATE, PlayerInfo.OP_RESUME);
                } else if (playBarReceiver.status == PlayerInfo.STATUS_PLAYING) {
                    intent.putExtra(PlayerInfo.OPERATE, PlayerInfo.OP_PAUSE);
                }
                mActivity.sendBroadcast(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private void register() {
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
                case PlayerInfo.STATUS_PAUSE: //接受到暂停
                    play.setImageResource(R.drawable.play_red);
                    break;
                case PlayerInfo.STATUS_PLAYING:  //接收到播放
                    play.setImageResource(R.drawable.pause_red);
                    break;
            }

            switch (opt) {
                case -1:
                    break;
                case PlayerInfo.OP_UPDATE_UI:
                    final Song song = (Song) JSONUtil.parseJSONToObject(intent.getStringExtra(SONG), Song.class);
                    songname.setText(song.getName());
                    singer.setText(song.getSinger());
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
