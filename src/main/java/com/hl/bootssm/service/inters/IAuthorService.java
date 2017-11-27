package com.hl.bootssm.service.inters;

import com.hl.bootssm.domain.AuthorInfo;

/**
 * @author Static
 */
public interface IAuthorService {
    /**
     * 新增记录
     *
     * @param item
     * @return
     */
    int insert(AuthorInfo item);

    /**
     * 获取记录
     *
     * @param id
     * @return
     */
    AuthorInfo getOne(Long id);
}