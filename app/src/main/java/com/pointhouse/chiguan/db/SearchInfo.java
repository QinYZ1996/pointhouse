package com.pointhouse.chiguan.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ljj on 2017/7/22.
 */
@DatabaseTable
public class SearchInfo {
    @DatabaseField(generatedId=true)
    private Integer id;

    @DatabaseField()
    private Integer sort;

    @DatabaseField()
    private String searchText;

    @DatabaseField()
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getid() {
        return id;
    }

    public void setid(Integer id) {
        this.id = id;
    }

    public Integer getsort() {
        return sort;
    }

    public void setsort(Integer sort) {
        this.sort = sort;
    }

    public String getsearchText() {
        return searchText;
    }

    public void setsearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * DFF
     */
    @DatabaseField()
    private Integer integerDFF1;

    public Integer getIntegerDFF1() {
        return integerDFF1;
    }

    public void setIntegerDFF1(Integer integerDFF1) {
        this.integerDFF1 = integerDFF1;
    }

    @DatabaseField()
    private Integer integerDFF2;

    public Integer getIntegerDFF2() {
        return integerDFF2;
    }

    public void setIntegerDFF2(Integer integerDFF2) {
        this.integerDFF2 = integerDFF2;
    }

    @DatabaseField()
    private Integer integerDFF3;

    public Integer getIntegerDFF3() {
        return integerDFF3;
    }

    public void setIntegerDFF3(Integer integerDFF3) {
        this.integerDFF3 = integerDFF3;
    }

    @DatabaseField()
    private String stringDFF1;

    public String getStringDFF1() {
        return stringDFF1;
    }

    public void setStringDFF1(String stringDFF1) {
        this.stringDFF1 = stringDFF1;
    }

    @DatabaseField()
    private String stringDFF2;

    public String getStringDFF2() {
        return stringDFF2;
    }

    public void setStringDFF2(String stringDFF2) {
        this.stringDFF2 = stringDFF2;
    }

    @DatabaseField()
    private String stringDFF3;

    public String getStringDFF3() {
        return stringDFF3;
    }

    public void setStringDFF3(String stringDFF3) {
        this.stringDFF3 = stringDFF3;
    }
}
