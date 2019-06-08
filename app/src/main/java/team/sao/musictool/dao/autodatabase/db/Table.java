package team.sao.musictool.dao.autodatabase.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 15:03
 * \* Description:
 **/
public class Table {

    private String tableCreateSQL;
    private String insertSQL;
    private String baseSelectSQL;
    private String baseDeleteSQL;

    private String tablename;
    private List<String> primaryKeys;
    private Class entityClass;
    private Map<String, Column> columns;

    public Table(String tablename, Class entityClass, Map<String, Column> columns) {
        this.tablename = tablename;
        this.columns = columns;
        this.entityClass = entityClass;
        setPrimaryKey();
        this.tableCreateSQL = tableCreateSQL(this);
        Log.i("Table " + tablename + " tableCreateSQL:", tableCreateSQL);
        this.insertSQL = insertSQL(this);
        Log.i("Table " + tablename + " insertSQL:", insertSQL);
        this.baseSelectSQL = baseSelectSQL(this);
        Log.i("Table " + tablename + " baseSelectSQL:", baseSelectSQL);
        this.baseDeleteSQL = baseDeleteSQL(this);
        Log.i("Table " + tablename + " baseDeleteSQL:", baseDeleteSQL);
    }


    public String getTableCreateSQL() {
        return tableCreateSQL;
    }

    public static String tableCreateSQL(Table table) {
        String tablename = table.getTablename();
        StringBuffer sql = new StringBuffer();
        sql.append("create table " + tablename + "(");
        String[] primaryKeys = table.getPrimaryKeys();
        if (primaryKeys == null) {
            for (Map.Entry<String, Column> e : table.getColumns().entrySet()) {
                Column column = e.getValue();
                String cname = column.getName();
                String type = column.getDataType().typeName();
                String autoincrement = column.isAutoincrement() ? "autoincrement" : "";
                sql.append(cname + " " + type + " " + autoincrement + ",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
        } else if (primaryKeys.length == 1) {
            for (Map.Entry<String, Column> e : table.getColumns().entrySet()) {
                Column column = e.getValue();
                String cname = column.getName();
                String type = column.getDataType().typeName();
                String primaryKey = column.isPrimaryKey() ? "primary key" : "";
                String autoincrement = column.isAutoincrement() ? "autoincrement" : "";
                sql.append(cname + " " + type + " " + primaryKey + " " + autoincrement + ",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
        } else if (primaryKeys.length > 1) {
            for (Map.Entry<String, Column> e : table.getColumns().entrySet()) {
                Column column = e.getValue();
                String cname = column.getName();
                String type = column.getDataType().typeName();
                String autoincrement = column.isAutoincrement() ? "autoincrement" : "";
                sql.append(cname + " " + type + " " + autoincrement + ",");
            }
            sql.append("primary key(");
            for (String primaryKey : primaryKeys) {
                sql.append(primaryKey + ",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
            sql.append(")");
        }
        return sql.toString();
    }

    public static String baseSelectSQL(Table table) {
        return "select * from " + table.getTablename() + " ";
    }

    public static String baseDeleteSQL(Table table) {
        return "delete from " + table.getTablename() + " ";
    }

    public static String insertSQL(Table table) {
        Map<String, Column> columns = table.getColumns();
        StringBuffer sql = new StringBuffer();
        sql.append("insert into " + table.getTablename() + "(");
        for (String s : columns.keySet()) {
            sql.append(s + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") values(");
        for (Column column : columns.values()) {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }


    //设置主键
    private void setPrimaryKey() {
        List<String> primaryKeysTemp = new ArrayList<>();
        for (Column column : columns.values()) {
            if (column.isPrimaryKey()) {
                primaryKeysTemp.add(column.getName());
            }
        }
        primaryKeys = primaryKeysTemp.isEmpty() ? null : primaryKeysTemp;
    }

    public Column addColumn(Column column) {
        return columns.put(column.getName(), column);
    }

    public Column getColumn(String name) {
        return columns.get(name);
    }

    public String getTablename() {
        return tablename;
    }

    public Map<String, Column> getColumns() {
        return columns;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public String[] getPrimaryKeys() {
        if (primaryKeys == null) return null;
        String[] pKeys = new String[primaryKeys.size()];
        for (int i = 0; i < primaryKeys.size(); i++){
            pKeys[i] = primaryKeys.get(i);
            Log.i("******", "getPrimaryKeys: " + pKeys[i]);
        }
        return pKeys;
    }

    public String getInsertSQL() {
        return insertSQL;
    }

    public String getBaseSelectSQL() {
        return baseSelectSQL;
    }
}
