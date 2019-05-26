package team.sao.musictool.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;

import static team.sao.musictool.annotation.AnnotationProcesser.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 0:47
 * \* Description:
 **/
public class MineFragment extends Fragment {

    @ViewID(R.id.local_music)
    private LinearLayout local_music;
    @ViewID(R.id.recent_music)
    private LinearLayout recent_music;
    @ViewID(R.id.favor_music)
    private LinearLayout favor_music;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        inject(this, view);
        return view;
    }
}
