package com.pointhouse.chiguan.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User表示例
 * Created by Maclaine on 2017/6/22.
 */
@DatabaseTable
public class User {
    @DatabaseField(generatedId=true)
    private Integer id;
    @DatabaseField(unique = true)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
