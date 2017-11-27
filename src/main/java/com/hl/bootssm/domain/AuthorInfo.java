package com.hl.bootssm.domain;

import com.hl.bootssm.enums.AuthorSex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Static
 * <p>
 * Author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInfo extends BaseInfo {
    private Long id;
    private String name;
    private Date birthDay;
    private AuthorSex sex;
    private String summary;
    private Timestamp createAt;
    private Timestamp updateAt;
}