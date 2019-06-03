package team.sao.musictool.annotation;

import android.app.Activity;
import android.view.View;
import team.sao.musictool.adapter.BaseListMenuAdapter;
import team.sao.musictool.adapter.SearchListMenuAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/23
 * \* Time: 18:07
 * \* Description:
 **/
public class AnnotationProcesser {

    public static void inject(Object injectTo, Class superEnd, Object source) {
        if (source instanceof View) {
            List<Field> fields = getAllDeclaredFields(injectTo.getClass(), superEnd);
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
            List<Field> fields = getAllDeclaredFields(injectTo.getClass(), superEnd);
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

    /**
     * 获取到superEnd为父类的所有域
     * @param clazz
     * @param superEnd
     * @return
     */
    public static List<Field> getAllDeclaredFields(Class clazz, Class superEnd) {
        ArrayList<Field> fields = new ArrayList<>();
        while (true) {
            if (clazz == null) {
                break;
            } else {
                if (clazz == superEnd) {
                    fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
                    break;
                } else {
                    fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
                    clazz = clazz.getSuperclass();
                }
            }

        }
        return fields;
    }
}

