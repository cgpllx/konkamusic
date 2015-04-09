package com.konka.music.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PalyControllerProxy implements InvocationHandler {
	private Object object;

	public PalyControllerProxy(Object object) {
		this.object = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		method.invoke(object, args);
		return null;
	}
}
