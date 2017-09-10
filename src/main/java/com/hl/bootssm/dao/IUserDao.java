package com.hl.bootssm.dao;

import com.demo.bootssm.domain.UserInfo;

public interface IUserDao {
	/**
	 * 添加用戶信息
	 * @param item
	 * @return
	 * @throws Exception
	 */
	boolean addUser(UserInfo item) throws Exception;
}
