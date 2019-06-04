package team.sao.musictool.dao.autodatabase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 14:14
 * \* Description:
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

    String value() default "";

}
