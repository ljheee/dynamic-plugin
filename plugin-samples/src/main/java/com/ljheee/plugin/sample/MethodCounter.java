package com.ljheee.plugin.sample;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方法执行次数统计
 */
public class MethodCounter implements Serializable{


    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    int allCount;

    protected void count(Method m) {
        count(m.getName());
    }

    private void count(String methodName) {

        Integer i = map.get(methodName);
        i = (i != null) ? i.intValue() + 1 : 1;
        map.put(methodName, i);
        allCount++;
    }


    public int getCalls(String methodName) {
        Integer i = map.get(methodName);
        return (i != null ? i.intValue() : 0);
    }

    public int getCalls() {
        return allCount;
    }
}
