package team.sao.musictool.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import team.sao.musictool.R;
import team.sao.musictool.adapter.BaseListMenuAdapter;
import team.sao.musictool.annotation.ViewID;
import team.sao.musictool.entity.ListMenuItem;

import java.util.ArrayList;
import java.util.List;

import static team.sao.musictool.annotation.AnnotationProcesser.inject;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/28
 * \* Time: 23:24
 * \* Description:
 **/
public class ListMenu extends Fragment {

    @ViewID(R.id.list_menu)
    private ListView list_menu;

    private List<ListMenuItem> menuItems;
    private BaseListMenuAdapter listMenuAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;

    public ListMenu() {
        this.menuItems = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_menu, container, false);
        inject(this, ListMenu.class, view);
        list_menu.setAdapter(listMenuAdapter == null ? initAdapter(listMenuAdapter = new BaseListMenuAdapter()) : initAdapter(listMenuAdapter));
        list_menu.setOnItemClickListener(onItemClickListener);
        return view;
    }

    public boolean addListMenuItem(ListMenuItem menuItem) {
        return menuItems.add(menuItem);
    }

    public void setOnMenuItemClicked(AdapterView.OnItemClickListener onItemClickListener) {
        if (list_menu != null)
            list_menu.setOnItemClickListener(onItemClickListener);
        else
            this.onItemClickListener = onItemClickListener;
    }

    public void setListMenuAdapter(BaseListMenuAdapter listMenuAdapter) {
        if (list_menu != null)
            list_menu.setOnItemClickListener(onItemClickListener);
        else
            this.listMenuAdapter = listMenuAdapter;
    }

    private BaseListMenuAdapter initAdapter(BaseListMenuAdapter adapter) {
        return adapter.setMenuItems(menuItems).setContext(getContext());
    }

    public List<ListMenuItem> getMenuItems() {
        return menuItems;
    }

    public BaseListMenuAdapter getListMenuAdapter() {
        return listMenuAdapter;
    }
}
