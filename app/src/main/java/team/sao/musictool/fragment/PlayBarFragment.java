package team.sao.musictool.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import team.sao.musictool.R;
import team.sao.musictool.activity.MusicPlayActivity;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.PlayerInfo;
import team.sao.musictool.config.ReceiverAction;
import team.sao.musictool.entity.Song;
import team.sao.musictool.receiver.MusicPlayReceiver;
import team.sao.musictool.util.IntentBuilder;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;
import static team.sao.musictool.config.PlayerInfo.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 13:58
 * \* Description:
 **/
public class PlayBarFragment extends Fragment implements View.OnTouchListener {

    public static final String ACTION = ReceiverAction.MUSICPLAY_UI;

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
    private PlayerInfo playerInfo = PlayerInfo.getInstance();


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
        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_SEND_PLAYBAR_UPDATE_UI).send(getContext());
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
        play_pause.setOnTouchListener(this);
        nextsong.setOnTouchListener(this);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        boolean flag = false;
        if (action == MotionEvent.ACTION_DOWN) {
            v.setAlpha(0.5f);
            flag = true;
        }  else if (action == MotionEvent.ACTION_UP) {
            v.setAlpha(1.0f);
            switch (v.getId()) {
                case  R.id.playbar_nextsong:
                    if (!playerInfo.isStatus(STATUS_NOTINIT)) {
                        new IntentBuilder().action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_NEXT_SONG).send(mActivity);
                    }
                    break;
                case R.id.playbar_play_pause:
                    if (!playerInfo.isStatus(STATUS_NOTINIT)) {
                        IntentBuilder ib = new IntentBuilder();
                        switch (playerInfo.getStatus()) {
                            case STATUS_PAUSE:
                                ib.action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_RESUME).send(mActivity);
                                break;
                            case STATUS_PLAYING:
                                ib.action(ReceiverAction.MUSICPLAY_CENTER).extra(OPERATE, OP_PAUSE).send(mActivity);
                                break;
                        }
                    }
                    break;
            }
            flag = true;
        }
        return flag;
    }


    public class PlayBarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int opt = intent.getIntExtra(OPERATE, -1);
            boolean isUpdateStatus = intent.getBooleanExtra(UPDATE_STATUS, false);
            if (isUpdateStatus) {
                switch (playerInfo.getStatus()) {
                    case STATUS_LOADING:
                    case STATUS_PAUSE: //暂停
                        play_pause.setImageResource(R.drawable.play_red);
                        break;
                    case STATUS_PLAYING:  //播放
                        play_pause.setImageResource(R.drawable.pause_red);
                        break;

                }
            }
            if (opt != -1) {
                switch (opt) {
                    case OP_UPDATE_UI:
                        Song song = playerInfo.getPlayingSong();
                        if (song != null) {
                            songname.setText(song.getName());
                            singer.setText(song.getSinger());
                            if (playerInfo.getSongImg() != null) {
                                img.setImageBitmap(playerInfo.getSongImg());
                            }
                        }
                        break;
                    case OP_UPDATE_UI_IMG: //更新img的ui
                        if (playerInfo.getSongImg() != null) {
                            img.setImageBitmap(playerInfo.getSongImg());
                        }
                        break;
                    case OP_UPDATE_UI_NOIMG : //更新除img的ui
                        final Song song1 = playerInfo.getPlayingSong();
                        if (song1 != null) {
                            songname.setText(song1.getName());
                            singer.setText(song1.getSinger());
                            img.setImageResource(R.drawable.music_logo_red);
                        }
                        break;
                }
            }
        }
    }

}
