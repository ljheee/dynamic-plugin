package com.ljheee.plugin.sample;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 输出Bean方法执行日志
 * 
 */
public class RequestLogPlugin implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		String classAndMethodName = String.format("类名方法名:%s.%s() 参数:%s", method.getDeclaringClass().getName(), method.getName(),
				Arrays.toString(args));
		System.out.println(classAndMethodName);
	}

	
}
