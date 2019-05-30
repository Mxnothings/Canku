package team.sao.musictool.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;

import static team.sao.musictool.annotation.AnnotationProcesser.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 13:58
 * \* Description:
 **/
public class PlayBarFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_bar, null);
        inject(this, PlayBarFragment.class, this);
        return view;
    }
}
