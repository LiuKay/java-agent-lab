package com.kay.agent.security;

import com.kay.agent.security.agent.AgentMain;
import com.kay.agent.security.agent.SecurityInterceptor;
import com.kay.agent.security.expose.Secured;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class NormalMain {

    //-javaagent:C:\kaybee\code\bytecode-lab\bytebuddy-agent-02\build\libs\bytebuddy-agent-02-1.0.jar
    public static void main(String[] args) {
        final UserService userService = new UserService();
        userService.doSensitiveAction();
    }


    /**
     * create a subclass of UserService, and use the new instance to invoke
     * compare with {@link AgentMain#premain(String, Instrumentation)}
     */
    public static void subclass() throws InstantiationException, IllegalAccessException {
        new ByteBuddy()
                .subclass(UserService.class)
                .method(ElementMatchers.isAnnotatedWith(Secured.class))
                .intercept(MethodDelegation.to(SecurityInterceptor.class)
                                           .andThen(SuperMethodCall.INSTANCE)) //invoke original method
                .make()
                .load(AgentMain.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .newInstance() // create instance of this subclass
                .doSensitiveAction();
    }
}
