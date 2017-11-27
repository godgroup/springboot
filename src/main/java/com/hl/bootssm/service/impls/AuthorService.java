package com.hl.bootssm.service.impls;

import com.hl.bootssm.dao.IAuthorDao;
import com.hl.bootssm.domain.AuthorInfo;
import com.hl.bootssm.service.inters.IAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Static
 */
@Service
public class AuthorService implements IAuthorService {
    @Autowired
    private IAuthorDao dao;

    @Override
    public int insert(AuthorInfo item) {
        if (item == null) {
            return -1;
        }
        return dao.insert(item);
    }

    @Override
    public AuthorInfo getOne(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.getOneById(id);
    }
}