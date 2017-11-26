package com.hl.bootssm.jobs;

import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @author Static
 */
public abstract class BaseJob implements Job {
    @Autowired
    private Environment env;

    protected Boolean isProduct() {
        String[] activeProfiles = env.getActiveProfiles();
        return "pro".equals(activeProfiles[0]);
    }
}