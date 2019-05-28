package team.sao.musictool.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.entity.ListMenuItem;

import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 0:47
 * \* Description:
 **/
public class MineFragment extends Fragment {

//    @ViewID(R.id.local_music)
//    private LinearLayout local_music;
//    @ViewID(R.id.recent_music)
//    private LinearLayout recent_music;
//    @ViewID(R.id.favor_music)
//    private LinearLayout favor_music;

    @ViewID(R.id.list_menu)
    private ListView list_menu;

    private List<ListMenuItem> menuItems;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuItems = new ArrayList<>();
        menuItems.add(new ListMenuItem(R.drawable.local_music, "本地音乐", "0", R.drawable.enter_black));
        menuItems.add(new ListMenuItem(R.drawable.recent_music, "最近音乐", "0", R.drawable.enter_black));
        menuItems.add(new ListMenuItem(R.drawable.favor_music, "收藏音乐", "0", R.drawable.enter_black));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_menu, container, false);
        inject(this, view);

//        list_menu.setAdapter(new ListMenuAdapter(menuItems, getContext()));

        return view;
    }
}
