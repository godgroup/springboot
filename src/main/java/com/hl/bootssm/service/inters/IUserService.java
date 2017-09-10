package com.hl.bootssm.service.inters;

import com.hl.bootssm.domain.UserInfo;

public interface IUserService {
	/**
	 * 添加用戶信息
	 * @param item
	 * @return
	 * @throws Exception
	 */
	boolean addUser(UserInfo item) throws Exception;
}
