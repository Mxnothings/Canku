package team.sao.musictool.dao.autodatabase.sql;

import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 20:59
 * \* Description:
 **/
public class ConditionBuilder {

    public static void main(String[] args) {
        System.out.println(new ConditionBuilder().key("id").bet("15 and 16").build());
    }

    private List<Condition> conditions;
    private Condition condition = new Condition();

    public ConditionBuilder() {}

    public ConditionBuilder key(String key) {
        condition.key = key;
        return this;
    }

    public ConditionBuilder like(String value) {
        condition.conditionWord = "like";
        condition.value = value;
        return this;
    }

    //大于
    public ConditionBuilder gt() {
        condition.conditionWord += ">";
        return this;
    }

    //大于
    public ConditionBuilder gt(String value) {
        condition.conditionWord += ">";
        condition.value = value;
        return this;
    }

    //小于
    public ConditionBuilder lt() {
        condition.conditionWord += "<";
        return this;
    }

    //小于
    public ConditionBuilder lt(String value) {
        condition.conditionWord += "<";
        condition.value = value;
        return this;
    }

    //等于
    public ConditionBuilder eq(String value) {
        condition.conditionWord += "=";
        condition.value = value;
        return this;
    }

    //等于
    public ConditionBuilder bet(String value) {
        condition.conditionWord += "between";
        condition.value = value;
        return this;
    }

    public Condition build() {
        return condition;
    }




}
