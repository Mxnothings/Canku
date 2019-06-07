package team.sao.musictool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.music.entity.SongList;

import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/7
 * \* Time: 14:54
 * \* Description:
 **/
public class SongListAdapter extends BaseAdapter {

    @ViewID(R.id.iv_songlist_img)
    private ImageView img;
    @ViewID(R.id.tv_songlist_name)
    private TextView name;
    @ViewID(R.id.tv_song_count)
    private TextView songCount;
    @ViewID(R.id.tv_play_count)
    private TextView playCount;


    private LayoutInflater inflater;
    private List<SongList> songLists;
    private Context context;

    public SongListAdapter(Context context, List<SongList> songLists) {
        this.songLists = songLists;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songLists.size();
    }

    @Override
    public Object getItem(int position) {
        return songLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.songlist_listview_item, null);
        SongList songList = songLists.get(position);
        inject(this, SongListAdapter.class, view);
        Glide.with(context).load(songList.getImgUrl()).into(img);
        name.setText(songList.getName());
        songCount.setText(songList.getSongCount() + "首");
        playCount.setText("播放" + songList.getPlayCount() + "次");
        return view;
    }
}
