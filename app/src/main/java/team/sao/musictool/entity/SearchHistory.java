package team.sao.musictool.entity;

import team.sao.musictool.dao.autodatabase.annotation.DBColumn;
import team.sao.musictool.dao.autodatabase.annotation.Entity;
import team.sao.musictool.dao.autodatabase.db.DataType;

/**
 * \* Author: MrWangx
 * \* Date: 2019/6/3
 * \* Time: 15:47
 * \* Description:
 **/
@Entity("search_history")
public class SearchHistory {

    @DBColumn(type = DataType.INTEGER, primaryKey = true, autoincrement = true)
    private Integer id;
    private String keyword;

    @Override
    public String toString() {
        return "SearchHistory{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                '}';
    }

    public SearchHistory() {
    }

    public SearchHistory(Integer id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
