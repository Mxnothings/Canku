package team.sao.musictool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import team.sao.musictool.R;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.entity.ListMenuItem;

import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/28
 * \* Time: 21:15
 * \* Description:
 **/
public class BaseListMenuAdapter extends BaseAdapter {

    public static final int LIST_MENU_VIEW_ITEM_RESOURCE = R.layout.list_menu_item;

    @ViewID(R.id.list_menu_item_icon_main)
    protected ImageView icon_main;
    @ViewID(R.id.list_menu_item_name)
    protected TextView name;
    @ViewID(R.id.list_menu_item_tip)
    protected TextView tip;
    @ViewID(R.id.list_menu_item_icon_enter)
    protected ImageView icon_enter;

    protected LayoutInflater inflater;
    protected List<ListMenuItem> menuItems;
    protected Context context;

    public BaseListMenuAdapter() {
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(LIST_MENU_VIEW_ITEM_RESOURCE, null);
        inject(this, BaseListMenuAdapter.class, view);

        ListMenuItem item = menuItems.get(position);
        icon_main.setImageResource(item.getIcon_main_resource());
        name.setText(item.getName());
        tip.setText(item.getTip());
        icon_enter.setImageResource(item.getIcon_enter_resource());
        return view;
    }

    public BaseListMenuAdapter setContext(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        return this;
    }

    public BaseListMenuAdapter setMenuItems(List<ListMenuItem> menuItems) {
        this.menuItems = menuItems;
        return this;
    }
}
