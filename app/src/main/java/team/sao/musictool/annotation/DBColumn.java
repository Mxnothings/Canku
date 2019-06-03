package team.sao.musictool.annotation;

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

    String name() default "";
    boolean isMap() default true;
    boolean primaryKey() default false;
    boolean autoincrement() default false;
    String type() default "";

}
