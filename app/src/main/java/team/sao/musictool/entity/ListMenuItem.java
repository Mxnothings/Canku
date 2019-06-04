package team.sao.musictool.entity;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/28
 * \* Time: 21:14
 * \* Description:
 **/
public class ListMenuItem {

    public static final String EXTRA_NAME = "ListMenuItem_Name";

    private int icon_main_resource;
    private String name;
    private String tip;
    private int icon_enter_resource;

    public ListMenuItem() {}

    public ListMenuItem(int icon_main_resource, String name, String tip, int icon_enter_resource) {
        this.icon_main_resource = icon_main_resource;
        this.name = name;
        this.tip = tip;
        this.icon_enter_resource = icon_enter_resource;
    }

    public int getIcon_main_resource() {
        return icon_main_resource;
    }

    public void setIcon_main_resource(int icon_main_resource) {
        this.icon_main_resource = icon_main_resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getIcon_enter_resource() {
        return icon_enter_resource;
    }

    public void setIcon_enter_resource(int icon_enter_resource) {
        this.icon_enter_resource = icon_enter_resource;
    }
}
