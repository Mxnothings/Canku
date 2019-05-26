package team.sao.musictool.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import team.sao.musictool.R;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 0:56
 * \* Description:
 **/
public class MusicFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }
}
