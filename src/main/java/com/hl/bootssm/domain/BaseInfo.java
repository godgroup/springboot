package com.hl.bootssm.domain;

import com.hl.bootssm.components.SpringUtil;
import org.springframework.context.MessageSource;

import java.io.Serializable;

/**
 * @author Static
 * 基类
 */
public class BaseInfo implements Serializable {
    /**
     * 获取messageSource的消息
     *
     * @param code
     * @return
     */
    protected static String messageSource(String code) {
        return SpringUtil.getBean(MessageSource.class).getMessage(code, null, code, null);
    }

    /**
     * 获取messageSource的消息
     *
     * @param code
     * @return
     */
    protected static String messageSource(String code, Object[] objects) {
        return SpringUtil.getBean(MessageSource.class).getMessage(code, objects, code, null);
    }
}