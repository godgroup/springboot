package com.hl.bootssm.service.impls;

import com.hl.bootssm.dao.IUserDao;
import com.hl.bootssm.domain.UserInfo;
import com.hl.bootssm.service.inters.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public boolean addUser(UserInfo item) throws Exception {
        return userDao.addUser(item);
    }
}