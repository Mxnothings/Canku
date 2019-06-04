package team.sao.musictool.dao.autodatabase.db;

import java.lang.reflect.Field;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 14:06
 * \* Description:
 **/
public class Column {

    private Field field;
    private String name;
    private boolean primaryKey;
    private boolean autoincrement;
    private DataType dataType;

    public Column() {
    }

    public Column(Field field, String name, boolean primaryKey, boolean autoincrement, DataType type) {
        this.field = field;
        this.name = name;
        this.primaryKey = primaryKey;
        this.autoincrement = autoincrement;
        this.dataType = type;
    }

    public Column(String name, boolean primaryKey, boolean autoincrement, DataType type) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.autoincrement = autoincrement;
        this.dataType = type;
    }

    @Override
    public String toString() {
        return "Column{" +
                "field=" + field.getType().getSimpleName() +
                ", name='" + name + '\'' +
                ", primaryKey=" + primaryKey +
                ", autoincrement=" + autoincrement +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
