package team.sao.musictool;

import team.sao.musictool.dao.autodatabase.db.Column;
import team.sao.musictool.dao.autodatabase.db.DataBase;
import team.sao.musictool.dao.autodatabase.db.Table;
import team.sao.musictool.entity.RecentSong;
import team.sao.musictool.entity.SearchHistory;
import team.sao.musictool.entity.Song;

import java.util.Map;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 14:17
 * \* Description:
 **/
public class Test {

    public static void main(String[] args) {
//        Map<String, Table> tables = DataBase.getTables(DataBase.getTableNameClassMap(Song.class, SearchHistory.class));
////        for (Map.Entry<String, Table> e : tables.entrySet()) {
////            System.out.println("tablename:" + e.getKey());
////            System.out.println("tablecolumns:");
////            for (Map.Entry<String, Column> e1 : e.getValue().getColumns().entrySet()) {
////                System.out.println(e1);
////            }
////            System.out.println("---------------------------");
////        }
////        for (Table table : tables.values()) {
////            System.out.println(table.getTableCreateSQL());
////        }
////        insert(tables.get("searchhistory"), new SearchHistory(1, "你好"));
        Song song = new Song();
        System.out.println(((RecentSong)song).getClass());
    }

    public static void insert(Table table, Object obj) {
        if (obj != null) {
            if (table != null) {
                System.out.print(Table.insertSQL(table));
                for (Column column : table.getColumns().values()) {
                    try {
                        System.out.print(column.getField().get(obj) + ",");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
