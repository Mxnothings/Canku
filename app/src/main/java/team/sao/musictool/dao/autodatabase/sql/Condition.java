package team.sao.musictool.dao.autodatabase.sql;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 21:06
 * \* Description:
 **/
public class Condition {

    public String key = "";
    public String conditionWord = "";
    public String value = "";

    public void reset() {
        key = "";
        conditionWord = "";
        value = "";
    }

    @Override
    public String toString() {
        return key + " " + conditionWord + " " + value;
    }
}
