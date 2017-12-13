package com.hl.bootssm;

import com.hl.bootssm.config.DBConfig;
import com.hl.bootssm.config.ThymeleafConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.devtools.autoconfigure.DevToolsDataSourceAutoConfiguration;
import org.springframework.boot.devtools.autoconfigure.LocalDevToolsAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Static
 */
@Configuration
@Import({
        DispatcherServletAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JmxAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        ThymeleafAutoConfiguration.class,
        ThymeleafConfig.class,
        WebMvcAutoConfiguration.class,
        WebSocketAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        WebSocketAutoConfiguration.class,
        LocalDevToolsAutoConfiguration.class,
        DevToolsDataSourceAutoConfiguration.class,
        MessageSourceAutoConfiguration.class,
        DBConfig.class
})
@EnableAutoConfiguration
@EnableSpringDataWebSupport
@ComponentScan(basePackages = {"com.hl.bootssm"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@MapperScan("com.hl.bootssm.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}