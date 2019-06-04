package team.sao.musictool.dao.autodatabase.db;

import android.database.Cursor;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 19:13
 * \* Description:
 **/
public enum  DataType {

    NULL(Cursor.FIELD_TYPE_NULL, "null"),
    INTEGER(Cursor.FIELD_TYPE_INTEGER, "integer"),
    REAL(Cursor.FIELD_TYPE_FLOAT, "real"),
    TEXT(Cursor.FIELD_TYPE_STRING, "text")
    ;

    private int type;
    private String typeName;

    private DataType(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public int type() {
        return type;
    }

    public String typeName() {
        return typeName;
    }
}
