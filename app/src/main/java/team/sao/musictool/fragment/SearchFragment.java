package team.sao.musictool.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import team.sao.musictool.R;
import team.sao.musictool.activity.SearchActivity;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.config.MusicType;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 0:57
 * \* Description:
 **/
public class SearchFragment extends Fragment {

    private Activity activity;

    @ViewID(R.id.netease)
    private ImageView netease;
    @ViewID(R.id.qqmusic)
    private ImageView qqmusic;

    public SearchFragment() {}

    @SuppressLint("ValidFragment")
    public SearchFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        inject(this, view);
        qqmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchActivity.class);
                intent.putExtra("musicType", MusicType.QQ_MUSIC);
                startActivity(intent);
            }
        });
        netease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchActivity.class);
                intent.putExtra("musicType", MusicType.NETEASE_MUSIC);
                startActivity(intent);
            }
        });
        return view;
    }
}
