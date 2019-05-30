package team.sao.musictool.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;

import java.util.Arrays;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/30
 * \* Time: 21:38
 * \* Description:
 **/
public class JSONUtil {
    /**
     * 忽略字段输出json
     * @param object
     * @param unSerializeField 忽略的属性
     * @return
     */
    public static String toJSONString(Object object, String... unSerializeField) {
        final List<String> fieldsName = Arrays.asList(unSerializeField);
        return JSON.toJSONString(object, new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                return fieldsName.contains(name) ? false : true;
            }
        });
    }

    /**
     * 忽略字段输出json
     * @param json
     * @param clazz
     * @return
     */
    public static Object parseJSONToObject(String json, Class clazz) {
        return JSON.toJavaObject(JSON.parseObject(json), clazz);
    }
}
