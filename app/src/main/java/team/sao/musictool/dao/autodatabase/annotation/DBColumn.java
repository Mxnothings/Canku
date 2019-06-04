package team.sao.musictool.dao.autodatabase.annotation;

import team.sao.musictool.dao.autodatabase.db.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 1:57
 * \* Description:
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBColumn {

    String DEFALT_NAME = "";
    boolean DEFALT_ISMAP = true;
    boolean DEFAULT_PRIMARYKEY = false;
    boolean DEFAULT_AUTOINCREMENT = false;
    DataType DEFAULT_TYPE = DataType.TEXT;


    String name() default DEFALT_NAME;

    boolean isMap() default DEFALT_ISMAP;

    boolean primaryKey() default DEFAULT_PRIMARYKEY;

    boolean autoincrement() default DEFAULT_AUTOINCREMENT;

    DataType type() default DataType.TEXT;

}
