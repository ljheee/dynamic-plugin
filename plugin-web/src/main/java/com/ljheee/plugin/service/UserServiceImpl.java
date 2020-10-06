package com.ljheee.plugin.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by lijianhua04 on 2018/12/9.
 */
@Service
public class UserServiceImpl implements UserService {

    @Async
    @Override
    public String getUser() {
        System.out.println("ljh");
        return "user=" + Thread.currentThread().getName();
    }
}
