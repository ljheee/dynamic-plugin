package com.ljheee.test;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 输出Bean方法执行日志
 * 
 */
public class RequestLogPlugin implements MethodBeforeAdvice {

	public void before(Method method, Object[] args, Object target) throws Throwable {
		String result = String.format("%s.%s() 参数:%s", method.getDeclaringClass().getName(), method.getName(),
				Arrays.toString(args));
		System.out.println(result);
	}

	
}
