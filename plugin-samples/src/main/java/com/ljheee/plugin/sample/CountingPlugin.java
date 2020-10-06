package com.ljheee.plugin.sample;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 方法执行次数统计
 */
public class CountingPlugin extends MethodCounter implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        count(method);
        System.out.println(String.format("方法%s,执行次数%s", method.getName(), getCalls()));
    }


    public String getStatus() {
        String time = SimpleDateFormat.getDateTimeInstance().format(new Date());
        String result = "";
        for (Map.Entry<String, Integer> m : map.entrySet()) {
            result += "方法:" + m.getKey();
            result += "执行次数:" + m.getValue();
            result += "\r\n";
        }
        return String.format("时间:%s, 执行信息:%s", time, result);
    }

}
