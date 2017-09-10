package com.hl.bootssm.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.bootssm.dao.IUserDao;
import com.demo.bootssm.domain.UserInfo;
import com.demo.bootssm.service.inters.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDao;
	
	@Override
	public boolean addUser(UserInfo item) throws Exception {
		return userDao.addUser(item);
	}
	
}
