package com.github.object.persistence.core;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class ProxyObject<T> implements MethodInterceptor {
    private T realObject;
    private final Supplier<T> objectSupplier;

    public ProxyObject(Supplier<T> supplier){
        this.objectSupplier = supplier;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.getDeclaringClass().equals(Object.class)) {
            return proxy.invokeSuper(obj, args);
        }

        initializeObject();

        return proxy.invoke(realObject, args);
    }

    private synchronized void initializeObject(){
        if (realObject == null) {
            realObject = objectSupplier.get();
        }
    }
}
