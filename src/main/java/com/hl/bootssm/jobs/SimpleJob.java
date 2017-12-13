package com.hl.bootssm.jobs;

import com.hl.bootssm.annotation.TimeTaskAnnotation;
import com.hl.bootssm.utils.DateTools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author Static
 * 定时任务Demo
 */
@DisallowConcurrentExecution
@Component
@Slf4j
@TimeTaskAnnotation(jobName = "simpleJob", jobDesc = "测试任务", triggerName = "simleJobTrigger")
public class SimpleJob extends BaseJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("定时任务,当前时间:" + DateTools.getCurrentDateTime());
    }
}