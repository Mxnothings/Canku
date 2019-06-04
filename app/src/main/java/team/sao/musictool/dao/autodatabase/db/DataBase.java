package team.sao.musictool.dao.autodatabase.db;

import team.sao.musictool.dao.autodatabase.annotation.DBColumn;
import team.sao.musictool.dao.autodatabase.annotation.Entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 16:35
 * \* Description:
 **/
public class DataBase {


    /**
     * 根据class获取tablename
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class clazz) {
        if (clazz == null) return null;
        Entity entity = (Entity) clazz.getAnnotation(Entity.class);
        String name;
        if (entity != null) {
            name = entity.value();
            name = name.equals("") ? clazz.getSimpleName().toLowerCase() : name;
        } else {
            name = clazz.getSimpleName().toLowerCase();
        }
        return name;
    }


    /**
     * 获取所有的table
     *
     * @param tablenames
     * @return
     */
    public static Map<String, Table> getTables(Map<String, Class> tablenames) {
        Map<String, Table> tables = new HashMap<>();
        for (Map.Entry<String, Class> tname : tablenames.entrySet()) {
            tables.put(tname.getKey(), new Table(tname.getKey(), tname.getValue(), getColumns(tname.getValue(), Object.class)));
        }
        return tables;
    }

    /**
     * 根据class获取所有的列属性
     *
     * @param clazz
     * @param superEnd
     * @return
     */
    public static Map<String, Column> getColumns(Class clazz, Class superEnd) {
        Map<String, Column> columns = new LinkedHashMap<>();
        while (true) {
            if (clazz == null) {
                break;
            } else {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!(("$change".equals(field.getName())) || ("serialVersionUID".equals(field.getName())))) {
                        DBColumn dbColumn = field.getAnnotation(DBColumn.class);
                        Column column = new Column();
                        if (dbColumn == null) {
                            field.setAccessible(true);
                            String name = field.getName();
                            column.setField(field);
                            column.setName(name);
                            column.setDataType(DBColumn.DEFAULT_TYPE);
                            column.setPrimaryKey(DBColumn.DEFAULT_PRIMARYKEY);
                            column.setAutoincrement(DBColumn.DEFAULT_AUTOINCREMENT);
                            columns.put(name, column);
                        } else if (dbColumn.isMap()) {
                            field.setAccessible(true);
                            String name = dbColumn.name().equals(DBColumn.DEFALT_NAME) ? field.getName() : dbColumn.name();
                            column.setField(field);
                            column.setName(name);
                            column.setDataType(dbColumn.type());
                            column.setPrimaryKey(dbColumn.primaryKey());
                            column.setAutoincrement(dbColumn.autoincrement());
                            columns.put(name, column);
                        }
                    }
                }
                if (clazz == superEnd) {
                    break;
                } else {
                    clazz = clazz.getSuperclass();
                }
            }


        }
        return columns;
    }

    /**
     * 获取tablename和class的映射
     *
     * @param clazz
     * @return
     */
    public static Map<String, Class> getTableNameClassMap(Class... clazz) {
        Map<String, Class> tablenams = new HashMap<>();
        for (Class c : clazz) {
            String name = getTableName(c);
            tablenams.put(name, c);
        }
        return tablenams;
    }

}
