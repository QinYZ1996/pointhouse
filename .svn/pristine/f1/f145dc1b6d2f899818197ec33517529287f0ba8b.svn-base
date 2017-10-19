package com.pointhouse.chiguan.common.sqlite.dao;

import java.sql.SQLException;

/**
 * Created by Maclaine on 2017/6/22.
 *
 * @param <T>  操作的实体类型
 * @param <Z>  表对应的主键类型
 */

public interface Idao<T, Z> {
    /**
     * 添加
     * @param t
     * @return
     * @throws SQLException
     */
    public int save(T t) throws SQLException;
    /**
     * 删除
     * @param t
     * @return
     * @throws SQLException
     */
    public int delete(T t) throws SQLException;
    /**
     * 修改
     * @param t
     * @return
     * @throws SQLException
     */
    public int update(T t) throws SQLException;
    /**
     * 根据主键查询
     * @param z
     * @return
     * @throws SQLException
     */
    public T queryById(Z z) throws SQLException;
}
