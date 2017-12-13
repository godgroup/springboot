package com.hl.bootssm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Static
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuartzBizInfo extends BaseInfo {
    private String jobName;
    private String jobClass;
    private String jobDescription;
    private String triggerName;
    private String cronEx;
    private Boolean hasJob;
    private Boolean hasTrigger;
    private String lastFireTime;
    private String nextFireTime;
    private String triggerState;
    private String errorMsg;
}