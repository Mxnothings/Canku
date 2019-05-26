package team.sao.musictool.annotation;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/23
 * \* Time: 18:07
 * \* Description:
 **/
public class AnnotationProcesser {

    public static void inject(Object injectTo, Object source) {
        if (source instanceof View) {
            Field[] fields = injectTo.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(ViewID.class)) {
                    ViewID viewId = f.getAnnotation(ViewID.class);
                    f.setAccessible(true);
                    try {
                        f.set(injectTo, ((View) source).findViewById(viewId.value()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (source instanceof Activity) {
            Field[] fields = injectTo.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(ViewID.class)) {
                    ViewID viewId = f.getAnnotation(ViewID.class);
                    f.setAccessible(true);
                    try {
                        f.set(injectTo, ((Activity) source).findViewById(viewId.value()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
