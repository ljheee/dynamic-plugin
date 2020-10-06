package com.ljheee.plugin.service;

import com.ljheee.plugin.sample.CountingPlugin;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by lijianhua04 on 2018/12/11.
 */
@Component
public class MyContextAware  implements ApplicationContextAware{

    ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public String active() {

        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(userService);
        Advised advised = (Advised) userService;

        CountingPlugin plugin = new CountingPlugin();
        advised.addAdvice(plugin);
        return userService.getUser();
    }


}
