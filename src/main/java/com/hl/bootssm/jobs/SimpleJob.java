package com.hl.bootssm.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Static
 * 定时任务Demo
 */
@DisallowConcurrentExecution
@Component
@Slf4j
public class SimpleJob extends BaseJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时任务,当前时间:" + new Date());
    }
}