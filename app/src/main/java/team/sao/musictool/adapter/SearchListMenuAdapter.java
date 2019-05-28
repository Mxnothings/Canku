package team.sao.musictool.adapter;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/29
 * \* Time: 2:44
 * \* Description:
 **/
public class SearchListMenuAdapter extends BaseListMenuAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        return view;
    }
}
