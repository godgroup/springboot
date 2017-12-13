package com.hl.bootssm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定时任务动态管理注解
 *
 * @author Static
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TimeTaskAnnotation {
    String jobName();

    String jobDesc();

    String triggerName();
}