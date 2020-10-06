package com.ljheee.plugin.controller;

import com.ljheee.plugin.service.MyContextAware;
import com.ljheee.plugin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;
    @Autowired
    MyContextAware contextAware;

    @RequestMapping("/hello")
    public String hello() {
        return "hello=" + System.currentTimeMillis();
    }


    @RequestMapping("/ok")
    public String getUser() {
        return userService.getUser();
    }


    /**
     * http://localhost:8080/user/active
     *
     * @return
     */
    @RequestMapping("/active")
    public String active() {

        contextAware.active();
        return userService.getUser();
    }


}
