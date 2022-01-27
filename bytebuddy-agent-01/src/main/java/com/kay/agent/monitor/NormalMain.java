package com.kay.agent.monitor;

import com.kay.agent.monitor.agent.MethodAdvice;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NormalMain {

    //-javaagent:C:\kaybee\code\bytecode-lab\bytebuddy-agent-01\build\libs\bytebuddy-agent-01-1.0.jar
    public static void main(String[] args) {
        Service service = new Service();
        service.getUserInfo("kay", "beijing");
    }


    public static void test() throws InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        final Class<?> loaded = new ByteBuddy()
                .redefine(Service.class)
                .visit(Advice.to(MethodAdvice.class)
                             .on(ElementMatchers.isMethod()))
                .make()
                .load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        Object instance = loaded.newInstance();
        final Method method = loaded.getDeclaredMethod("getUserInfo", String.class, String.class);
        method.invoke(instance, "kaybee", "beijing");
    }
}
