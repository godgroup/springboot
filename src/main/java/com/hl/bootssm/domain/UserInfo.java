package com.hl.bootssm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Static
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo extends BaseInfo {
    private Integer id;
    private String userName;
    private String password;
    private Integer roleId;
}