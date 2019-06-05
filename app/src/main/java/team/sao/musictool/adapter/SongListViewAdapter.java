package team.sao.musictool.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.entity.Song;

import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;
import static team.sao.musictool.config.MusicType.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/27
 * \* Time: 17:44
 * \* Description:
 **/
public class SongListViewAdapter extends BaseAdapter {

    @ViewID(R.id.tv_song_name)
    private TextView songName;
    @ViewID(R.id.tv_singer_album)
    private TextView singerAlbum;
    @ViewID(R.id.tv_time)
    private TextView time;
    @ViewID(R.id.songslist_item_ic_more)
    private ImageView more;
    @ViewID(R.id.songslist_item_ic_music_type)
    private ImageView musicType;

    private LayoutInflater inflater;
    private Context context;
    private List<Song> songs;
    private boolean reversal;

    public SongListViewAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
        this.inflater = LayoutInflater.from(context);
        this.reversal = false;
    }

    public SongListViewAdapter(Context context, List<Song> songs, boolean reversal) {
        this.context = context;
        this.songs = songs;
        this.inflater = LayoutInflater.from(context);
        this.reversal = reversal;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song = reversal ? songs.get(songs.size() - 1 - position) : songs.get(position);
        convertView = inflater.inflate(R.layout.songlist_item, null);
        inject(this, SongListViewAdapter.class, convertView);
        switch (song.getMusicType()) {
            case QQ_MUSIC:
                musicType.setImageResource(R.drawable.qqmusic_logo);
                break;
            case NETEASE_MUSIC:
                musicType.setImageResource(R.drawable.netease_cloud_music_logo);
                break;
        }
        songName.setText(song.getName());
        singerAlbum.setText(song.getSinger() + "-" + song.getAlbumname());
        time.setText(song.getFormatTime());

        return convertView;
    }

    public List<Song> getSongs() {
        return songs;
    }

    private void showPopupWindow() {
        View root = LayoutInflater.from(context).inflate(R.layout.songslist_popupwindow, null);
        PopupWindow popupWindow = new PopupWindow(root,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(root);
        //显示PopupWindow
        View rootview = LayoutInflater.from(context).inflate(R.layout.activity_search, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

}
