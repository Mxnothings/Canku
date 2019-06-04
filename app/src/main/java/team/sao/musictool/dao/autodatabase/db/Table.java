package team.sao.musictool.dao.autodatabase.db;

import java.util.Map;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 15:03
 * \* Description:
 **/
public class Table  {

    private String tableCreateSQL;
    private String insertSQL;
    private String baseSelectSQL;
    private String baseDeleteSQL;

    private String tablename;
    private String primaryKey;
    private Class entityClass;
    private Map<String, Column> columns;

    public Table(String tablename, Class entityClass, Map<String, Column> columns) {
        this.tablename = tablename;
        this.columns = columns;
        this.entityClass = entityClass;
        this.tableCreateSQL = tableCreateSQL(this);
        this.insertSQL = insertSQL(this);
        this.baseSelectSQL = baseSelectSQL(this);
        this.baseDeleteSQL = baseDeleteSQL(this);
        setPrimaryKey();
    }


    public String getTableCreateSQL() {
        return tableCreateSQL;
    }

    public static String tableCreateSQL(Table table) {
        String tablename = table.getTablename();
        StringBuffer sql = new StringBuffer();
        sql.append("create table " + tablename + "(");
        for (Map.Entry<String, Column> e : table.getColumns().entrySet()) {
            Column column = e.getValue();
            String cname = column.getName();
            String type = column.getDataType().typeName();
            String primarykey = column.isPrimaryKey() ? "primary key" : "";
            String autoincrement = column.isAutoincrement() ? "autoincrement" : "";
            sql.append(cname + " " + type + " " + primarykey + " " + autoincrement + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
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


    private void setPrimaryKey() {
        for (Column column : columns.values()) {
            if (column.isPrimaryKey()) {
                this.primaryKey = column.getName();
                break;
            }
        }
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

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getInsertSQL() {
        return insertSQL;
    }

    public String getBaseSelectSQL() {
        return baseSelectSQL;
    }
}
