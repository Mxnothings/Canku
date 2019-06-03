package team.sao.musictool.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import team.sao.musictool.annotation.DBColumn;

import java.lang.reflect.Field;
import java.util.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 1:41
 * \* Description:
 **/
public class AutoDataBase<T> extends SQLiteOpenHelper {

    private Class entityClass;
    private String dbname;
    private Map<String, Field> columns;

    public AutoDataBase(Context context, String dbname, int version, Class entityClass) {
        super(context, dbname, null, version);
        this.entityClass = entityClass;
        this.dbname = dbname;
        columns = getAllColumns(entityClass, Object.class);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(dataBaseSQL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(T obj) {
        if (!entityClass.isAssignableFrom(obj.getClass())) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Map.Entry<String, Field> e : columns.entrySet()) {
            String name = e.getKey();
            Field field = e.getValue();
            // TODO: 2019/6/3  添加值
        }
       return 0;
    }

    private String dataBaseSQL() {
        StringBuffer sql = new StringBuffer();
        sql.append("create table " + "test" + "(");
        for (Map.Entry<String, Field> e : columns.entrySet()) {
            Field field = e.getValue();
            DBColumn dbColumn = field.getAnnotation(DBColumn.class);
            if (dbColumn == null) {
                sql.append(e.getKey() + " varchar,");
            } else {
                String cname = dbColumn.name().equals("") ? e.getKey() : dbColumn.name();
                String type = dbColumn.type().equals("") ? "varchar" : dbColumn.type();
                String primarykey = dbColumn.primaryKey() ? "primary key" : "";
                String autoincrement = dbColumn.autoincrement() ? "autoincrement" : "";
                sql.append(cname+ " " + type + " " + primarykey + " " + autoincrement + ",");
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    private Map<String, Field> getAllColumns(Class clazz, Class superEnd) {
        Map<String, Field> columns = new LinkedHashMap<>();
        while (true) {
            if (clazz == null) {
                break;
            } else {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    DBColumn dbColumn = field.getAnnotation(DBColumn.class);
                    if (dbColumn == null) {
                        columns.put(field.getName(), field);
                    } else if(dbColumn.isMap()) {
                        columns.put(dbColumn.name().equals("") ? field.getName() : dbColumn.name(), field);
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



}
