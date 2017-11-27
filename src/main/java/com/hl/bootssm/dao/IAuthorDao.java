package com.hl.bootssm.dao;

import com.hl.bootssm.domain.AuthorInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Static
 */
@Repository
@Mapper
public interface IAuthorDao {
    /**
     * 获取记录
     *
     * @param id
     * @return
     */
    AuthorInfo getOneById(Long id);

    /**
     * 新增记录
     *
     * @param item
     * @return
     */
    int insert(AuthorInfo item);
}