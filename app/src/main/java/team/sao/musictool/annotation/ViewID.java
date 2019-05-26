package team.sao.musictool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/23
 * \* Time: 18:04
 * \* Description:
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewID {

    int value();

}
