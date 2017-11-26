package com.hl.bootssm.domain.resultmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Static
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {
    private int code;
    private Object result;
}