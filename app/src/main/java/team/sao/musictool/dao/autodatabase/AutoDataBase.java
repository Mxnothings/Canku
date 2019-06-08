package team.sao.musictool.dao.autodatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import team.sao.musictool.dao.autodatabase.db.Column;
import team.sao.musictool.dao.autodatabase.db.DataBase;
import team.sao.musictool.dao.autodatabase.db.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 1:41
 * \* Description:
 **/
public class AutoDataBase extends SQLiteOpenHelper {

    private String dbname;
    private Map<String, Table> tables;
    private Context context;

    public AutoDataBase(Context context, String dbname, int version, Class... entityClass) {
        super(context, dbname, null, version);
        this.dbname = dbname;
        this.context = context;
        this.tables = DataBase.getTables(DataBase.getTableNameClassMap(entityClass));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Table table : tables.values()) {
            try {
                db.execSQL(table.getTableCreateSQL());
                Log.i("创建Table:" + table.getTablename(), "成功");
            } catch (Exception e) {
                Log.i("创建Table:" + table.getTablename(), "失败");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(Object obj) {
        if (obj != null) {
            Table table = getTable(obj.getClass());
            if (table != null) {
                SQLiteDatabase db = this.getWritableDatabase();
                List<Object> bindArgs = new ArrayList<>();
                for (Column column : table.getColumns().values()) {
                    try {
                        bindArgs.add(column.getField().get(obj));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    db.execSQL(table.getInsertSQL(), bindArgs.toArray());
                    Log.i("插入searchhistory", obj.toString());
                } catch (Exception e) {
                    Log.i("插入searchhistory", "失败");
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    public List select(String tablename, String condition, String[] args) {
        Table table = getTable(tablename);
        List list = new ArrayList();
        if (table != null) {
            SQLiteDatabase db = this.getReadableDatabase();
            try {
                Cursor cursor = db.rawQuery(table.getBaseSelectSQL() + condition, args);
                while (cursor.moveToNext()) {
                    try {
                        Object obj = table.getEntityClass().newInstance();
                        for (Column column : table.getColumns().values()) {
                            column.getField().set(obj, getColumnValue(cursor, column.getName()));
                        }
                        list.add(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("查询" + tablename, "失败");
            }
        }
        return list;
    }

    public List select(Class entityClass, String condition, String[] arg) {
        Table table = getTable(entityClass);
        return select(table.getTablename(), condition, arg);
    }


    public List selectTableAll(String tablename) {
        return select(tablename, "", null);
    }

    public List selectTableAll(Class entityClass) {
        Table table = getTable(entityClass);
        return selectTableAll(table.getTablename());
    }

    public Object selectTableByPrimaryKey(String tablename, String... val) {
        Table table = getTable(tablename);
        if (table != null) {
            StringBuffer sql = new StringBuffer("where");
            String[] primaryKeys = table.getPrimaryKeys();
            if (primaryKeys != null && val != null && primaryKeys.length == val.length) {
                int i;
                for (i = 0; i < primaryKeys.length - 1; i++) {
                    sql.append(" " + primaryKeys[i] + "=" + val[i] + " and");
                }
                sql.append(" " + primaryKeys[i] + "=" + val[i]);
            }
            Log.i("****", "selectTableByPrimaryKey: " + sql.toString());
            List list = select(table.getTablename(), sql.toString(), null);
            if (list != null && !list.isEmpty()) return list.get(0);
        }
        return null;
    }

    public Object selectTableByPrimaryKey(Class entityClass, String... val) {
        Table table = getTable(entityClass);
        if (table != null) {
            return selectTableByPrimaryKey(table.getTablename(), val);
        }
        return null;
    }

    public long selectTableCount(String tablename) {
        Table table = getTable(tablename);
        if (table != null) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery("select count(*) from " + table.getTablename(), null);
                cursor.moveToFirst();
                long count = cursor.getLong(0);
                Log.i("查找所有记录", "selectTableCount: " + count);
                cursor.close();
                return count;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return -1;
    }

    public long selectTableCount(Class entityClass) {
        Table table = getTable(entityClass);
        if (table != null) {
            return selectTableCount(table.getTablename());
        }
        return -1;
    }

    public int delete(String tableName, String whereClause, String[] whereArgs) {
        Table table = getTable(tableName);
        if (table != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            int result = -1;
            try {
                result = db.delete(table.getTablename(), whereClause, whereArgs);
            } catch (Exception e) {
                Log.e("删除" + tableName, "出错");
                e.printStackTrace();
            }
            return result;
        }
        return -1;
    }

    public int delete(Class entityClass, String whereClause, String[] whereArgs) {
        Table table = getTable(entityClass);
        if (table != null) return delete(getTable(entityClass).getTablename(), whereClause, whereArgs);
        return 0;
    }

    public int deleteAll(String tablename) {
        return delete(tablename, null, null);
    }

    public int deleteAll(Class entityClass) {
        return delete(entityClass, null, null);
    }

    public int deleteByPrimaryKey(String tablename, String... val) {
        Table table = getTable(tablename);
        if (table != null) {
            StringBuffer sql = new StringBuffer();
            String[] primaryKeys = table.getPrimaryKeys();
            if (primaryKeys != null && val != null && primaryKeys.length == val.length) {
                for (int i = 0; i < primaryKeys.length; i++) {
                    if (i == primaryKeys.length - 1) {
                        sql.append(" " + primaryKeys[i] + "=" + val[i]);
                    } else {
                        sql.append(" " + primaryKeys[i] + "=" + val[i] + " and");
                    }
                }
            }
            Log.i("AutoDataBase:", "deleteByPrimaryKey: " + sql.toString());
            return delete(table.getTablename(), sql.toString(), null);
        }
        return 0;
    }

    public int deleteByPrimaryKey(Class entityClass, String... val) {
        Table table = getTable(entityClass);
        if (table != null) {
            return deleteByPrimaryKey(table.getTablename(), val);
        }
        return 0;
    }

    /**
     * @param cursor
     * @param columnName
     * @return
     */
    private Object getColumnValue(Cursor cursor, String columnName) {
        Object val = null;
        int columnIndex = cursor.getColumnIndex(columnName);
        int type = cursor.getType(columnIndex);
        switch (type) {
            case Cursor.FIELD_TYPE_INTEGER:
                val = cursor.getInt(columnIndex);
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                val = cursor.getFloat(columnIndex);
                break;
            case Cursor.FIELD_TYPE_STRING:
                val = cursor.getString(columnIndex);
                break;
        }
        return val;
    }

    public Table getTable(String tablename) {
        return tables.get(tablename);
    }

    public Table getTable(Class entityClass) {
        return tables.get(DataBase.getTableName(entityClass));
    }

    public Column getColumn(String tablename, String colname) {
        Table table = getTable(tablename);
        return table == null ? null : table.getColumn(colname);
    }

    public Column getColumn(Class clazz, String colname) {
        Table table = getTable(clazz);
        return table == null ? null : table.getColumn(colname);
    }


    public String getDbname() {
        return dbname;
    }

    public Map<String, Table> getTables() {
        return tables;
    }
}
