package com.hl.bootssm.dao;

import com.hl.bootssm.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Static
 */
@Mapper
public interface IUserDao {
    /**
     * 添加用戶信息
     *
     * @param item
     * @return
     * @throws Exception
     */
    boolean addUser(UserInfo item) throws Exception;
}