package team.sao.musictool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import team.sao.musictool.R;

import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/6
 * \* Time: 13:33
 * \* Description:
 **/
public class FragmentMusicItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<String> data;

    public FragmentMusicItemAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return inflater.inflate(R.layout.fragment_music_item, null);
    }
}
