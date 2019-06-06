package team.sao.musictool.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import team.sao.musictool.R;
import team.sao.musictool.adapter.FragmentMusicItemAdapter;
import team.sao.musictool.annotation.ViewID;

import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 0:56
 * \* Description:
 **/
public class MusicFragment extends Fragment {

    @ViewID(R.id.items)
    private ListView items;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_music, container, false);
        inject(this, MusicFragment.class, view);
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        items.setAdapter(new FragmentMusicItemAdapter(getContext(), data));
        return view;
    }
}
